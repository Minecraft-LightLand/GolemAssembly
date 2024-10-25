package dev.xkmc.modulargolems.content.entity.ranged;

import dev.xkmc.modulargolems.content.entity.humanoid.HumanoidGolemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;

import java.util.EnumSet;

public class GolemBowAttackGoal extends Goal {

	private static double attackRadiusSqr() {
		return 25 * 25;
	}

	private final HumanoidGolemEntity mob;
	private final double speedModifier;
	private int attackIntervalMin;
	private int attackTime = -1;
	private int seeTime;
	private boolean strafingClockwise;
	private boolean strafingBackwards;
	private int strafingTime = -1;

	public GolemBowAttackGoal(HumanoidGolemEntity pMob, double pSpeedModifier, int pAttackIntervalMin) {
		this.mob = pMob;
		this.speedModifier = pSpeedModifier;
		this.attackIntervalMin = pAttackIntervalMin;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	public void setMinAttackInterval(int pAttackCooldown) {
		this.attackIntervalMin = pAttackCooldown;
	}

	/**
	 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
	 * method as well.
	 */
	public boolean canUse() {
		return this.mob.getTarget() != null && this.isHoldingBow() && !this.mob.getProjectile(
				this.mob.getItemInHand(mob.getWeaponHand())).isEmpty();
	}

	protected boolean isHoldingBow() {
		return this.mob.isHolding(is -> is.getItem() instanceof BowItem);
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean canContinueToUse() {
		return (this.canUse() || !this.mob.getNavigation().isDone()) && this.isHoldingBow();
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void start() {
		super.start();
		mob.setAggressive(true);
		mob.setInRangeAttack(true);
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by another one
	 */
	public void stop() {
		super.stop();
		this.mob.setAggressive(false);
		mob.setInRangeAttack(false);
		this.seeTime = 0;
		this.attackTime = -1;
		this.mob.stopUsingItem();
	}

	public boolean requiresUpdateEveryTick() {
		return true;
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void tick() {
		LivingEntity livingentity = this.mob.getTarget();
		if (livingentity != null) {
			double d0 = this.mob.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
			boolean flag = this.mob.getSensing().hasLineOfSight(livingentity);
			boolean flag1 = this.seeTime > 0;
			if (flag != flag1) {
				this.seeTime = 0;
			}

			if (flag) {
				++this.seeTime;
			} else {
				--this.seeTime;
			}

			if (!(d0 > attackRadiusSqr()) && this.seeTime >= 20) {
				this.mob.getNavigation().stop();
				++this.strafingTime;
			} else {
				this.mob.getNavigation().moveTo(livingentity, this.speedModifier);
				this.strafingTime = -1;
			}

			if (this.strafingTime >= 20) {
				if ((double) this.mob.getRandom().nextFloat() < 0.3D) {
					this.strafingClockwise = !this.strafingClockwise;
				}

				if ((double) this.mob.getRandom().nextFloat() < 0.3D) {
					this.strafingBackwards = !this.strafingBackwards;
				}

				this.strafingTime = 0;
			}

			if (this.strafingTime > -1) {
				if (d0 > attackRadiusSqr() * 0.75) {
					this.strafingBackwards = false;
				} else if (d0 < attackRadiusSqr() * 0.25) {
					this.strafingBackwards = true;
				}

				this.mob.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
				this.mob.lookAt(livingentity, 30.0F, 30.0F);
			} else {
				this.mob.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
			}

			if (this.mob.isUsingItem()) {
				if (!flag && this.seeTime < -60) {
					this.mob.stopUsingItem();
				} else if (flag) {
					int i = this.mob.getTicksUsingItem();
					if (i >= 20) {
						this.mob.stopUsingItem();
						this.mob.performRangedAttack(livingentity, BowItem.getPowerForTime(i));
						this.attackTime = this.attackIntervalMin;
					}
				}
			} else if (--this.attackTime <= 0 && this.seeTime >= -60) {
				this.mob.startUsingItem(ProjectileUtil.getWeaponHoldingHand(this.mob, item -> item instanceof BowItem));
			}

		}
	}
}
