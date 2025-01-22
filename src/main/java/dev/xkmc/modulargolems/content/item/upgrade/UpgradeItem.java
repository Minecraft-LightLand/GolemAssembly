package dev.xkmc.modulargolems.content.item.upgrade;

import dev.xkmc.modulargolems.content.core.GolemType;
import dev.xkmc.modulargolems.content.modifier.base.ModifierInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class UpgradeItem extends Item implements IUpgradeItem {

	public static final List<UpgradeItem> LIST = new ArrayList<>();

	private final boolean foil;

	protected UpgradeItem(Properties props, boolean foil) {
		super(props);
		this.foil = foil;
		LIST.add(this);
	}

	public abstract List<ModifierInstance> get();

	@Override
	public boolean isFoil(ItemStack stack) {
		return foil;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		for (var e : get()) {
			list.add(e.mod().getTooltip(e.level()));
			list.addAll(e.mod().getDetail(e.level()));
		}
	}

	public boolean fitsOn(GolemType<?, ?> type) {
		boolean fits = false;
		for (var e : get()) {
			fits |= e.mod().fitsOn(type);
		}
		return fits;
	}

}
