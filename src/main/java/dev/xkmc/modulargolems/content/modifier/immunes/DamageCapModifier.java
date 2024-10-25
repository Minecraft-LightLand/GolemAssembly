package dev.xkmc.modulargolems.content.modifier.immunes;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.data.MGConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

import java.util.List;

public class DamageCapModifier extends GolemModifier {

	public DamageCapModifier() {
		super(StatFilterType.HEALTH, 5);
	}

	@Override
	public void onDamaged(AbstractGolemEntity<?, ?> entity, LivingDamageEvent event, int level) {
		if (event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
			return;
		}
		float factor = (float) Math.max(0, (2 - level * 0.2) * MGConfig.COMMON.damageCap.get());
		if (event.getAmount() > factor * entity.getMaxHealth()) {
			event.setAmount(factor * entity.getMaxHealth());
			entity.level().broadcastEntityEvent(entity, EntityEvent.ATTACK_BLOCKED);
		}
	}

	@Override
	public List<MutableComponent> getDetail(int level) {
		float factor = (float) Math.max(0, (2 - level * 0.2) * MGConfig.COMMON.damageCap.get());
		int perc = Math.round(100 * factor);
		return List.of(Component.translatable(getDescriptionId() + ".desc", perc).withStyle(ChatFormatting.GREEN));
	}
}
