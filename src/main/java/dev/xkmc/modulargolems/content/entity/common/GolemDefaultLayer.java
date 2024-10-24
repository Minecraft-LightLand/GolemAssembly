package dev.xkmc.modulargolems.content.entity.common;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.modulargolems.content.core.IGolemPart;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

public class GolemDefaultLayer<
		T extends AbstractGolemEntity<T, P>,
		P extends IGolemPart<P>,
		M extends EntityModel<T> & IGolemModel<T, P, M>
		> extends RenderLayer<T, M> {

	private final AbstractGolemRenderer<T, P, M> parent;

	public GolemDefaultLayer(AbstractGolemRenderer<T, P, M> parent) {
		super(parent);
		this.parent = parent;
	}

	@Override
	public void render(
			PoseStack pose, MultiBufferSource buffer, int light, T e,
			float legAngle, float legSwing, float bob, float pTick, float yRot, float xRot
	) {
		parent.renderAllParts(pose, buffer, light, e, pTick);
	}

}
