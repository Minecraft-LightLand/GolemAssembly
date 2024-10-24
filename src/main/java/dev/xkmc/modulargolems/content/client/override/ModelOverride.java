package dev.xkmc.modulargolems.content.client.override;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.modulargolems.content.core.IGolemPart;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemRenderer;
import dev.xkmc.modulargolems.content.entity.common.IGolemModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class ModelOverride {

	public static final ModelOverride DEFAULT = new ModelOverride(false);
	public static final ModelOverride EMISSIVE = new ModelOverride(true);

	public static ModelOverride texturePredicate(Function<AbstractGolemEntity<?, ?>, String> modifier) {
		return new ModelOverride(false) {

			@Override
			public ResourceLocation getTexture(AbstractGolemEntity<?, ?> golem, ResourceLocation id) {
				return super.getTexture(golem, id).withSuffix(modifier.apply(golem));
			}

		};

	}

	private final boolean emissive;

	public ModelOverride(boolean emissive) {
		this.emissive = emissive;
	}

	public ResourceLocation getTexture(AbstractGolemEntity<?, ?> golem, ResourceLocation id) {
		return id;
	}

	public <M extends EntityModel<T> & IGolemModel<T, P, M>, T extends AbstractGolemEntity<T, P>, P extends IGolemPart<P>> void renderAll(
			AbstractGolemRenderer<T, P, M> renderer, T entity, P part, PoseStack pose, MultiBufferSource buffer, ResourceLocation mat,
			int light, float pTick, boolean visible, boolean ghost, boolean glowing
	) {
		ResourceLocation tex = getTexture(entity, mat);
		RenderType rt = getRenderType(renderer.getModel(), tex, visible, ghost, glowing);
		if (rt != null) {
			renderer.renderPartModel(entity, part, pose, buffer.getBuffer(rt), light, pTick, ghost);
		}
		if (emissive) {
			rt = getRenderType(renderer.getModel(), tex.withSuffix("_emissive"), visible, ghost, glowing);
			if (rt != null) {
				renderer.renderPartModel(entity, part, pose, buffer.getBuffer(rt), LightTexture.FULL_BRIGHT, pTick, ghost);
			}
		}
	}

	@Nullable
	protected <M extends EntityModel<?> & IGolemModel<?, ?, M>> RenderType getRenderType(
			M model, ResourceLocation id, boolean visible, boolean ghost, boolean glowing
	) {
		var tex = model.getTextureLocationInternal(id);
		if (ghost) return RenderType.itemEntityTranslucentCull(tex);
		if (visible) return model.renderType(tex);
		return glowing ? RenderType.outline(tex) : null;
	}

}
