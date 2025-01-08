package dev.xkmc.modulargolems.content.entity.dog;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.xkmc.more_wolf_armors.content.WolfArmorItem;
import dev.xkmc.more_wolf_armors.init.MoreWolfArmors;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.Crackiness;
import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.fml.ModList;

import java.util.Map;

public class DogArmorLayer extends RenderLayer<DogGolemEntity, DogGolemModel> {
	private final DogGolemModel model;
	private static final Map<Crackiness.Level, ResourceLocation> ARMOR_CRACK_LOCATIONS;

	public DogArmorLayer(RenderLayerParent<DogGolemEntity, DogGolemModel> renderer, EntityModelSet models) {
		super(renderer);
		this.model = new DogGolemModel(models.bakeLayer(ModelLayers.WOLF_ARMOR));
	}

	public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, DogGolemEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
		if (livingEntity.hasArmor()) {
			ItemStack itemstack = livingEntity.getBodyArmorItem();
			if (itemstack.getItem() instanceof AnimalArmorItem animalarmoritem) {
				if (animalarmoritem.getBodyType() == AnimalArmorItem.BodyType.CANINE) {
					this.getParentModel().copyPropertiesTo(this.model);
					this.model.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTick);
					this.model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
					VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(animalarmoritem.getTexture()));
					this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
					this.maybeRenderColoredLayer(poseStack, buffer, packedLight, itemstack, animalarmoritem);
					this.maybeRenderCracks(poseStack, buffer, packedLight, itemstack);
				}
			}
		}

	}

	private void maybeRenderColoredLayer(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ItemStack armorStack, AnimalArmorItem armorItem) {
		if (ModList.get().isLoaded(MoreWolfArmors.MODID)) {
			if (armorStack.getItem() instanceof WolfArmorItem) {
				int i = DyedItemColor.getOrDefault(armorStack, DyeColor.RED.getTextureDiffuseColor());
				ResourceLocation tex = armorItem.getOverlayTexture();
				if (tex == null) return;
				model.renderToBuffer(
						poseStack,
						buffer.getBuffer(RenderType.entityCutoutNoCull(tex)),
						packedLight,
						OverlayTexture.NO_OVERLAY,
						FastColor.ARGB32.opaque(i)
				);
				return;
			}
		}
		if (armorStack.is(ItemTags.DYEABLE)) {

			int i = DyedItemColor.getOrDefault(armorStack, 0);
			if (FastColor.ARGB32.alpha(i) == 0) {
				return;
			}

			ResourceLocation resourcelocation = armorItem.getOverlayTexture();
			if (resourcelocation == null) {
				return;
			}

			this.model.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(resourcelocation)), packedLight, OverlayTexture.NO_OVERLAY, FastColor.ARGB32.opaque(i));
		}

	}

	private void maybeRenderCracks(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ItemStack armorStack) {
		Crackiness.Level crackiness$level = Crackiness.WOLF_ARMOR.byDamage(armorStack);
		if (crackiness$level != Crackiness.Level.NONE) {
			ResourceLocation resourcelocation = ARMOR_CRACK_LOCATIONS.get(crackiness$level);
			VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityTranslucent(resourcelocation));
			this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
		}

	}

	static {
		ARMOR_CRACK_LOCATIONS = Map.of(
				Crackiness.Level.LOW, ResourceLocation.withDefaultNamespace("textures/entity/wolf/wolf_armor_crackiness_low.png"),
				Crackiness.Level.MEDIUM, ResourceLocation.withDefaultNamespace("textures/entity/wolf/wolf_armor_crackiness_medium.png"),
				Crackiness.Level.HIGH, ResourceLocation.withDefaultNamespace("textures/entity/wolf/wolf_armor_crackiness_high.png")
		);
	}
}
