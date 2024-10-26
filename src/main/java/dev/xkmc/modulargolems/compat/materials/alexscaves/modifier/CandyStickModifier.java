package dev.xkmc.modulargolems.compat.materials.alexscaves.modifier;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class CandyStickModifier extends GolemModifier {

	private static int time() {
		return 200;//TODO
	}

	public CandyStickModifier() {
		super(StatFilterType.ATTACK, 2);
	}

	@Override
	public void onHurtTarget(AbstractGolemEntity<?, ?> entity, LivingHurtEvent event, int level) {
		event.getEntity().addEffect(new MobEffectInstance(
				MobEffects.MOVEMENT_SLOWDOWN, time(), level * 2 - 1
		));
	}

}
