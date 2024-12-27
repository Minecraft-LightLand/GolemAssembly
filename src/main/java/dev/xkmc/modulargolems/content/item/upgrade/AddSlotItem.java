package dev.xkmc.modulargolems.content.item.upgrade;

import net.minecraft.world.item.Item;

public class AddSlotItem extends Item {

	public final int slot;

	public AddSlotItem(Properties properties, int slot) {
		super(properties);
		this.slot = slot;
	}

}
