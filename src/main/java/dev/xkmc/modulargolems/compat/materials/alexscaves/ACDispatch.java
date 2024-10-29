package dev.xkmc.modulargolems.compat.materials.alexscaves;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import dev.xkmc.modulargolems.compat.materials.common.ModDispatch;
import dev.xkmc.modulargolems.init.data.RecipeGen;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;

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


		RecipeGen.unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC,
								ACCompatRegistry.REPAIR_CANDY.get())::unlockedBy,
						ACItemRegistry.CARAMEL.get())
				.pattern("BBB").pattern("CDC").pattern("AAA")
				.define('A', ACBlockRegistry.FROSTED_GINGERBREAD_BLOCK.get())
				.define('B', ACBlockRegistry.CANDY_CANE_BLOCK.get())
				.define('C', ACItemRegistry.CARAMEL.get())
				.define('D', ACBlockRegistry.LICOROOT.get())
				.save(pvd);

		RecipeGen.unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC,
								ACCompatRegistry.CRAFT_CANDY.get(), 3)::unlockedBy,
						ACItemRegistry.RADIANT_ESSENCE.get().asItem())
				.pattern("GBG").pattern("ZCZ").pattern("AAA")
				.define('A', ACCompatRegistry.REPAIR_CANDY.get())
				.define('B', ACItemRegistry.GUMBALL_PILE.get())
				.define('G', ACBlockRegistry.SUGAR_GLASS.get())
				.define('Z', ACItemRegistry.HOT_CHOCOLATE_BOTTLE.get())
				.define('C', ACItemRegistry.RADIANT_ESSENCE.get())
				.save(pvd);

		RecipeGen.unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC,
								ACCompatRegistry.REPAIR_MAGNETIC.get())::unlockedBy,
						ACItemRegistry.SCARLET_NEODYMIUM_INGOT.get())
				.pattern("SBC").pattern("BCB").pattern("CBA")
				.define('A', ACItemRegistry.AZURE_NEODYMIUM_INGOT.get())
				.define('S', ACItemRegistry.SCARLET_NEODYMIUM_INGOT.get())
				.define('B', ACItemRegistry.FERROUSLIME_BALL.get())
				.define('C', ACBlockRegistry.PACKED_GALENA.get())
				.save(pvd);

		RecipeGen.unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC,
								ACCompatRegistry.CRAFT_MAGNETIC.get(), 3)::unlockedBy,
						ACBlockRegistry.HEART_OF_IRON.get().asItem())
				.pattern("ANA").pattern("TCT").pattern("ANA")
				.define('A', ACCompatRegistry.REPAIR_MAGNETIC.get())
				.define('T', ACItemRegistry.TELECORE.get())
				.define('N', ACItemRegistry.NOTOR_COMPONENT.get())
				.define('C', ACBlockRegistry.HEART_OF_IRON.get())
				.save(pvd);

		RecipeGen.unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC,
								ACCompatRegistry.CRAFT_NUCLEAR.get(), 3)::unlockedBy,
						ACBlockRegistry.HEART_OF_IRON.get().asItem())
				.pattern("AAA").pattern("CDC").pattern("BBB")
				.define('A', ACItemRegistry.POLYMER_PLATE.get())
				.define('B', ACItemRegistry.CHARRED_REMNANT.get())
				.define('C', ACBlockRegistry.URANIUM_ROD.get())
				.define('D', ACItemRegistry.FISSILE_CORE.get())
				.save(pvd);
	}

	@Override
	public ConfigDataProvider getDataGen(DataGenerator gen) {
		return new ACConfigGen(gen);
	}

	@Override
	public void dispatchClientSetup() {
	}

}
