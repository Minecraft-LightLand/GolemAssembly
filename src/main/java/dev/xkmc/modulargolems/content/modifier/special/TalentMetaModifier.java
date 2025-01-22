package dev.xkmc.modulargolems.content.modifier.special;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.item.upgrade.IUpgradeItem;
import dev.xkmc.modulargolems.content.item.upgrade.UpgradeItem;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.data.MGTagGen;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TalentMetaModifier extends GolemModifier {

	public TalentMetaModifier() {
		super(StatFilterType.HEALTH, 1);
	}

	@Override
	public int addSlot(List<IUpgradeItem> upgrades, int lv) {
		int ans = 0;
		Set<IUpgradeItem> set = new HashSet<>(upgrades);
		for (IUpgradeItem item : set) {
			if (item.asItem().getDefaultInstance().is(MGTagGen.BLUE_UPGRADES)) {
				ans++;
			}
		}
		return Math.min(4, ans);
	}
}
