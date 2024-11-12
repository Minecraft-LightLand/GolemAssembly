package dev.xkmc.modulargolems.content.menu.equipment;

import dev.xkmc.l2core.base.menu.base.BaseContainerScreen;
import dev.xkmc.l2tabs.tabs.core.ITabScreen;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import dev.xkmc.modulargolems.content.entity.dog.DogGolemEntity;
import dev.xkmc.modulargolems.content.entity.humanoid.HumanoidGolemEntity;
import dev.xkmc.modulargolems.content.menu.registry.EquipmentGroup;
import dev.xkmc.modulargolems.content.menu.registry.GolemTabRegistry;
import dev.xkmc.modulargolems.init.data.MGLangData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

public class EquipmentsScreen extends BaseContainerScreen<EquipmentsMenu> implements ITabScreen {

	public EquipmentsScreen(EquipmentsMenu cont, Inventory plInv, Component title) {
		super(cont, plInv, title);
	}

	@Override
	protected void renderBg(GuiGraphics g, float pTick, int mx, int my) {
		var sr = getRenderer();
		sr.start(g);
		if (menu.golem instanceof DogGolemEntity) {
			sr.draw(g, "chest", "slot", -1, -1);
		} else {
			sr.draw(g, "main", "slot", -1, -1);
			sr.draw(g, "off", "slot", -1, -1);
			sr.draw(g, "head", "slot", -1, -1);
			sr.draw(g, "chest", "slot", -1, -1);
			sr.draw(g, "legs", "slot", -1, -1);
			sr.draw(g, "feet", "slot", -1, -1);
			if (menu.getAsPredSlot("off", 0, 0).getItem().isEmpty())
				sr.draw(g, "hand", "altas_shield", 0, 0);
			if (menu.getAsPredSlot("head", 0, 0).getItem().isEmpty())
				sr.draw(g, "armor", "altas_helmet", 0, 0);
			if (menu.getAsPredSlot("chest", 0, 0).getItem().isEmpty())
				sr.draw(g, "armor", "altas_chestplate", 0, 0);
			if (menu.getAsPredSlot("legs", 0, 0).getItem().isEmpty())
				sr.draw(g, "armor", "altas_leggings", 0, 0);
			if (menu.getAsPredSlot("feet", 0, 0).getItem().isEmpty())
				sr.draw(g, "armor", "altas_boots", 0, 0);
			if (menu.golem instanceof HumanoidGolemEntity) {
				sr.draw(g, "arrow", "slot", -1, -1);
				sr.draw(g, "backup", "slot", -1, -1);
				if (menu.getAsPredSlot("arrow", 0, 0).getItem().isEmpty())
					sr.draw(g, "arrow", "slotbg_arrow", -1, -1);
			}
		}

	}

	@Override
	protected void init() {
		super.init();
		new TabManager<>(this, new EquipmentGroup(menu.golem))
				.init(this::addRenderableWidget, GolemTabRegistry.EQUIPMENT.get());
	}

	@Override
	protected void renderTooltip(GuiGraphics g, int mx, int my) {
		super.renderTooltip(g, mx, my);
		if (menu.golem instanceof HumanoidGolemEntity &&
				menu.getCarried().isEmpty() &&
				hoveredSlot != null && !hoveredSlot.hasItem()) {
			List<Component> list = null;
			if (hoveredSlot.getContainerSlot() == 0) {
				list = List.of(MGLangData.SLOT_MAIN.get(),
						MGLangData.SLOT_MAIN_DESC.get());
			}
			if (hoveredSlot.getContainerSlot() == 1) {
				list = List.of(MGLangData.SLOT_OFF.get());
			}
			if (hoveredSlot.getContainerSlot() == 6) {
				list = List.of(MGLangData.SLOT_BACKUP.get(),
						MGLangData.SLOT_BACKUP_DESC.get(),
						MGLangData.SLOT_BACKUP_INFO.get());
			}
			if (hoveredSlot.getContainerSlot() == 7) {
				list = List.of(MGLangData.SLOT_ARROW.get(),
						MGLangData.SLOT_ARROW_DESC.get());

			}
			if (list != null) {
				g.renderTooltip(this.font, list, Optional.empty(), ItemStack.EMPTY, mx, my);
			}
		}
	}

	@Override
	public int screenWidth() {
		return width;
	}

	@Override
	public int screenHeight() {
		return height;
	}

}
