package dev.xkmc.modulargolems.init.data;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class MGConfig {

	public static class Client {

		public final ForgeConfigSpec.BooleanValue shieldUsePoseFixForModdedShields;

		Client(ForgeConfigSpec.Builder builder) {
			shieldUsePoseFixForModdedShields = builder
					.comment("Replace isUsingItem with isBlocking for modded shield model predicate")
					.comment("Fix shield rendering on humanoid golem but may break stuff")
					.define("shieldUsePoseFixForModdedShields", false);
		}

	}

	public static class Common {

		public final ForgeConfigSpec.DoubleValue thorn;
		public final ForgeConfigSpec.DoubleValue fiery;
		public final ForgeConfigSpec.DoubleValue magicResistance;
		public final ForgeConfigSpec.DoubleValue explosionResistance;
		public final ForgeConfigSpec.DoubleValue compatTFHealing;
		public final ForgeConfigSpec.DoubleValue compatTFDamage;
		public final ForgeConfigSpec.IntValue manaMendingCost;
		public final ForgeConfigSpec.DoubleValue manaMendingVal;
		public final ForgeConfigSpec.DoubleValue manaBoostingDamage;
		public final ForgeConfigSpec.IntValue manaBoostingCost;
		public final ForgeConfigSpec.IntValue manaProductionVal;
		public final ForgeConfigSpec.DoubleValue manaBurstDamage;
		public final ForgeConfigSpec.IntValue manaBurstCost;
		public final ForgeConfigSpec.DoubleValue pixieAttackProb;
		public final ForgeConfigSpec.DoubleValue pixieCounterattackProb;
		public final ForgeConfigSpec.IntValue carminiteTime;
		public final ForgeConfigSpec.DoubleValue coating;
		public final ForgeConfigSpec.DoubleValue mechSpeed;
		public final ForgeConfigSpec.DoubleValue mechAttack;
		public final ForgeConfigSpec.IntValue mechMaxFuel;
		public final ForgeConfigSpec.BooleanValue barehandRetrieve;
		public final ForgeConfigSpec.BooleanValue strictInteract;
		public final ForgeConfigSpec.DoubleValue damageCap;
		public final ForgeConfigSpec.IntValue conduitCooldown;
		public final ForgeConfigSpec.IntValue conduitDamage;
		public final ForgeConfigSpec.DoubleValue conduitBoostAttack;
		public final ForgeConfigSpec.IntValue conduitBoostArmor;
		public final ForgeConfigSpec.IntValue conduitBoostToughness;
		public final ForgeConfigSpec.DoubleValue conduitBoostReduction;
		public final ForgeConfigSpec.DoubleValue conduitBoostSpeed;
		public final ForgeConfigSpec.IntValue thunderHeal;
		public final ForgeConfigSpec.IntValue teleportRadius;
		public final ForgeConfigSpec.IntValue teleportCooldown;
		public final ForgeConfigSpec.DoubleValue targetDamageBonus;
		public final ForgeConfigSpec.IntValue basePickupRange;
		public final ForgeConfigSpec.IntValue mendingXpCost;

		public final ForgeConfigSpec.IntValue summonDistance;
		public final ForgeConfigSpec.IntValue retrieveDistance;
		public final ForgeConfigSpec.IntValue retrieveRange;
		public final ForgeConfigSpec.BooleanValue ownerPickupOnly;
		public final ForgeConfigSpec.BooleanValue allowEditCuriosForOthers;
		public final ForgeConfigSpec.DoubleValue startFollowRadius;
		public final ForgeConfigSpec.DoubleValue stopWanderRadius;
		public final ForgeConfigSpec.DoubleValue maxWanderRadius;
		public final ForgeConfigSpec.DoubleValue riddenSpeedFactor;

		public final ForgeConfigSpec.IntValue targetResetTime;
		public final ForgeConfigSpec.DoubleValue targetResetNoMovementRange;

		public final ForgeConfigSpec.IntValue dogGolemSlot;
		public final ForgeConfigSpec.IntValue humanoidGolemSlot;
		public final ForgeConfigSpec.IntValue largeGolemSlot;
		public final ForgeConfigSpec.BooleanValue doEnemyAggro;
		public final ForgeConfigSpec.BooleanValue allowDimensionChange;

		public final ForgeConfigSpec.DoubleValue candyDamage;
		public final ForgeConfigSpec.IntValue caramelDuration;
		public final ForgeConfigSpec.DoubleValue polarizeRange;
		public final ForgeConfigSpec.DoubleValue polarizeDamage;
		public final ForgeConfigSpec.DoubleValue polarizeForce;
		public final ForgeConfigSpec.DoubleValue reformationAbsorption;
		public final ForgeConfigSpec.DoubleValue reformationHealing;
		public final ForgeConfigSpec.IntValue reformationMax;
		public final ForgeConfigSpec.IntValue radiationDuration;
		public final ForgeConfigSpec.DoubleValue radiationDamage;
		public final ForgeConfigSpec.DoubleValue atomicHeal;
		public final ForgeConfigSpec.IntValue atomicDuration;

		Common(ForgeConfigSpec.Builder builder) {
			{
				barehandRetrieve = builder.comment("Allow players to retrieve the golems by bare hand")
						.define("barehandRetrieve", true);
				doEnemyAggro = builder.comment("Make mobs aggro to iron golem automatically aggro to modular golems")
						.define("doEnemyAggro", true);
				allowDimensionChange = builder.comment("Allow golems to enter portal")
						.define("allowDimensionChange", false);
				summonDistance = builder.comment("Max distance to summon single golem")
						.defineInRange("summonDistance", 64, 1, 1000);
				retrieveDistance = builder.comment("Max distance to retrieve single golem")
						.defineInRange("retrieveDistance", 64, 1, 1000);
				retrieveRange = builder.comment("Max distance to retrieve all golems")
						.defineInRange("retrieveRange", 20, 1, 1000);
				ownerPickupOnly = builder.comment("Only owner can pickup")
						.define("ownerPickupOnly", true);
				strictInteract = builder.comment("When enabled, the following features will be disabled when player holds item in hand:")
						.comment("- give item to golem or take items from golem by right click").comment("- order dog golem to seat")
						.define("strictInteract", false);
				allowEditCuriosForOthers = builder.comment("Allow command wand to edit curios of other mobs you own")
						.define("allowEditCuriosForOthers", true);
				startFollowRadius = builder.comment("Max golem activity radius before following player in follow mode")
						.defineInRange("startFollowRadius", 10d, 1, 100);
				stopWanderRadius = builder.comment("Max golem activity radius before willing to go back to origin in wander mode")
						.defineInRange("stopWanderRadius", 20d, 1, 100);
				maxWanderRadius = builder.comment("Max golem activity radius before being teleported back")
						.defineInRange("maxWanderRadius", 30d, 1, 100);
				riddenSpeedFactor = builder.comment("Speed factor for dog golem when ridden by entities")
						.defineInRange("riddenSpeedFactor", 0.8, 0, 2);
				targetResetTime = builder.comment("Target reset time after no movement, in ticks")
						.defineInRange("targetResetTime", 600, 1, 10000);
				targetResetNoMovementRange = builder.comment("Distance considered as no movement")
						.defineInRange("targetResetNoMovementRange", 0.5, 0, 10);

				largeGolemSlot = builder.comment("Default slots for large golem")
						.defineInRange("largeGolemSlot", 4, 0, 100);
				humanoidGolemSlot = builder.comment("Default slots for humanoid golem")
						.defineInRange("humanoidGolemSlot", 3, 0, 100);
				dogGolemSlot = builder.comment("Default slots for dog golem")
						.defineInRange("dogGolemSlot", 2, 0, 100);
			}

			// modifiers
			{
				builder.push("modifiers");
				{
					thorn = builder.comment("Percentage damage reflection per level of thorn")
							.defineInRange("thorn", 0.2, 0, 100);
					magicResistance = builder.comment("Percentage damage reduction per level of magic resistance")
							.defineInRange("magicResistance", 0.2, 0, 1);
					explosionResistance = builder.comment("Percentage damage reduction per level of explosion resistance")
							.defineInRange("explosionResistance", 0.2, 0, 1);
					damageCap = builder.comment("Damage Cap at max level")
							.defineInRange("damageCap", 0.1d, 0, 1);
					thunderHeal = builder.comment("Healing when thunder immune golems are striked")
							.defineInRange("thunderHeal", 10, 1, 10000);
				}
				builder.pop();

				builder.push("twilight forest compat");
				{
					fiery = builder.comment("Percentage damage addition per level of fiery")
							.defineInRange("fiery", 0.5, 0, 100);
					compatTFHealing = builder.comment("Percentage healing bonus per level of twilight healing")
							.defineInRange("compatTFHealing", 0.5, 0, 100);
					compatTFDamage = builder.comment("Percentage damage bonus per level of twilight damage")
							.defineInRange("compatTFDamage", 0.2, 0, 100);
					carminiteTime = builder.comment("Time for the golem to be invincible (in ticks) per level of carminite")
							.defineInRange("carminiteTime", 20, 1, 100000);
				}
				builder.pop();

				builder.push("botania compat");
				{
					manaMendingCost = builder.comment("Mana mending cost per HP")
							.defineInRange("mendingManaCost", 200, 1, 50000);
					manaMendingVal = builder.comment("Mana mending value per level")
							.defineInRange("manaMendingVal", 4D, 0.1D, 100D);
					manaBoostingDamage = builder.comment("Percentage damage bonus per level of mana boost")
							.defineInRange("manaBoostingDamage", 0.2, 0, 100);
					manaBoostingCost = builder.comment("Mana boosting cost per level (of 1 hit)")
							.defineInRange("manaBoostingCost", 500, 0, 100000);
					manaProductionVal = builder.comment("Mana production value per level")
							.defineInRange("manaProductionVal", 20, 0, 100000);
					manaBurstDamage = builder.comment("Damage of magic burst as percentage of attack damage")
							.defineInRange("manaBurstDamage", 0.25, 0, 1.0);
					manaBurstCost = builder.comment("Mana burst cost per level")
							.defineInRange("manaBurstCost", 2000, 0, 100000);
					pixieAttackProb = builder.comment("Probability of summoning a pixie when attacking")
							.defineInRange("pixieAttackProb", 0.05, 0, 1.0);
					pixieCounterattackProb = builder.comment("Probability of summoning a pixie when attacked")
							.defineInRange("pixiecounterattackProb", 0.25, 0, 1.0);
				}
				builder.pop();

				builder.push("create compat");
				{
					coating = builder.comment("Damage absorbed per level of coating")
							.defineInRange("coating", 1d, 0, 10000);
					mechSpeed = builder.comment("Speed boost per level of Mechanical Mobility")
							.defineInRange("mechSpeed", 0.2, 0, 1);
					mechAttack = builder.comment("Attack boost per level of Mechanical Force")
							.defineInRange("mechAttack", 0.2, 0, 1);
					mechMaxFuel = builder.comment("Maximum number of fuel item that can be added at the same time")
							.defineInRange("mechMaxFuel", 3, 1, 364);
				}
				builder.pop();

				builder.push("l2complements compat");
				{
					conduitCooldown = builder.comment("Conduit cooldown")
							.defineInRange("conduitCooldown", 120, 0, 10000);
					conduitDamage = builder.comment("Conduit damage")
							.defineInRange("conduitDamage", 2, 0, 10000);
					conduitBoostAttack = builder.comment("Conduit modifier boosts attack (percentage) when golem is in water")
							.defineInRange("conduitBoostAttack", 0.2, 0, 100);
					conduitBoostArmor = builder.comment("Conduit modifier boosts armor when golem is in water")
							.defineInRange("conduitBoostArmor", 5, 0, 100);
					conduitBoostToughness = builder.comment("Conduit modifier boosts toughness when golem is in water")
							.defineInRange("conduitBoostToughness", 2, 0, 100);
					conduitBoostSpeed = builder.comment("Conduit modifier boosts speed (percentage) when golem is in water")
							.defineInRange("conduitBoostSpeed", 0.2, 0, 100);
					conduitBoostReduction = builder.comment("Conduit modifier reduce damage taken when golem is in water (multiplicative)")
							.defineInRange("conduitBoostReduction", 0.2, 0, 100);
					teleportRadius = builder.comment("Teleport max radius")
							.defineInRange("teleportRadius", 6, 1, 32);
					teleportCooldown = builder.comment("Teleport cooldown in ticks for avoiding physical damage")
							.defineInRange("teleportCooldown", 40, 1, 10000);
					targetDamageBonus = builder.comment("Damage bonus for attacking specific type of enemy")
							.defineInRange("targetDamageBonus", 0.5, 0, 100);
					basePickupRange = builder.comment("Pickup range per level for pickup upgrade")
							.defineInRange("basePickupRange", 6, 1, 16);
					mendingXpCost = builder.comment("Mending Xp Cost per health point")
							.defineInRange("mendingXpCost", 2, 1, 10000);
				}
				builder.pop();

				builder.push("alexscaves compat");
				{
					candyDamage = builder.comment("Gum ball damage")
							.defineInRange("candyDamage", 4d, 1, 100);
					caramelDuration = builder.comment("Molten caramel duration in ticks")
							.defineInRange("caramelDuration", 200, 1, 10000);
					polarizeRange = builder.comment("Polarize modifier pull/push base radius")
							.defineInRange("polarizeRange", 5d, 1, 40);
					polarizeDamage = builder.comment("Polarize modifier base damage")
							.defineInRange("polarizeDamage", 2d, 0, 100);
					polarizeForce = builder.comment("Polarize modifier pull/push force")
							.defineInRange("polarizeForce", 0.1d, 0, 1);
					reformationAbsorption = builder.comment("Reformation modifier absorption per iron ingot")
							.defineInRange("reformationAbsorption", 10d, 1, 100);
					reformationHealing = builder.comment("Reformation modifier healing per iron ingot")
							.defineInRange("reformationHealing", 2d, 0, 100);
					reformationMax = builder.comment("Reformation modifier max stacking per level")
							.defineInRange("reformationMax", 2, 1, 10);
					radiationDuration = builder.comment("Radiation modifier: duration of irradiated effect")
							.defineInRange("radiationDuration", 200, 20, 20000);
					radiationDamage = builder.comment("Radiation modifier: damage boost per irradiated level")
							.defineInRange("radiationDamage", 0.2, 0, 1);
					atomicHeal = builder.comment("Atomic Fueling modifier: percent healing per uranium nugget per level")
							.defineInRange("atomicHeal", 0.1, 0, 1);
					atomicDuration = builder.comment("Atomic Fueling modifier: boost duration per uranium nugget used")
							.defineInRange("atomicDuration", 200, 20, 20000);

				}
				builder.pop();
			}
		}

	}

	public static final ForgeConfigSpec CLIENT_SPEC;
	public static final Client CLIENT;

	public static final ForgeConfigSpec COMMON_SPEC;
	public static final Common COMMON;

	static {
		final Pair<Client, ForgeConfigSpec> client = new ForgeConfigSpec.Builder().configure(Client::new);
		CLIENT_SPEC = client.getRight();
		CLIENT = client.getLeft();

		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	public static void init() {
		register(ModConfig.Type.CLIENT, CLIENT_SPEC);
		register(ModConfig.Type.COMMON, COMMON_SPEC);
	}

	private static void register(ModConfig.Type type, IConfigSpec<?> spec) {
		var mod = ModLoadingContext.get().getActiveContainer();
		String path = "l2_configs/" + mod.getModId() + "-" + type.extension() + ".toml";
		ModLoadingContext.get().registerConfig(type, spec, path);
	}


}
