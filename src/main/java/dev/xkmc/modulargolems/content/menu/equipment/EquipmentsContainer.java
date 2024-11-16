package dev.xkmc.modulargolems.content.menu.equipment;

import dev.xkmc.l2core.base.menu.base.BaseContainerMenu;
import dev.xkmc.modulargolems.content.entity.humanoid.HumanoidGolemEntity;
import dev.xkmc.modulargolems.content.entity.humanoid.ItemWrapper;
import net.minecraft.world.item.ItemStack;

public class EquipmentsContainer extends BaseContainerMenu.BaseContainer<EquipmentsMenu> {

	public EquipmentsContainer(EquipmentsMenu menu) {
		super(0, menu);
	}

	private ItemWrapper getWrapper(int index) {
		if (parent.golem == null || index < 0) return ItemWrapper.EMPTY;
		if (index < 6) {
			return parent.golem.getWrapperOfHand(parent.equipmentSlots[index]);
		}
		if (!(parent.golem instanceof HumanoidGolemEntity humanoid))
			return ItemWrapper.EMPTY;
		if (index == 6)
			return humanoid.getBackupHand();
		if (index == 7)
			return humanoid.getArrowSlot();
		return ItemWrapper.EMPTY;
	}

	@Override
	public ItemStack getItem(int index) {
		return getWrapper(index).getItem();
	}

	@Override
	public void setItem(int index, ItemStack stack) {
		getWrapper(index).setItem(stack);
	}

	@Override
	public ItemStack removeItem(int index, int count) {
		return getWrapper(index).getItem().split(count);
	}

}
