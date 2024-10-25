package dev.xkmc.modulargolems.compat.materials.botania;

import com.tterrag.registrate.util.entry.RegistryEntry;

import static dev.xkmc.modulargolems.init.registrate.GolemModifiers.reg;

public class BotCompatRegistry {
	public static final RegistryEntry<ManaMendingModifier> MANA_MENDING;
	public static final RegistryEntry<ManaBoostModifier> MANA_BOOSTING;
	public static final RegistryEntry<ManaProductionModifier> MANA_PRODUCTION;
	public static final RegistryEntry<ManaBurstModifier> MANA_BURST;
	public static final RegistryEntry<PixieAttackModifier> PIXIE_ATTACK;
	public static final RegistryEntry<PixieCounterattackModifier> PIXIE_COUNTERATTACK;

	static {
		MANA_MENDING = reg("mana_mending", ManaMendingModifier::new, "Get %s regeneration at a %s mana/hp efficiency");
		MANA_BOOSTING = reg("mana_boosting", ManaBoostModifier::new, "Deal %s%% extra damage costing %s mana");
		MANA_PRODUCTION = reg("mana_production", ManaProductionModifier::new, "Generate %s mana per second");
		MANA_BURST = reg("mana_burst", ManaBurstModifier::new, "Shoot mana burst toward faraway targets dealing %s%% attack damage, costing %s mana");
		PIXIE_ATTACK = reg("pixie_attack", PixieAttackModifier::new, "Summon a pixie with a %s%% chance when attacking. Pixie damage +%s.");
		PIXIE_COUNTERATTACK = reg("pixie_counterattack", PixieCounterattackModifier::new, "Summon a pixie with a %s%% chance when attacked.");
	}

	public static void register() {
	}

}
