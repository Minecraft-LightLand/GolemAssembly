package dev.xkmc.modulargolems.content.client.override;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.modulargolems.content.core.IGolemPart;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemRenderer;
import dev.xkmc.modulargolems.content.entity.common.IGolemModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class ModelOverride {

	public static ModelOverride texturePredicate(Function<AbstractGolemEntity<?, ?>, String> modifier) {
		return new ModelOverride() {

			@Override
			public ResourceLocation getTexture(AbstractGolemEntity<?, ?> golem, ResourceLocation id) {
				return super.getTexture(golem, id).withSuffix(modifier.apply(golem));
			}

		};
	}

	public ModelOverride() {
	}

	public ResourceLocation getTexture(AbstractGolemEntity<?, ?> golem, ResourceLocation id) {
		return id;
	}

	public synchronized <M extends EntityModel<T> & IGolemModel<T, P, M>, T extends AbstractGolemEntity<T, P>, P extends IGolemPart<P>> void renderAll(
			AbstractGolemRenderer<T, P, M> renderer, T entity, P part, PoseStack pose, MultiBufferSource buffer, ResourceLocation mat,
			int light, float pTick, boolean visible, boolean ghost, boolean glowing
	) {
		var model = renderer.getModel();
		ResourceLocation tex = getTexture(entity, mat);
		RenderType rt = getRenderType(model, model.getTextureLocationInternal(tex), visible, ghost, glowing);
		if (rt != null) {
			renderer.renderPartModel(entity, part, pose, buffer.getBuffer(rt), light, pTick, ghost);
		}
		var etex = model.getTextureLocationInternal(tex.withSuffix("_emissive"));
		if (ModelOverrides.isValid(etex)) {
			rt = getRenderType(renderer.getModel(), etex, visible, ghost, glowing);
			if (rt != null) {
				renderer.renderPartModel(entity, part, pose, buffer.getBuffer(rt), LightTexture.FULL_BRIGHT, pTick, ghost);
			}
		}
	}

	@Nullable
	protected <M extends EntityModel<?> & IGolemModel<?, ?, M>> RenderType getRenderType(
			M model, ResourceLocation tex, boolean visible, boolean ghost, boolean glowing
	) {
		if (ghost) return RenderType.itemEntityTranslucentCull(tex);
		if (visible) return model.renderType(tex);
		return glowing ? RenderType.outline(tex) : null;
	}

}
