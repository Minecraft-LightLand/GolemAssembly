package dev.xkmc.modulargolems.content.item.card;

import dev.xkmc.modulargolems.init.data.MGLangData;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class EntityTypeFilterCard extends ClickEntityFilterCard<EntityType<?>> {

	public EntityTypeFilterCard(Properties properties) {
		super(properties);
	}

	@Override
	protected EntityType<?> getValue(LivingEntity entity) {
		return entity.getType();
	}

	@Override
	protected Component getName(EntityType<?> entityType) {
		return entityType.getDescription();
	}

	protected List<EntityType<?>> getList(ItemStack stack) {
		return GolemItems.DC_FILTER_ENTITY.getOrDefault(stack, List.of());
	}

	protected void setList(ItemStack stack, List<EntityType<?>> list) {
		if (list.isEmpty()) stack.remove(GolemItems.DC_FILTER_ENTITY);
		else GolemItems.DC_FILTER_ENTITY.set(stack, list);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		var types = getList(stack);
		if (!types.isEmpty() && !flag.hasShiftDown()) {
			for (var e : types) {
				list.add(getName(e));
			}
			list.add(MGLangData.TARGET_SHIFT.get());
		} else {
			list.add(MGLangData.TARGET_TYPE_ADD.get());
			list.add(MGLangData.TARGET_TYPE_REMOVE.get());
			list.add(MGLangData.TARGET_REMOVE.get());
		}
	}

}
