package dev.xkmc.modulargolems.compat.materials.twilightforest;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.data.MGConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import twilightforest.init.TFDimension;

import java.util.List;

public class TFHealingModifier extends GolemModifier {

	public TFHealingModifier() {
		super(StatFilterType.HEALTH, MAX_LEVEL);
	}

	@Override
	public double onInventoryHealTick(double heal, HealingContext ctx, int level) {
		if (ctx.owner().level().dimension().equals(TFDimension.DIMENSION_KEY)) {
			return heal * (1 + MGConfig.COMMON.compatTFHealing.get() * level);
		}
		return heal;
	}

	public List<MutableComponent> getDetail(int v) {
		int bonus = (int) Math.round((MGConfig.COMMON.compatTFHealing.get() * v) * 100);
		return List.of(Component.translatable(getDescriptionId() + ".desc", bonus).withStyle(ChatFormatting.GREEN));
	}

}
