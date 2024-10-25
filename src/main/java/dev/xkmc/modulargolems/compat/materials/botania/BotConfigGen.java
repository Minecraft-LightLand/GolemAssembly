package dev.xkmc.modulargolems.compat.materials.botania;

import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import dev.xkmc.modulargolems.content.config.GolemMaterialConfig;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import vazkii.botania.common.item.BotaniaItems;

public class BotConfigGen extends ConfigDataProvider {

	public BotConfigGen(DataGenerator generator) {
		super(generator, "Golem Config for Botania");
	}

	public void add(Collector map) {
		map.add(ModularGolems.MATERIALS, new ResourceLocation(BotDispatch.MODID, BotDispatch.MODID), new GolemMaterialConfig()
				.addMaterial(new ResourceLocation(BotDispatch.MODID, "manasteel"), Ingredient.of(BotaniaItems.manaSteel))
				.addStat(GolemTypes.STAT_HEALTH.get(), 150)
				.addStat(GolemTypes.STAT_ATTACK.get(), 15)
				.addModifier(BotCompatRegistry.MANA_MENDING.get(), 1)
				.addModifier(BotCompatRegistry.MANA_BOOSTING.get(), 1)
				.end()

				.addMaterial(new ResourceLocation(BotDispatch.MODID, "terrasteel"), Ingredient.of(BotaniaItems.terrasteel))
				.addStat(GolemTypes.STAT_HEALTH.get(), 300)
				.addStat(GolemTypes.STAT_ATTACK.get(), 30)
				.addModifier(BotCompatRegistry.MANA_MENDING.get(), 2)
				.addModifier(BotCompatRegistry.MANA_BOOSTING.get(), 2)
				.addModifier(BotCompatRegistry.MANA_PRODUCTION.get(), 1)
				.addModifier(BotCompatRegistry.MANA_BURST.get(), 1)
				.end()

				.addMaterial(new ResourceLocation(BotDispatch.MODID, "elementium"), Ingredient.of(BotaniaItems.elementium))
				.addStat(GolemTypes.STAT_HEALTH.get(), 200)
				.addStat(GolemTypes.STAT_ATTACK.get(), 15)
				.addModifier(BotCompatRegistry.MANA_MENDING.get(), 1)
				.addModifier(BotCompatRegistry.MANA_BOOSTING.get(), 1)
				.addModifier(BotCompatRegistry.PIXIE_ATTACK.get(), 1)
				.addModifier(BotCompatRegistry.PIXIE_COUNTERATTACK.get(), 1)
				.end()
		);
	}

}
