package dev.xkmc.modulargolems.events;

import dev.xkmc.modulargolems.events.event.GolemEquipEvent;
import dev.xkmc.modulargolems.events.event.GolemThrowableEvent;
import dev.xkmc.modulargolems.init.ModularGolems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;


@EventBusSubscriber(modid = ModularGolems.MODID, bus = EventBusSubscriber.Bus.GAME)
public class GolemEventListeners {

	@SubscribeEvent
	public static void onEquip(GolemEquipEvent event) {
		if (event.getStack().getItem() instanceof ArrowItem) {
			event.setSlot(EquipmentSlot.OFFHAND, event.getStack().getCount());
		}
		ItemStack main = event.getEntity().getItemBySlot(EquipmentSlot.MAINHAND);
		if (!main.isEmpty() && !(main.getItem() instanceof BowItem)) {
			if (event.getStack().getItem() instanceof BowItem) {
				event.setSlot(EquipmentSlot.OFFHAND, 1);
			}
		}
		if (event.getStack().getItem() instanceof BannerItem) {
			event.setSlot(EquipmentSlot.HEAD, 1);
		}
	}

	@SubscribeEvent
	public static void isThrowable(GolemThrowableEvent event) {
		if (event.getStack().getItem() instanceof TridentItem) {
			event.setThrowable(level -> {
				var ans = new ThrownTrident(level, event.getEntity(), event.getStack());
				ans.pickup = AbstractArrow.Pickup.DISALLOWED;
				return ans;
			});
		}
	}
}
