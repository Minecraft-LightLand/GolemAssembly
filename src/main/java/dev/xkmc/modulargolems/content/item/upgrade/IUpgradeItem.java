package dev.xkmc.modulargolems.content.item.upgrade;

import dev.xkmc.modulargolems.content.modifier.base.ModifierInstance;
import net.minecraft.world.level.ItemLike;

import java.util.List;

public interface IUpgradeItem extends ItemLike {

	List<ModifierInstance> get();

}
