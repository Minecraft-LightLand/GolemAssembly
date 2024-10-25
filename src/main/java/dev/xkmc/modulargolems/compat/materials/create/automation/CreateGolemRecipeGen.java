package dev.xkmc.modulargolems.compat.materials.create.automation;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import dev.xkmc.modulargolems.compat.materials.common.CompatManager;
import dev.xkmc.modulargolems.compat.materials.common.ModDispatch;
import dev.xkmc.modulargolems.compat.materials.create.CreateDispatch;
import dev.xkmc.modulargolems.content.config.GolemMaterialConfig;
import dev.xkmc.modulargolems.content.item.golem.GolemPart;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.data.MGConfigGen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CreateGolemRecipeGen {

	private static final Set<String> SPECIAL = Set.of("andesite_alloy", "brass", "railway");

	public static void genAllUpgradeRecipes(RegistrateRecipeProvider pvd) {
		var ing = gatherConfig();
		for (var part : GolemPart.LIST) {
			for (var ent : ing.entrySet()) {
				if (SPECIAL.contains(ent.getKey().getPath())) continue;
				genAssembly(pvd, part, ent.getKey(), ent.getValue());
			}
		}
		genSpecialRecipes(pvd);
	}

	public static void genSpecialRecipes(RegistrateRecipeProvider pvd) {
		for (var part : GolemPart.LIST) {
			genAssembly(pvd, part, new ResourceLocation(CreateDispatch.MODID, "andesite_alloy"), AllItems.ANDESITE_ALLOY, AllBlocks.COGWHEEL);
			genAssembly(pvd, part, new ResourceLocation(CreateDispatch.MODID, "brass"), Ingredient.of(AllTags.forgeItemTag("ingots/brass")), AllItems.PRECISION_MECHANISM);
			genAssembly(pvd, part, new ResourceLocation(CreateDispatch.MODID, "railway"), Ingredient.of(AllTags.forgeItemTag("plates/brass")), AllItems.PRECISION_MECHANISM, AllItems.ELECTRON_TUBE, AllItems.STURDY_SHEET);
		}
	}

	private static void genAssembly(RegistrateRecipeProvider pvd, GolemPart<?, ?> part, ResourceLocation id, ItemLike ingredient, ItemLike... parts) {
		genAssembly(pvd, part, id, Ingredient.of(ingredient), parts);
	}

	private static void genAssembly(RegistrateRecipeProvider pvd, GolemPart<?, ?> part, ResourceLocation id, Ingredient ingredient, ItemLike... parts) {
		var part_rl = ForgeRegistries.ITEMS.getKey(part);
		assert part_rl != null;
		String item_name = part_rl.getPath();
		var recipe = new ConditionalSARecipeBuilder(new ResourceLocation(ModularGolems.MODID,
				id.getPath() + "_assemble_" + item_name));
		var incomplete = ForgeRegistries.ITEMS.getValue(part_rl.withPrefix("incomplete_"));
		recipe.require(part).transitionTo(incomplete);
		recipe.addStep(DeployerApplicationRecipe::new, e -> e.require(ingredient));
		if (parts.length == 0) {
			for (int i = 0; i < 3; i++) {
				recipe.addStep(PressingRecipe::new, e -> e);
			}
		} else {
			recipe.addStep(PressingRecipe::new, e -> e);
			for (var p : parts) {
				recipe.addStep(DeployerApplicationRecipe::new, e -> e.require(p));
			}
			recipe.addStep(DeployerApplicationRecipe::new, e -> e.require(AllItems.WRENCH).toolNotConsumed());
		}
		recipe.loops(part.count);
		String modid = id.getNamespace();
		recipe.withCondition(new ModLoadedCondition(CreateDispatch.MODID));
		if (!modid.equals(ModularGolems.MODID) && !modid.equals(CreateDispatch.MODID)) {
			recipe.withCondition(new ModLoadedCondition(modid));
		}
		recipe.addOutput(GolemPart.setMaterial(part.getDefaultInstance(), id), 1);
		recipe.build(pvd);
	}

	@SuppressWarnings("ConstantConditions")
	private static Map<ResourceLocation, Ingredient> gatherConfig() {
		ConfigDataProvider.Collector map = new ConfigDataProvider.Collector(new HashMap<>());
		for (ModDispatch dispatch : CompatManager.LIST) {
			var gen = dispatch.getDataGen(null);
			gen.add(map);
		}
		new MGConfigGen(null).add(map);
		Map<ResourceLocation, Ingredient> ing = new HashMap<>();
		for (ConfigDataProvider.ConfigEntry<?> config : map.map().values()) {
			if (config.config() instanceof GolemMaterialConfig mat) {
				ing.putAll(mat.ingredients);
			}
		}
		return ing;
	}

}
