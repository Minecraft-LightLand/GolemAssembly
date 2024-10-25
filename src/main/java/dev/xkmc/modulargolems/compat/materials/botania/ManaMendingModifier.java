package dev.xkmc.modulargolems.compat.materials.botania;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.init.data.MGConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;

public class ManaMendingModifier extends ManaModifier {

	public ManaMendingModifier() {
		super(StatFilterType.HEALTH, MAX_LEVEL);
	}

	@Override
	public double onHealTick(double heal, AbstractGolemEntity<?, ?> le, int level) {
		var healthDiff = le.getMaxHealth() - le.getHealth() - heal;
		var cost = MGConfig.COMMON.manaMendingCost.get();
		int maxHeal = (int) Math.floor(Math.min(healthDiff, MGConfig.COMMON.manaMendingVal.get() * level));
		if (maxHeal <= 0) return heal;
		int maxCost = maxHeal * cost;
		int consume = new BotUtils(le).consumeMana(maxCost);
		double toHeal = 1d * consume / cost;
		return heal + toHeal;
	}

	public List<MutableComponent> getDetail(int v) {
		int eff = MGConfig.COMMON.manaMendingCost.get();
		double val = MGConfig.COMMON.manaMendingVal.get() * v;
		return List.of(Component.translatable(getDescriptionId() + ".desc", val, eff).withStyle(ChatFormatting.GREEN));
	}
}
