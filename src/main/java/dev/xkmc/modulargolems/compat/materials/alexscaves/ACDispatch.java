package dev.xkmc.modulargolems.compat.materials.alexscaves;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import dev.xkmc.modulargolems.compat.materials.common.ModDispatch;
import net.minecraft.data.DataGenerator;

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
