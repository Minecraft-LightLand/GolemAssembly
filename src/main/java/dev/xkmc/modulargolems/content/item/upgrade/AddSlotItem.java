package dev.xkmc.modulargolems.content.item.upgrade;

import dev.xkmc.modulargolems.init.data.MGLangData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class AddSlotItem extends Item {

	public final int slot;

	public AddSlotItem(Properties properties, int slot) {
		super(properties);
		this.slot = slot;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> list, TooltipFlag tooltipFlag) {
		list.add(MGLangData.ADD_SLOT.get(slot));
	}

}
