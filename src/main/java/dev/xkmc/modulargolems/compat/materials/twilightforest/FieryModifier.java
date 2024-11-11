package dev.xkmc.modulargolems.compat.materials.twilightforest;

import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.data.MGConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;

public class FieryModifier extends GolemModifier {

	private static float getPercent() {
		return (float) (double) MGConfig.COMMON.fiery.get();
	}

	public FieryModifier() {
		super(StatFilterType.ATTACK, MAX_LEVEL);
	}

	public List<MutableComponent> getDetail(int v) {
		int reflect = Math.round((1 + getPercent() * v) * 100);
		return List.of(Component.translatable(getDescriptionId() + ".desc", reflect).withStyle(ChatFormatting.GREEN));
	}

	@Override
	public void onHurtTarget(AbstractGolemEntity<?, ?> entity, DamageData.Offence event, int level) {
		if (!event.getTarget().fireImmune()) {
			event.getTarget().setRemainingFireTicks(200);
			event.addHurtModifier(DamageModifier.multTotal(1 + getPercent() * level, getRegistryName()));
		}
	}

}
