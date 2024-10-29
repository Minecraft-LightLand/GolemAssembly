package dev.xkmc.modulargolems.compat.jei;

import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.modulargolems.compat.curio.CurioCompatRegistry;
import dev.xkmc.modulargolems.content.config.GolemMaterial;
import dev.xkmc.modulargolems.content.config.GolemMaterialConfig;
import dev.xkmc.modulargolems.content.core.GolemType;
import dev.xkmc.modulargolems.content.core.IGolemPart;
import dev.xkmc.modulargolems.content.item.golem.GolemHolder;
import dev.xkmc.modulargolems.content.item.golem.GolemPart;
import dev.xkmc.modulargolems.content.item.upgrade.UpgradeItem;
import dev.xkmc.modulargolems.content.menu.config.ToggleGolemConfigScreen;
import dev.xkmc.modulargolems.content.menu.equipment.EquipmentsScreen;
import dev.xkmc.modulargolems.content.menu.filter.ItemConfigScreen;
import dev.xkmc.modulargolems.content.menu.path.PathConfigScreen;
import dev.xkmc.modulargolems.content.menu.tabs.ITabScreen;
import dev.xkmc.modulargolems.content.menu.target.TargetConfigScreen;
import dev.xkmc.modulargolems.content.recipe.GolemAssembleRecipe;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.data.MGTagGen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.gui.handlers.IGuiProperties;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class GolemJEIPlugin implements IModPlugin {

	public static final ResourceLocation ID = new ResourceLocation(ModularGolems.MODID, "main");

	@Override
	public ResourceLocation getPluginUid() {
		return ID;
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration) {
		for (Item item : GolemPart.LIST) {
			registration.registerSubtypeInterpreter(item, GolemJEIPlugin::partSubtype);
		}
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		GolemMaterialConfig config = GolemMaterialConfig.get();
		List<IJeiAnvilRecipe> recipes = new ArrayList<>();
		addPartCraftRecipes(recipes, config, registration.getVanillaRecipeFactory());
		addRepairRecipes(recipes, config, registration.getVanillaRecipeFactory());
		addUpgradeRecipes(recipes, config, registration.getVanillaRecipeFactory());
		registration.addRecipes(RecipeTypes.ANVIL, recipes);
		MinecraftForge.EVENT_BUS.post(new CustomRecipeEvent(registration));
	}

	@Override
	public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
		registration.getCraftingCategory().addCategoryExtension(GolemAssembleRecipe.class, GolemAssemblyExtension::new);
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addGhostIngredientHandler(ItemConfigScreen.class, new ItemFilterHandler());

		registration.addGuiScreenHandler(EquipmentsScreen.class, GolemJEIPlugin::create);
		registration.addGuiScreenHandler(ToggleGolemConfigScreen.class, GolemJEIPlugin::create);
		registration.addGuiScreenHandler(ItemConfigScreen.class, GolemJEIPlugin::create);
		registration.addGuiScreenHandler(TargetConfigScreen.class, GolemJEIPlugin::create);
		registration.addGuiScreenHandler(PathConfigScreen.class, GolemJEIPlugin::create);
		CurioCompatRegistry.onJEIRegistry(e -> registration.addGuiScreenHandler(Wrappers.cast(e), GolemJEIPlugin::create));
	}

	@Nullable
	public static IGuiProperties create(ITabScreen screen) {
		if (screen.screenWidth() <= 0 || screen.screenHeight() <= 0) {
			return null;
		}
		int x = screen.getGuiLeft();
		int y = screen.getGuiTop();
		int width = screen.getXSize() + 32;
		int height = screen.getYSize();
		if (width <= 0 || height <= 0) {
			return null;
		}
		return new GuiProperties(screen.asScreen().getClass(),
				x, y, width, height,
				screen.screenWidth(), screen.screenHeight()
		);
	}


	private static String partSubtype(ItemStack stack, UidContext ctx) {
		return GolemPart.getMaterial(stack).orElse(GolemMaterial.EMPTY).toString();
	}

	private static void addPartCraftRecipes(List<IJeiAnvilRecipe> recipes, GolemMaterialConfig config, IVanillaRecipeFactory factory) {
		for (var mat : config.getAllMaterials()) {
			var arr = config.getCraftIngredient(mat).getItems();
			boolean special = false;
			for (ItemStack stack : arr) {
				if (stack.is(MGTagGen.SPECIAL_CRAFT)) {
					special = true;
					break;
				}
			}
			if (special) continue;
			for (var item : GolemPart.LIST) {
				List<ItemStack> list = new ArrayList<>();
				for (ItemStack stack : arr) {
					list.add(new ItemStack(stack.getItem(), item.count));
				}
				recipes.add(factory.createAnvilRecipe(new ItemStack(item), list,
						List.of(GolemPart.setMaterial(new ItemStack(item), mat))));
			}
		}
	}

	private static void addRepairRecipes(List<IJeiAnvilRecipe> recipes, GolemMaterialConfig config, IVanillaRecipeFactory factory) {
		for (var types : GolemType.GOLEM_TYPE_TO_ITEM.values()) {
			List<ItemStack> input = new ArrayList<>();
			List<ItemStack> material = new ArrayList<>();
			List<ItemStack> result = new ArrayList<>();
			for (var mat : config.getAllMaterials()) {
				ItemStack golem = new ItemStack(types);
				for (IGolemPart<?> part : types.getEntityType().values()) {
					GolemHolder.addMaterial(golem, part.toItem(), mat);
				}
				ItemStack damaged = golem.copy();
				damaged.getOrCreateTag().putFloat(GolemHolder.KEY_DISPLAY, 0.75f);
				input.add(damaged);
				var arr = config.getRepairIngredient(mat).getItems();
				material.add(new ItemStack(arr.length > 0 ? arr[0].getItem() : Items.BARRIER));
				golem.getOrCreateTag().putFloat(GolemHolder.KEY_DISPLAY, 1f);
				result.add(golem);
			}
			recipes.add(factory.createAnvilRecipe(input, material, result));
		}
	}

	private static void addUpgradeRecipes(List<IJeiAnvilRecipe> recipes, GolemMaterialConfig config, IVanillaRecipeFactory factory) {
		for (UpgradeItem item : UpgradeItem.LIST) {
			List<ItemStack> input = new ArrayList<>();
			List<ItemStack> material = new ArrayList<>();
			List<ItemStack> result = new ArrayList<>();
			for (var types : GolemType.GOLEM_TYPE_TO_ITEM.values()) {
				input.add(new ItemStack(types));
			}
			material.add(new ItemStack(item));
			for (var types : GolemType.GOLEM_TYPE_TO_ITEM.values()) {
				result.add(GolemHolder.addUpgrade(new ItemStack(types), item));
			}
			recipes.add(factory.createAnvilRecipe(input, material, result));
		}
	}

}
