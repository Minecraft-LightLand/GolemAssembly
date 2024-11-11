package dev.xkmc.modulargolems.content.item.card;

import dev.xkmc.modulargolems.init.data.MGLangData;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.*;

public class UuidFilterCard extends ClickEntityFilterCard<UUID> {

	private static final String KEY = "idList";

	public UuidFilterCard(Properties properties) {
		super(properties);
	}

	@Override
	protected UUID getValue(LivingEntity entity) {
		return entity.getUUID();
	}

	@Override
	protected Component getName(UUID uuid) {
		return Component.literal(uuid.toString().substring(0, 8));
	}

	public List<UUID> getList(ItemStack stack) {
		return new ArrayList<>(GolemItems.DC_FILTER_UUID.getOrDefault(stack, Set.of()));
	}

	public void setList(ItemStack stack, List<UUID> list) {
		if (list.isEmpty()) stack.remove(GolemItems.DC_FILTER_UUID);
		else GolemItems.DC_FILTER_UUID.set(stack, new LinkedHashSet<>(list));
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		var ids = getList(stack);
		if (!ids.isEmpty() && !flag.hasShiftDown()) {
			for (var e : ids) {
				list.add(getName(e));
			}
			list.add(MGLangData.TARGET_SHIFT.get());
		} else {
			list.add(MGLangData.TARGET_UUID_ADD.get());
			list.add(MGLangData.TARGET_UUID_REMOVE.get());
			list.add(MGLangData.TARGET_REMOVE.get());
		}
	}


}
