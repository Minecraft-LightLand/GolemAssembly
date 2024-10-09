package dev.xkmc.modulargolems.content.entity.humanoid;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.modulargolems.content.entity.common.SweepGolemEntity;
import dev.xkmc.modulargolems.content.entity.dog.DogGolemEntity;
import dev.xkmc.modulargolems.content.entity.goals.GolemMeleeGoal;
import dev.xkmc.modulargolems.content.entity.ranged.GolemBowAttackGoal;
import dev.xkmc.modulargolems.content.entity.ranged.GolemCrossbowAttackGoal;
import dev.xkmc.modulargolems.content.entity.ranged.GolemShooterHelper;
import dev.xkmc.modulargolems.content.entity.ranged.GolemTridentAttackGoal;
import dev.xkmc.modulargolems.content.item.golem.GolemHolder;
import dev.xkmc.modulargolems.events.event.*;
import dev.xkmc.modulargolems.init.advancement.GolemTriggers;
import dev.xkmc.modulargolems.init.data.MGConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolActions;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.Predicate;

@SerialClass
public class HumanoidGolemEntity extends SweepGolemEntity<HumanoidGolemEntity, HumaniodGolemPartType> implements CrossbowAttackMob {

	private static final EntityDataAccessor<Boolean> IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(HumanoidGolemEntity.class, EntityDataSerializers.BOOLEAN);

	private final GolemBowAttackGoal bowGoal = new GolemBowAttackGoal(this, 1.0D, 20);
	private final GolemCrossbowAttackGoal crossbowGoal = new GolemCrossbowAttackGoal(this, 1.0D, 15.0F);
	private final GolemMeleeGoal meleeGoal = new GolemMeleeGoal(this);
	private final GolemTridentAttackGoal tridentGoal = new GolemTridentAttackGoal(this, 1, 40, 15, meleeGoal);
	@SerialClass.SerialField(toClient = true)
	public int shieldCooldown = 0;
	@SerialClass.SerialField
	private ItemStack backupHand = ItemStack.EMPTY;
	@SerialClass.SerialField
	private ItemStack arrowSlot = ItemStack.EMPTY;

	public HumanoidGolemEntity(EntityType<HumanoidGolemEntity> type, Level level) {
		super(type, level);
		if (!this.level().isClientSide) {
			reassessWeaponGoal();
			this.groundNavigation.setCanOpenDoors(true);
		}
	}

	public void reassessWeaponGoal() {
		if (!this.level().isClientSide) {
			this.goalSelector.removeGoal(this.meleeGoal);
			this.goalSelector.removeGoal(this.bowGoal);
			this.goalSelector.removeGoal(this.crossbowGoal);
			this.goalSelector.removeGoal(this.tridentGoal);
			InteractionHand hand = getWeaponHand();
			ItemStack weapon = getItemInHand(hand);
			if (!weapon.isEmpty() && GolemShooterHelper.isValidThrowableWeapon(this, weapon, hand).isThrowable()) {
				this.goalSelector.addGoal(2, this.tridentGoal);
				this.goalSelector.addGoal(3, this.meleeGoal);
				return;
			}
			if (!weapon.isEmpty() && weapon.getItem() instanceof BowItem) {
				this.bowGoal.setMinAttackInterval(20);
				this.goalSelector.addGoal(2, this.bowGoal);
				return;
			}
			if (!weapon.isEmpty() && weapon.getItem() instanceof CrossbowItem) {
				this.goalSelector.addGoal(2, this.crossbowGoal);
				return;
			}
			this.goalSelector.addGoal(2, this.meleeGoal);
		}
	}

	public ItemStack getProjectile(ItemStack pShootable) {
		if (pShootable.getItem() instanceof ProjectileWeaponItem) {
			Predicate<ItemStack> predicate = ((ProjectileWeaponItem) pShootable.getItem()).getSupportedHeldProjectiles();
			ItemStack stack = ProjectileWeaponItem.getHeldProjectile(this, predicate);
			if (stack.isEmpty() && !arrowSlot.isEmpty() && predicate.test(arrowSlot)) {
				stack = arrowSlot;
			}
			return ForgeHooks.getProjectile(this, pShootable, stack);
		} else {
			return ForgeHooks.getProjectile(this, pShootable, ItemStack.EMPTY);
		}
	}

	public void readAdditionalSaveData(CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		this.reassessWeaponGoal();
	}

	public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) {
		super.setItemSlot(pSlot, pStack);
		if (!this.level().isClientSide) {
			doReassessGoal = true;
		}
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(IS_CHARGING_CROSSBOW, false);
	}

	protected AbstractArrow getArrow(ItemStack pArrowStack, float pVelocity) {
		return ProjectileUtil.getMobArrow(this, pArrowStack, pVelocity);
	}

	public boolean canFireProjectileWeapon(ProjectileWeaponItem pProjectileWeapon) {
		return true;
	}

	public boolean isChargingCrossbow() {
		return this.entityData.get(IS_CHARGING_CROSSBOW);
	}

	public void setChargingCrossbow(boolean pIsCharging) {
		this.entityData.set(IS_CHARGING_CROSSBOW, pIsCharging);
	}

	@Override
	public void shootCrossbowProjectile(LivingEntity pTarget, ItemStack pCrossbowStack, Projectile pProjectile, float pProjectileAngle) {
		shootCrossbowProjectile(this, pTarget, pProjectile, pProjectileAngle, 1.6F);
	}

	public void shootCrossbowProjectile(LivingEntity pUser, LivingEntity pTarget, Projectile pProjectile, float pProjectileAngle, float pVelocity) {
		GolemShooterHelper.shootAimHelper(pTarget, pProjectile);
		pUser.playSound(SoundEvents.CROSSBOW_SHOOT, 1.0F, 1.0F / (pUser.getRandom().nextFloat() * 0.4F + 0.8F));
	}

	@Override
	public void onCrossbowAttackPerformed() {
		noActionTime = 0;
	}

	public void performCrossbowAttack(LivingEntity pUser, float pVelocity) {
		InteractionHand interactionhand = ProjectileUtil.getWeaponHoldingHand(pUser, item -> item instanceof CrossbowItem);
		ItemStack itemstack = pUser.getItemInHand(interactionhand);
		if (pUser.isHolding(is -> is.getItem() instanceof CrossbowItem)) {
			CrossbowItem.performShooting(pUser.level(), pUser, interactionhand, itemstack, pVelocity, 0);
		}
		this.onCrossbowAttackPerformed();
	}

	public InteractionHand getWeaponHand() {
		ItemStack stack = this.getMainHandItem();
		InteractionHand hand = InteractionHand.MAIN_HAND;
		if (stack.canPerformAction(ToolActions.SHIELD_BLOCK)) {
			hand = InteractionHand.OFF_HAND;
		}
		return hand;
	}

	@Override
	public void performRangedAttack(LivingEntity pTarget, float dist) {
		InteractionHand hand = getWeaponHand();
		ItemStack stack = getItemInHand(hand);
		var throwable = GolemShooterHelper.isValidThrowableWeapon(this, stack, hand);
		if (throwable.isThrowable()) {
			Projectile projectile = throwable.createProjectile(level());
			GolemShooterHelper.shootAimHelper(pTarget, projectile);
			this.playSound(SoundEvents.TRIDENT_THROW, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
			projectile.getPersistentData().putInt("DespawnFactor", 20);
			this.level().addFreshEntity(projectile);
			stack.hurtAndBreak(1, this, e -> e.broadcastBreakEvent(InteractionHand.MAIN_HAND));
		} else if (stack.getItem() instanceof CrossbowItem) {
			performCrossbowAttack(this, 3);
		} else if (stack.getItem() instanceof BowItem bow) {
			ItemStack arrowStack = this.getProjectile(stack);
			if (arrowStack.isEmpty()) return;
			AbstractArrow arrowEntity = bow.customArrow(getArrow(arrowStack, dist));
			boolean infinite = GolemShooterHelper.arrowIsInfinite(arrowStack, stack);
			GolemBowAttackEvent event = new GolemBowAttackEvent(this, stack, hand, arrowEntity, infinite);
			MinecraftForge.EVENT_BUS.post(event);
			arrowEntity = event.getArrow();
			if (event.isNoPickup()) {
				arrowEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
			} else {
				arrowEntity.pickup = AbstractArrow.Pickup.ALLOWED;
			}
			if (!event.isNoConsume()) {
				arrowStack.shrink(1);
			}
			GolemShooterHelper.shootAimHelper(pTarget, arrowEntity, event.speed(), event.gravity());
			this.playSound(SoundEvents.ARROW_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
			arrowEntity.getPersistentData().putInt("DespawnFactor", 20);
			this.level().addFreshEntity(arrowEntity);
			stack.hurtAndBreak(1, this, e -> e.broadcastBreakEvent(InteractionHand.MAIN_HAND));
		}
	}

	protected boolean rendering, render_trigger = false;

	@Override
	public boolean isBlocking() {
		boolean ans = shieldCooldown == 0 && shieldSlot() != null;
		if (ans && rendering) {
			render_trigger = true;
		}
		return ans;
	}

	public ItemStack getUseItem() {
		ItemStack ans = super.getUseItem();
		if (rendering && render_trigger) {
			render_trigger = false;
			InteractionHand hand = shieldSlot();
			if (hand != null) return getItemInHand(hand);
		}
		return ans;
	}

	// ------ common golem behavior

	@Override
	public void broadcastBreakEvent(EquipmentSlot pSlot) {
		super.broadcastBreakEvent(pSlot);
		Player player = getOwner();
		if (player != null) {
			GolemTriggers.BREAK.trigger((ServerPlayer) player);
		}
	}

	public boolean doHurtTarget(Entity target) {
		boolean can_sweep = getMainHandItem().canPerformAction(ToolActions.SWORD_SWEEP);
		if (!can_sweep) {
			if (super.doHurtTarget(target)) {
				ItemStack stack = getItemBySlot(EquipmentSlot.MAINHAND);
				stack.hurtAndBreak(1, this, self -> self.broadcastBreakEvent(EquipmentSlot.MAINHAND));
				return true;
			}
		} else {
			if (performRangedDamage(target, 0, 0)) {// trigger vanilla attack code, ignore values
				ItemStack stack = getItemBySlot(EquipmentSlot.MAINHAND);
				stack.hurtAndBreak(1, this, self -> self.broadcastBreakEvent(EquipmentSlot.MAINHAND));
				return true;
			}
		}
		return false;
	}

	@Override
	protected AABB getAttackBoundingBox(Entity target, double range) {
		GolemSweepEvent event = new GolemSweepEvent(this, getMainHandItem(), target, range);
		MinecraftForge.EVENT_BUS.post(event);
		return event.getBox();
	}

	@Override
	protected boolean performDamageTarget(Entity target, float damage, double kb) {
		return super.doHurtTarget(target);
	}

	@Override
	protected InteractionResult mobInteractImpl(Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (MGConfig.COMMON.strictInteract.get() && !itemstack.isEmpty())
			return InteractionResult.PASS;
		if (player.isShiftKeyDown()) {
			if (canModify(player)) {
				for (EquipmentSlot slot : EquipmentSlot.values()) {
					dropSlot(slot, false);
				}
			}
			if (itemstack.isEmpty()) {
				super.mobInteractImpl(player, hand);
			}
			return InteractionResult.SUCCESS;
		}
		if (itemstack.isEmpty()) {
			return super.mobInteractImpl(player, hand);
		}
		if ((itemstack.getItem() instanceof GolemHolder) ||
				!itemstack.getItem().canFitInsideContainerItems() ||
				!canModify(player)) {
			return InteractionResult.FAIL;
		}
		GolemEquipEvent event = new GolemEquipEvent(this, itemstack);
		MinecraftForge.EVENT_BUS.post(event);
		if (event.canEquip()) {
			if (level().isClientSide()) {
				return InteractionResult.SUCCESS;
			}
			if (hasItemInSlot(event.getSlot())) {
				dropSlot(event.getSlot(), false);
			}
			if (hasItemInSlot(event.getSlot())) {
				return InteractionResult.FAIL;
			}
			setItemSlot(event.getSlot(), itemstack.split(event.getAmount()));
			int count = (int) Arrays.stream(EquipmentSlot.values()).filter(e -> !getItemBySlot(e).isEmpty()).count();
			GolemTriggers.EQUIP.trigger((ServerPlayer) player, count);
			return InteractionResult.CONSUME;
		}
		return InteractionResult.FAIL;
	}

	@Override
	public double getMyRidingOffset() {
		return -0.35;
	}

	// ------ player equipment hurt

	@Override
	protected void hurtArmor(DamageSource source, float damage) {
		if (damage <= 0.0F) return;
		damage /= 4.0F;
		if (damage < 1.0F) {
			damage = 1.0F;
		}
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (slot.getType() != EquipmentSlot.Type.ARMOR) continue;
			ItemStack itemstack = this.getItemBySlot(slot);
			if ((!source.is(DamageTypeTags.IS_FIRE) || !itemstack.getItem().isFireResistant()) && itemstack.getItem() instanceof ArmorItem) {
				itemstack.hurtAndBreak((int) damage, this, (entity) -> entity.broadcastBreakEvent(slot));
			}
		}
	}

	@Nullable
	public InteractionHand shieldSlot() {
		return getItemBySlot(EquipmentSlot.MAINHAND).canPerformAction(ToolActions.SHIELD_BLOCK) ? InteractionHand.MAIN_HAND :
				getItemBySlot(EquipmentSlot.OFFHAND).canPerformAction(ToolActions.SHIELD_BLOCK) ? InteractionHand.OFF_HAND :
						null;
	}

	protected void hurtCurrentlyUsedShield(float damage) {
		InteractionHand hand = shieldSlot();
		if (hand == null) return;
		ItemStack stack = getItemInHand(hand);
		int i = damage < 3f ? 0 : 1 + Mth.floor(damage);
		GolemDamageShieldEvent event = new GolemDamageShieldEvent(this, stack, hand, damage, i);
		MinecraftForge.EVENT_BUS.post(event);
		i = event.getCost();
		if (i > 0) {
			stack.hurtAndBreak(i, this, (self) -> self.broadcastBreakEvent(hand));
		}
		if (stack.isEmpty()) {
			this.setItemInHand(hand, ItemStack.EMPTY);
			this.playSound(SoundEvents.SHIELD_BREAK, 0.8F, 0.8F + level().random.nextFloat() * 0.4F);
		} else {
			this.playSound(SoundEvents.SHIELD_BLOCK, 0.8F, 0.8F + level().random.nextFloat() * 0.4F);
		}
	}

	protected void blockUsingShield(LivingEntity source) {
		super.blockUsingShield(source);
		InteractionHand hand = shieldSlot();
		if (hand == null) return;
		ItemStack stack = getItemInHand(hand);
		boolean canDisable = source.canDisableShield() || source.getMainHandItem().canDisableShield(stack, this, source);
		GolemDisableShieldEvent event = new GolemDisableShieldEvent(this, stack, hand, source, canDisable);
		MinecraftForge.EVENT_BUS.post(event);
		if (event.shouldDisable()) {
			this.shieldCooldown = 100;
			this.level().broadcastEntityEvent(this, EntityEvent.SHIELD_DISABLED);
		}
	}

	@Override
	public void handleEntityEvent(byte event) {
		if (event == EntityEvent.SHIELD_DISABLED) {
			shieldCooldown = 100;
		}
		super.handleEntityEvent(event);
	}

	@Override
	public void tick() {
		super.tick();
		shieldCooldown = Mth.clamp(shieldCooldown - 1, 0, 100);
	}

	private boolean doReassessGoal = false;

	@Override
	public void aiStep() {
		if (doReassessGoal) {
			reassessWeaponGoal();
			doReassessGoal = false;
		}
		super.aiStep();
		attackStep();
	}

	public void attackStep() {
		if (level().isClientSide()) return;
		if (inventoryTick > 0) return;
		inventoryTick = 4;
		switchWeapon(
				getWrapperOfHand(EquipmentSlot.MAINHAND),
				!backupHand.isEmpty() ?
						getBackupHand() :
						getWrapperOfHand(EquipmentSlot.OFFHAND)
		);
	}

	public ItemWrapper getBackupHand() {
		return ItemWrapper.simple(() -> this.backupHand, e -> this.backupHand = e);
	}

	public ItemWrapper getArrowSlot() {
		return ItemWrapper.simple(() -> this.arrowSlot, e -> this.arrowSlot = e);
	}

	private void switchWeapon(ItemWrapper mainhand, ItemWrapper offhand) {
		LivingEntity target = getTarget();
		ItemStack main = mainhand.getItem();
		ItemStack off = offhand.getItem();
		if (main.getItem() instanceof ProjectileWeaponItem) {
			if (!getProjectile(main).isEmpty()) {
				if (off.canPerformAction(ToolActions.SHIELD_BLOCK)) {
					return;
				}
			}
			if (off.isEmpty() ||
					off.getItem() instanceof ProjectileWeaponItem ||
					off.getItem() instanceof ArrowItem) {
				return;
			}
			if (target == null || !meleeGoal.canReachTarget(target)) {
				return;
			}
		} else if (off.getItem() instanceof ProjectileWeaponItem) {
			boolean noArrow = getProjectile(off).isEmpty();
			if (noArrow) {
				return;
			}
			if (target != null && meleeGoal.canReachTarget(target)) {
				return;
			}
		} else if (!main.isEmpty() || off.isEmpty()) {
			return;
		}
		mainhand.setItem(off);
		offhand.setItem(main);
		doReassessGoal = true;
		inventoryTick = 10;
	}

	@Override
	public void checkRide(LivingEntity target) {
		if (target instanceof DogGolemEntity || target instanceof AbstractHorse) {
			startRiding(target);
		}
	}

}
