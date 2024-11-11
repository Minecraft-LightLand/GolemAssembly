package dev.xkmc.modulargolems.init.data;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.tterrag.registrate.providers.RegistrateItemTagsProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MGTagGen {

	public static final TagKey<Item> SCULK_MATS = createItemTag("sculk_materials");
	public static final TagKey<Item> GOLEM_PARTS = createItemTag("parts");
	public static final TagKey<Item> GOLEM_HOLDERS = createItemTag("holders");
	public static final TagKey<Item> GOLEM_UPGRADES = createItemTag("upgrades");
	public static final TagKey<Item> BLUE_UPGRADES = createItemTag("blue_upgrades");
	public static final TagKey<Item> POTION_UPGRADES = createItemTag("potion_upgrades");
	public static final TagKey<Item> CONFIG_CARD = createItemTag("config_card");
	public static final TagKey<Item> SPECIAL_CRAFT = createItemTag("special_crafting_material");
	public static final TagKey<Item> GOLEM_INTERACT = createItemTag("golem_interact");
	public static final TagKey<Item> CURIO_SKIN = ItemTags.create(ResourceLocation.fromNamespaceAndPath("curios", "golem_skin"));
	public static final TagKey<Item> CURIO_PATH = ItemTags.create(ResourceLocation.fromNamespaceAndPath("curios", "golem_route"));
	public static final TagKey<Item> PLAYER_SKIN = createItemTag("player_skin");
	public static final TagKey<EntityType<?>> GOLEM_FRIENDLY = createEntityTag("friendly");
	public static final TagKey<Block> POTENTIAL_DST = createBlockTag("potential_destination");

	public static final List<Consumer<RegistrateItemTagsProvider>> OPTIONAL_ITEM = new ArrayList<>();
	public static final List<Consumer<RegistrateTagsProvider<Block>>> OPTIONAL_BLOCK = new ArrayList<>();
	public static final List<Consumer<RegistrateTagsProvider<MobEffect>>> OPTIONAL_EFF = new ArrayList<>();

	public static void onBlockTagGen(RegistrateTagsProvider.IntrinsicImpl<Block> pvd) {
		pvd.addTag(POTENTIAL_DST)
				.addTag(BlockTags.SHULKER_BOXES)
				.addTag(Tags.Blocks.CHESTS)
				.addTag(Tags.Blocks.BARRELS);
		OPTIONAL_BLOCK.forEach(e -> e.accept(pvd));
	}

	public static void onEffTagGen(RegistrateTagsProvider.IntrinsicImpl<MobEffect> pvd) {
		OPTIONAL_EFF.forEach(e -> e.accept(pvd));
	}

	public static void onItemTagGen(RegistrateItemTagsProvider pvd) {
		pvd.addTag(SCULK_MATS).add(Items.ECHO_SHARD);
		pvd.addTag(SPECIAL_CRAFT);
		pvd.addTag(GOLEM_INTERACT).addTag(CONFIG_CARD).addTag(GOLEM_HOLDERS);
		OPTIONAL_ITEM.forEach(e -> e.accept(pvd));
		pvd.addTag(BLUE_UPGRADES).add(
				GolemItems.BELL.get(),
				GolemItems.ENDER_SIGHT.get(),
				GolemItems.FLOAT.get(),
				GolemItems.PICKUP.get(),
				GolemItems.PICKUP_MENDING.get(),
				GolemItems.PICKUP_NO_DESTROY.get(),
				GolemItems.RECYCLE.get(),
				GolemItems.SWIM.get(),
				GolemItems.FIRE_IMMUNE.get(),
				GolemItems.THUNDER_IMMUNE.get(),
				GolemItems.PLAYER_IMMUNE.get(),
				GolemItems.MOUNT_UPGRADE.get(),
				GolemItems.SIZE_UPGRADE.get()
		);
		pvd.addTag(POTION_UPGRADES).add(
				GolemItems.WEAK.get(),
				GolemItems.SLOW.get(),
				GolemItems.WITHER.get()
		);
		pvd.addTag(PLAYER_SKIN).add(Items.ZOMBIE_HEAD, Items.SKELETON_SKULL, Items.WITHER_SKELETON_SKULL);
		var skin = pvd.addTag(CURIO_SKIN);
		skin.addTag(PLAYER_SKIN).add(Items.PLAYER_HEAD, Items.PIGLIN_HEAD);

		if (ModList.get().isLoaded(TouhouLittleMaid.MOD_ID)) {
			skin.addOptional(ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "garage_kit"));
		}
	}

	public static void onEntityTagGen(RegistrateTagsProvider.IntrinsicImpl<EntityType<?>> pvd) {
		pvd.addTag(GOLEM_FRIENDLY).add(EntityType.PLAYER, EntityType.SNOW_GOLEM);
		pvd.addTag(EntityTypeTags.FALL_DAMAGE_IMMUNE).add(GolemTypes.ENTITY_GOLEM.get(),
				GolemTypes.ENTITY_HUMANOID.get(), GolemTypes.ENTITY_DOG.get());
	}

	private static TagKey<Item> createItemTag(String id) {
		return ItemTags.create(ModularGolems.loc(id));
	}

	private static TagKey<EntityType<?>> createEntityTag(String id) {
		return TagKey.create(Registries.ENTITY_TYPE, ModularGolems.loc(id));
	}

	private static TagKey<Block> createBlockTag(String id) {
		return TagKey.create(Registries.BLOCK, ModularGolems.loc(id));
	}

}
