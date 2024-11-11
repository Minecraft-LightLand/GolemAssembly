package dev.xkmc.modulargolems.content.item.card;

import dev.xkmc.modulargolems.init.data.MGLangData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;

public abstract class ClickEntityFilterCard<T> extends TargetFilterCard {

	public ClickEntityFilterCard(Properties properties) {
		super(properties);
	}

	protected abstract List<T> getList(ItemStack stack);

	protected abstract T getValue(LivingEntity entity);

	protected abstract Component getName(T t);

	protected abstract void setList(ItemStack stack, List<T> list);

	protected InteractionResult addTargetEntity(Player player, ItemStack stack, LivingEntity target) {
		var list = new ArrayList<>(getList(stack));
		if (list.contains(getValue(target))) {
			return InteractionResult.SUCCESS;
		}
		if (!player.level().isClientSide()) {
			var val = getValue(target);
			list.add(val);
			setList(stack, list);
			player.sendSystemMessage(MGLangData.TARGET_MSG_ADDED.get(getName(val)));
		}
		return InteractionResult.SUCCESS;
	}

	protected InteractionResult removeTargetEntity(Player player, ItemStack stack, LivingEntity target) {
		var list = new ArrayList<>(getList(stack));
		if (!list.contains(getValue(target))) {
			return InteractionResult.FAIL;
		}
		if (!player.level().isClientSide()) {
			var val = getValue(target);
			if (list.remove(val)) {
				setList(stack, list);
				player.sendSystemMessage(MGLangData.TARGET_MSG_REMOVED.get(getName(val)));
			}
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	protected InteractionResultHolder<ItemStack> removeLast(Player player, ItemStack stack) {
		var list = new ArrayList<>(getList(stack));
		if (list.isEmpty()) return InteractionResultHolder.fail(stack);
		if (!player.level().isClientSide()) {
			var val = list.removeLast();
			setList(stack, list);
			player.sendSystemMessage(MGLangData.TARGET_MSG_REMOVED.get(getName(val)));
		}
		return InteractionResultHolder.success(stack);
	}

	@Override
	public Predicate<LivingEntity> mayTarget(ItemStack stack) {
		var set = new HashSet<>(getList(stack));
		return e -> set.contains(getValue(e));
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
		ItemStack item = player.getItemInHand(hand);
		if (player.isShiftKeyDown()) {
			return removeTargetEntity(player, item, target);
		} else {
			return addTargetEntity(player, item, target);
		}
	}

}
