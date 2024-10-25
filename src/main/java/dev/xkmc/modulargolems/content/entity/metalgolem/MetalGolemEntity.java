package dev.xkmc.modulargolems.content.entity.metalgolem;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.modulargolems.content.config.GolemMaterialConfig;
import dev.xkmc.modulargolems.content.entity.common.SweepGolemEntity;
import dev.xkmc.modulargolems.content.entity.goals.GolemMeleeGoal;
import dev.xkmc.modulargolems.init.advancement.GolemTriggers;
import dev.xkmc.modulargolems.init.data.MGConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class MetalGolemEntity extends SweepGolemEntity<MetalGolemEntity, MetalGolemPartType> {
	public MetalGolemEntity(EntityType<MetalGolemEntity> type, Level level) {
		super(type, level);
		this.setMaxUpStep(1);
	}

	protected boolean performDamageTarget(Entity target, float damage, double kb) {
		if (target instanceof LivingEntity le) {
			le.setLastHurtByPlayer(getOwner());
			damage += EnchantmentHelper.getDamageBonus(this.getMainHandItem(), le.getMobType());
			kb += (float) EnchantmentHelper.getKnockbackBonus(this);
		}
		boolean succeed = target.hurt(level().damageSources().mobAttack(this), damage);
		if (succeed) {
			double d1 = Math.max(0.0D, 1.0D - kb);
			double dokb = getAttributeValue(Attributes.ATTACK_KNOCKBACK) * 0.4;
			target.setDeltaMovement(target.getDeltaMovement().add(0.0D, dokb * d1, 0.0D));
			this.doEnchantDamageEffects(this, target);
		}
		return succeed;
	}

	// ------ vanilla golem behavior

	private int attackAnimationTick;

	protected void registerGoals() {
		this.goalSelector.addGoal(2, new GolemMeleeGoal(this));
		super.registerGoals();
	}

	public void aiStep() {
		super.aiStep();
		if (this.attackAnimationTick > 0) {
			--this.attackAnimationTick;
		}
		if (this.getDeltaMovement().horizontalDistanceSqr() > (double) 2.5000003E-7F && this.random.nextInt(5) == 0) {
			int i = Mth.floor(this.getX());
			int j = Mth.floor(this.getY() - (double) 0.2F);
			int k = Mth.floor(this.getZ());
			BlockPos pos = new BlockPos(i, j, k);
			BlockState blockstate = this.level().getBlockState(pos);
			if (!blockstate.isAir()) {
				this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockstate).setPos(pos), this.getX() + ((double) this.random.nextFloat() - 0.5D) * (double) this.getBbWidth(), this.getY() + 0.1D, this.getZ() + ((double) this.random.nextFloat() - 0.5D) * (double) this.getBbWidth(), 4.0D * ((double) this.random.nextFloat() - 0.5D), 0.5D, ((double) this.random.nextFloat() - 0.5D) * 4.0D);
			}
		}
	}

	public boolean doHurtTarget(Entity target) {
		this.attackAnimationTick = 10;
		this.level().broadcastEntityEvent(this, (byte) 4);
		float damage = this.getAttackDamage();
		double kb;
		if (target instanceof LivingEntity livingentity) {
			kb = livingentity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
		} else {
			kb = 0;
		}
		boolean flag = performRangedDamage(target, damage, kb);
		this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
		return flag;
	}

	public boolean hurt(DamageSource source, float amount) {
		IronGolem.Crackiness crack = this.getCrackiness();
		boolean flag = super.hurt(source, amount);
		if (flag && this.getCrackiness() != crack) {
			this.playSound(SoundEvents.IRON_GOLEM_DAMAGE, 1.0F, 1.0F);
		}
		return flag;
	}

	public IronGolem.Crackiness getCrackiness() {
		return IronGolem.Crackiness.byFraction(this.getHealth() / this.getMaxHealth());
	}

	public void handleEntityEvent(byte event) {
		if (event == 4) {
			this.attackAnimationTick = 10;
			this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
		} else {
			super.handleEntityEvent(event);
		}

	}

	public int getAttackAnimationTick() {
		return this.attackAnimationTick;
	}

	protected SoundEvent getHurtSound(DamageSource p_28872_) {
		return SoundEvents.IRON_GOLEM_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.IRON_GOLEM_DEATH;
	}

	protected void playStepSound(BlockPos p_28864_, BlockState p_28865_) {
		this.playSound(SoundEvents.IRON_GOLEM_STEP, 1.0F, 1.0F);
	}

	public boolean checkSpawnObstruction(LevelReader p_28853_) {
		BlockPos blockpos = this.blockPosition();
		BlockPos blockpos1 = blockpos.below();
		BlockState blockstate = p_28853_.getBlockState(blockpos1);
		if (!blockstate.entityCanStandOn(p_28853_, blockpos1, this)) {
			return false;
		} else {
			for (int i = 1; i < 3; ++i) {
				BlockPos blockpos2 = blockpos.above(i);
				BlockState blockstate1 = p_28853_.getBlockState(blockpos2);
				if (!NaturalSpawner.isValidEmptySpawnBlock(p_28853_, blockpos2, blockstate1, blockstate1.getFluidState(), EntityType.IRON_GOLEM)) {
					return false;
				}
			}

			return NaturalSpawner.isValidEmptySpawnBlock(p_28853_, blockpos, p_28853_.getBlockState(blockpos), Fluids.EMPTY.defaultFluidState(), EntityType.IRON_GOLEM) && p_28853_.isUnobstructed(this);
		}
	}

	public Vec3 getLeashOffset() {
		return new Vec3(0.0D, 0.875F * this.getEyeHeight(), this.getBbWidth() * 0.4F);
	}

	protected InteractionResult mobInteractImpl(Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (getMaterials().size() != MetalGolemPartType.values().length)
			return super.mobInteractImpl(player, hand);
		var mat = getMaterials().get(MetalGolemPartType.BODY.ordinal());
		Ingredient ing = GolemMaterialConfig.get().ingredients.get(mat.id());
		if (!ing.test(itemstack)) {
			if (MGConfig.COMMON.strictInteract.get() && !itemstack.isEmpty())
				return InteractionResult.PASS;
			return super.mobInteractImpl(player, hand);
		} else {
			float f = this.getHealth();
			this.heal(getMaxHealth() / 4f);
			if (this.getHealth() == f) {
				return InteractionResult.PASS;
			} else {
				float f1 = 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F;
				this.playSound(SoundEvents.IRON_GOLEM_REPAIR, 1.0F, f1);
				if (!player.getAbilities().instabuild) {
					itemstack.shrink(1);
				}
				if (!this.level().isClientSide()) {
					GolemTriggers.HOT_FIX.trigger((ServerPlayer) player);
				}
				return InteractionResult.sidedSuccess(this.level().isClientSide);
			}
		}
	}

}
