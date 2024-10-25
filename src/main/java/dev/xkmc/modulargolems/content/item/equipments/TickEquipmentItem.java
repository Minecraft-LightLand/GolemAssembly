package dev.xkmc.modulargolems.content.item.equipments;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface TickEquipmentItem {
	void tick(ItemStack stack, Level level, Entity entity);
}
