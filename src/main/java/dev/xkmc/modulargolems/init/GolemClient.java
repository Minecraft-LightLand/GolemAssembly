package dev.xkmc.modulargolems.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import dev.xkmc.modulargolems.compat.curio.CurioCompatRegistry;
import dev.xkmc.modulargolems.compat.materials.common.CompatManager;
import dev.xkmc.modulargolems.compat.misc.MaidCompat;
import dev.xkmc.modulargolems.content.client.armor.GolemEquipmentModels;
import dev.xkmc.modulargolems.content.client.overlay.GolemStatusOverlay;
import dev.xkmc.modulargolems.content.entity.humanoid.skin.PlayerSkinRenderer;
import dev.xkmc.modulargolems.content.item.golem.GolemBEWLR;
import dev.xkmc.modulargolems.content.item.upgrade.UpgradeItem;
import dev.xkmc.modulargolems.content.menu.registry.GolemTabRegistry;
import dev.xkmc.modulargolems.init.data.MGConfig;
import dev.xkmc.modulargolems.init.data.MGTagGen;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ModularGolems.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GolemClient {

	private static final boolean ENABLE_TLM = true;

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		if (ENABLE_TLM && ModList.get().isLoaded(TouhouLittleMaid.MOD_ID)) {
			MinecraftForge.EVENT_BUS.register(MaidCompat.class);
		}
		event.enqueueWork(() -> {
			ClampedItemPropertyFunction func = (stack, level, entity, layer) ->
					entity != null && entity.isBlocking() && entity.getUseItem() == stack ? 1.0F : 0.0F;
			if (MGConfig.CLIENT.shieldUsePoseFixForModdedShields.get()) {
				for (var e : ForgeRegistries.ITEMS) {
					if (e instanceof ShieldItem) {
						ItemProperties.register(e, new ResourceLocation("blocking"), func);
					}
				}
			} else {
				ItemProperties.register(Items.SHIELD, new ResourceLocation("blocking"), func);
			}
			ClampedItemPropertyFunction arrow = (stack, level, entity, layer) ->
					stack.is(MGTagGen.BLUE_UPGRADES) ? 1 : stack.is(MGTagGen.POTION_UPGRADES) ? 0.5f : 0;
			for (var item : UpgradeItem.LIST)
				ItemProperties.register(item, new ResourceLocation(ModularGolems.MODID, "blue_arrow"), arrow);
			CompatManager.dispatchClientSetup();

			GolemTabRegistry.register();
			CurioCompatRegistry.clientRegister();
		});
	}

	@SubscribeEvent
	public static void registerOverlays(RegisterGuiOverlaysEvent event) {
		event.registerAbove(VanillaGuiOverlay.CROSSHAIR.id(), "golem_stats", new GolemStatusOverlay());
	}

	@SubscribeEvent
	public static void registerArmorLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
		GolemEquipmentModels.registerArmorLayer(event);
	}

	@SubscribeEvent
	public static void onResourceReload(RegisterClientReloadListenersEvent event) {
		event.registerReloadListener(GolemBEWLR.INSTANCE.get());
	}

	@SubscribeEvent
	public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
		PlayerSkinRenderer.SLIM = new PlayerSkinRenderer(event.getContext(), true);
		PlayerSkinRenderer.REGULAR = new PlayerSkinRenderer(event.getContext(), false);
		if (ENABLE_TLM && ModList.get().isLoaded(TouhouLittleMaid.MOD_ID)) {
			MaidCompat.addLayers(event);
		}
	}

}
