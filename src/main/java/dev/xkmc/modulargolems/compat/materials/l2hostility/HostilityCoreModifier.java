package dev.xkmc.modulargolems.compat.materials.l2hostility;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.item.upgrade.IUpgradeItem;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;

import java.util.List;

public class HostilityCoreModifier extends GolemModifier {

	public HostilityCoreModifier(StatFilterType type, int maxLevel) {
		super(type, maxLevel);
	}

	@Override
	public int addSlot(List<IUpgradeItem> upgrades, int lv) {
		int ans = 0;
		for (var e : upgrades) {
			if (e.asItem().getDefaultInstance().is(LHCompatRegistry.HOSTILITY_UPGRADE)) {
				ans++;
			}
		}
		return ans;
	}
}
