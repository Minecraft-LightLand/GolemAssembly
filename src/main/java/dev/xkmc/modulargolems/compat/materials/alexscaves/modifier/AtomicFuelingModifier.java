package dev.xkmc.modulargolems.compat.materials.alexscaves.modifier;

import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import dev.xkmc.modulargolems.compat.materials.alexscaves.ACCompatRegistry;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiConsumer;

public class AtomicFuelingModifier extends GolemModifier {

	private static int time() {
		return 200;
	}

	private static float health() {
		return 0.1f;
	}

	public AtomicFuelingModifier() {
		super(StatFilterType.HEALTH, 2);
	}

	@Override
	public void onAiStep(AbstractGolemEntity<?, ?> golem, int level) {
		if (golem.level().isClientSide()) return;
		if (golem.tickCount % 20 != 0) return;
		float hp = golem.getHealth();
		float max = golem.getMaxHealth();
		if (max * (1 - health()) <= hp) return;
		ItemStack uranium = golem.getProjectile(ACCompatRegistry.DUMMY_URANIUM.asStack());
		if (uranium.isEmpty()) return;
		uranium.shrink(1);
		golem.heal(max * health());
		golem.addEffect(new MobEffectInstance(ACCompatRegistry.EFF_ATOMIC.get(), time()));
		//TODO visual effects
	}

	@Override
	public void onRegisterGoals(AbstractGolemEntity<?, ?> entity, int lv, BiConsumer<Integer, Goal> addGoal) {
		entity.effectImmunity.add(ACEffectRegistry.IRRADIATED.get());
	}

}
