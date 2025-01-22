package dev.xkmc.modulargolems.init.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2core.serial.ingredients.EnchantmentIngredient;
import dev.xkmc.l2core.serial.recipe.DataRecipeWrapper;
import dev.xkmc.modulargolems.compat.materials.common.CompatManager;
import dev.xkmc.modulargolems.content.core.IGolemPart;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.item.card.NameFilterCard;
import dev.xkmc.modulargolems.content.item.golem.GolemHolder;
import dev.xkmc.modulargolems.content.recipe.GolemAssembleBuilder;
import dev.xkmc.modulargolems.content.recipe.GolemSmithBuilder;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.material.GolemWeaponType;
import dev.xkmc.modulargolems.init.material.VanillaGolemWeaponMaterial;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.function.BiFunction;

public class RecipeGen {

	public static void genRecipe(RegistrateRecipeProvider pvd) {

		// golem base
		{

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GolemItems.GOLEM_TEMPLATE.get())::unlockedBy,
					Items.CLAY).pattern("CBC").pattern("BAB").pattern("CBC")
					.define('A', Items.COPPER_INGOT).define('B', Items.STICK)
					.define('C', Items.CLAY_BALL).save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GolemItems.RETRIEVAL_WAND.get())::unlockedBy, GolemItems.GOLEM_TEMPLATE.get())
					.pattern(" ET").pattern(" SE").pattern("S  ")
					.define('E', Items.ENDER_PEARL)
					.define('S', Items.STICK)
					.define('T', GolemItems.GOLEM_TEMPLATE.get())
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GolemItems.DISPENSE_WAND.get())::unlockedBy, GolemItems.GOLEM_TEMPLATE.get())
					.pattern(" ET").pattern(" SE").pattern("S  ")
					.define('E', Items.DISPENSER)
					.define('S', Items.STICK)
					.define('T', GolemItems.GOLEM_TEMPLATE.get())
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GolemItems.COMMAND_WAND.get())::unlockedBy, GolemItems.GOLEM_TEMPLATE.get())
					.pattern(" ET").pattern(" SE").pattern("S  ")
					.define('E', Items.GOLD_INGOT)
					.define('S', Items.STICK)
					.define('T', GolemItems.GOLEM_TEMPLATE.get())
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GolemItems.RIDER_WAND.get())::unlockedBy, GolemItems.GOLEM_TEMPLATE.get())
					.pattern(" ET").pattern(" SE").pattern("S  ")
					.define('E', Items.WHITE_BANNER)
					.define('S', Items.STICK)
					.define('T', GolemItems.GOLEM_TEMPLATE.get())
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GolemItems.SQUAD_WAND.get())::unlockedBy, GolemItems.GOLEM_TEMPLATE.get())
					.pattern("  T").pattern(" E ").pattern("S  ")
					.define('E', Items.WHITE_BANNER)
					.define('S', Items.STICK)
					.define('T', GolemItems.GOLEM_TEMPLATE.get())
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GolemItems.OMNI_COMMAND.get())::unlockedBy, GolemItems.GOLEM_TEMPLATE.get())
					.pattern(" 1T").pattern("2S3").pattern("S4 ")
					.define('1', GolemItems.COMMAND_WAND.get())
					.define('2', GolemItems.DISPENSE_WAND.get())
					.define('3', GolemItems.RETRIEVAL_WAND.get())
					.define('4', GolemItems.RIDER_WAND.get())
					.define('S', Items.GOLD_INGOT)
					.define('T', Items.REDSTONE_BLOCK)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GolemItems.EMPTY_UPGRADE.get(), 4)::unlockedBy,
					Items.AMETHYST_SHARD).pattern("CBC").pattern("BAB").pattern("CBC")
					.define('A', Items.AMETHYST_SHARD).define('B', Items.IRON_INGOT)
					.define('C', Items.CLAY_BALL).save(pvd);

			pvd.stonecutting(DataIngredient.items(GolemItems.GOLEM_TEMPLATE.get()), RecipeCategory.MISC, GolemItems.GOLEM_BODY);
			pvd.stonecutting(DataIngredient.items(GolemItems.GOLEM_TEMPLATE.get()), RecipeCategory.MISC, GolemItems.GOLEM_ARM);
			pvd.stonecutting(DataIngredient.items(GolemItems.GOLEM_TEMPLATE.get()), RecipeCategory.MISC, GolemItems.GOLEM_LEGS);
			pvd.stonecutting(DataIngredient.items(GolemItems.GOLEM_TEMPLATE.get()), RecipeCategory.MISC, GolemItems.HUMANOID_BODY);
			pvd.stonecutting(DataIngredient.items(GolemItems.GOLEM_TEMPLATE.get()), RecipeCategory.MISC, GolemItems.HUMANOID_ARMS);
			pvd.stonecutting(DataIngredient.items(GolemItems.GOLEM_TEMPLATE.get()), RecipeCategory.MISC, GolemItems.HUMANOID_LEGS);
			pvd.stonecutting(DataIngredient.items(GolemItems.GOLEM_TEMPLATE.get()), RecipeCategory.MISC, GolemItems.DOG_BODY);
			pvd.stonecutting(DataIngredient.items(GolemItems.GOLEM_TEMPLATE.get()), RecipeCategory.MISC, GolemItems.DOG_LEGS);

			unlock(pvd, new GolemAssembleBuilder(GolemItems.HOLDER_GOLEM.get(), 1)::unlockedBy,
					GolemItems.GOLEM_BODY.get())
					.pattern("ABA").pattern(" L ")
					.define('A', GolemItems.GOLEM_ARM.get())
					.define('B', GolemItems.GOLEM_BODY.get())
					.define('L', GolemItems.GOLEM_LEGS.get())
					.save(pvd);

			unlock(pvd, new GolemAssembleBuilder(GolemItems.HOLDER_HUMANOID.get(), 1)::unlockedBy,
					GolemItems.HUMANOID_BODY.get())
					.pattern("A").pattern("B").pattern("C")
					.define('A', GolemItems.HUMANOID_BODY.get())
					.define('B', GolemItems.HUMANOID_ARMS.get())
					.define('C', GolemItems.HUMANOID_LEGS.get())
					.save(pvd);

			unlock(pvd, new GolemAssembleBuilder(GolemItems.HOLDER_DOG.get(), 1)::unlockedBy,
					GolemItems.HUMANOID_BODY.get())
					.pattern("A").pattern("B")
					.define('A', GolemItems.DOG_BODY.get())
					.define('B', GolemItems.DOG_LEGS.get())
					.save(pvd);

			expand(pvd, GolemItems.HOLDER_GOLEM, GolemItems.ADD_DIAMOND);
			expand(pvd, GolemItems.HOLDER_GOLEM, GolemItems.ADD_NETHERITE);
			expand(pvd, GolemItems.HOLDER_HUMANOID, GolemItems.ADD_DIAMOND);
			expand(pvd, GolemItems.HOLDER_HUMANOID, GolemItems.ADD_NETHERITE);
			expand(pvd, GolemItems.HOLDER_DOG, GolemItems.ADD_DIAMOND);
			expand(pvd, GolemItems.HOLDER_DOG, GolemItems.ADD_NETHERITE);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GolemItems.ADD_DIAMOND, 1)::unlockedBy, Items.DIAMOND)
					.pattern("AAA").pattern("CBC").pattern("ACA")
					.define('A', GolemItems.EMPTY_UPGRADE)
					.define('B', Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
					.define('C', Items.DIAMOND)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GolemItems.ADD_NETHERITE, 1)::unlockedBy, Items.NETHERITE_INGOT)
					.pattern("ADA").pattern("CBC").pattern("ACA")
					.define('A', GolemItems.EMPTY_UPGRADE)
					.define('B', Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
					.define('C', Items.NETHERITE_INGOT)
					.define('D', Items.NETHER_STAR)
					.save(pvd);

		}

		// card
		{

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GolemItems.CARD[DyeColor.WHITE.getId()].get())::unlockedBy, GolemItems.GOLEM_TEMPLATE.get())
					.pattern(" P ").pattern("PTP").pattern(" P ")
					.define('P', Items.PAPER)
					.define('T', GolemItems.GOLEM_TEMPLATE.get())
					.save(pvd, ModularGolems.loc("craft_config_card"));

			for (int i = 0; i < 16; i++) {
				Item dye = BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(DyeColor.byId(i).getName() + "_dye"));
				assert dye != null;
				unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GolemItems.CARD[i].get())::unlockedBy, GolemItems.GOLEM_TEMPLATE.get())
						.requires(MGTagGen.CONFIG_CARD).requires(dye).save(pvd);

			}

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GolemItems.CARD_PATH.get())::unlockedBy, GolemItems.GOLEM_TEMPLATE.get())
					.requires(GolemItems.GOLEM_TEMPLATE.get())
					.requires(Items.MAP).requires(Items.INK_SAC)
					.save(pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GolemItems.CARD_NAME.get())::unlockedBy, GolemItems.GOLEM_TEMPLATE.get())
					.requires(GolemItems.GOLEM_TEMPLATE.get())
					.requires(Items.BOOK)
					.requires(Items.INK_SAC)
					.save(pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GolemItems.CARD_NAME.get())::unlockedBy, GolemItems.GOLEM_TEMPLATE.get())
					.requires(GolemItems.GOLEM_TEMPLATE.get())
					.requires(Items.BOOK)
					.requires(Items.INK_SAC)
					.requires(Items.PAPER)
					.save(new DataRecipeWrapper(pvd, NameFilterCard.getFriendly()), ModularGolems.loc("target_filter_friendly"));

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GolemItems.CARD_DEF.get())::unlockedBy, GolemItems.GOLEM_TEMPLATE.get())
					.requires(GolemItems.GOLEM_TEMPLATE.get())
					.requires(Items.PAPER)
					.requires(Items.IRON_INGOT)
					.save(pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GolemItems.CARD_TYPE.get())::unlockedBy, GolemItems.GOLEM_TEMPLATE.get())
					.requires(GolemItems.GOLEM_TEMPLATE.get())
					.requires(Items.PAPER)
					.requires(Items.CLAY_BALL)
					.save(pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GolemItems.CARD_UUID.get())::unlockedBy, GolemItems.GOLEM_TEMPLATE.get())
					.requires(GolemItems.GOLEM_TEMPLATE.get())
					.requires(Items.PAPER)
					.requires(Items.INK_SAC)
					.save(pvd);
		}

		// armor
		{
			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GolemItems.GOLEMGUARD_HELMET.get())::unlockedBy, Items.IRON_INGOT)
					.pattern(" B ").pattern("III").pattern("IAI")
					.define('I', Items.IRON_HELMET)
					.define('A', GolemItems.GOLEM_TEMPLATE.get())
					.define('B', Items.REDSTONE)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GolemItems.GOLEMGUARD_CHESTPLATE.get())::unlockedBy, Items.IRON_INGOT)
					.pattern("IAI").pattern("III").pattern("BIB")
					.define('I', Items.IRON_CHESTPLATE)
					.define('A', GolemItems.GOLEM_TEMPLATE.get())
					.define('B', Items.REDSTONE)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GolemItems.GOLEMGUARD_SHINGUARD.get())::unlockedBy, Items.IRON_INGOT)
					.pattern("BIB").pattern(" A ").pattern("I I")
					.define('I', Items.IRON_LEGGINGS)
					.define('A', GolemItems.GOLEM_TEMPLATE.get())
					.define('B', Items.REDSTONE)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GolemItems.WINDSPIRIT_HELMET.get())::unlockedBy, Items.DIAMOND)
					.pattern(" B ").pattern("III").pattern("IAI")
					.define('I', Items.DIAMOND_HELMET)
					.define('A', GolemItems.GOLEM_TEMPLATE.get())
					.define('B', Items.LAPIS_LAZULI)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GolemItems.WINDSPIRIT_CHESTPLATE.get())::unlockedBy, Items.DIAMOND)
					.pattern("IAI").pattern("III").pattern("BIB")
					.define('I', Items.DIAMOND_CHESTPLATE)
					.define('A', GolemItems.GOLEM_TEMPLATE.get())
					.define('B', Items.LAPIS_LAZULI)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GolemItems.WINDSPIRIT_SHINGUARD.get())::unlockedBy, Items.DIAMOND)
					.pattern("BIB").pattern(" A ").pattern("I I")
					.define('I', Items.DIAMOND_LEGGINGS)
					.define('A', GolemItems.GOLEM_TEMPLATE.get())
					.define('B', Items.LAPIS_LAZULI)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GolemItems.BARBARICFLAMEVANGUARD_HELMET.get())::unlockedBy, Items.DIAMOND)
					.pattern(" B ").pattern("III").pattern("IAI")
					.define('I', Items.NETHERITE_HELMET)
					.define('A', GolemItems.GOLEM_TEMPLATE.get())
					.define('B', Items.QUARTZ)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GolemItems.BARBARICFLAMEVANGUARD_CHESTPLATE.get())::unlockedBy, Items.DIAMOND)
					.pattern("IAI").pattern("III").pattern("BIB")
					.define('I', Items.NETHERITE_CHESTPLATE)
					.define('A', GolemItems.GOLEM_TEMPLATE.get())
					.define('B', Items.QUARTZ)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GolemItems.BARBARICFLAMEVANGUARD_SHINGUARD.get())::unlockedBy, Items.DIAMOND)
					.pattern("BIB").pattern(" A ").pattern("I I")
					.define('I', Items.NETHERITE_LEGGINGS)
					.define('A', GolemItems.GOLEM_TEMPLATE.get())
					.define('B', Items.QUARTZ)
					.save(pvd);
		}

		// weapon
		{
			for (var type : GolemWeaponType.values()) {
				for (var mat : VanillaGolemWeaponMaterial.values()) {
					Item item = GolemItems.METALGOLEM_WEAPON[type.ordinal()][mat.ordinal()].get();
					if (mat == VanillaGolemWeaponMaterial.NETHERITE) {
						Item prev = GolemItems.METALGOLEM_WEAPON[type.ordinal()][VanillaGolemWeaponMaterial.DIAMOND.ordinal()].get();
						smithing(pvd, prev, mat.getIngot(), item);
					} else {
						type.pattern(unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, item)::unlockedBy, mat.getIngot()))
								.define('I', mat.getIngot())
								.define('S', Items.STICK)
								.define('T', GolemItems.GOLEM_TEMPLATE.get())
								.save(pvd);
					}
				}
			}

		}

		// upgrades
		{

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GolemItems.FIRE_IMMUNE.get())::unlockedBy, GolemItems.EMPTY_UPGRADE.get())
					.pattern(" A ").pattern("ABA").pattern(" A ")
					.define('A', Items.MAGMA_CREAM)
					.define('B', GolemItems.EMPTY_UPGRADE.get())
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GolemItems.THUNDER_IMMUNE.get())::unlockedBy, GolemItems.EMPTY_UPGRADE.get())
					.pattern(" A ").pattern("ABA").pattern(" A ")
					.define('A', Items.LIGHTNING_ROD)
					.define('B', GolemItems.EMPTY_UPGRADE.get())
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GolemItems.RECYCLE.get())::unlockedBy, GolemItems.EMPTY_UPGRADE.get())
					.pattern(" C ").pattern("ABA").pattern(" D ")
					.define('A', Items.ENDER_PEARL)
					.define('B', GolemItems.EMPTY_UPGRADE.get())
					.define('C', Items.TOTEM_OF_UNDYING)
					.define('D', Items.RESPAWN_ANCHOR)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GolemItems.DIAMOND.get())::unlockedBy, GolemItems.EMPTY_UPGRADE.get())
					.pattern("CCC").pattern("CBC").pattern("CCC")
					.define('B', GolemItems.EMPTY_UPGRADE.get())
					.define('C', Items.DIAMOND)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GolemItems.NETHERITE.get())::unlockedBy, GolemItems.EMPTY_UPGRADE.get())
					.pattern("CAC").pattern("ABA").pattern("CAC")
					.define('A', Items.NETHERITE_INGOT)
					.define('B', GolemItems.EMPTY_UPGRADE.get())
					.define('C', Items.DIAMOND)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GolemItems.QUARTZ.get())::unlockedBy, GolemItems.EMPTY_UPGRADE.get())
					.pattern("CAC").pattern("ABA").pattern("CAC")
					.define('A', Items.QUARTZ_BLOCK)
					.define('B', GolemItems.EMPTY_UPGRADE.get())
					.define('C', Items.QUARTZ)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GolemItems.GOLD.get())::unlockedBy, GolemItems.EMPTY_UPGRADE.get())
					.pattern("CAC").pattern("ABA").pattern("CAC")
					.define('A', Items.GOLDEN_APPLE)
					.define('B', GolemItems.EMPTY_UPGRADE.get())
					.define('C', Items.GOLDEN_CARROT)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GolemItems.SPONGE.get())::unlockedBy, GolemItems.EMPTY_UPGRADE.get())
					.pattern(" A ").pattern("ABA").pattern(" A ")
					.define('A', Items.WET_SPONGE)
					.define('B', GolemItems.EMPTY_UPGRADE.get())
					.save(pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GolemItems.ENCHANTED_GOLD.get())::unlockedBy, GolemItems.EMPTY_UPGRADE.get())
					.requires(GolemItems.EMPTY_UPGRADE.get()).requires(Items.ENCHANTED_GOLDEN_APPLE)
					.save(pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GolemItems.FLOAT.get())::unlockedBy, GolemItems.EMPTY_UPGRADE.get())
					.requires(GolemItems.EMPTY_UPGRADE.get()).requires(ItemTags.BOATS)
					.save(pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GolemItems.SWIM.get())::unlockedBy, GolemItems.EMPTY_UPGRADE.get())
					.requires(GolemItems.EMPTY_UPGRADE.get()).requires(Items.HEART_OF_THE_SEA)
					.save(pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GolemItems.PLAYER_IMMUNE.get())::unlockedBy, GolemItems.EMPTY_UPGRADE.get())
					.requires(GolemItems.EMPTY_UPGRADE.get()).requires(Items.NETHER_STAR)
					.save(pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GolemItems.BELL.get())::unlockedBy, GolemItems.EMPTY_UPGRADE.get())
					.requires(GolemItems.EMPTY_UPGRADE.get()).requires(Items.BELL)
					.save(pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GolemItems.ENDER_SIGHT.get())::unlockedBy, GolemItems.EMPTY_UPGRADE.get())
					.requires(GolemItems.EMPTY_UPGRADE.get()).requires(Items.ENDER_EYE)
					.save(pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GolemItems.SPEED.get())::unlockedBy, GolemItems.EMPTY_UPGRADE.get())
					.requires(GolemItems.EMPTY_UPGRADE.get()).requires(Items.RABBIT_FOOT)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GolemItems.WEAK.get())::unlockedBy, GolemItems.EMPTY_UPGRADE.get())
					.pattern("CDC").pattern("ABA").pattern("CDC")
					.define('A', Items.DRAGON_BREATH)
					.define('B', GolemItems.EMPTY_UPGRADE.get())
					.define('C', Items.FERMENTED_SPIDER_EYE)
					.define('D', Items.REDSTONE)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GolemItems.SLOW.get())::unlockedBy, GolemItems.EMPTY_UPGRADE.get())
					.pattern("CDC").pattern("ABA").pattern("EDE")
					.define('A', Items.DRAGON_BREATH)
					.define('B', GolemItems.EMPTY_UPGRADE.get())
					.define('C', Items.FERMENTED_SPIDER_EYE)
					.define('D', Items.REDSTONE)
					.define('E', Items.SUGAR)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GolemItems.WITHER.get())::unlockedBy, GolemItems.EMPTY_UPGRADE.get())
					.pattern("CDC").pattern("ABA").pattern("CDC")
					.define('A', Items.DRAGON_BREATH)
					.define('B', GolemItems.EMPTY_UPGRADE.get())
					.define('C', Items.WITHER_ROSE)
					.define('D', Items.REDSTONE)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GolemItems.EMERALD.get())::unlockedBy, GolemItems.EMPTY_UPGRADE.get())
					.pattern("CAC").pattern("ABA").pattern("CAC")
					.define('A', Items.EMERALD_BLOCK)
					.define('B', GolemItems.EMPTY_UPGRADE.get())
					.define('C', Items.EMERALD)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GolemItems.PICKUP.get())::unlockedBy, GolemItems.EMPTY_UPGRADE.get())
					.pattern("AAA").pattern("DBD").pattern(" C ")
					.define('A', Items.HOPPER)
					.define('B', GolemItems.EMPTY_UPGRADE.get())
					.define('C', Items.LAVA_BUCKET)
					.define('D', Items.ENDER_PEARL)
					.save(pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GolemItems.PICKUP_MENDING.get())::unlockedBy, GolemItems.EMPTY_UPGRADE.get())
					.requires(GolemItems.EMPTY_UPGRADE.get())
					.requires(EnchantmentIngredient.of(pvd.getProvider(), Enchantments.MENDING, 1))
					.save(pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GolemItems.PICKUP_NO_DESTROY.get())::unlockedBy, GolemItems.EMPTY_UPGRADE.get())
					.requires(GolemItems.EMPTY_UPGRADE.get())
					.requires(Items.ZOMBIE_HEAD)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GolemItems.TALENTED.get())::unlockedBy, GolemItems.EMPTY_UPGRADE.get())
					.pattern("CEC").pattern("ABA").pattern("CAC")
					.define('E', Items.NETHER_STAR)
					.define('B', GolemItems.EMPTY_UPGRADE.get())
					.define('C', ItemTags.SKULLS)
					.define('A', Items.CHORUS_FLOWER)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GolemItems.CAULDRON.get())::unlockedBy, GolemItems.EMPTY_UPGRADE.get())
					.pattern("CEC").pattern("ABA").pattern("CDC")
					.define('A', Items.NETHER_STAR)
					.define('B', GolemItems.EMPTY_UPGRADE.get())
					.define('C', Items.DRAGON_BREATH)
					.define('D', Items.CAULDRON)
					.define('E', Items.DRAGON_HEAD)
					.save(pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GolemItems.MOUNT_UPGRADE.get())::unlockedBy, GolemItems.EMPTY_UPGRADE.get())
					.requires(GolemItems.EMPTY_UPGRADE.get()).requires(Items.SADDLE)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GolemItems.SIZE_UPGRADE.get())::unlockedBy, GolemItems.EMPTY_UPGRADE.get())
					.pattern("CAC").pattern("ABA").pattern("CAC")
					.define('A', Items.IRON_BLOCK)
					.define('B', GolemItems.EMPTY_UPGRADE.get())
					.define('C', Items.COPPER_BLOCK)
					.save(pvd);

		}

		CompatManager.dispatchGenRecipe(pvd);
	}

	public static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, Criterion<InventoryChangeTrigger.TriggerInstance>, T> func, Item item) {
		return func.apply("has_" + pvd.safeName(item), DataIngredient.items(item).getCriterion(pvd));
	}

	public static void smithing(RegistrateRecipeProvider pvd, Item in, Item mat, Item out) {
		Ingredient ing = Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE);
		unlock(pvd, SmithingTransformRecipeBuilder.smithing(ing, Ingredient.of(in), Ingredient.of(mat),
				RecipeCategory.COMBAT, out)::unlocks, mat).save(pvd, getID(out));
	}

	public static <T extends AbstractGolemEntity<T, P>, P extends IGolemPart<P>> void
	expand(RegistrateRecipeProvider pvd, ItemEntry<GolemHolder<T, P>> holder, ItemEntry<?> template) {
		unlock(pvd, new GolemSmithBuilder(holder.get(), template)::unlocks, template.get())
				.save(pvd, template.getId().withSuffix("_" + holder.getId().getPath()));
	}

	@SuppressWarnings("ConstantConditions")
	private static ResourceLocation getID(Item item) {
		return ModularGolems.loc(BuiltInRegistries.ITEM.getKey(item).getPath());
	}


}
