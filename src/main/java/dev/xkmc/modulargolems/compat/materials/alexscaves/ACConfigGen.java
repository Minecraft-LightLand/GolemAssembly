package dev.xkmc.modulargolems.compat.materials.alexscaves;

import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import dev.xkmc.modulargolems.compat.materials.twilightforest.TFCompatRegistry;
import dev.xkmc.modulargolems.compat.materials.twilightforest.TFDispatch;
import dev.xkmc.modulargolems.content.config.GolemMaterialConfig;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.registrate.GolemModifiers;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import twilightforest.data.tags.ItemTagGenerator;

public class ACConfigGen extends ConfigDataProvider {

	public ACConfigGen(DataGenerator generator) {
		super(generator, "Golem Config for Alex's Caves");
	}

	public void add(Collector map) {
		map.add(ModularGolems.MATERIALS, new ResourceLocation(ACDispatch.MODID, ACDispatch.MODID), new GolemMaterialConfig()
				.addMaterial(new ResourceLocation(ACDispatch.MODID, "candy"), Ingredient.of(ACItemRegistry.CANDY_CANE_HOOK.get()))
				.addStat(GolemTypes.STAT_HEALTH.get(), 80)
				.addStat(GolemTypes.STAT_ATTACK.get(), 6)
				.addStat(GolemTypes.STAT_SPEED.get(), 0.3)
				.end()

				.addMaterial(new ResourceLocation(ACDispatch.MODID, "magnetic"), Ingredient.of(ACItemRegistry.TELECORE.get()))
				.addStat(GolemTypes.STAT_HEALTH.get(), 200)
				.addStat(GolemTypes.STAT_ATTACK.get(), 15)
				.addStat(GolemTypes.STAT_WEIGHT.get(), -0.2)
				.end()

				.addMaterial(new ResourceLocation(ACDispatch.MODID, "nuclear"), Ingredient.of(ACItemRegistry.URANIUM.get()))
				.addStat(GolemTypes.STAT_HEALTH.get(), 200)
				.addStat(GolemTypes.STAT_ATTACK.get(), 10)
				.addStat(GolemTypes.STAT_WEIGHT.get(), -0.4)
				.end()

		);
	}

}
