package dev.xkmc.modulargolems.compat.materials.cataclysm;

import com.github.L_Ender.cataclysm.entity.effect.Sandstorm_Entity;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.special.BaseRangedAttackGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class AncientRemnantSandstormAttackGoal extends BaseRangedAttackGoal {

	public AncientRemnantSandstormAttackGoal(AbstractGolemEntity<?, ?> golem, int lv) {
		super(100, 4, 35, golem, lv);
	}

	@Override
	protected void performAttack(LivingEntity target) {
		Vec3 diff = target.position().subtract(golem.position()).normalize();
		float angle = (float) Math.atan2(diff.z, diff.x);
		double sx = target.getX();
		double sy = target.getY();
		double sz = target.getZ();
		Sandstorm_Entity projectile = new Sandstorm_Entity(golem.level(), sx, sy, sz, 100, angle, golem.getUUID());
		golem.level().addFreshEntity(projectile);
	}

}
