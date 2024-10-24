package dev.xkmc.modulargolems.compat.materials.common;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.modulargolems.compat.materials.alexscaves.ACDispatch;
import dev.xkmc.modulargolems.compat.materials.blazegear.BGDispatch;
import dev.xkmc.modulargolems.compat.materials.botania.BotDispatch;
import dev.xkmc.modulargolems.compat.materials.cataclysm.CataDispatch;
import dev.xkmc.modulargolems.compat.materials.create.CreateDispatch;
import dev.xkmc.modulargolems.compat.materials.l2complements.LCDispatch;
import dev.xkmc.modulargolems.compat.materials.l2hostility.LHDispatch;
import dev.xkmc.modulargolems.compat.materials.twilightforest.TFDispatch;
import dev.xkmc.modulargolems.compat.misc.CEICompat;
import dev.xkmc.modulargolems.compat.musket.GolemMusketCompat;
import ewewukek.musketmod.MusketMod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.ModList;
import plus.dragons.createenchantmentindustry.EnchantmentIndustry;

import java.util.ArrayList;
import java.util.List;

public abstract class CompatManager {

	public static final List<ModDispatch> LIST = new ArrayList<>();

	public static void register() {
		if (ModList.get().isLoaded(BotDispatch.MODID)) LIST.add(new BotDispatch());
		if (ModList.get().isLoaded(TFDispatch.MODID)) LIST.add(new TFDispatch());
		if (ModList.get().isLoaded(CreateDispatch.MODID)) LIST.add(new CreateDispatch());
		if (ModList.get().isLoaded(LCDispatch.MODID)) LIST.add(new LCDispatch());
		if (ModList.get().isLoaded(BGDispatch.MODID)) LIST.add(new BGDispatch());
		if (ModList.get().isLoaded(LHDispatch.MODID)) LIST.add(new LHDispatch());
		if (ModList.get().isLoaded(CataDispatch.MODID)) LIST.add(new CataDispatch());
		if (ModList.get().isLoaded(ACDispatch.MODID)) LIST.add(new ACDispatch());
		if (ModList.get().isLoaded(EnchantmentIndustry.ID)) CEICompat.register();
		if (ModList.get().isLoaded(MusketMod.MODID)) GolemMusketCompat.init();
	}

	public static void dispatchGenLang(RegistrateLangProvider pvd) {
		for (ModDispatch dispatch : LIST) {
			dispatch.genLang(pvd);
		}
	}

	public static void gatherData(GatherDataEvent event) {
		for (ModDispatch dispatch : LIST) {
			var gen = dispatch.getDataGen(event.getGenerator());
			if (gen != null) {
				event.getGenerator().addProvider(event.includeServer(), gen);
			}
		}
	}

	public static void dispatchGenRecipe(RegistrateRecipeProvider pvd) {
		for (ModDispatch dispatch : LIST) {
			dispatch.genRecipe(pvd);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static void dispatchClientSetup() {
		for (ModDispatch dispatch : LIST) {
			dispatch.dispatchClientSetup();
		}
	}

	public static void lateRegister() {
		for (ModDispatch dispatch : LIST) {
			dispatch.lateRegister();
		}
	}

}
