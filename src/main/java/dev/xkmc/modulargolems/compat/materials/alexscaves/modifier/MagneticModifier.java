package dev.xkmc.modulargolems.compat.materials.alexscaves.modifier;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

import java.util.List;

public class MagneticModifier extends GolemModifier {

	public static double range() {
		return 5;//TODO
	}

	public static double force() {
		return 0.04;//TODO
	}

	public MagneticModifier() {
		super(StatFilterType.MASS, 4);
	}

	@Override
	public void onAiStep(AbstractGolemEntity<?, ?> golem, int level) {
		if (golem.level().isClientSide()) {
			//TODO
			return;
		}
		var target = golem.getTarget();
		if (target == null) return;
		double r = (4 + level) * 0.2 * range();
		boolean dmg = golem.tickCount % 10 == 0;
		float damage = (1 + level) / 2f;

		List<LivingEntity> list;
		if (!golem.canSweep()) {
			list = List.of(target);
		} else {
			var aabb = golem.getBoundingBox().inflate(r);
			list = golem.level().getEntities(EntityTypeTest.forClass(LivingEntity.class), aabb,
					e -> e != golem && golem.canAttack(e));
		}
		Vec3 pos = golem.position().add(0, golem.getBbHeight() / 2, 0);
		Vec3 forward = target.position().add(0, target.getBbHeight() / 2, 0).subtract(pos);
		forward = forward.length() < 1 ? golem.getForward().normalize() : forward.normalize();
		double val = golem.getAttributeValue(ForgeMod.ENTITY_REACH.get()) * 0.8;
		if (val < 1) val = 1;
		forward = forward.scale(val);
		for (var e : list) {
			Vec3 tar = e.position().add(0, e.getBbHeight() / 2, 0);
			if (tar.distanceTo(pos) > r) continue;
			Vec3 dir;
			if (golem.isInRangedMode()) {
				dir = tar.subtract(pos);
				if (dir.length() > 1) dir = dir.normalize();
			} else {
				dir = pos.add(forward).subtract(tar);
			}
			if (dir.length() > 1) dir = dir.normalize();
			dir = dir.scale(force());
			e.push(dir.x, dir.y, dir.z);
			if (dmg) e.hurt(golem.damageSources().mobAttack(golem), damage);
		}
	}

}
