package dev.xkmc.modulargolems.content.entity.metalgolem;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.xkmc.modulargolems.content.client.armor.GolemModelPath;
import dev.xkmc.modulargolems.content.item.equipments.GolemModelItem;
import dev.xkmc.modulargolems.content.item.equipments.MetalGolemBeaconItem;
import dev.xkmc.modulargolems.init.ModularGolems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.HashMap;
import java.util.List;

import static dev.xkmc.modulargolems.content.client.armor.GolemEquipmentModels.LIST;

public class GolemEquipmentRenderer extends RenderLayer<MetalGolemEntity, MetalGolemModel> {

	public HashMap<ModelLayerLocation, MetalGolemModel> map = new HashMap<>();

	public GolemEquipmentRenderer(RenderLayerParent<MetalGolemEntity, MetalGolemModel> r, EntityRendererProvider.Context e) {
		super(r);
		for (var l : LIST) {
			map.put(l, new MetalGolemModel(e.bakeLayer(l)));
		}
	}

	@Override
	public void render(@NotNull PoseStack pose, MultiBufferSource source, int i, @NotNull MetalGolemEntity entity, float f1, float f2, float f3, float f4, float f5, float f6) {
		for (var e : EquipmentSlot.values()) {
			ItemStack stack = entity.getItemBySlot(e);
			if (stack.getItem() instanceof GolemModelItem mgaitem) {
				GolemModelPath gmpath = GolemModelPath.get(mgaitem.getModelPath());
				for (List<String> ls : gmpath.paths()) {
					MetalGolemModel model = map.get(gmpath.models());
					model.copyFrom(getParentModel());
					ModelPart gemr = model.root();
					pose.pushPose();
					for (String s : ls) {
						gemr.translateAndRotate(pose);
						gemr = gemr.getChild(s);
					}
					gemr.render(pose, source.getBuffer(RenderType.armorCutoutNoCull(mgaitem.getModelTexture())),
							i, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
					pose.popPose();
				}
			} else if (stack.getItem() instanceof MetalGolemBeaconItem beacon) {
				if (!entity.isAddedToWorld())
					return;
				var color = new float[]{1.0F, 1.0F, 1.0F};
				renderBeacon(pose, source, i, entity.level().getGameTime(), entity.getBbHeight());
				renderBeam(pose, source, i, 1F, entity.level().getGameTime(), entity.getBbHeight(), color);
			} else {
				renderArmWithItem(entity, stack, e, pose, source, i);
			}
		}
	}

	protected void renderArmWithItem(MetalGolemEntity entity, ItemStack stack, EquipmentSlot slot,
									 PoseStack pose, MultiBufferSource source, int light) {
		if (stack.isEmpty()) return;
		ItemDisplayContext ctx = null;
		if (slot == EquipmentSlot.MAINHAND) {
			ctx = ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
		} else if (slot == EquipmentSlot.OFFHAND) {
			ctx = ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
		}
		if (ctx == null) return;

		pose.pushPose();
		getParentModel().transformToHand(slot, pose);
		boolean offhand = slot == EquipmentSlot.OFFHAND;
		pose.translate((offhand ? 1 : -1) * 0.7f, 0.8F, -0.25F);
		pose.mulPose(Axis.XP.rotationDegrees(-90));
		pose.mulPose(Axis.YP.rotationDegrees(180));
		Minecraft.getInstance().getItemRenderer()
				.renderStatic(entity, stack, ctx, offhand,
						pose, source, entity.level(), light, OverlayTexture.NO_OVERLAY,
						entity.getId() + slot.ordinal());
		pose.popPose();

	}

	final ResourceLocation BEACON_LOCATION = new ResourceLocation(ModularGolems.MODID, "textures/equipments/beacon.png");

	protected void renderBeacon(PoseStack pose, MultiBufferSource source, float pTick, long gameTick, float height) {
		float width = 3F;

		pose.pushPose();
		pose.scale(1, -1, 1);
		pose.translate(0D, -height / 2, 0D);
		float accurateTick = (float) Math.floorMod(gameTick, 90) + pTick;
		pose.mulPose(Axis.YP.rotationDegrees(accurateTick - 45.0F));

		var buffer = source.getBuffer(RenderType.armorCutoutNoCull(BEACON_LOCATION));
		PoseStack.Pose posestack$pose = pose.last();
		Matrix4f matrix4f = posestack$pose.pose();
		Matrix3f matrix3f = posestack$pose.normal();
		addVertex(matrix4f, matrix3f, buffer, 1.0F, 1.0F, 1.0F, 1.0F, 0, 0, width, 0, 0);
		addVertex(matrix4f, matrix3f, buffer, 1.0F, 1.0F, 1.0F, 1.0F, 0, width, 0, 0, 1);
		addVertex(matrix4f, matrix3f, buffer, 1.0F, 1.0F, 1.0F, 1.0F, 0, 0, -width, 1, 1);
		addVertex(matrix4f, matrix3f, buffer, 1.0F, 1.0F, 1.0F, 1.0F, 0, -width, 0, 1, 0);
		pose.popPose();
	}

	final ResourceLocation BEAM_LOCATION = new ResourceLocation("textures/entity/beacon_beam.png");

	protected void renderBeam(PoseStack pose, MultiBufferSource source, float pTick, float scale, long gameTick, float height, float[] color) {
		float width1 = 0.2F;
		float width2 = 0.25F;
		int length = 1024;

		pose.pushPose();
		pose.scale(1, -1, 1);
		pose.translate(0D, height / 2, 0D);
		float accurateTick = (float) Math.floorMod(gameTick, 40) + pTick;
		float f2 = Mth.frac(accurateTick * 0.2F - (float) Mth.floor(accurateTick * 0.1F));
		float colorR = color[0];
		float colorG = color[1];
		float colorB = color[2];
		pose.pushPose();
		pose.mulPose(Axis.YP.rotationDegrees(accurateTick * 2.25F - 45.0F));
		float v1 = -1.0F + f2;
		float v2 = (float) length * scale * (0.5F / width1) + v1;
		renderPart(pose, source.getBuffer(RenderType.beaconBeam(BEAM_LOCATION, false)),
				colorR, colorG, colorB, 1.0F,
				0, length, 0.0F, width1, width1, 0.0F, -width1, 0.0F, 0.0F, -width1,
				0.0F, 1.0F, v2, v1);
		pose.popPose();
		v1 = -1.0F + f2;
		v2 = (float) length * scale + v1;
		renderPart(pose, source.getBuffer(RenderType.beaconBeam(BEAM_LOCATION, true)),
				colorR, colorG, colorB, 0.125F,
				0, length, -width2, -width2, width2, -width2, -width2, width2, width2, width2,
				0.0F, 1.0F, v2, v1);
		pose.popPose();
	}

	private static void renderPart(PoseStack pose, VertexConsumer buffer, float r, float g, float b, float a, int start, int end, float p_112164_, float p_112165_, float p_112166_, float p_112167_, float p_112168_, float p_112169_, float p_112170_, float p_112171_, float u1, float u2, float v1, float v2) {
		PoseStack.Pose posestack$pose = pose.last();
		Matrix4f matrix4f = posestack$pose.pose();
		Matrix3f matrix3f = posestack$pose.normal();
		renderQuad(matrix4f, matrix3f, buffer, r, g, b, a, start, end, p_112164_, p_112165_, p_112166_, p_112167_, u1, u2, v1, v2);
		renderQuad(matrix4f, matrix3f, buffer, r, g, b, a, start, end, p_112170_, p_112171_, p_112168_, p_112169_, u1, u2, v1, v2);
		renderQuad(matrix4f, matrix3f, buffer, r, g, b, a, start, end, p_112166_, p_112167_, p_112170_, p_112171_, u1, u2, v1, v2);
		renderQuad(matrix4f, matrix3f, buffer, r, g, b, a, start, end, p_112168_, p_112169_, p_112164_, p_112165_, u1, u2, v1, v2);
	}

	private static void renderQuad(Matrix4f pose, Matrix3f normal, VertexConsumer buffer, float r, float g, float b, float a, int y1, int y2, float x1, float z1, float x2, float z2, float u1, float u2, float v1, float v2) {
		addVertex(pose, normal, buffer, r, g, b, a, y2, x1, z1, u2, v1);
		addVertex(pose, normal, buffer, r, g, b, a, y1, x1, z1, u2, v2);
		addVertex(pose, normal, buffer, r, g, b, a, y1, x2, z2, u1, v2);
		addVertex(pose, normal, buffer, r, g, b, a, y2, x2, z2, u1, v1);
	}

	private static void addVertex(Matrix4f pose, Matrix3f normal, VertexConsumer buffer, float r, float g, float b, float a, int y, float x, float z, float u, float v) {
		buffer.vertex(pose, x, (float) y, z).color(r, g, b, a).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
	}


}
