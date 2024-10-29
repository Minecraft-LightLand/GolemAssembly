package dev.xkmc.modulargolems.content.entity.common;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.xkmc.l2core.util.Proxy;
import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.modulargolems.content.client.override.ModelOverride;
import dev.xkmc.modulargolems.content.client.override.ModelOverrides;
import dev.xkmc.modulargolems.content.config.GolemMaterial;
import dev.xkmc.modulargolems.content.core.IGolemPart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractGolemRenderer<
		T extends AbstractGolemEntity<T, P>,
		P extends IGolemPart<P>,
		M extends EntityModel<T> & IGolemModel<T, P, M>
		> extends MobRenderer<T, M> {

	public static final List<Function<AbstractGolemRenderer<?, ?, ?>, RenderLayer<? extends AbstractGolemEntity<?, ?>, ?>>> LIST = new ArrayList<>();

	private static final ResourceLocation GOLEM_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/iron_golem/iron_golem.png");

	private final Supplier<P[]> list;

	public AbstractGolemRenderer(EntityRendererProvider.Context ctx, M model, float f, Supplier<P[]> list) {
		super(ctx, model, f);
		this.list = list;
		addLayer(new GolemDefaultLayer<>(this));
		LIST.forEach(e -> this.addLayer(Wrappers.cast(e.apply(this))));
	}

	public ResourceLocation getTextureLocation(T entity) {
		return GOLEM_LOCATION;
	}

	protected boolean delegated(T entity) {
		return false;
	}

	@Override
	public void render(T entity, float f1, float f2, PoseStack stack, MultiBufferSource source, int i) {
		super.render(entity, f1, f2, stack, source, i);
	}

	@Nullable
	@Override
	protected RenderType getRenderType(T entity, boolean b1, boolean b2, boolean b3) {
		if (delegated(entity))
			return super.getRenderType(entity, b1, b2, b3);
		return null;
	}

	protected void renderAllParts(PoseStack pose, MultiBufferSource buffer, int light, T entity, float pTick) {
		if (delegated(entity)) return;
		var player = Proxy.getClientPlayer();
		boolean visible = isBodyVisible(entity);
		boolean ghost = !visible && player != null && !entity.isInvisibleTo(player);
		boolean glowing = Minecraft.getInstance().shouldEntityAppearGlowing(entity);
		pose.pushPose();
		for (P part : list.get()) {
			var materials = entity.getMaterials();
			int index = part.ordinal();
			ResourceLocation rl = materials.size() > index ? materials.get(index).id() : GolemMaterial.EMPTY;
			ModelOverride override = ModelOverrides.getOverride(rl);
			override.renderAll(this, entity, part, pose, buffer, rl, light, pTick, visible, ghost, glowing);
		}
		pose.popPose();
	}

	public void renderPartModel(T entity, P part, PoseStack pose, VertexConsumer vc, int light, float pTick, boolean ghost) {
		int overlay = getOverlayCoords(entity, this.getWhiteOverlayProgress(entity, pTick));
		this.model.renderToBufferInternal(part, pose, vc, light, overlay, ghost ? 0x26FFFFFF : -1);
	}

}