package dev.xkmc.modulargolems.compat.materials.alexscaves.modifier;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.data.MGConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

import java.util.List;

public class PolarizeModifier extends GolemModifier {

	public static double range(int lv) {
		return (4 + lv) * 0.2 * MGConfig.COMMON.polarizeRange.get();
	}

	public static double damage(int lv) {
		return (1 + lv) / 2f * MGConfig.COMMON.polarizeDamage.get();
	}

	public static double force() {
		return MGConfig.COMMON.polarizeForce.get();
	}

	public PolarizeModifier() {
		super(StatFilterType.MASS, 4);
	}

	@Override
	public void onClientTick(AbstractGolemEntity<?, ?> golem, int level) {
		if (golem.tickCount % 5 != 0) return;
		if (!golem.isAggressive()) return;
		double r = range(level);
		boolean ranged = golem.isInRangedMode();
		Vec3 src = golem.position().add(0.0, 0.2, 0.0);
		float particleMax = (float) (5 + golem.getRandom().nextInt(5));
		for (int particles = 0; (float) particles < particleMax; ++particles) {
			Vec3 dst = new Vec3(
					(golem.getRandom().nextFloat() - 0.5) * 0.3,
					(golem.getRandom().nextFloat() - 0.5) * 0.3,
					(r * 0.5F + r * 0.5F * golem.getRandom().nextFloat())
			).yRot((float) ((particles / particleMax) * Math.PI * 2.0)).add(src);
			if (!ranged) {
				golem.level().addParticle(ACParticleRegistry.SCARLET_SHIELD_LIGHTNING.get(),
						dst.x, dst.y, dst.z, src.x, src.y, src.z);
			} else {
				golem.level().addParticle(ACParticleRegistry.AZURE_SHIELD_LIGHTNING.get(),
						src.x, src.y, src.z, dst.x, dst.y, dst.z);
			}
		}
	}

	@Override
	public void onAiStep(AbstractGolemEntity<?, ?> golem, int level) {
		var target = golem.getTarget();
		if (target == null) return;
		boolean dmg = golem.tickCount % 10 == 0;
		float damage = (float) damage(level);
		double r = range(level);
		boolean ranged = golem.isInRangedMode();
		var aabb = golem.getBoundingBox().inflate(r);
		List<LivingEntity> list = golem.level().getEntities(EntityTypeTest.forClass(LivingEntity.class), aabb,
				e -> e != golem && golem.canAttack(e));
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
			if (ranged) {
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

	@Override
	public List<MutableComponent> getDetail(int v) {
		int num = (int) Math.round(damage(v));
		var val = Component.literal("" + num).withStyle(ChatFormatting.AQUA);
		return List.of(Component.translatable(getDescriptionId() + ".desc", val).withStyle(ChatFormatting.GREEN));
	}


}
