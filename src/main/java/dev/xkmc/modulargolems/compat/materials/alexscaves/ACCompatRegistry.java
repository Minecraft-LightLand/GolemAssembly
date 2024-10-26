package dev.xkmc.modulargolems.compat.materials.alexscaves;

import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.modulargolems.compat.materials.alexscaves.modifier.*;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;

import static dev.xkmc.modulargolems.init.registrate.GolemModifiers.reg;

public class ACCompatRegistry {

	public static final RegistryEntry<CandyStickModifier> STICKY;
	public static final RegistryEntry<CandyTrapModifier> TRAP;
	public static final RegistryEntry<MagneticModifier> POLARIZE;
	public static final RegistryEntry<RadiationModifier> RADIATION;

	public static final ItemEntry<DummyConsumer> DUMMY_IRON, DUMMY_URANIUM;

	static {
		STICKY = reg("radiation", CandyStickModifier::new,
				"Inflict %s to attack targets");
		TRAP = reg("radiation", CandyTrapModifier::new,
				"Inflict %s to attackers");
		POLARIZE = reg("polarize", MagneticModifier::new,
				"Pull enemies in melee mode and push enemies away in ranged/standing mode. Deal electrical damage in the process.");
		RADIATION = reg("radiation", RadiationModifier::new,
				"Inflict %s to attack targets");

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

	public static void register() {

	}

}
