package dev.xkmc.modulargolems.content.config;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.modulargolems.content.core.GolemStatType;
import dev.xkmc.modulargolems.content.core.IGolemPart;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.item.data.GolemUpgrade;
import dev.xkmc.modulargolems.content.item.golem.GolemPart;
import dev.xkmc.modulargolems.content.item.upgrade.UpgradeItem;
import dev.xkmc.modulargolems.content.modifier.base.AttributeGolemModifier;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.*;

public record GolemMaterial(HashMap<GolemStatType, Double> stats, HashMap<GolemModifier, Integer> modifiers,
							ResourceLocation id, Item part) {

	public static final ResourceLocation EMPTY = ModularGolems.loc("empty");

	public static Map<Holder<Attribute>, Pair<GolemStatType, Double>> collectAttributes(List<GolemMaterial> list, GolemUpgrade upgrades) {
		HashMap<Holder<Attribute>, Map<GolemStatType, Double>> values = new LinkedHashMap<>();
		for (var type : GolemTypes.STAT_TYPES.get().entrySet()) {
			appendStat(values, type.getValue(), 0);
		}
		for (GolemMaterial stats : list) {
			stats.stats.forEach((k, v) -> appendStat(values, k, v));
		}
		for (var entry : collectModifiers(list, upgrades).entrySet()) {
			if (entry.getKey() instanceof AttributeGolemModifier attr) {
				for (var ent : attr.entries) {
					appendStat(values, ent.type().get(), ent.getValue(entry.getValue()));
				}
			}
		}
		HashMap<Holder<Attribute>, Pair<GolemStatType, Double>> ans = new LinkedHashMap<>();
		for (var ent : values.entrySet()) {
			HashMap<GolemStatType.Kind, Pair<GolemStatType, Double>> sorted = new LinkedHashMap<>();
			for (var entry : ent.getValue().entrySet()) {
				sorted.compute(entry.getKey().kind, (k, old) -> Pair.of(entry.getKey(), (old == null ? 0 : old.getSecond()) + entry.getValue()));
			}
			if (sorted.isEmpty()) {
				continue;
			}
			if (sorted.size() == 1) {
				ans.put(ent.getKey(), sorted.values().stream().findFirst().get());
				continue;
			}
			if (!sorted.containsKey(GolemStatType.Kind.BASE)) {
				throw new IllegalStateException("Only attributes with BASE modification allows multi-operation. Attribute: " + ent.getKey().value().getDescriptionId());
			}
			Pair<GolemStatType, Double> candidate = sorted.get(GolemStatType.Kind.BASE);
			GolemStatType type = candidate.getFirst();
			double val = candidate.getSecond();
			if (sorted.containsKey(GolemStatType.Kind.ADD)) {
				val += sorted.get(GolemStatType.Kind.ADD).getSecond();
			}
			if (sorted.containsKey(GolemStatType.Kind.PERCENT)) {
				val *= 1 + sorted.get(GolemStatType.Kind.PERCENT).getSecond();
			}
			ans.put(ent.getKey(), Pair.of(type, val));
		}
		return ans;
	}

	private static void appendStat(Map<Holder<Attribute>, Map<GolemStatType, Double>> values, GolemStatType k, double v) {
		values.computeIfAbsent(k.getAttribute(), e -> new LinkedHashMap<>())
				.compute(k, (e, old) -> (old == null ? 0 : old) + v);
	}

	public static HashMap<GolemModifier, Integer> collectModifiers(Collection<GolemMaterial> list, GolemUpgrade upgrades) {
		HashMap<GolemModifier, Integer> values = new LinkedHashMap<>();
		for (GolemMaterial stats : list) {
			stats.modifiers.forEach((k, v) -> values.compute(k, (a, old) -> Math.min(a.maxLevel, (old == null ? 0 : old) + v)));
		}
		for (var e : upgrades.upgradeItems()) {
			for (var m : e.get()) {
				values.compute(m.mod(), (a, old) -> Math.min(a.maxLevel, (old == null ? 0 : old) + m.level()));
			}
		}
		return values;
	}

	public static <T extends AbstractGolemEntity<T, P>, P extends IGolemPart<P>> void addAttributes(List<GolemMaterial> list, GolemUpgrade upgrades, T entity) {
		var map = DefaultAttributes.getSupplier(Wrappers.cast(entity.getType()));
		var attrs = collectAttributes(list, upgrades);
		attrs.keySet().forEach(e -> {
			var attr = entity.getAttribute(e);
			if (attr != null) {
				attr.setBaseValue(map.getBaseValue(e));
			}
		});
		attrs.forEach((k, v) -> v.getFirst().applyToEntity(entity, v.getSecond()));
	}

	public static Optional<ResourceLocation> getMaterial(ItemStack stack) {
		for (Map.Entry<ResourceLocation, Ingredient> ent : GolemMaterialConfig.get().ingredients.entrySet()) {
			if (ent.getValue().test(stack)) {
				return Optional.of(ent.getKey());
			}
		}
		return Optional.empty();
	}

	public MutableComponent getDesc() {
		return Component.translatable("golem_material." + id.getNamespace() + "." + id.getPath()).withStyle(ChatFormatting.GOLD);
	}

	public GolemPart<?, ?> getPart() {
		return Wrappers.cast(part());
	}
}
