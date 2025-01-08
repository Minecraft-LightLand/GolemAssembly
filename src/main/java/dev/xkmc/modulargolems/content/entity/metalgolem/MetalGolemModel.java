package dev.xkmc.modulargolems.content.entity.metalgolem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.xkmc.modulargolems.content.client.armor.GolemEquipmentModels;
import dev.xkmc.modulargolems.content.entity.common.IGolemModel;
import dev.xkmc.modulargolems.content.entity.common.IHeadedModel;
import dev.xkmc.modulargolems.content.item.equipments.MetalGolemWeaponItem;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import dev.xkmc.modulargolems.content.client.pose.CustomModelAnimation;
@OnlyIn(Dist.CLIENT)
public class MetalGolemModel extends HierarchicalModel<MetalGolemEntity> implements IGolemModel<MetalGolemEntity, MetalGolemPartType, MetalGolemModel>, IHeadedModel {

	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart rightLeg;
	private final ModelPart leftLeg;
	private final ModelPart body;

	public final ModelPart rightArm;
	public final ModelPart leftArm;
	public final ModelPart leftForeArm;
	public final ModelPart rightForeArm;

	public MetalGolemModel(EntityModelSet set) {
		this(set.bakeLayer(GolemEquipmentModels.METALGOLEM));
	}

	public MetalGolemModel(ModelPart part) {
		this.root = part;
		this.body = part.getChild("body");
		this.head = part.getChild("head");
		this.rightArm = part.getChild("right_arm");
		this.leftArm = part.getChild("left_arm");
		this.rightLeg = part.getChild("right_leg");
		this.leftLeg = part.getChild("left_leg");
		this.leftForeArm = leftArm.getChild("left_forearm");
		this.rightForeArm = rightArm.getChild("right_forearm");
	}

	public ModelPart root() {
		return this.root;
	}

	public void copyFrom(MetalGolemModel other) {
		head.copyFrom(other.head);
		body.copyFrom(other.body);
		rightArm.copyFrom(other.rightArm);
		leftArm.copyFrom(other.leftArm);
		rightLeg.copyFrom(other.rightLeg);
		leftLeg.copyFrom(other.leftLeg);
		leftForeArm.copyFrom(other.leftForeArm);
		rightForeArm.copyFrom(other.rightForeArm);
	}
	public void setupAnim(MetalGolemEntity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		int atkTick = pEntity.getAttackAnimationTick();
        Item its =pEntity.getMainHandItem().getItem();
		this.animateWalk(pNetHeadYaw, pHeadPitch, pLimbSwing, pLimbSwingAmount);
			if(its instanceof MetalGolemWeaponItem wi){
				switch (wi.getGolemWeaponType(wi)) {
					case SWORD, AXE -> {
						if (atkTick > 0) {
							this.animate(pEntity.axeAttackAnimationState, CustomModelAnimation.attackInAxe, pAgeInTicks);
						}else if(pEntity.isAggressive()) {
							this.animate(pEntity.axeWarningAnimationState, CustomModelAnimation.warningInAxe, pAgeInTicks);
						}
					}
					case SPEAR -> {
						if (atkTick > 0) {
							this.animate(pEntity.spearAttackAnimationState, CustomModelAnimation.attackInSpear, pAgeInTicks);
						} else if (pEntity.isAggressive()) {
							this.animate(pEntity.spearWarningAnimationState, CustomModelAnimation.warningInSpear, pAgeInTicks);
						}
					}
				}
			}
	}
	private void animateWalk(float pNetHeadYaw, float pHeadPitch,float pLimbSwing,float pLimbSwingAmount) {
		this.head.yRot = pNetHeadYaw * ((float) Math.PI / 180F);
		this.head.xRot = pHeadPitch * ((float) Math.PI / 180F);
		this.rightLeg.xRot = -1.5F * Mth.triangleWave(pLimbSwing, 13.0F) * pLimbSwingAmount;
		this.leftLeg.xRot = 1.5F * Mth.triangleWave(pLimbSwing, 13.0F) * pLimbSwingAmount;
		this.rightLeg.yRot = 0.0F;
		this.leftLeg.yRot = 0.0F;
		this.rightArm.xRot = (-0.2F + 1.5F * Mth.triangleWave(pLimbSwing, 13.0F)) * pLimbSwingAmount;
		this.leftArm.xRot = (-0.2F - 1.5F * Mth.triangleWave(pLimbSwing, 13.0F)) * pLimbSwingAmount;
		this.rightForeArm.xRot = 0;
		this.leftForeArm.xRot = 0;
		this.resetArmPoses();
	}
	private void resetArmPoses() {
		this.leftArm.yRot = 0.0F;
		this.leftArm.z = 0.0F;
		this.leftArm.x = 0.0F;
		this.leftArm.y = -7.0F;
		this.rightArm.yRot = 0.0F;
		this.rightArm.z = 0.0F;
		this.rightArm.x = -0.0F;
		this.rightArm.y = -7.0F;
	}
	public void renderToBufferInternal(MetalGolemPartType type, PoseStack stack, VertexConsumer consumer, int i, int j, float f1, float f2, float f3, float f4) {
		if (type == MetalGolemPartType.BODY) {
			this.body.render(stack, consumer, i, j, f1, f2, f3, f4);
			this.head.render(stack, consumer, i, j, f1, f2, f3, f4);
		} else if (type == MetalGolemPartType.LEFT) {
			this.leftArm.render(stack, consumer, i, j, f1, f2, f3, f4);
		} else if (type == MetalGolemPartType.RIGHT) {
			this.rightArm.render(stack, consumer, i, j, f1, f2, f3, f4);
		} else if (type == MetalGolemPartType.LEG) {
			this.leftLeg.render(stack, consumer, i, j, f1, f2, f3, f4);
			this.rightLeg.render(stack, consumer, i, j, f1, f2, f3, f4);
		}
	}

	public ResourceLocation getTextureLocationInternal(ResourceLocation rl) {
		String id = rl.getNamespace();
		String mat = rl.getPath();
		return new ResourceLocation(id, "textures/entity/metal_golem/" + mat + ".png");
	}

	public void transformToHand(EquipmentSlot slot, PoseStack pose) {
		if (slot == EquipmentSlot.MAINHAND) {
			rightArm.translateAndRotate(pose);
			rightForeArm.translateAndRotate(pose);
		}
		if (slot == EquipmentSlot.OFFHAND) {
			leftArm.translateAndRotate(pose);
			leftForeArm.translateAndRotate(pose);
		}
	}

	@Override
	public ModelPart getHead() {
		return head;
	}

	public void translateToHead(PoseStack pose) {
		pose.translate(0.0F, -0.45F, -0.08F);
		pose.mulPose(Axis.YP.rotationDegrees(180.0F));
		pose.scale(0.625F, -0.625F, -0.625F);
	}

}