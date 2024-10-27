package dev.xkmc.modulargolems.compat.materials.alexscaves.modifier;

import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class RadiationModifier extends GolemModifier {

	private static int time() {
		return 200;//TODO
	}

	private static float atk() {
		return 0.2f;//TODO
	}

	public RadiationModifier() {
		super(StatFilterType.ATTACK, 2);
	}

	@Override
	public void onHurtTarget(AbstractGolemEntity<?, ?> entity, LivingHurtEvent event, int level) {
		event.getEntity().addEffect(new MobEffectInstance(
				ACEffectRegistry.IRRADIATED.get(), time(), level * 2 - 1
		));
	}

	@Override
	public void modifyDamage(AttackCache cache, AbstractGolemEntity<?, ?> entity, int level) {
		var ins = cache.getAttackTarget().getEffect(ACEffectRegistry.IRRADIATED.get());
		if (ins == null) return;
		cache.addHurtModifier(DamageModifier.multTotal(1 + (ins.getAmplifier() + 1) * atk()));
	}
}
