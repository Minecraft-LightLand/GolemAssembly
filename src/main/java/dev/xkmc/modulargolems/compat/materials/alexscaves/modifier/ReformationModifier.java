package dev.xkmc.modulargolems.compat.materials.alexscaves.modifier;

import dev.xkmc.modulargolems.compat.materials.alexscaves.ACCompatRegistry;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import net.minecraft.world.item.ItemStack;

public class ReformationModifier extends GolemModifier {

	private static int max() {
		return 2;
	}

	private static float health() {
		return 10;
	}

	public ReformationModifier() {
		super(StatFilterType.HEALTH, 2);
	}

	@Override
	public void onAiStep(AbstractGolemEntity<?, ?> golem, int level) {
		if (golem.tickCount % 20 != 0) return;
		float hp = golem.getAbsorptionAmount();
		float max = max() * level * health();
		if (max <= hp) return;
		ItemStack iron = golem.getProjectile(ACCompatRegistry.DUMMY_IRON.asStack());
		if (iron.isEmpty()) return;
		iron.shrink(1);
		golem.setAbsorptionAmount(hp + health());
		//TODO visual effects
	}

}
