package dev.xkmc.modulargolems.compat.materials.twilightforest;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.modulargolems.content.item.upgrade.SimpleUpgradeItem;
import dev.xkmc.modulargolems.content.modifier.base.AttributeGolemModifier;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;

import static dev.xkmc.modulargolems.init.registrate.GolemItems.regModUpgrade;
import static dev.xkmc.modulargolems.init.registrate.GolemModifiers.THORN;
import static dev.xkmc.modulargolems.init.registrate.GolemModifiers.reg;

public class TFCompatRegistry {

	public static final Val<FieryModifier> FIERY;
	public static final Val<TFDamageModifier> TF_DAMAGE;
	public static final Val<TFHealingModifier> TF_HEALING;
	public static final Val<CarminiteModifier> CARMINITE;
	public static final Val<AttributeGolemModifier> NAGA;

	public static final ItemEntry<SimpleUpgradeItem> UP_CARMINITE, UP_STEELEAF, UP_FIERY, UP_IRONWOOD, UP_KNIGHTMETAL, UP_NAGA;

	static {
		FIERY = reg("fiery", FieryModifier::new, "Deal %s%% fire damage to mobs not immune to fire");
		TF_DAMAGE = reg("tf_damage", TFDamageModifier::new, "TF Damage Bonus", "Deal %s%% extra damage in twilight forest");
		TF_HEALING = reg("tf_healing", TFHealingModifier::new, "TF Healing Bonus", "Healing becomes %s%% more in twilight forest");
		CARMINITE = reg("carminite", CarminiteModifier::new, "After being hurt, turn invisible and invinsible for %s seconds");
		NAGA = reg("naga", () -> new AttributeGolemModifier(2,
				new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_ARMOR, () -> 10),
				new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_SPEED, () -> 0.3),
				new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_ATTACK, () -> 4),
				new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_ATKKB, () -> 1)
		));

		UP_CARMINITE = regModUpgrade("carminite", () -> CARMINITE, TFDispatch.MODID).lang("Carminite Upgrade").register();
		UP_STEELEAF = regModUpgrade("steeleaf", () -> TF_DAMAGE, TFDispatch.MODID).lang("Steeleaf Upgrade").register();
		UP_FIERY = regModUpgrade("fiery", () -> FIERY, TFDispatch.MODID).lang("Fiery Upgrade").register();
		UP_IRONWOOD = regModUpgrade("ironwood", () -> TF_HEALING, TFDispatch.MODID).lang("Ironwood Upgrade").register();
		UP_KNIGHTMETAL = regModUpgrade("knightmetal", () -> THORN, TFDispatch.MODID).lang("Knightmetal Upgrade").register();
		UP_NAGA = regModUpgrade("naga", () -> NAGA, TFDispatch.MODID).lang("Naga Upgrade").register();

	}

	public static void register() {

	}

}
