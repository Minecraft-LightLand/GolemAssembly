package dev.xkmc.modulargolems.compat.materials.alexscaves;

import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2complements.init.L2Complements;
import dev.xkmc.l2complements.init.data.TagGen;
import dev.xkmc.modulargolems.compat.materials.alexscaves.modifier.*;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.data.MGTagGen;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.ModList;

import static dev.xkmc.modulargolems.init.registrate.GolemModifiers.reg;

public class ACCompatRegistry {

	public static final RegistryEntry<CandyStickModifier> STICKY;
	public static final RegistryEntry<CandyShootModifier> SHOOT;
	public static final RegistryEntry<FreeMoveModifier> FREE;
	public static final RegistryEntry<PolarizeModifier> POLARIZE;
	public static final RegistryEntry<ReformationModifier> REFORMATION;
	public static final RegistryEntry<RadiationModifier> RADIATION;
	public static final RegistryEntry<AtomicFuelingModifier> ATOMIC;

	public static final RegistryEntry<AtomicBoostedEffect> EFF_ATOMIC;
	public static final ItemEntry<DummyConsumer> DUMMY_IRON, DUMMY_URANIUM;

	static {
		STICKY = reg("sticky_caramel", CandyStickModifier::new,
				"Put molten caramel on attack targets");
		SHOOT = reg("gum_shooter", CandyShootModifier::new,
				"Shoot gumballs at targets dealing %s damage");
		FREE = reg("free_movement", FreeMoveModifier::new,
				"Golem will not be stuck by blocks or caramel");
		POLARIZE = reg("polarize", PolarizeModifier::new,
				"Pull enemies in melee mode and push enemies away in ranged/standing mode, deal %s electrical damage constantly.");
		REFORMATION = reg("reformation", ReformationModifier::new,
				"Consume iron ingot to gain %s absorption and heal %s. Golem may hold at most %s absorption");
		RADIATION = reg("radiation", RadiationModifier::new,
				"Inflict %s to attack targets. Damage to radiated target increase by %s per radiation level.");
		ATOMIC = reg("atomic_fueling", AtomicFuelingModifier::new,
				"Consume [%s] to heal %s HP, and boost attack and speed by %s for %s seconds. Makes golem immune to irradiated effect.");

		EFF_ATOMIC = genEffect("atomic_boost", () -> new AtomicBoostedEffect(MobEffectCategory.BENEFICIAL, 0xffffffff),
				"Increase golem attack damage and movement speed");

		DUMMY_IRON = ModularGolems.REGISTRATE.item("dummy_iron_consumer", p -> new DummyConsumer(Tags.Items.INGOTS_IRON))
				.model((ctx, pvd) -> pvd.withExistingParent("item/" + ctx.getName(), "block/air"))
				.removeTab(GolemItems.TAB.getKey())
				.register();

		DUMMY_URANIUM = ModularGolems.REGISTRATE.item("dummy_uranium_consumer",
						p -> new DummyConsumer(ItemTags.create(new ResourceLocation("forge", "nuggets/uranium"))))
				.model((ctx, pvd) -> pvd.withExistingParent("item/" + ctx.getName(), "block/air"))
				.removeTab(GolemItems.TAB.getKey())
				.register();
	}

	private static <T extends MobEffect> RegistryEntry<T> genEffect(String name, NonNullSupplier<T> sup, String desc) {
		return ModularGolems.REGISTRATE.effect(name, sup, desc).lang(MobEffect::getDescriptionId).register();
	}

	public static void register() {
		if (ModList.get().isLoaded(L2Complements.MODID)) {
			MGTagGen.OPTIONAL_EFF.add(e -> e.addTag(TagGen.SKILL_EFFECT)
					.addOptional(EFF_ATOMIC.getId()));
		}
	}

}
