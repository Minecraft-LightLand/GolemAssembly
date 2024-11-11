package dev.xkmc.modulargolems.compat.materials.twilightforest;

import dev.xkmc.l2core.serial.config.ConfigDataProvider;
import dev.xkmc.modulargolems.content.config.GolemMaterialConfig;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.registrate.GolemModifiers;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import twilightforest.data.tags.ItemTagGenerator;

import java.util.concurrent.CompletableFuture;

public class TFConfigGen extends ConfigDataProvider {

	public TFConfigGen(DataGenerator generator, CompletableFuture<HolderLookup.Provider> pvd) {
		super(generator, pvd, "Golem Config for Twilight Forest");
	}

	public void add(Collector map) {
		map.add(ModularGolems.MATERIALS, ResourceLocation.fromNamespaceAndPath(TFDispatch.MODID, TFDispatch.MODID), new GolemMaterialConfig()
				.addMaterial(ResourceLocation.fromNamespaceAndPath(TFDispatch.MODID, "ironwood"), Ingredient.of(ItemTagGenerator.IRONWOOD_INGOTS))
				.addStat(GolemTypes.STAT_HEALTH.get(), 200)
				.addStat(GolemTypes.STAT_ATTACK.get(), 10)
				.addStat(GolemTypes.STAT_REGEN.get(), 2)
				.addModifier(TFCompatRegistry.TF_DAMAGE.get(), 1)
				.addModifier(TFCompatRegistry.TF_HEALING.get(), 1).end()

				.addMaterial(ResourceLocation.fromNamespaceAndPath(TFDispatch.MODID, "steeleaf"), Ingredient.of(ItemTagGenerator.STEELEAF_INGOTS))
				.addStat(GolemTypes.STAT_HEALTH.get(), 20)
				.addStat(GolemTypes.STAT_ATTACK.get(), 24)
				.addModifier(TFCompatRegistry.TF_DAMAGE.get(), 1)
				.addModifier(TFCompatRegistry.TF_HEALING.get(), 1).end()

				.addMaterial(ResourceLocation.fromNamespaceAndPath(TFDispatch.MODID, "knightmetal"), Ingredient.of(ItemTagGenerator.KNIGHTMETAL_INGOTS))
				.addStat(GolemTypes.STAT_HEALTH.get(), 300)
				.addStat(GolemTypes.STAT_ATTACK.get(), 20)
				.addStat(GolemTypes.STAT_WEIGHT.get(), -0.4)
				.addStat(GolemTypes.STAT_KBRES.get(), 1)
				.addModifier(GolemModifiers.THORN.get(), 2)
				.addModifier(TFCompatRegistry.TF_DAMAGE.get(), 1)
				.addModifier(TFCompatRegistry.TF_HEALING.get(), 1)
				.end()

				.addMaterial(ResourceLocation.fromNamespaceAndPath(TFDispatch.MODID, "fiery"), Ingredient.of(ItemTagGenerator.FIERY_INGOTS))
				.addStat(GolemTypes.STAT_HEALTH.get(), 200)
				.addStat(GolemTypes.STAT_ATTACK.get(), 20)
				.addModifier(GolemModifiers.FIRE_IMMUNE.get(), 1)
				.addModifier(GolemModifiers.THORN.get(), 1)
				.addModifier(TFCompatRegistry.FIERY.get(), 1)
				.addModifier(TFCompatRegistry.TF_DAMAGE.get(), 1)
				.addModifier(TFCompatRegistry.TF_HEALING.get(), 1)
				.end()
		);
	}

}
