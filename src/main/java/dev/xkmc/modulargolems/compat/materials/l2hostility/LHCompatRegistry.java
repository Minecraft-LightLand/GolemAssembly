package dev.xkmc.modulargolems.compat.materials.l2hostility;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.item.upgrade.SimpleUpgradeItem;
import dev.xkmc.modulargolems.content.modifier.base.AttributeGolemModifier;
import dev.xkmc.modulargolems.content.modifier.base.PotionDefenseModifier;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.data.MGTagGen;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import dev.xkmc.modulargolems.init.registrate.GolemModifiers;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;

public class LHCompatRegistry {

	public static final Val<HostilityCoreModifier> LH_CORE;
	public static final Val<HostilityPotionModifier> LH_POTION;

	public static final Val<AttributeGolemModifier> LH_TANK, LH_SPEED;
	public static final Val<PotionDefenseModifier> LH_PROTECTION;
	public static final Val<RegenModifier> LH_REGEN;
	public static final Val<ReflectiveModifier> LH_REFLECTIVE;

	public static final ItemEntry<SimpleUpgradeItem> CORE, POTION, TANK, SPEED, PROTECTION, REGEN, REFLECTIVE;

	public static final TagKey<Item> HOSTILITY_UPGRADE = ItemTags.create(ModularGolems.loc("hostility_upgrades"));

	static {
		LH_CORE = GolemModifiers.reg("hostility_core", () -> new HostilityCoreModifier(StatFilterType.HEALTH, 1), "Hostility Core",
				"All other hostility upgrades don't consume upgrade slots");

		LH_POTION = GolemModifiers.reg("hostility_potion", () -> new HostilityPotionModifier(StatFilterType.HEALTH, 1), "Hostility Upgrade: Potions",
				"First level of each kind of potion upgrades don't consume upgrade slot");

		LH_TANK = GolemModifiers.reg("hostility_tank", () -> new AttributeGolemModifier(5,
						new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_HEALTH_P, LHConfig.SERVER.tankHealth::get),
						new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_ARMOR, LHConfig.SERVER.tankArmor::get),
						new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_TOUGH, LHConfig.SERVER.tankTough::get)),
				"Hostility Upgrade: Tanky", null);

		LH_SPEED = GolemModifiers.reg("hostility_speed", () -> new AttributeGolemModifier(5,
						new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_SPEED, LHConfig.SERVER.speedy::get)),
				"Hostility Upgrade: Speedy", null);

		LH_PROTECTION = GolemModifiers.reg("hostility_protection", () -> new PotionDefenseModifier(4, () -> MobEffects.DAMAGE_RESISTANCE),
				"Hostility Upgrade: Protection", null);

		LH_REGEN = GolemModifiers.reg("hostility_regen", () -> new RegenModifier(StatFilterType.HEALTH, 5),
				"Hostility Upgrade: Regeneration", null);

		LH_REFLECTIVE = GolemModifiers.reg("hostility_reflect", () -> new ReflectiveModifier(StatFilterType.HEALTH, 5),
				"Hostility Upgrade: Reflective", null);

		CORE = GolemItems.regModUpgrade("hostility_core", () -> LH_CORE, L2Hostility.MODID).lang("Hostility Core").register();
		POTION = GolemItems.regModUpgrade("hostility_potion", () -> LH_POTION, L2Hostility.MODID).lang("Hostility Upgrade: Potion").register();
		TANK = GolemItems.regModUpgrade("hostility_tank", () -> LH_TANK, L2Hostility.MODID).lang("Hostility Upgrade: Tanky").register();
		SPEED = GolemItems.regModUpgrade("hostility_speed", () -> LH_SPEED, L2Hostility.MODID).lang("Hostility Upgrade: Speedy").register();
		PROTECTION = GolemItems.regModUpgrade("hostility_protection", () -> LH_PROTECTION, L2Hostility.MODID).lang("Hostility Upgrade: Protection").register();
		REGEN = GolemItems.regModUpgrade("hostility_regen", () -> LH_REGEN, L2Hostility.MODID).lang("Hostility Upgrade: Regeneration").register();
		REFLECTIVE = GolemItems.regModUpgrade("hostility_reflect", () -> LH_REFLECTIVE, L2Hostility.MODID).lang("Hostility Upgrade: Reflective").register();
	}

	public static void register() {
		MGTagGen.OPTIONAL_ITEM.add(pvd -> pvd.addTag(HOSTILITY_UPGRADE)
				.addOptional(POTION.getId())
				.addOptional(TANK.getId())
				.addOptional(SPEED.getId())
				.addOptional(PROTECTION.getId())
				.addOptional(REGEN.getId())
				.addOptional(REFLECTIVE.getId()));
	}

}
