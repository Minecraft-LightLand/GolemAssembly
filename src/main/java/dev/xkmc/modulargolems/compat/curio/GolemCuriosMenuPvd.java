package dev.xkmc.modulargolems.compat.curio;

import dev.xkmc.l2tabs.compat.CuriosWrapper;
import dev.xkmc.modulargolems.content.menu.registry.IMenuPvd;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public record GolemCuriosMenuPvd(LivingEntity e, int page) implements IMenuPvd {

	@Override
	public Component getDisplayName() {
		return e.getDisplayName();
	}

	public void writeBuffer(FriendlyByteBuf buf) {
		buf.writeInt(e.getId());
		buf.writeInt(page);
	}

	@Override
	public AbstractContainerMenu createMenu(int wid, Inventory inv, Player player) {
		var compat = CurioCompatRegistry.get();
		if (compat == null) return null;
		return new GolemCuriosListMenu(compat.menuType.get(), wid, inv, new CuriosWrapper(e, page));
	}
}
