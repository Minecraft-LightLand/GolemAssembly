package dev.xkmc.modulargolems.events;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.xkmc.modulargolems.content.entity.humanoid.skin.ClientProfileManager;
import dev.xkmc.modulargolems.content.entity.humanoid.skin.SpecialRenderProfile;
import dev.xkmc.modulargolems.events.event.HumanoidSkinEvent;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.data.MGTagGen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;


@EventBusSubscriber(value = Dist.CLIENT, modid = ModularGolems.MODID, bus = EventBusSubscriber.Bus.GAME)
public class GolemClientEventListeners {

	@SubscribeEvent
	public static void onHumanoidSkin(HumanoidSkinEvent event) {
		if (event.getStack().is(Items.PLAYER_HEAD)) {
			String name = event.getStack().getHoverName().getString();
			var rl = ResourceLocation.tryParse(name);
			if (rl != null)
				event.setSkin(new SpecialRenderProfile(true, rl));
		}
		if (event.getStack().is(Items.PIGLIN_HEAD)) {
			String name = event.getStack().getHoverName().getString();
			var rl = ResourceLocation.tryParse(name);
			if (rl != null)
				event.setSkin(new SpecialRenderProfile(false, rl));
		}
		if (event.getStack().is(MGTagGen.PLAYER_SKIN)) {
			event.setSkin(ClientProfileManager.get(event.getStack().getHoverName().getString()));
		}
	}

	@SubscribeEvent
	public static void onLayerRender(RenderGuiLayerEvent.Pre event) {
		if (event.getName().equals(VanillaGuiLayers.HOTBAR)) {
			clearDepth(event.getGuiGraphics());
		}
	}

	private static void clearDepth(GuiGraphics g) {
		g.pose().popPose();
		g.pose().pushPose();
		g.pose().translate(0, 0, -1000);
		g.fill(LayerRenderType.GUI, 0, 0, g.guiWidth(), g.guiHeight(), -1);
		g.pose().translate(0, 0, 1000);
	}

	private static class LayerRenderType extends RenderType {
		public static final RenderType GUI = create(
				"reverse_gui",
				DefaultVertexFormat.POSITION_COLOR,
				VertexFormat.Mode.QUADS,
				786432,
				RenderType.CompositeState.builder()
						.setShaderState(RENDERTYPE_GUI_SHADER)
						.setWriteMaskState(RenderStateShard.DEPTH_WRITE)
						.setDepthTestState(GREATER_DEPTH_TEST)
						.createCompositeState(false));

		public LayerRenderType(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
			super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
		}
	}

}
