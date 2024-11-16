package dev.xkmc.modulargolems.content.capability;

import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import dev.xkmc.modulargolems.content.item.card.NameFilterCard;
import dev.xkmc.modulargolems.content.item.card.TargetFilterCard;
import dev.xkmc.modulargolems.content.item.card.UuidFilterCard;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;

@SerialClass
public class TargetFilterConfig {

	public static final int LINE = 18;

	@SerialField
	protected final ArrayList<ItemStack> hostileTo = new ArrayList<>();

	@SerialField
	protected final ArrayList<ItemStack> friendlyTo = new ArrayList<>();

	public boolean internalMatch(ArrayList<ItemStack> list, ItemStack stack) {
		for (ItemStack filter : list) {
			if (stack.getItem() == filter.getItem()) {
				if (ItemStack.isSameItemSameComponents(stack, filter)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean aggressiveToward(LivingEntity le) {
		for (var e : friendlyTo) {
			if (e.getItem() instanceof UuidFilterCard card) {
				if (card.mayTarget(e).test(le)) {
					return false;
				}
			}
		}
		for (var e : hostileTo) {
			if (e.getItem() instanceof TargetFilterCard card) {
				if (card.mayTarget(e).test(le)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean friendlyToward(LivingEntity le) {
		for (var e : hostileTo) {
			if (e.getItem() instanceof UuidFilterCard card) {
				if (card.mayTarget(e).test(le)) {
					return false;
				}
			}
		}
		for (var e : friendlyTo) {
			if (e.getItem() instanceof TargetFilterCard card) {
				if (card.mayTarget(e).test(le)) {
					return true;
				}
			}
		}
		return false;
	}

	public void initDefault() {
		resetHostile();
		resetFriendly();
	}

	public void resetHostile() {
		hostileTo.clear();
		hostileTo.add(GolemItems.CARD_DEF.asStack());
	}

	public void resetFriendly() {
		friendlyTo.clear();
		friendlyTo.add(NameFilterCard.getFriendly());
	}

}
