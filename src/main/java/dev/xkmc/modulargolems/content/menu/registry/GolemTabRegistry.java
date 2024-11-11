package dev.xkmc.modulargolems.content.menu.registry;

import dev.xkmc.l2core.init.reg.simple.SR;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.l2tabs.init.L2Tabs;
import dev.xkmc.l2tabs.tabs.core.TabGroup;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import dev.xkmc.l2tabs.tabs.core.TabType;
import dev.xkmc.modulargolems.content.menu.attribute.AttributeTab;
import dev.xkmc.modulargolems.content.menu.config.ConfigToggleTab;
import dev.xkmc.modulargolems.content.menu.equipment.EquipmentTab;
import dev.xkmc.modulargolems.content.menu.filter.ConfigItemTab;
import dev.xkmc.modulargolems.content.menu.path.ConfigPathTab;
import dev.xkmc.modulargolems.content.menu.target.ConfigTargetTab;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.data.MGLangData;
import net.minecraft.resources.ResourceLocation;

public class GolemTabRegistry {

	public static final ResourceLocation DUMMY = L2Tabs.loc(ModularGolems.MODID);

	public static final TabGroup<ConfigGroup> CONFIG = new TabGroup<>(TabType.RIGHT, 8, false);
	public static final TabGroup<EquipmentGroup> EQUIPMENTS = new TabGroup<>(TabType.RIGHT, 8, false);

	public static final SR<TabToken<?, ?>> TAB_REG = SR.of(ModularGolems.REG, L2Tabs.TABS.reg());

	public static final Val<TabToken<ConfigGroup, ConfigToggleTab>> CONFIG_TOGGLE =
			TAB_REG.reg("toggles", () -> CONFIG.registerTab(
					() -> ConfigToggleTab::new, MGLangData.TAB_TOGGLE.get()));

	public static final Val<TabToken<ConfigGroup, ConfigItemTab>> CONFIG_ITEM =
			TAB_REG.reg("item_filter", () -> CONFIG.registerTab(
					() -> ConfigItemTab::new, MGLangData.TAB_PICKUP.get()));

	public static final Val<TabToken<ConfigGroup, ConfigTargetTab>> CONFIG_TARGET =
			TAB_REG.reg("target_filter", () -> CONFIG.registerTab(
					() -> ConfigTargetTab::new, MGLangData.TAB_TARGET.get()));

	public static final Val<TabToken<ConfigGroup, ConfigPathTab>> CONFIG_PATH =
			TAB_REG.reg("path", () -> CONFIG.registerTab(
					() -> ConfigPathTab::new, MGLangData.TAB_PATH.get()));

	public static final Val<TabToken<EquipmentGroup, EquipmentTab>> EQUIPMENT =
			TAB_REG.reg("equipment", () -> EQUIPMENTS.registerTab(
					() -> EquipmentTab::new, MGLangData.TAB_EQUIPMENT.get()));

	public static final Val<TabToken<EquipmentGroup, AttributeTab>> ATTRIBUTE =
			TAB_REG.reg("attribute", () -> EQUIPMENTS.registerTab(
					() -> AttributeTab::new, MGLangData.TAB_ATTRIBUTE.get()));

	public static void register() {
	}

}
