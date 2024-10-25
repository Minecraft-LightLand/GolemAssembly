package dev.xkmc.modulargolems.content.entity.humanoid;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.xkmc.modulargolems.content.entity.common.IGolemModel;
import dev.xkmc.modulargolems.content.entity.common.IHeadedModel;
import dev.xkmc.modulargolems.content.entity.ranged.GolemShooterHelper;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolActions;

public class HumanoidGolemModel extends PlayerModel<HumanoidGolemEntity> implements
		IGolemModel<HumanoidGolemEntity, HumaniodGolemPartType, HumanoidGolemModel>, IHeadedModel {

	public HumanoidGolemModel(EntityModelSet set) {
		this(set.bakeLayer(ModelLayers.PLAYER), false);
	}

	public HumanoidGolemModel(ModelPart modelPart, boolean slim) {
		super(modelPart, slim);
	}

	@Override
	public void renderToBufferInternal(HumaniodGolemPartType type, PoseStack stack, VertexConsumer consumer, int i, int j, float f1, float f2, float f3, float f4) {
		if (type == HumaniodGolemPartType.BODY) {
			this.body.render(stack, consumer, i, j, f1, f2, f3, f4);
			this.head.render(stack, consumer, i, j, f1, f2, f3, f4);
			this.hat.render(stack, consumer, i, j, f1, f2, f3, f4);
		} else if (type == HumaniodGolemPartType.ARMS) {
			this.leftArm.render(stack, consumer, i, j, f1, f2, f3, f4);
			this.rightArm.render(stack, consumer, i, j, f1, f2, f3, f4);
		} else if (type == HumaniodGolemPartType.LEGS) {
			this.leftLeg.render(stack, consumer, i, j, f1, f2, f3, f4);
			this.rightLeg.render(stack, consumer, i, j, f1, f2, f3, f4);
		}
	}

	@Override
	public ResourceLocation getTextureLocationInternal(ResourceLocation rl) {
		String id = rl.getNamespace();
		String mat = rl.getPath();
		return new ResourceLocation(id, "textures/entity/humanoid_golem/" + mat + ".png");
	}

	@Override
	public void setupAnim(HumanoidGolemEntity entity, float f1, float f2, float f3, float f4, float f5) {
		super.setupAnim(entity, f1, f2, f3, f4, f5);
		if (entity.isAggressive() && this.attackTime == 0.0F) {
			if (leftArmPose == ArmPose.ITEM) {
				this.leftArm.xRot = -1.8F;
			} else if (rightArmPose == ArmPose.ITEM) {
				this.rightArm.xRot = -1.8F;
			}
		}

		this.leftSleeve.copyFrom(this.leftArm);
		this.rightSleeve.copyFrom(this.rightArm);

	}

	protected void setupAttackAnimation(HumanoidGolemEntity entity, float time) {
		if ((leftArmPose == ArmPose.ITEM || rightArmPose == ArmPose.ITEM) && this.attackTime > 0.0F) {
			AnimationUtils.swingWeaponDown(this.rightArm, this.leftArm, entity, this.attackTime, time);
		} else {
			super.setupAttackAnimation(entity, time);
		}
	}

	public void prepareMobModel(HumanoidGolemEntity entity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick) {
		this.rightArmPose = ArmPose.EMPTY;
		this.leftArmPose = ArmPose.EMPTY;
		InteractionHand hand = entity.getWeaponHand();
		ItemStack itemstack = entity.getItemInHand(hand);
		ArmPose pos = ArmPose.EMPTY;
		if (entity.isAggressive() && GolemShooterHelper.isValidThrowableWeapon(entity, itemstack, hand).isThrowable() && entity.isUsingItem()) {
			pos = ArmPose.THROW_SPEAR;
		} else if (entity.isAggressive() && itemstack.getItem() instanceof BowItem) {
			pos = ArmPose.BOW_AND_ARROW;
		} else if (itemstack.getItem() instanceof CrossbowItem) {
			if (entity.isChargingCrossbow()) {
				pos = ArmPose.CROSSBOW_CHARGE;
			} else if (entity.isAggressive()) {
				pos = ArmPose.CROSSBOW_HOLD;
			}
		} else if (entity.isAggressive()) {
			pos = ArmPose.ITEM;
		}
		ArmPose anti_pos = ArmPose.EMPTY;
		if (hand == InteractionHand.OFF_HAND) {
			anti_pos = pos;
			pos = ArmPose.EMPTY;
		}
		if (entity.isBlocking()) {
			if (entity.getMainHandItem().canPerformAction(ToolActions.SHIELD_BLOCK)) {
				pos = ArmPose.BLOCK;
			} else {
				anti_pos = ArmPose.BLOCK;
			}
		}
		if (entity.getMainArm() == HumanoidArm.RIGHT) {
			this.rightArmPose = pos;
			this.leftArmPose = anti_pos;
		} else {
			this.leftArmPose = pos;
			this.rightArmPose = anti_pos;
		}
		super.prepareMobModel(entity, pLimbSwing, pLimbSwingAmount, pPartialTick);
	}

	@Override
	public void translateToHead(PoseStack pose) {
		pose.translate(0.0F, -0.25F, 0.0F);
		pose.mulPose(Axis.YP.rotationDegrees(180.0F));
		pose.scale(0.625F, -0.625F, -0.625F);
	}

}
