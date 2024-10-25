package dev.xkmc.modulargolems.compat.materials.alexscaves;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import dev.xkmc.l2library.serial.recipe.ConditionalRecipeWrapper;
import dev.xkmc.modulargolems.compat.materials.common.ModDispatch;
import dev.xkmc.modulargolems.compat.materials.twilightforest.TFCompatRegistry;
import dev.xkmc.modulargolems.compat.materials.twilightforest.TFConfigGen;
import dev.xkmc.modulargolems.content.client.override.ModelOverride;
import dev.xkmc.modulargolems.content.client.override.ModelOverrides;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;

public class ACDispatch extends ModDispatch {

	public static final String MODID = "alexscaves";

	public ACDispatch() {
		ACCompatRegistry.register();
	}

	public void genLang(RegistrateLangProvider pvd) {
		pvd.add("golem_material." + MODID + ".candy", "Candy");
		pvd.add("golem_material." + MODID + ".magnetic", "Magnetic");
		pvd.add("golem_material." + MODID + ".nuclear", "Nuclear");
	}

	@Override
	public void genRecipe(RegistrateRecipeProvider pvd) {
	}

	@Override
	public ConfigDataProvider getDataGen(DataGenerator gen) {
		return new ACConfigGen(gen);
	}

	@Override
	public void dispatchClientSetup() {
	}

}
