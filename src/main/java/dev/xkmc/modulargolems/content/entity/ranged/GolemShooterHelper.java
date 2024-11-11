package dev.xkmc.modulargolems.content.entity.ranged;

import dev.xkmc.l2library.init.FlagMarker;
import dev.xkmc.modulargolems.content.entity.humanoid.HumanoidGolemEntity;
import dev.xkmc.modulargolems.events.event.GolemBowAttackEvent;
import dev.xkmc.modulargolems.events.event.GolemThrowableEvent;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.NeoForge;

import java.util.List;

public class GolemShooterHelper {


	public static void shoot(
			ServerLevel sl,
			HumanoidGolemEntity user,
			ItemStack weapon,
			List<ItemStack> ammos,
			InteractionHand hand,
			BowItem item,
			boolean crit,
			LivingEntity target
	) {
		float f = EnchantmentHelper.processProjectileSpread(sl, weapon, user, 0.0F);
		float f1 = ammos.size() == 1 ? 0.0F : 2.0F * f / (float) (ammos.size() - 1);
		float f2 = (float) ((ammos.size() - 1) % 2) * f1 / 2.0F;
		float f3 = 1.0F;
		Vec3 param = null;
		for (int i = 0; i < ammos.size(); i++) {
			ItemStack itemstack = ammos.get(i);
			if (!itemstack.isEmpty()) {
				float f4 = f2 + f3 * (float) ((i + 1) / 2) * f1;
				f3 = -f3;
				Projectile projectile = item.createProjectile(sl, user, weapon, itemstack, crit);
				GolemBowAttackEvent event = new GolemBowAttackEvent(user, weapon, hand, projectile);
				NeoForge.EVENT_BUS.post(event);
				projectile = event.getArrow();
				if (param == null) {
					param = GolemShooterHelper.shootAimHelper(target, projectile, event.speed(), event.gravity());
				}
				var vec = param.yRot(f4);
				projectile.shoot(vec.x, vec.y, vec.z, (float) event.speed(), 0);
				projectile.getPersistentData().putInt(FlagMarker.ARROW_DESPAWN, 20);
				sl.addFreshEntity(projectile);
				weapon.hurtAndBreak(1, user, LivingEntity.getSlotForHand(hand));
				if (weapon.isEmpty()) break;
			}
		}
	}


	public static GolemThrowableEvent isValidThrowableWeapon(HumanoidGolemEntity golem, ItemStack stack, InteractionHand hand) {
		var reg = CommonHooks.resolveLookup(Registries.ENCHANTMENT);
		if (reg != null) {
			var loyalty = reg.get(Enchantments.LOYALTY);
			if (loyalty.isPresent()) {
				if (stack.getEnchantmentLevel(loyalty.get()) > 0) {
					stack = stack.copy();
					ItemEnchantments map = stack.getAllEnchantments(reg);
					var mutable = new ItemEnchantments.Mutable(map);
					mutable.set(loyalty.get(), 0);
					EnchantmentHelper.setEnchantments(stack, mutable.toImmutable());
				}
			}
		}

		GolemThrowableEvent event = new GolemThrowableEvent(golem, stack, hand);
		NeoForge.EVENT_BUS.post(event);
		return event;
	}

	public static void shootAimHelper(LivingEntity target, Projectile arrow) {
		var v = 3;
		var vec = shootAimHelper(target, arrow, v, 0.05);
		arrow.shoot(vec.x, vec.y, vec.z, v, 0);
	}

	public static Vec3 shootAimHelper(LivingEntity target, Projectile arrow, double v, double g) {
		double dx = target.getX() - arrow.getX();
		double dy = target.getY(0.5) - arrow.getY();
		double dz = target.getZ() - arrow.getZ();

		double c = dx * dx + dz * dz + dy * dy;
		boolean completed = false;
		if (target instanceof Slime) {
			var clip = target.level().clip(new ClipContext(target.position(), target.position().add(0, -3, 0),
					ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, target));
			if (clip.getType() == HitResult.Type.BLOCK) {
				dy += clip.getLocation().y() - target.getY();
				completed = true;
			}
		}
		if (!completed) {
			double rt = Math.sqrt(c) / v;
			var tv = target.getDeltaMovement();
			dx += tv.x * rt;
			dy += tv.y * rt;
			dz += tv.z * rt;
		}

		c = dx * dx + dz * dz + dy * dy;

		if (g > 0 && c > v * v * 4) {
			double a = g * g / 4;
			double b = dy * g - v * v;

			double delta = b * b - 4 * a * c;
			if (delta > 0) {
				double t21 = (-b + Math.sqrt(delta)) / (2 * a);
				double t22 = (-b - Math.sqrt(delta)) / (2 * a);
				if (t21 > 0 || t22 > 0) {
					double t2 = t21 > 0 ? t22 > 0 ? Math.min(t21, t22) : t21 : t22;
					return new Vec3(dx, dy + g * t2 / 2, dz);
				}
			}
		}
		return new Vec3(dx, dy, dz);
	}

}
