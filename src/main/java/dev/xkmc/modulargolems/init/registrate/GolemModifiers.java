package dev.xkmc.modulargolems.init.registrate;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2core.init.reg.registrate.NamedEntry;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.modulargolems.compat.materials.common.AddSlotModifier;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.GolemFlags;
import dev.xkmc.modulargolems.content.modifier.base.*;
import dev.xkmc.modulargolems.content.modifier.common.BellModifier;
import dev.xkmc.modulargolems.content.modifier.common.ThornModifier;
import dev.xkmc.modulargolems.content.modifier.immunes.*;
import dev.xkmc.modulargolems.content.modifier.ride.RideUpgrade;
import dev.xkmc.modulargolems.content.modifier.special.PickupModifier;
import dev.xkmc.modulargolems.content.modifier.special.PotionMetaModifier;
import dev.xkmc.modulargolems.content.modifier.special.SonicModifier;
import dev.xkmc.modulargolems.content.modifier.special.TalentMetaModifier;
import dev.xkmc.modulargolems.init.ModularGolems;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;

import javax.annotation.Nullable;

import static dev.xkmc.modulargolems.init.ModularGolems.REGISTRATE;

public class GolemModifiers {

	public static final Val<FireImmuneModifier> FIRE_IMMUNE;
	public static final Val<ThunderImmuneModifier> THUNDER_IMMUNE;
	public static final Val<MagicImmuneModifier> MAGIC_IMMUNE;
	public static final Val<MagicResistanceModifier> MAGIC_RES;
	public static final Val<ThornModifier> THORN;
	public static final Val<ExplosionResistanceModifier> EXPLOSION_RES;
	public static final Val<DamageCapModifier> DAMAGE_CAP;
	public static final Val<ProjectileRejectModifier> PROJECTILE_REJECT;
	public static final Val<ImmunityModifier> IMMUNITY;
	public static final Val<PlayerImmuneModifier> PLAYER_IMMUNE;
	public static final Val<SonicModifier> SONIC;
	public static final Val<BellModifier> BELL;
	public static final Val<PickupModifier> PICKUP;
	public static final Val<TargetBonusModifier> EMERALD;
	public static final Val<TalentMetaModifier> TALENTED;
	public static final Val<PotionMetaModifier> CAULDRON;
	public static final Val<SimpleFlagModifier> FLOAT, SWIM, ENDER_SIGHT, RECYCLE, PICKUP_NODESTROY, PICKUP_MENDING;
	public static final Val<AttributeGolemModifier> ARMOR, TOUGH, DAMAGE, REGEN, SPEED, SIZE_UPGRADE;
	public static final Val<PotionAttackModifier> SLOW, WEAK, WITHER;
	public static final Val<RideUpgrade> MOUNT_UPGRADE;
	public static final Val<AddSlotModifier> DIAMOND_ADD, NETHERITE_ADD;

	static {
		FIRE_IMMUNE = reg("fire_immune", FireImmuneModifier::new,
				"Immune to fire damage. Floats in Lava.");
		THUNDER_IMMUNE = reg("thunder_immune", ThunderImmuneModifier::new,
				"Immune to lightning bolt damage. Attract Lightning. When struck, gain fire resistance for 10 seconds, and heal %s health.");
		MAGIC_IMMUNE = reg("magic_immune", MagicImmuneModifier::new,
				"Immune to magic damage");
		MAGIC_RES = reg("magic_resistant", MagicResistanceModifier::new,
				"Magic damage taken reduced to %s%% of original");
		RECYCLE = reg("recycle", () -> new SimpleFlagModifier(StatFilterType.HEALTH, GolemFlags.RECYCLE),
				"Drop golem holder of 0 health when killed. Holder will return to inventory is player is present.");
		ARMOR = reg("armor_up", () -> new AttributeGolemModifier(2,
				new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_ARMOR, () -> 10)
		));
		TOUGH = reg("toughness_up", () -> new AttributeGolemModifier(2,
				new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_ARMOR, () -> 15),
				new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_TOUGH, () -> 6)
		));
		DAMAGE = reg("damage_up", () -> new AttributeGolemModifier(GolemModifier.MAX_LEVEL,
				new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_ATTACK, () -> 2)
		));
		SPEED = reg("speed_up", () -> new AttributeGolemModifier(GolemModifier.MAX_LEVEL,
				new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_SPEED, () -> 0.2)
		));
		REGEN = reg("regeneration_up", () -> new AttributeGolemModifier(GolemModifier.MAX_LEVEL,
				new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_REGEN, () -> 1)
		));

		DIAMOND_ADD = reg("add_slot_diamond", () -> new AddSlotModifier(1), "Diamond Expansion", "Add 1 upgrade slot. Only once per golem.");
		NETHERITE_ADD = reg("add_slot_netherite", () -> new AddSlotModifier(1), "Netherite Expansion", "Add 1 upgrade slot. Only once per golem.");

		THORN = reg("thorn", ThornModifier::new,
				"Reflect %s%% damage");
		FLOAT = reg("float", () -> new SimpleFlagModifier(StatFilterType.HEALTH, GolemFlags.FLOAT),
				"Golem floats in water and lava instead of sinking");
		EXPLOSION_RES = reg("explosion_resistant", ExplosionResistanceModifier::new,
				"Explosion damage taken reduced to %s%% of original, and will not break blocks.");
		DAMAGE_CAP = reg("damage_cap", DamageCapModifier::new,
				"Damage taken are limited within %s%% of max health.");
		PROJECTILE_REJECT = reg("projectile_reject", ProjectileRejectModifier::new,
				"Deflect projectiles. Takes no projectile damage.");
		IMMUNITY = reg("immunity", ImmunityModifier::new,
				"Immune to all damage, except void damage. Mobs won't attack invulnerable golem.");
		SWIM = reg("swim", () -> new SimpleFlagModifier(StatFilterType.MOVEMENT, GolemFlags.SWIM),
				"Golem can swim");
		PLAYER_IMMUNE = reg("player_immune", PlayerImmuneModifier::new,
				"Immune to friendly fire.");
		SONIC = reg("sonic_boom", SonicModifier::new,
				"Golem can use Sonic Boom Attack. If the golem can perform area attack, then Sonic Boom can hit multiple targets.");
		ENDER_SIGHT = reg("ender_sight", () -> new SimpleFlagModifier(StatFilterType.HEALTH, GolemFlags.SEE_THROUGH),
				"Golem can see through wall and ceilings.");
		BELL = reg("bell", BellModifier::new,
				"When the golem wants to attack, it will ring its bell, attracting all enemies and light them up.");
		SLOW = reg("slow", () -> new PotionAttackModifier(StatFilterType.MASS, 3,
						i -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, i - 1)),
				"Potion Upgrade: Slowness", null);
		WEAK = reg("weak", () -> new PotionAttackModifier(StatFilterType.MASS, 3,
						i -> new MobEffectInstance(MobEffects.WEAKNESS, 60, i - 1)),
				"Potion Upgrade: Weakness", null);
		WITHER = reg("wither", () -> new PotionAttackModifier(StatFilterType.MASS, 3,
						i -> new MobEffectInstance(MobEffects.WITHER, 60, i - 1)),
				"Potion Upgrade: Wither", null);
		EMERALD = reg("emerald", () -> new TargetBonusModifier(e -> e.getType().is(EntityTypeTags.ILLAGER)),
				"Deal %s%% more damage to illagers");
		PICKUP = reg("pickup", PickupModifier::new, "Pickup",
				"Golems will pickup items and experiences within %s blocks and give them to you. See Patchouli for full documentation. The golem may destroy items if it find nowhere to store them");
		PICKUP_NODESTROY = reg("pickup_no_destroy", () -> new SimpleFlagModifier(StatFilterType.MASS, GolemFlags.NO_DESTROY),
				"Pickup Augment: No Destroy", "When a golem attempts to pickup an item and find nowhere to place it, it will not pickup the item instead. It will cause lag if the golem is in a region with lots of items.");
		PICKUP_MENDING = reg("pickup_mending", () -> new SimpleFlagModifier(StatFilterType.MASS, GolemFlags.MENDING),
				"Pickup Augment: Mending", "When a golem picks up experiences, it will try to heal itself with the experience.");
		TALENTED = reg("talented", TalentMetaModifier::new, "Talented",
				"First of every kind of upgrades with blue arrow will no longer consume upgrade slots (up to 4)");
		CAULDRON = reg("cauldron", PotionMetaModifier::new, "Cauldron",
				"Repeated potion upgrades will no longer consume upgrade slots.");

		MOUNT_UPGRADE = reg("ridding_speed_up", () -> new RideUpgrade(1,
				new RideUpgrade.AttrEntry(GolemTypes.STAT_SPEED, () -> 0.3),
				new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_JUMP, () -> 0.25),
				new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_HEALTH_P, () -> 0.2)
		), "Mount Upgrade", "Golem will not attack, and will not be targeted for attack.");

		SIZE_UPGRADE = reg("size_up", () -> new AttributeGolemModifier(2,
				new RideUpgrade.AttrEntry(GolemTypes.STAT_SPEED, () -> 0.15),
				new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_HEALTH_P, () -> 0.2),
				new RideUpgrade.AttrEntry(GolemTypes.STAT_SIZE, () -> 0.5),
				new RideUpgrade.AttrEntry(GolemTypes.STAT_RANGE, () -> 0.5)
		));
	}

	public static <T extends GolemModifier> Val<T> reg(String id, NonNullSupplier<T> sup, @Nullable String name, @Nullable String def) {
		Mutable<RegistryEntry<GolemModifier, T>> holder = new MutableObject<>();
		var ans = REGISTRATE.generic(GolemTypes.MODIFIERS, id, sup).defaultLang();
		if (name != null) ans.lang(NamedEntry::getDescriptionId, name);
		if (def != null) ans.getOwner().addRawLang("modifier." + ModularGolems.MODID + "." + id + ".desc", def);
		var result = ans.register();
		holder.setValue(result);
		return new Val.Registrate<>(result);
	}

	public static <T extends GolemModifier> Val<T> reg(String id, NonNullSupplier<T> sup, String def) {
		return reg(id, sup, null, def);
	}

	public static <T extends GolemModifier> Val<T> reg(String id, NonNullSupplier<T> sup) {
		return reg(id, sup, null, null);
	}

	public static void register() {

	}

}
