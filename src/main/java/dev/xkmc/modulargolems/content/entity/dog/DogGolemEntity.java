package dev.xkmc.modulargolems.content.entity.dog;

import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.goals.GolemMeleeGoal;
import dev.xkmc.modulargolems.init.data.MGConfig;
import dev.xkmc.modulargolems.init.registrate.GolemModifiers;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class DogGolemEntity extends AbstractGolemEntity<DogGolemEntity, DogGolemPartType> {

	public float getJumpStrength() {
		float ans = (float) getAttributeValue(GolemTypes.GOLEM_JUMP.holder());
		MobEffectInstance ins = getEffect(MobEffects.JUMP);
		if (ins != null) {
			int lv = ins.getAmplifier() + 1;
			ans *= (1 + lv * 0.625f);
		}
		return ans;
	}

	public DogGolemEntity(EntityType<DogGolemEntity> type, Level level) {
		super(type, level);
	}

	public float getTailAngle() {
		if (this.isAngry()) {
			return 1.5393804F;
		} else {
			float percentage = 1 - this.getHealth() / this.getMaxHealth();
			return (0.55F - percentage * 0.16F) * (float) Math.PI;
		}
	}

	// ride

	protected void tickRidden(Player player, Vec3 vec3) {
		super.tickRidden(player, vec3);
		Vec2 vec2 = this.getRiddenRotation(player);
		this.setRot(vec2.y, vec2.x);
		this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();
		if (this.isControlledByLocalInstance()) {
			if (this.onGround() || this.isInWaterOrBubble()) {
				if (player.jumping) {
					this.executeRidersJump(vec3);
				}
			}
		}

	}

	protected Vec2 getRiddenRotation(LivingEntity rider) {
		return new Vec2(rider.getXRot() * 0.5F, rider.getYRot());
	}

	protected Vec3 getRiddenInput(Player player, Vec3 input) {
		float f = player.xxa * 0.5F;
		float f1 = player.zza;
		if (f1 <= 0.0F) {
			f1 *= 0.25F;
		}
		var ans = new Vec3(f, 0.0D, f1);
		if (player.isShiftKeyDown()) {
			ans = ans.add(0, -1, 0);
		}
		return ans;
	}

	public LivingEntity getControllingPassenger() {
		Entity entity = this.getFirstPassenger();
		if (entity instanceof Player pl) {
			return pl;
		}
		if (entity instanceof AbstractGolemEntity<?, ?> pl) {
			return pl;
		}
		return null;
	}

	protected float getRiddenSpeed(Player rider) {
		return (float) (this.getAttributeValue(Attributes.MOVEMENT_SPEED) *
				MGConfig.COMMON.riddenSpeedFactor.get());
	}

	protected void executeRidersJump(Vec3 action) {
		Vec3 vec3 = this.getDeltaMovement();
		float jump = getJumpStrength();
		this.setDeltaMovement(vec3.x, jump, vec3.z);
		this.hasImpulse = true;
		CommonHooks.onLivingJump(this);
		if (action.z > 0.0D) {
			float x0 = Mth.sin(this.getYRot() * ((float) Math.PI / 180F));
			float z0 = Mth.cos(this.getYRot() * ((float) Math.PI / 180F));
			this.setDeltaMovement(this.getDeltaMovement().add(-0.4F * x0 * jump, 0.0D, 0.4F * z0 * jump));
		}
	}

	protected void positionRider(Entity rider, Entity.MoveFunction setPos) {
		int index = this.getPassengers().indexOf(rider);
		int total = this.getPassengers().size();
		if (index < 0) return;
		float width = getBbWidth();
		float offset = index == 0 ? index + 0.7f : index + (getControllingPassenger() instanceof Player ? 1.7f : 1.2f);
		float pos = width / 2 - width / total * offset;
		Vec3 off = new Vec3(0, 0, pos).yRot(-this.yBodyRot * ((float) Math.PI / 180F));
		Vec3 vec3 = this.getPassengerRidingPosition(rider).add(off);
		Vec3 vec31 = rider.getVehicleAttachmentPoint(this);
		setPos.accept(rider, vec3.x - vec31.x, vec3.y - vec31.y, vec3.z - vec31.z);
		if (index > 0) {
			this.clampRotation(rider);
		}
	}

	public void onPassengerTurned(Entity rider) {
		if (this.getControllingPassenger() != rider) {
			this.clampRotation(rider);
		}
	}

	private void clampRotation(Entity rider) {
		rider.setYBodyRot(this.getYRot());
		float yr0 = rider.getYRot();
		float dyr = Mth.wrapDegrees(yr0 - this.getYRot());
		float yr1 = Mth.clamp(dyr, -160.0F, 160.0F);
		rider.yRotO += yr1 - dyr;
		float yr2 = yr0 + yr1 - dyr;
		rider.setYRot(yr2);
		rider.setYHeadRot(yr2);
	}

	protected boolean canAddPassenger(Entity entity) {
		return this.getPassengers().size() <= getModifiers().getOrDefault(GolemModifiers.SIZE_UPGRADE.get(), 0);
	}

	@Override
	protected void addPassenger(Entity rider) {
		setInSittingPose(false);
		super.addPassenger(rider);
	}

	// sit

	protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(DogGolemEntity.class, EntityDataSerializers.BYTE);

	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(DATA_FLAGS_ID, (byte) 0);
	}

	public void addAdditionalSaveData(CompoundTag p_21819_) {
		super.addAdditionalSaveData(p_21819_);
		p_21819_.putBoolean("Sitting", isInSittingPose());
	}

	public void readAdditionalSaveData(CompoundTag p_21815_) {
		super.readAdditionalSaveData(p_21815_);
		this.setInSittingPose(p_21815_.getBoolean("Sitting"));
	}

	public boolean isInSittingPose() {
		return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
	}

	public void setInSittingPose(boolean sit) {
		byte b0 = this.entityData.get(DATA_FLAGS_ID);
		this.getNavigation().stop();
		this.setTarget(null);
		if (sit) {
			this.entityData.set(DATA_FLAGS_ID, (byte) (b0 | 1));
		} else {
			this.entityData.set(DATA_FLAGS_ID, (byte) (b0 & -2));
		}

	}

	// ------ vanilla golem behavior

	protected void registerGoals() {
		this.goalSelector.addGoal(2, new GolemMeleeGoal(this));
		super.registerGoals();
	}

	@Override
	protected boolean predicatePriorityTarget(LivingEntity e) {
		return !isInSittingPose() && super.predicatePriorityTarget(e);
	}

	@Override
	protected boolean predicateSecondaryTarget(LivingEntity e) {
		return !isInSittingPose() && super.predicateSecondaryTarget(e);
	}

	public boolean hurt(DamageSource source, float amount) {
		if (!this.level().isClientSide) {
			this.setInSittingPose(false);
		}
		return super.hurt(source, amount);
	}

	protected SoundEvent getHurtSound(DamageSource p_28872_) {
		return SoundEvents.WOLF_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.WOLF_DEATH;
	}

	protected void playStepSound(BlockPos p_28864_, BlockState p_28865_) {
		this.playSound(SoundEvents.WOLF_STEP, 1.0F, 1.0F);
	}

	public Vec3 getLeashOffset() {
		return new Vec3(0.0D, 0.6F * this.getEyeHeight(), this.getBbWidth() * 0.4F);
	}

	protected InteractionResult mobInteractImpl(Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (MGConfig.COMMON.strictInteract.get() && !itemstack.isEmpty())
			return InteractionResult.PASS;
		if (!player.isShiftKeyDown() && itemstack.isEmpty())
			return super.mobInteractImpl(player, hand);
		else {
			if (!level().isClientSide()) {
				ItemStack armor = getBodyArmorItem();
				if (!armor.isEmpty() && armor.getItem() instanceof AnimalArmorItem ani) {
					if (ani.getMaterial().value().repairIngredient().get().test(itemstack) && armor.isDamaged()) {
						itemstack.shrink(1);
						playSound(SoundEvents.WOLF_ARMOR_REPAIR);
						int i = (int) (armor.getMaxDamage() * 0.125F);
						armor.setDamageValue(Math.max(0, armor.getDamageValue() - i));
						return InteractionResult.SUCCESS;
					}
				}
				if (canModify(player)) {
					if (itemstack.is(Items.WOLF_ARMOR)) {//TODO
						if (getItemBySlot(EquipmentSlot.BODY).isEmpty()) {
							if (!level().isClientSide()) {
								setItemSlot(EquipmentSlot.BODY, itemstack.split(1));
							}
							return InteractionResult.CONSUME;
						}
					}
				}
				setInSittingPose(!isInSittingPose());
			}
			return InteractionResult.SUCCESS;
		}
	}

	@Override
	public void setTarget(@Nullable LivingEntity target) {
		if (target != null && isInSittingPose()) {
			return;
		}
		super.setTarget(target);
	}

	// armor

	protected void actuallyHurt(DamageSource source, float amount) {
		if (!this.canArmorAbsorb(source)) {
			super.actuallyHurt(source, amount);
		} else {
			ItemStack stack = getBodyArmorItem();
			int i = stack.getDamageValue();
			int j = stack.getMaxDamage();
			stack.hurtAndBreak(Mth.ceil(amount), this, EquipmentSlot.BODY);
			if (Crackiness.WOLF_ARMOR.byDamage(i, j) != Crackiness.WOLF_ARMOR.byDamage(this.getBodyArmorItem())) {
				this.playSound(SoundEvents.WOLF_ARMOR_CRACK);
				if (level() instanceof ServerLevel serverlevel) {
					serverlevel.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, Items.ARMADILLO_SCUTE.getDefaultInstance()),
							getX(), getY() + 1.0, getZ(), 20, 0.2, 0.1, 0.2, 0.1);
				}
			}
		}
	}

	private boolean canArmorAbsorb(DamageSource source) {
		if (source.is(DamageTypeTags.BYPASSES_WOLF_ARMOR) || source.is(DamageTypeTags.BYPASSES_ARMOR)) {
			return false;
		}
		return this.hasArmor();
	}

	protected void hurtArmor(DamageSource source, float amount) {
		this.doHurtEquipment(source, amount, EquipmentSlot.BODY);
	}

	public boolean hasArmor() {
		return getBodyArmorItem().is(Items.WOLF_ARMOR);//TODO
	}

}

