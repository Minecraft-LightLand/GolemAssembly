package dev.xkmc.modulargolems.compat.materials.alexscaves.modifier;

import dev.xkmc.l2library.base.effects.api.InherentEffect;
import dev.xkmc.l2library.util.math.MathHelper;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class AtomicBoostedEffect extends InherentEffect {

	public AtomicBoostedEffect(MobEffectCategory category, int color) {
		super(category, color);
		String name = "atomic_boost";
		UUID id = MathHelper.getUUIDFromString(name);
		addAttributeModifier(Attributes.ATTACK_DAMAGE, id.toString(), 0.2, AttributeModifier.Operation.MULTIPLY_TOTAL);
		addAttributeModifier(Attributes.MOVEMENT_SPEED, id.toString(), 0.2, AttributeModifier.Operation.MULTIPLY_BASE);
	}

}
