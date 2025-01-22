package dev.xkmc.modulargolems.compat.materials.l2complements;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ForceFieldLayer<T extends AbstractGolemEntity<T, ?>, M extends EntityModel<T>> extends EnergySwirlLayer<T, M> {

	public static void registerLayer() {
		AbstractGolemRenderer.LIST.add(ForceFieldLayer::new);
	}

	private static final ResourceLocation WITHER_ARMOR_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/wither/wither_armor.png");
	private final M model;

	public ForceFieldLayer(RenderLayerParent<T, M> pRenderer) {
		super(pRenderer);
		this.model = pRenderer.getModel();
	}

	@Override
	public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, T e, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		if (e.getUpgrades().contains(LCCompatRegistry.FORCE_FIELD.get()))
			super.render(pMatrixStack, pBuffer, pPackedLight, e, pLimbSwing, pLimbSwingAmount, pPartialTicks, pAgeInTicks, pNetHeadYaw, pHeadPitch);
	}

	protected float xOffset(float t) {
		return Mth.cos(t * 0.02F) * 3.0F;
	}

	protected ResourceLocation getTextureLocation() {
		return WITHER_ARMOR_LOCATION;
	}

	protected M model() {
		return this.model;
	}

}