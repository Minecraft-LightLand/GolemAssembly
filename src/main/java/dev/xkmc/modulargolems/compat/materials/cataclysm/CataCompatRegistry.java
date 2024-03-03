package dev.xkmc.modulargolems.compat.materials.cataclysm;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.modulargolems.compat.materials.create.CreateDispatch;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.item.upgrade.SimpleUpgradeItem;

import static dev.xkmc.modulargolems.init.registrate.GolemItems.regModUpgrade;
import static dev.xkmc.modulargolems.init.registrate.GolemModifiers.reg;

public class CataCompatRegistry {

	public static final RegistryEntry<IgnisFireballModifier> IGNIS_FIREBALL;
	public static final RegistryEntry<IgnisAttackModifier> IGNIS_ATTACK;
	public static final RegistryEntry<HarbingerDeathBeamModifier> HARBINGER_BEAM;
	public static final RegistryEntry<HarbingerHomingMissileModifier> HARBINGER_MISSILE;

	public static final RegistryEntry<LeviathanBlastPortalModifier> PORTAL;
	public static final RegistryEntry<EnderGuardianVoidRuneModifier> RUNE;
	public static final RegistryEntry<SimpleUpgradeItem> LEVIATHAN, ENDER_GUARDIAN;

	static {
		IGNIS_FIREBALL = reg("ignis_fireball", () -> new IgnisFireballModifier(StatFilterType.HEAD, 2),
				"Shoot Ignis fireballs toward target.");

		IGNIS_ATTACK = reg("ignis_attack", () -> new IgnisAttackModifier(StatFilterType.ATTACK, 2),
				"Stack Blazing Brande effect and regenerate health when hit target. When health is lower than half, direct damage bypasses armor.");

		HARBINGER_BEAM = reg("harbinger_death_beam", () -> new HarbingerDeathBeamModifier(StatFilterType.HEAD, 1),
				"Shoot Death Beam toward target.");

		HARBINGER_MISSILE = reg("harbinger_missile", () -> new HarbingerHomingMissileModifier(StatFilterType.ATTACK, 2),
				"Shoot Homing Missile toward target.");

		PORTAL = reg("leviathan_blast_portal", LeviathanBlastPortalModifier::new, "Create blast portal at target");
		RUNE = reg("ender_guardian_void_rune", EnderGuardianVoidRuneModifier::new, "Summon void rune toward target");

		LEVIATHAN = regModUpgrade("leviathan_blast_portal", () -> PORTAL, CataDispatch.MODID).lang("Leviathan Upgrade").register();
		ENDER_GUARDIAN = regModUpgrade("ender_guardian_void_rune", () -> RUNE, CataDispatch.MODID).lang("Ender Guardian Upgrade").register();

	}

	public static void register() {

	}

}
