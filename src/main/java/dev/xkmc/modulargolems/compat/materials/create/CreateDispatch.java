package dev.xkmc.modulargolems.compat.materials.create;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import dev.xkmc.l2library.serial.recipe.ConditionalRecipeWrapper;
import dev.xkmc.modulargolems.compat.materials.common.ModDispatch;
import dev.xkmc.modulargolems.compat.materials.create.automation.CreateGolemRecipeGen;
import dev.xkmc.modulargolems.compat.materials.create.automation.CreateJEIEvents;
import dev.xkmc.modulargolems.compat.materials.create.automation.CreateRecipeEvents;
import dev.xkmc.modulargolems.compat.materials.create.automation.GolemIncompleteItem;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;

import java.util.List;

public class CreateDispatch extends ModDispatch {

	public static final String MODID = "create";

	public CreateDispatch() {
		CreateCompatRegistry.register();
		MinecraftForge.EVENT_BUS.register(CreateRecipeEvents.class);
		if (ModList.get().isLoaded("jei")) {
			MinecraftForge.EVENT_BUS.register(CreateJEIEvents.class);
		}

	}

	@Override
	public void lateRegister() {
		List<ItemEntry<?>> str = List.of(GolemItems.GOLEM_BODY, GolemItems.GOLEM_ARM, GolemItems.GOLEM_LEGS,
				GolemItems.HUMANOID_BODY, GolemItems.HUMANOID_ARMS, GolemItems.HUMANOID_LEGS,
				GolemItems.DOG_BODY, GolemItems.DOG_LEGS);
		for (var part : str) {
			ModularGolems.REGISTRATE.item("incomplete_" + part.getId().getPath(),
							p -> new GolemIncompleteItem(p, part))
					.removeTab(GolemItems.TAB.getKey())
					.model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("builtin/entity"))
							.texture("particle", "minecraft:block/clay"))
					.defaultLang().register();
		}
	}

	public void genLang(RegistrateLangProvider pvd) {
		pvd.add("golem_material." + MODID + ".zinc", "Zinc");
		pvd.add("golem_material." + MODID + ".andesite_alloy", "Andesite Alloy");
		pvd.add("golem_material." + MODID + ".brass", "Brass");
		pvd.add("golem_material." + MODID + ".railway", "Railway");
	}

	@Override
	public void genRecipe(RegistrateRecipeProvider pvd) {
		safeUpgrade(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CreateCompatRegistry.UP_COATING.get())::unlockedBy, AllItems.ZINC_INGOT.get())
				.pattern(" A ").pattern("ABA").pattern(" A ")
				.define('A', AllTags.forgeItemTag("ingots/zinc"))
				.define('B', GolemItems.EMPTY_UPGRADE.get())
				.save(ConditionalRecipeWrapper.mod(pvd, MODID));


		safeUpgrade(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CreateCompatRegistry.UP_PUSH.get())::unlockedBy, AllItems.EXTENDO_GRIP.get())
				.pattern(" C ").pattern("ABA").pattern(" C ")
				.define('A', AllItems.EXTENDO_GRIP.get())
				.define('B', GolemItems.EMPTY_UPGRADE.get())
				.define('C', AllItems.PRECISION_MECHANISM.get())
				.save(ConditionalRecipeWrapper.mod(pvd, MODID));

		CreateGolemRecipeGen.genAllUpgradeRecipes(pvd);

	}

	@Override
	public ConfigDataProvider getDataGen(DataGenerator gen) {
		return new CreateConfigGen(gen);
	}

}
