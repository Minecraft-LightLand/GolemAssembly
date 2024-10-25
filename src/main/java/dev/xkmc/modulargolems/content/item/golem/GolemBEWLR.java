package dev.xkmc.modulargolems.content.item.golem;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.modulargolems.content.client.override.ModelOverrides;
import dev.xkmc.modulargolems.content.config.GolemMaterial;
import dev.xkmc.modulargolems.content.core.GolemType;
import dev.xkmc.modulargolems.content.core.IGolemPart;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.common.IGolemModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Supplier;

public class GolemBEWLR extends BlockEntityWithoutLevelRenderer {

	public static final Supplier<BlockEntityWithoutLevelRenderer> INSTANCE = Suppliers.memoize(() ->
			new GolemBEWLR(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()));

	public static final IClientItemExtensions EXTENSIONS = new IClientItemExtensions() {

		@Override
		public BlockEntityWithoutLevelRenderer getCustomRenderer() {
			return INSTANCE.get();
		}

	};

	private final EntityModelSet entityModelSet;
	private final HashMap<ResourceLocation, IGolemModel<?, ?, ?>> map = new HashMap<>();

	public GolemBEWLR(BlockEntityRenderDispatcher dispatcher, EntityModelSet set) {
		super(dispatcher, set);
		entityModelSet = set;
	}

	public void onResourceManagerReload(ResourceManager manager) {
		map.clear();
		GolemType.GOLEM_TYPE_TO_MODEL.forEach((k, v) -> map.put(k, v.get().generateModel(entityModelSet)));
		ModelOverrides.reload();
	}

	@Override
	public void renderByItem(ItemStack stack, ItemDisplayContext type, PoseStack poseStack,
							 MultiBufferSource bufferSource, int light, int overlay) {
		BEWLRHandle handle = new BEWLRHandle(stack, type, poseStack, bufferSource, light, overlay);
		poseStack.pushPose();
		if (stack.getItem() instanceof IGolemPartItem part) {
			render(handle, part.asPart());
		}
		if (stack.getItem() instanceof GolemHolder<?, ?> holder) {
			if (!renderEntity(handle, holder))
				render(handle, holder);
		}
		poseStack.popPose();
	}

	private <T extends AbstractGolemEntity<T, P>, P extends IGolemPart<P>> void render(BEWLRHandle handle, GolemHolder<T, P> item) {
		ArrayList<GolemMaterial> list = GolemHolder.getMaterial(handle.stack());
		P[] parts = item.getEntityType().values();
		PoseStack stack = handle.poseStack();
		parts[0].setupItemRender(stack, handle.type(), null);
		for (int i = 0; i < parts.length; i++) {
			ResourceLocation id = list.size() > i ? list.get(i).id() : GolemMaterial.EMPTY;
			renderPart(handle, id, item.getEntityType(), parts[i]);
		}
	}

	private <T extends AbstractGolemEntity<T, P>, P extends IGolemPart<P>> void render(BEWLRHandle handle, GolemPart<T, P> item) {
		PoseStack stack = handle.poseStack();
		P part = item.getPart();
		part.setupItemRender(stack, handle.type(), part);
		Optional<ResourceLocation> id = GolemPart.getMaterial(handle.stack());
		renderPart(handle, id.orElse(GolemMaterial.EMPTY), item.getEntityType(), part);
	}

	private <T extends AbstractGolemEntity<T, P>, P extends IGolemPart<P>, M extends EntityModel<T> & IGolemModel<T, P, M>>
	void renderPart(BEWLRHandle handle, ResourceLocation id, GolemType<T, P> type, P part) {
		M model = Wrappers.cast(map.get(type.getRegistryName()));
		RenderType render = model.renderType(model.getTextureLocationInternal(id));
		VertexConsumer vc = ItemRenderer.getFoilBufferDirect(handle.bufferSource(), render, false, handle.stack().hasFoil());
		model.renderToBufferInternal(part, handle.poseStack(), vc, handle.light(), handle.overlay(), 1.0F, 1.0F, 1.0F, 1.0F);
	}

	private <T extends AbstractGolemEntity<T, P>, P extends IGolemPart<P>> boolean renderEntity(BEWLRHandle handle, GolemHolder<T, P> item) {
		T golem = ClientHolderManager.getEntityForDisplay(item, handle.stack());
		if (golem == null) return false;
		P[] parts = item.getEntityType().values();
		PoseStack stack = handle.poseStack();
		parts[0].setupItemRender(stack, handle.type(), null);
		stack.translate(0, 1.501, 0);
		stack.scale(1, -1, -1);
		var renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(golem);
		golem.tickCount = Proxy.getClientPlayer().tickCount;
		renderer.render(golem, 0, Minecraft.getInstance().getPartialTick(), handle.poseStack(), handle.bufferSource(), handle.light());
		return true;
	}
}
