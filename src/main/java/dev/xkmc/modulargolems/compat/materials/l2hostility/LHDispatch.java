package dev.xkmc.modulargolems.compat.materials.l2hostility;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2complements.init.materials.LCMats;
import dev.xkmc.l2complements.init.registrate.LCItems;
import dev.xkmc.l2core.serial.config.ConfigDataProvider;
import dev.xkmc.l2core.serial.recipe.ConditionalRecipeWrapper;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.modulargolems.compat.materials.common.ModDispatch;
import dev.xkmc.modulargolems.compat.materials.l2complements.LCCompatRegistry;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class LHDispatch extends ModDispatch {

	public static final String MODID = "l2hostility";

	public LHDispatch() {
		LHCompatRegistry.register();
	}

	public void genLang(RegistrateLangProvider pvd) {
	}

	@Override
	public void genRecipe(RegistrateRecipeProvider pvd) {
		safeUpgrade(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHCompatRegistry.CORE.get(), 1)::unlockedBy, LHItems.CHAOS_INGOT.get())
				.pattern("CBC").pattern("BAB").pattern("CBC")
				.define('A', GolemItems.TALENTED.get())
				.define('B', LHItems.CHAOS_INGOT.get())
				.define('C', LHTraits.ADAPTIVE.get().asItem())
				.save(ConditionalRecipeWrapper.mod(pvd, MODID));

		safeUpgrade(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHCompatRegistry.POTION.get(), 1)::unlockedBy, LHTraits.CURSED.get().asItem())
				.pattern("1B2").pattern("BAB").pattern("3B4")
				.define('1', LHTraits.CURSED.get().asItem())
				.define('2', LHTraits.SOUL_BURNER.get().asItem())
				.define('3', LHTraits.SLOWNESS.get().asItem())
				.define('4', LHTraits.WEAKNESS.get().asItem())
				.define('A', GolemItems.CAULDRON.get())
				.define('B', LCItems.STORM_CORE.get())
				.save(ConditionalRecipeWrapper.mod(pvd, MODID));

		safeUpgrade(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHCompatRegistry.TANK.get(), 1)::unlockedBy, LHTraits.TANK.get().asItem())
				.pattern("CBC").pattern("BAB").pattern("CBC")
				.define('A', GolemItems.NETHERITE.get())
				.define('B', LCItems.WARDEN_BONE_SHARD.get())
				.define('C', LHTraits.TANK.get().asItem())
				.save(ConditionalRecipeWrapper.mod(pvd, MODID));

		safeUpgrade(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHCompatRegistry.SPEED.get(), 1)::unlockedBy, LHTraits.SPEEDY.get().asItem())
				.pattern("CBC").pattern("BAB").pattern("CBC")
				.define('A', LCCompatRegistry.SPEED_UP.get())
				.define('B', LCItems.CAPTURED_WIND.get())
				.define('C', LHTraits.SPEEDY.get().asItem())
				.save(ConditionalRecipeWrapper.mod(pvd, MODID));

		safeUpgrade(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHCompatRegistry.PROTECTION.get(), 1)::unlockedBy, LHTraits.PROTECTION.get().asItem())
				.pattern("CBC").pattern("BAB").pattern("CBC")
				.define('A', GolemItems.DIAMOND.get())
				.define('B', Items.TURTLE_SCUTE)
				.define('C', LHTraits.PROTECTION.get().asItem())
				.save(ConditionalRecipeWrapper.mod(pvd, MODID));

		safeUpgrade(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHCompatRegistry.REGEN.get(), 1)::unlockedBy, LHTraits.REGEN.get().asItem())
				.pattern("CBC").pattern("BAB").pattern("CBC")
				.define('A', GolemItems.ENCHANTED_GOLD.get())
				.define('B', LCMats.TOTEMIC_GOLD.getIngot())
				.define('C', LHTraits.REGEN.get().asItem())
				.save(ConditionalRecipeWrapper.mod(pvd, MODID));

		safeUpgrade(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, LHCompatRegistry.REFLECTIVE.get(), 1)::unlockedBy, LHTraits.REFLECT.get().asItem())
				.pattern("CBC").pattern("BAB").pattern("CBC")
				.define('A', LCCompatRegistry.ATK_UP.get())
				.define('B', LCItems.EXPLOSION_SHARD)
				.define('C', LHTraits.REFLECT.get().asItem())
				.save(ConditionalRecipeWrapper.mod(pvd, MODID));
	}

	@Nullable
	@Override
	public ConfigDataProvider getDataGen(DataGenerator gen, CompletableFuture<HolderLookup.Provider> pvd) {
		return new LHConfigGen(gen, pvd);
	}

	@Override
	public void dispatchClientSetup() {
	}

}
