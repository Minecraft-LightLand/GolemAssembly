package dev.xkmc.modulargolems.content.config;

import dev.xkmc.l2library.serial.config.BaseConfig;
import dev.xkmc.l2library.serial.config.CollectType;
import dev.xkmc.l2library.serial.config.ConfigCollect;
import dev.xkmc.l2library.util.annotation.DataGenOnly;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.modulargolems.content.core.GolemStatType;
import dev.xkmc.modulargolems.content.modifier.base.AttributeGolemModifier;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.ModularGolems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.apache.http.util.Asserts;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

@SerialClass
public class GolemMaterialConfig extends BaseConfig {

	public static GolemMaterialConfig get() {
		return ModularGolems.MATERIALS.getMerged();
	}

	@ConfigCollect(CollectType.MAP_COLLECT)
	@SerialClass.SerialField
	public HashMap<ResourceLocation, HashMap<GolemStatType, Double>> stats = new HashMap<>();

	@ConfigCollect(CollectType.MAP_COLLECT)
	@SerialClass.SerialField
	public HashMap<ResourceLocation, HashMap<GolemModifier, Integer>> modifiers = new HashMap<>();

	@ConfigCollect(CollectType.MAP_OVERWRITE)
	@SerialClass.SerialField
	public HashMap<ResourceLocation, Ingredient> ingredients = new HashMap<>();

	@ConfigCollect(CollectType.MAP_OVERWRITE)
	@SerialClass.SerialField
	public HashMap<ResourceLocation, Ingredient> repairIngredients = new HashMap<>();

	public Set<ResourceLocation> getAllMaterials() {
		TreeSet<ResourceLocation> set = new TreeSet<>(stats.keySet());
		set.retainAll(modifiers.keySet());
		set.retainAll(ingredients.keySet());
		set.removeIf(e -> isEmpty(ingredients.get(e)));
		return set;
	}

	public Ingredient getCraftIngredient(ResourceLocation id) {
		var ans = ingredients.get(id);
		return ans == null ? Ingredient.EMPTY : ans;
	}

	public Ingredient getRepairIngredient(ResourceLocation id) {
		var rep = repairIngredients.get(id);
		if (rep != null) return rep;
		var ans = ingredients.get(id);
		return ans == null ? Ingredient.EMPTY : ans;
	}

	private static boolean isEmpty(Ingredient ing) {
		var items = ing.getItems();
		return items.length == 0 || items[0].is(Items.BARRIER);
	}

	@DataGenOnly
	public Builder addMaterial(ResourceLocation id, Ingredient ingredient) {
		return new Builder(this, id, ingredient);
	}

	@DataGenOnly
	public Builder addMaterial(ResourceLocation id, Ingredient ingredient, Ingredient repair) {
		return new Builder(this, id, ingredient, repair);
	}

	@DataGenOnly
	public static class Builder {

		private final GolemMaterialConfig parent;
		private final ResourceLocation id;
		private final Ingredient ingredient;
		private final Ingredient repairIngredient;

		private final HashMap<GolemStatType, Double> stats = new HashMap<>();
		private final HashMap<GolemModifier, Integer> modifiers = new HashMap<>();

		private Builder(GolemMaterialConfig parent, ResourceLocation id, Ingredient ingredient) {
			this.parent = parent;
			this.id = id;
			this.ingredient = ingredient;
			this.repairIngredient = ingredient;
		}

		private Builder(GolemMaterialConfig parent, ResourceLocation id, Ingredient ingredient, Ingredient repair) {
			this.parent = parent;
			this.id = id;
			this.ingredient = ingredient;
			this.repairIngredient = repair;
		}

		public Builder addStat(GolemStatType type, double val) {
			stats.put(type, val);
			return this;
		}

		public Builder addModifier(GolemModifier modifier, int lv) {
			Asserts.check(!(modifier instanceof AttributeGolemModifier), "Material cannot use attribute modifier");
			modifiers.put(modifier, lv);
			return this;
		}

		public GolemMaterialConfig end() {
			parent.stats.put(id, stats);
			parent.modifiers.put(id, modifiers);
			parent.ingredients.put(id, ingredient);
			parent.repairIngredients.put(id, repairIngredient);
			return parent;
		}

	}

}
