package dev.xkmc.modulargolems.compat.musket;

import dev.xkmc.modulargolems.content.entity.humanoid.HumanoidGolemEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public class GolemMusketGoal extends RangedGunAttackGoal<HumanoidGolemEntity> {
	private static final double speedModifier = 1.0;
	private static final float attackRadius = 15.0F;
	private int seeTime;
	private int attackDelay;
	private boolean strafingClockwise;
	private boolean strafingBackwards;
	private int strafingTime = -1;

	public GolemMusketGoal(HumanoidGolemEntity mob) {
		super(mob);
	}

	public boolean canContinueToUse() {
		return (this.isTargetValid() || !this.mob.getNavigation().isDone()) && this.canUseGun();
	}

	public void start() {
		super.start();
		mob.setAggressive(true);
		mob.setInRangeAttack(true);
	}

	public void stop() {
		super.stop();
		this.seeTime = 0;
		this.attackDelay = 0;
		mob.setInRangeAttack(false);
	}

	public void tick() {
		super.tick();
		LivingEntity target = mob.getTarget();
		if (target != null) {
			boolean canSee = mob.getSensing().hasLineOfSight(target);
			boolean wasSeeing = this.seeTime > 0;
			if (canSee != wasSeeing) {
				this.seeTime = 0;
			}

			if (canSee) {
				++this.seeTime;
			} else {
				--this.seeTime;
			}

			float dist = mob.distanceTo(target);
			if (dist < 15.0F && this.seeTime >= 20) {
				mob.getNavigation().stop();
				++this.strafingTime;
			} else {
				mob.getNavigation().moveTo(target, 1.0);
				this.strafingTime = -1;
			}

			if (this.strafingTime >= 20) {
				if (mob.getRandom().nextFloat() < 0.3F) {
					this.strafingClockwise = !this.strafingClockwise;
				}

				if (mob.getRandom().nextFloat() < 0.3F) {
					this.strafingBackwards = !this.strafingBackwards;
				}

				this.strafingTime = 0;
			}

			if (this.strafingTime > -1) {
				if (dist > attackRadius * 0.75) {
					this.strafingBackwards = false;
				} else if (dist < attackRadius * 0.25) {
					this.strafingBackwards = true;
				}

				mob.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
				Entity var6 = mob.getControlledVehicle();
				if (var6 instanceof Mob vehicle) {
					vehicle.lookAt(target, 30.0F, 30.0F);
				}

				mob.lookAt(target, 30.0F, 30.0F);
			} else {
				mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
			}

			if (this.seeTime < -60) {
				this.attackDelay = Math.max(20, this.attackDelay);
			}

			if (this.attackDelay > 0) {
				--this.attackDelay;
			} else if (this.isReady()) {
				if (canSee) {
					this.fire(0);
					this.attackDelay = 10;
				}
			} else {
				this.reload();
			}

		}
	}

	public void onReady() {
		this.attackDelay = Math.max(20, this.attackDelay);
	}
}