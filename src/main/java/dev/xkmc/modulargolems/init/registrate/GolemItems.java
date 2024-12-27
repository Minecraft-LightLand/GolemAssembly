package dev.xkmc.modulargolems.init.registrate;

import com.mojang.serialization.Codec;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.l2core.init.reg.simple.DCReg;
import dev.xkmc.l2core.init.reg.simple.DCVal;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.l2itemselector.init.data.L2ISTagGen;
import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.modulargolems.compat.materials.common.CompatManager;
import dev.xkmc.modulargolems.content.client.armor.GolemModelPaths;
import dev.xkmc.modulargolems.content.entity.dog.DogGolemEntity;
import dev.xkmc.modulargolems.content.entity.dog.DogGolemPartType;
import dev.xkmc.modulargolems.content.entity.humanoid.HumaniodGolemPartType;
import dev.xkmc.modulargolems.content.entity.humanoid.HumanoidGolemEntity;
import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemEntity;
import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemPartType;
import dev.xkmc.modulargolems.content.item.card.*;
import dev.xkmc.modulargolems.content.item.data.GolemConfigKey;
import dev.xkmc.modulargolems.content.item.data.GolemHolderMaterial;
import dev.xkmc.modulargolems.content.item.data.GolemIcon;
import dev.xkmc.modulargolems.content.item.data.GolemUpgrade;
import dev.xkmc.modulargolems.content.item.equipments.MetalGolemArmorItem;
import dev.xkmc.modulargolems.content.item.equipments.MetalGolemBeaconItem;
import dev.xkmc.modulargolems.content.item.equipments.MetalGolemWeaponItem;
import dev.xkmc.modulargolems.content.item.golem.GolemBEWLR;
import dev.xkmc.modulargolems.content.item.golem.GolemHolder;
import dev.xkmc.modulargolems.content.item.golem.GolemPart;
import dev.xkmc.modulargolems.content.item.upgrade.AddSlotItem;
import dev.xkmc.modulargolems.content.item.upgrade.SimpleUpgradeItem;
import dev.xkmc.modulargolems.content.item.wand.*;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.data.MGTagGen;
import dev.xkmc.modulargolems.init.material.GolemWeaponType;
import dev.xkmc.modulargolems.init.material.VanillaGolemWeaponMaterial;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

import static dev.xkmc.modulargolems.init.ModularGolems.REGISTRATE;

public class GolemItems {

	public static final SimpleEntry<CreativeModeTab> TAB;

	static {
		TAB = REGISTRATE.buildL2CreativeTab("golems", "Modular Golems", b -> b
				.icon(GolemItems.HOLDER_GOLEM::asStack));
	}

	public static final ItemEntry<Item> GOLEM_TEMPLATE, EMPTY_UPGRADE;

	public static final ItemEntry<GolemPart<MetalGolemEntity, MetalGolemPartType>> GOLEM_BODY, GOLEM_ARM, GOLEM_LEGS;
	public static final ItemEntry<GolemHolder<MetalGolemEntity, MetalGolemPartType>> HOLDER_GOLEM;

	public static final ItemEntry<GolemPart<HumanoidGolemEntity, HumaniodGolemPartType>> HUMANOID_BODY, HUMANOID_ARMS, HUMANOID_LEGS;
	public static final ItemEntry<GolemHolder<HumanoidGolemEntity, HumaniodGolemPartType>> HOLDER_HUMANOID;

	public static final ItemEntry<GolemPart<DogGolemEntity, DogGolemPartType>> DOG_BODY, DOG_LEGS;
	public static final ItemEntry<GolemHolder<DogGolemEntity, DogGolemPartType>> HOLDER_DOG;

	public static final ItemEntry<SimpleUpgradeItem> FIRE_IMMUNE, THUNDER_IMMUNE, RECYCLE, DIAMOND, NETHERITE, QUARTZ,
			GOLD, ENCHANTED_GOLD, FLOAT, SPONGE, SWIM, PLAYER_IMMUNE, ENDER_SIGHT, BELL, SPEED, SLOW, WEAK, WITHER,
			EMERALD, PICKUP, PICKUP_MENDING, PICKUP_NO_DESTROY, TALENTED, CAULDRON, MOUNT_UPGRADE, SIZE_UPGRADE;

	public static final ItemEntry<RetrievalWandItem> RETRIEVAL_WAND, OMNI_RETRIVAL;
	public static final ItemEntry<CommandWandItem> COMMAND_WAND, OMNI_COMMAND;
	public static final ItemEntry<DispenseWand> DISPENSE_WAND, OMNI_DISPENSE;
	public static final ItemEntry<RiderWandItem> RIDER_WAND, OMNI_RIDER;
	public static final ItemEntry<SquadWandItem> SQUAD_WAND, OMNI_SQUAD;

	public static final ItemEntry<MetalGolemArmorItem> GOLEMGUARD_HELMET, WINDSPIRIT_HELMET, BARBARICFLAMEVANGUARD_HELMET;
	public static final ItemEntry<MetalGolemArmorItem> GOLEMGUARD_CHESTPLATE, WINDSPIRIT_CHESTPLATE, BARBARICFLAMEVANGUARD_CHESTPLATE;
	public static final ItemEntry<MetalGolemArmorItem> GOLEMGUARD_SHINGUARD, WINDSPIRIT_SHINGUARD, BARBARICFLAMEVANGUARD_SHINGUARD;
	public static final ItemEntry<MetalGolemWeaponItem>[][] METALGOLEM_WEAPON;
	public static final ItemEntry<MetalGolemBeaconItem>[] METALGOLEM_BEACONS;
	public static final ItemEntry<ConfigCard>[] CARD;
	public static final ItemEntry<PathRecordCard> CARD_PATH;
	public static final ItemEntry<NameFilterCard> CARD_NAME;
	public static final ItemEntry<EntityTypeFilterCard> CARD_TYPE;
	public static final ItemEntry<UuidFilterCard> CARD_UUID;
	public static final ItemEntry<DefaultFilterCard> CARD_DEF;
	public static final ItemEntry<AddSlotItem> ADD_SLOT, INF_SLOT;

	private static final DCReg DC = DCReg.of(ModularGolems.REG);
	public static final DCVal<ResourceLocation> DC_PART_MAT = DC.loc("part_material");
	public static final DCVal<CustomData> ENTITY = DC.reg("golem_entity", CustomData.CODEC, CustomData.STREAM_CODEC, true);
	public static final DCVal<GolemHolderMaterial> HOLDER_MAT = DC.reg("golem_materials", GolemHolderMaterial.class, true);
	public static final DCVal<GolemUpgrade> UPGRADE = DC.reg("upgrades", GolemUpgrade.class, true);
	public static final DCVal<GolemConfigKey> CONFIG_KEY = DC.reg("config_key", GolemConfigKey.class, true);
	public static final DCVal<GolemIcon> DC_ICON = DC.reg("golem_as_icon", GolemIcon.class, true);
	public static final DCVal<Double> DC_DISP_HP = DC.doubleVal("display_health");
	public static final DCVal<UUID> DC_OWNER = DC.uuid("owner");
	public static final DCVal<Set<UUID>> DC_FILTER_UUID = DC.uuidSet("filter_uuid");
	public static final DCVal<List<String>> DC_FILTER_NAME = DC.list("filter_name", Codec.STRING, ByteBufCodecs.STRING_UTF8, true);
	public static final DCVal<List<EntityType<?>>> DC_FILTER_ENTITY = DC.list("filter_entity", Wrappers.cast(EntityType.class), true);
	public static final DCVal<PathRecordCard.Pos> DC_PATH = DC.reg("path", PathRecordCard.Pos.class, true);

	static {

		GOLEM_TEMPLATE = REGISTRATE.item("metal_golem_template", Item::new).defaultModel().defaultLang().register();

		{
			RETRIEVAL_WAND = REGISTRATE.item("retrieval_wand", p -> new RetrievalWandItem(p.stacksTo(1), null))
					.model((ctx, pvd) -> pvd.handheld(ctx)).defaultLang().tag(MGTagGen.GOLEM_INTERACT).register();
			COMMAND_WAND = REGISTRATE.item("command_wand", p -> new CommandWandItem(p.stacksTo(1), null))
					.model((ctx, pvd) -> pvd.handheld(ctx)).defaultLang().tag(MGTagGen.GOLEM_INTERACT).register();
			DISPENSE_WAND = REGISTRATE.item("summon_wand", p -> new DispenseWand(p.stacksTo(1), null))
					.model((ctx, pvd) -> pvd.handheld(ctx)).defaultLang().tag(MGTagGen.GOLEM_INTERACT).register();
			RIDER_WAND = REGISTRATE.item("rider_wand", p -> new RiderWandItem(p.stacksTo(1), null))
					.model((ctx, pvd) -> pvd.handheld(ctx)).defaultLang().tag(MGTagGen.GOLEM_INTERACT).register();
			SQUAD_WAND = REGISTRATE.item("squad_wand", p -> new SquadWandItem(p.stacksTo(1), null))
					.model((ctx, pvd) -> pvd.handheld(ctx)).defaultLang().tag(MGTagGen.GOLEM_INTERACT).register();


			OMNI_COMMAND = REGISTRATE.item("omnipotent_wand_command", p -> new CommandWandItem(p.stacksTo(1), COMMAND_WAND))
					.model((ctx, pvd) -> pvd.handheld(ctx, pvd.modLoc("item/omnipotent_wand")))
					.lang("Omnipotent Wand: Command").tag(L2ISTagGen.SELECTABLE, MGTagGen.GOLEM_INTERACT)
					.register();
			OMNI_RETRIVAL = REGISTRATE.item("omnipotent_wand_retrieval", p -> new RetrievalWandItem(p.stacksTo(1), RETRIEVAL_WAND))
					.model((ctx, pvd) -> pvd.handheld(ctx, pvd.modLoc("item/omnipotent_wand")))
					.lang("Omnipotent Wand: Retrieval").tag(L2ISTagGen.SELECTABLE, MGTagGen.GOLEM_INTERACT)
					.removeTab(TAB.key()).register();
			OMNI_DISPENSE = REGISTRATE.item("omnipotent_wand_summon", p -> new DispenseWand(p.stacksTo(1), DISPENSE_WAND))
					.model((ctx, pvd) -> pvd.handheld(ctx, pvd.modLoc("item/omnipotent_wand")))
					.lang("Omnipotent Wand: Summon").tag(L2ISTagGen.SELECTABLE, MGTagGen.GOLEM_INTERACT)
					.removeTab(TAB.key()).register();
			OMNI_RIDER = REGISTRATE.item("omnipotent_wand_rider", p -> new RiderWandItem(p.stacksTo(1), RIDER_WAND))
					.model((ctx, pvd) -> pvd.handheld(ctx, pvd.modLoc("item/omnipotent_wand")))
					.lang("Omnipotent Wand: Rider").tag(L2ISTagGen.SELECTABLE, MGTagGen.GOLEM_INTERACT)
					.removeTab(TAB.key()).register();
			OMNI_SQUAD = REGISTRATE.item("omnipotent_wand_squad", p -> new SquadWandItem(p.stacksTo(1), SQUAD_WAND))
					.model((ctx, pvd) -> pvd.handheld(ctx, pvd.modLoc("item/omnipotent_wand")))
					.lang("Omnipotent Wand: Squad").tag(L2ISTagGen.SELECTABLE, MGTagGen.GOLEM_INTERACT)
					.removeTab(TAB.key()).register();

		}

		// golemguard armor
		{
			GOLEMGUARD_HELMET = REGISTRATE.item("roman_guard_helmet", p -> new MetalGolemArmorItem(p.stacksTo(1),
							ArmorItem.Type.HELMET, 8, 4, GolemModelPaths.HELMETS))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/equipments/" + ctx.getName())))
					.tag(ItemTags.ARMOR_ENCHANTABLE, ItemTags.HEAD_ARMOR_ENCHANTABLE)
					.defaultLang().register();
			GOLEMGUARD_CHESTPLATE = REGISTRATE.item("roman_guard_chestplate", p -> new MetalGolemArmorItem(p.stacksTo(1),
							ArmorItem.Type.CHESTPLATE, 10, 4, GolemModelPaths.CHESTPLATES))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/equipments/" + ctx.getName())))
					.tag(ItemTags.ARMOR_ENCHANTABLE, ItemTags.CHEST_ARMOR_ENCHANTABLE)
					.defaultLang().register();
			GOLEMGUARD_SHINGUARD = REGISTRATE.item("roman_guard_shinguard", p -> new MetalGolemArmorItem(p.stacksTo(1),
							ArmorItem.Type.LEGGINGS, 6, 4, GolemModelPaths.LEGGINGS))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/equipments/" + ctx.getName())))
					.tag(ItemTags.ARMOR_ENCHANTABLE, ItemTags.LEG_ARMOR_ENCHANTABLE)
					.defaultLang().register();

			WINDSPIRIT_HELMET = REGISTRATE.item("wind_spirit_helmet", p -> new MetalGolemArmorItem(p.stacksTo(1),
							ArmorItem.Type.HELMET, 11, 6, GolemModelPaths.HELMETS))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/equipments/" + ctx.getName())))
					.tag(ItemTags.ARMOR_ENCHANTABLE, ItemTags.HEAD_ARMOR_ENCHANTABLE)
					.defaultLang().register();
			WINDSPIRIT_CHESTPLATE = REGISTRATE.item("wind_spirit_chestplate", p -> new MetalGolemArmorItem(p.stacksTo(1),
							ArmorItem.Type.CHESTPLATE, 14, 6, GolemModelPaths.CHESTPLATES))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/equipments/" + ctx.getName())))
					.tag(ItemTags.ARMOR_ENCHANTABLE, ItemTags.CHEST_ARMOR_ENCHANTABLE)
					.defaultLang().register();
			WINDSPIRIT_SHINGUARD = REGISTRATE.item("wind_spirit_shinguard", p -> new MetalGolemArmorItem(p.stacksTo(1),
							ArmorItem.Type.LEGGINGS, 8, 6, GolemModelPaths.LEGGINGS))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/equipments/" + ctx.getName())))
					.tag(ItemTags.ARMOR_ENCHANTABLE, ItemTags.LEG_ARMOR_ENCHANTABLE)
					.defaultLang().register();

			BARBARICFLAMEVANGUARD_HELMET = REGISTRATE.item("barbaric_vanguard_helmet", p -> new MetalGolemArmorItem(p.stacksTo(1).fireResistant(),
							ArmorItem.Type.HELMET, 14, 8, GolemModelPaths.HELMETS))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/equipments/" + ctx.getName())))
					.tag(ItemTags.ARMOR_ENCHANTABLE, ItemTags.HEAD_ARMOR_ENCHANTABLE)
					.defaultLang().register();
			BARBARICFLAMEVANGUARD_CHESTPLATE = REGISTRATE.item("barbaric_vanguard_chestplate", p -> new MetalGolemArmorItem(p.stacksTo(1).fireResistant(),
							ArmorItem.Type.CHESTPLATE, 18, 8, GolemModelPaths.CHESTPLATES))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/equipments/" + ctx.getName())))
					.tag(ItemTags.ARMOR_ENCHANTABLE, ItemTags.CHEST_ARMOR_ENCHANTABLE)
					.defaultLang().register();
			BARBARICFLAMEVANGUARD_SHINGUARD = REGISTRATE.item("barbaric_vanguard_shinguard", p -> new MetalGolemArmorItem(p.stacksTo(1).fireResistant(),
							ArmorItem.Type.LEGGINGS, 10, 8, GolemModelPaths.LEGGINGS))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/equipments/" + ctx.getName())))
					.tag(ItemTags.ARMOR_ENCHANTABLE, ItemTags.LEG_ARMOR_ENCHANTABLE)
					.defaultLang().register();
		}

		//metalgolem weapon
		{
			METALGOLEM_WEAPON = GolemWeaponType.build(VanillaGolemWeaponMaterial.values());
		}

		//metalgolem beacon
		{
			METALGOLEM_BEACONS = new ItemEntry[5];
			for (int i = 0; i < 5; i++) {
				int lv = i + 1;
				METALGOLEM_BEACONS[i] = REGISTRATE.item("golem_beacon_level_" + lv,
								p -> new MetalGolemBeaconItem(p.stacksTo(1), lv))
						.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/equipments/" + ctx.getName())))
						.removeTab(TAB.key()).register();
			}
		}

		// cards
		{
			CARD = new ItemEntry[16];
			for (int i = 0; i < 16; i++) {
				DyeColor color = DyeColor.byId(i);
				String name = color.getName();
				CARD[i] = REGISTRATE.item(name + "_config_card", p -> new ConfigCard(p.stacksTo(1), color))
						.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/card/" + name)))
						.tag(MGTagGen.CONFIG_CARD).defaultLang().register();
			}

			CARD_NAME = REGISTRATE.item("target_filter_name", p -> new NameFilterCard(p.stacksTo(1)))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/card/name")))
					.tag(MGTagGen.GOLEM_INTERACT)
					.lang("Target Filter: Datapack").register();
			CARD_TYPE = REGISTRATE.item("target_filter_type", p -> new EntityTypeFilterCard(p.stacksTo(1)))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/card/type")))
					.tag(MGTagGen.GOLEM_INTERACT)
					.lang("Target Filter: Entity Type").register();
			CARD_UUID = REGISTRATE.item("target_filter_uuid", p -> new UuidFilterCard(p.stacksTo(1)))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/card/uuid")))
					.tag(MGTagGen.GOLEM_INTERACT)
					.lang("Target Filter: Entity UUID").register();
			CARD_DEF = REGISTRATE.item("target_filter_default", p -> new DefaultFilterCard(p.stacksTo(1)))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/card/default")))
					.tag(MGTagGen.GOLEM_INTERACT)
					.lang("Target Filter: Default Target").register();
			CARD_PATH = REGISTRATE.item("patrol_path_recorder", p -> new PathRecordCard(p.stacksTo(1)))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/card/path")))
					.tag(MGTagGen.GOLEM_INTERACT, MGTagGen.CURIO_PATH)
					.lang("Patrol Path Recorder").register();
		}

		// upgrades
		{
			EMPTY_UPGRADE = REGISTRATE.item("empty_upgrade", Item::new).defaultModel().defaultLang().register();
			ADD_SLOT = REGISTRATE.item("add_slot", p -> new AddSlotItem(p, 1)).defaultModel().lang("Add Slot Upgrade").register();
			INF_SLOT = REGISTRATE.item("creative_add_slot", p -> new AddSlotItem(p, 100)).defaultModel().lang("Creative Add Slot Upgrade").register();
			FIRE_IMMUNE = regUpgrade("fire_immune", () -> GolemModifiers.FIRE_IMMUNE).lang("Fire Immune Upgrade").register();
			THUNDER_IMMUNE = regUpgrade("thunder_immune", () -> GolemModifiers.THUNDER_IMMUNE).lang("Thunder Immune Upgrade").register();
			RECYCLE = regUpgrade("recycle", () -> GolemModifiers.RECYCLE).lang("Recycle Ugpgrade").register();
			DIAMOND = regUpgrade("diamond", () -> GolemModifiers.ARMOR).lang("Diamond Upgrade").register();
			NETHERITE = regUpgrade("netherite", () -> GolemModifiers.TOUGH).lang("Netherite Upgrade").register();
			QUARTZ = regUpgrade("quartz", () -> GolemModifiers.DAMAGE).lang("Quartz Upgrade").register();
			GOLD = regUpgrade("gold", () -> GolemModifiers.REGEN).lang("Golden Apple Upgrade").register();
			ENCHANTED_GOLD = regUpgrade("enchanted_gold", () -> GolemModifiers.REGEN, 2, true).lang("Enchanted Golden Apple Upgrade").register();
			FLOAT = regUpgrade("float", () -> GolemModifiers.FLOAT).lang("Float Upgrade").register();
			SPONGE = regUpgrade("sponge", () -> GolemModifiers.EXPLOSION_RES).lang("Sponge Upgrade").register();
			SWIM = regUpgrade("swim", () -> GolemModifiers.SWIM).lang("Swim Upgrade").register();
			PLAYER_IMMUNE = regUpgrade("player", () -> GolemModifiers.PLAYER_IMMUNE).lang("Player Immune Upgrade").register();
			ENDER_SIGHT = regUpgrade("ender_sight", () -> GolemModifiers.ENDER_SIGHT).lang("Ender Sight Upgrade").register();
			BELL = regUpgrade("bell", () -> GolemModifiers.BELL).lang("Bell Upgrade").register();
			SPEED = regUpgrade("speed", () -> GolemModifiers.SPEED).lang("Speed Upgrade").register();
			SLOW = regUpgrade("slow", () -> GolemModifiers.SLOW).lang("Potion Upgrade: Slowness").register();
			WEAK = regUpgrade("weak", () -> GolemModifiers.WEAK).lang("Potion Upgrade: Weakness").register();
			WITHER = regUpgrade("wither", () -> GolemModifiers.WITHER).lang("Potion Upgrade: Wither").register();
			EMERALD = regUpgrade("emerald", () -> GolemModifiers.EMERALD).lang("Emerald Upgrade").register();
			PICKUP = regUpgrade("pickup", () -> GolemModifiers.PICKUP).lang("Pickup Upgrade").register();
			PICKUP_MENDING = regUpgrade("pickup_mending", () -> GolemModifiers.PICKUP_MENDING).lang("Pickup Augment: Mending").register();
			PICKUP_NO_DESTROY = regUpgrade("pickup_no_destroy", () -> GolemModifiers.PICKUP_NODESTROY).lang("Pickup Augment: No Destroy").register();
			TALENTED = regUpgrade("talented", () -> GolemModifiers.TALENTED).lang("Meta Upgrade: Talented").register();
			CAULDRON = regUpgrade("cauldron", () -> GolemModifiers.CAULDRON).lang("Meta Upgrade: Cauldron").register();
			MOUNT_UPGRADE = regUpgrade("mount_upgrade", () -> GolemModifiers.MOUNT_UPGRADE).lang("Mount Upgrade").register();
			SIZE_UPGRADE = regUpgrade("size_upgrade", () -> GolemModifiers.SIZE_UPGRADE).lang("Size Upgrade").register();

		}
		CompatManager.register();

		// holders
		{
			HOLDER_GOLEM = REGISTRATE.item("metal_golem_holder", p ->
							new GolemHolder<>(p.fireResistant(), GolemTypes.TYPE_GOLEM))
					.model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("builtin/entity"))
							.texture("particle", "minecraft:block/clay"))
					.transform(e -> e.tab(TAB.key(), x -> e.getEntry().fillItemCategory(x)))
					.clientExtension(() -> () -> GolemBEWLR.EXTENSIONS)
					.tag(MGTagGen.GOLEM_HOLDERS).defaultLang().register();

			HOLDER_HUMANOID = REGISTRATE.item("humanoid_golem_holder", p ->
							new GolemHolder<>(p.fireResistant(), GolemTypes.TYPE_HUMANOID))
					.model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("builtin/entity"))
							.texture("particle", "minecraft:block/clay"))
					.transform(e -> e.tab(TAB.key(), x -> e.getEntry().fillItemCategory(x)))
					.clientExtension(() -> () -> GolemBEWLR.EXTENSIONS)
					.tag(MGTagGen.GOLEM_HOLDERS).defaultLang().register();

			HOLDER_DOG = REGISTRATE.item("dog_golem_holder", p ->
							new GolemHolder<>(p.fireResistant(), GolemTypes.TYPE_DOG))
					.model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("builtin/entity"))
							.texture("particle", "minecraft:block/clay"))
					.transform(e -> e.tab(TAB.key(), x -> e.getEntry().fillItemCategory(x)))
					.clientExtension(() -> () -> GolemBEWLR.EXTENSIONS)
					.tag(MGTagGen.GOLEM_HOLDERS).defaultLang().register();
		}

		// metal golem
		{
			GOLEM_BODY = REGISTRATE.item("metal_golem_body", p ->
							new GolemPart<>(p.fireResistant(), GolemTypes.TYPE_GOLEM, MetalGolemPartType.BODY, 9))
					.model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("builtin/entity"))
							.texture("particle", "minecraft:block/clay"))
					.transform(e -> e.tab(TAB.key(), x -> e.getEntry().fillItemCategory(x)))
					.clientExtension(() -> () -> GolemBEWLR.EXTENSIONS)
					.tag(MGTagGen.GOLEM_PARTS).defaultLang().register();
			GOLEM_ARM = REGISTRATE.item("metal_golem_arm", p ->
							new GolemPart<>(p.fireResistant(), GolemTypes.TYPE_GOLEM, MetalGolemPartType.LEFT, 9))
					.model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("builtin/entity"))
							.texture("particle", "minecraft:block/clay"))
					.transform(e -> e.tab(TAB.key(), x -> e.getEntry().fillItemCategory(x)))
					.clientExtension(() -> () -> GolemBEWLR.EXTENSIONS)
					.tag(MGTagGen.GOLEM_PARTS).defaultLang().register();
			GOLEM_LEGS = REGISTRATE.item("metal_golem_legs", p ->
							new GolemPart<>(p.fireResistant(), GolemTypes.TYPE_GOLEM, MetalGolemPartType.LEG, 9))
					.model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("builtin/entity"))
							.texture("particle", "minecraft:block/clay"))
					.transform(e -> e.tab(TAB.key(), x -> e.getEntry().fillItemCategory(x)))
					.clientExtension(() -> () -> GolemBEWLR.EXTENSIONS)
					.tag(MGTagGen.GOLEM_PARTS).defaultLang().register();
		}

		// humanoid golem
		{
			HUMANOID_BODY = REGISTRATE.item("humanoid_golem_body", p ->
							new GolemPart<>(p.fireResistant(), GolemTypes.TYPE_HUMANOID, HumaniodGolemPartType.BODY, 6))
					.model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("builtin/entity"))
							.texture("particle", "minecraft:block/clay"))
					.transform(e -> e.tab(TAB.key(), x -> e.getEntry().fillItemCategory(x)))
					.clientExtension(() -> () -> GolemBEWLR.EXTENSIONS)
					.tag(MGTagGen.GOLEM_PARTS).defaultLang().register();
			HUMANOID_ARMS = REGISTRATE.item("humanoid_golem_arms", p ->
							new GolemPart<>(p.fireResistant(), GolemTypes.TYPE_HUMANOID, HumaniodGolemPartType.ARMS, 6))
					.model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("builtin/entity"))
							.texture("particle", "minecraft:block/clay"))
					.transform(e -> e.tab(TAB.key(), x -> e.getEntry().fillItemCategory(x)))
					.clientExtension(() -> () -> GolemBEWLR.EXTENSIONS)
					.tag(MGTagGen.GOLEM_PARTS).defaultLang().register();
			HUMANOID_LEGS = REGISTRATE.item("humanoid_golem_legs", p ->
							new GolemPart<>(p.fireResistant(), GolemTypes.TYPE_HUMANOID, HumaniodGolemPartType.LEGS, 6))
					.model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("builtin/entity"))
							.texture("particle", "minecraft:block/clay"))
					.transform(e -> e.tab(TAB.key(), x -> e.getEntry().fillItemCategory(x)))
					.clientExtension(() -> () -> GolemBEWLR.EXTENSIONS)
					.tag(MGTagGen.GOLEM_PARTS).defaultLang().register();
		}

		// dog golem
		{
			DOG_BODY = REGISTRATE.item("dog_golem_body", p ->
							new GolemPart<>(p.fireResistant(), GolemTypes.TYPE_DOG, DogGolemPartType.BODY, 6))
					.model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("builtin/entity"))
							.texture("particle", "minecraft:block/clay"))
					.transform(e -> e.tab(TAB.key(), x -> e.getEntry().fillItemCategory(x)))
					.clientExtension(() -> () -> GolemBEWLR.EXTENSIONS)
					.tag(MGTagGen.GOLEM_PARTS).defaultLang().register();

			DOG_LEGS = REGISTRATE.item("dog_golem_legs", p ->
							new GolemPart<>(p.fireResistant(), GolemTypes.TYPE_DOG, DogGolemPartType.LEGS, 3))
					.model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("builtin/entity"))
							.texture("particle", "minecraft:block/clay"))
					.transform(e -> e.tab(TAB.key(), x -> e.getEntry().fillItemCategory(x)))
					.clientExtension(() -> () -> GolemBEWLR.EXTENSIONS)
					.tag(MGTagGen.GOLEM_PARTS).defaultLang().register();
		}

		CompatManager.lateRegister();

	}

	public static ItemBuilder<SimpleUpgradeItem, L2Registrate> regModUpgrade(String id, Supplier<Val<? extends GolemModifier>> mod, int lv, boolean foil, String modid) {
		return regUpgradeImpl(id, mod, lv, foil, modid).asOptional().tag(MGTagGen.GOLEM_UPGRADES);
	}

	public static ItemBuilder<SimpleUpgradeItem, L2Registrate> regModUpgrade(String id, Supplier<Val<? extends GolemModifier>> mod, String modid) {
		return regModUpgrade(id, mod, 1, false, modid);
	}

	private static ItemBuilder<SimpleUpgradeItem, L2Registrate> regUpgrade(String id, Supplier<Val<? extends GolemModifier>> mod) {
		return regUpgrade(id, mod, 1, false);
	}

	private static ItemBuilder<SimpleUpgradeItem, L2Registrate> regUpgrade(String id, Supplier<Val<? extends GolemModifier>> mod, int level, boolean foil) {
		return regUpgradeImpl(id, mod, level, foil, ModularGolems.MODID).tag(MGTagGen.GOLEM_UPGRADES);
	}

	private static ItemBuilder<SimpleUpgradeItem, L2Registrate> regUpgradeImpl(String id, Supplier<Val<? extends GolemModifier>> mod, int level, boolean foil, String modid) {
		return REGISTRATE.item(id, p -> new SimpleUpgradeItem(p, mod.get()::get, level, foil))
				.model((ctx, pvd) -> pvd.generated(ctx, ResourceLocation.fromNamespaceAndPath(modid, "item/upgrades/" + id))
						.override().predicate(ResourceLocation.fromNamespaceAndPath(ModularGolems.MODID, "blue_arrow"), 0.5f)
						.model(pvd.getBuilder(pvd.name(ctx) + "_purple")
								.parent(new ModelFile.UncheckedModelFile("item/generated"))
								.texture("layer0", ResourceLocation.fromNamespaceAndPath(modid, "item/upgrades/" + id))
								.texture("layer1", ResourceLocation.fromNamespaceAndPath(ModularGolems.MODID, "item/purple_arrow")))
						.end().override().predicate(ResourceLocation.fromNamespaceAndPath(ModularGolems.MODID, "blue_arrow"), 1)
						.model(pvd.getBuilder(pvd.name(ctx) + "_blue")
								.parent(new ModelFile.UncheckedModelFile("item/generated"))
								.texture("layer0", ResourceLocation.fromNamespaceAndPath(modid, "item/upgrades/" + id))
								.texture("layer1", ResourceLocation.fromNamespaceAndPath(ModularGolems.MODID, "item/blue_arrow")))
						.end());
	}

	public static void register() {
	}

}
