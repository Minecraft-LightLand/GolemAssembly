package dev.xkmc.modulargolems.content.item.upgrade;

import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.modulargolems.compat.materials.common.AddSlotModifier;
import dev.xkmc.modulargolems.content.modifier.base.ModifierInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class AddSlotTemplate extends Item implements IUpgradeItem {

	private final Val<AddSlotModifier> sup;

	public AddSlotTemplate(Properties p, Val<AddSlotModifier> sup) {
		super(p);
		this.sup = sup;
	}

	@Override
	public List<ModifierInstance> get() {
		return List.of(new ModifierInstance(sup.get(), 1));
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		for (var e : get()) {
			list.add(e.mod().getTooltip(e.level()));
			list.addAll(e.mod().getDetail(e.level()));
		}
	}

}
