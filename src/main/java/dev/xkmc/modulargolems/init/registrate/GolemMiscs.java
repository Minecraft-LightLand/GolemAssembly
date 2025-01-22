package dev.xkmc.modulargolems.init.registrate;

import com.tterrag.registrate.util.entry.MenuEntry;
import dev.xkmc.l2complements.init.L2Complements;
import dev.xkmc.l2core.init.reg.simple.SR;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.l2core.serial.recipe.AbstractShapedRecipe;
import dev.xkmc.l2core.serial.recipe.AbstractSmithingRecipe;
import dev.xkmc.modulargolems.content.menu.config.ToggleGolemConfigMenu;
import dev.xkmc.modulargolems.content.menu.config.ToggleGolemConfigScreen;
import dev.xkmc.modulargolems.content.menu.equipment.EquipmentsMenu;
import dev.xkmc.modulargolems.content.menu.equipment.EquipmentsScreen;
import dev.xkmc.modulargolems.content.menu.filter.ItemConfigMenu;
import dev.xkmc.modulargolems.content.menu.filter.ItemConfigScreen;
import dev.xkmc.modulargolems.content.menu.path.PathConfigMenu;
import dev.xkmc.modulargolems.content.menu.path.PathConfigScreen;
import dev.xkmc.modulargolems.content.menu.target.TargetConfigMenu;
import dev.xkmc.modulargolems.content.menu.target.TargetConfigScreen;
import dev.xkmc.modulargolems.content.recipe.GolemAssembleRecipe;
import dev.xkmc.modulargolems.content.recipe.GolemSmithAddSlotRecipe;
import dev.xkmc.modulargolems.init.ModularGolems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;

import static dev.xkmc.modulargolems.init.ModularGolems.REGISTRATE;

public class GolemMiscs {

	private static final SR<RecipeSerializer<?>> RS = SR.of(ModularGolems.REG, BuiltInRegistries.RECIPE_SERIALIZER);

	public static final Val<AbstractShapedRecipe.Serializer<GolemAssembleRecipe>> ASSEMBLE =
			RS.reg("golem_assemble", () -> new AbstractShapedRecipe.Serializer<>(GolemAssembleRecipe::new));
	public static final Val<AbstractSmithingRecipe.Serializer<GolemSmithAddSlotRecipe>> SMITH_ADD_SLOT =
			RS.reg("golem_add_slot", () -> new AbstractSmithingRecipe.Serializer<>(GolemSmithAddSlotRecipe::new));

	public static final MenuEntry<EquipmentsMenu> EQUIPMENTS =
			REGISTRATE.menu("equipments", EquipmentsMenu::fromNetwork, () -> EquipmentsScreen::new)
					.register();

	public static final MenuEntry<ToggleGolemConfigMenu> CONFIG_TOGGLE =
			REGISTRATE.menu("config_toggle", ToggleGolemConfigMenu::fromNetwork, () -> ToggleGolemConfigScreen::new)
					.register();

	public static final MenuEntry<ItemConfigMenu> CONFIG_PICKUP =
			REGISTRATE.menu("config_pickup", ItemConfigMenu::fromNetwork, () -> ItemConfigScreen::new)
					.register();

	public static final MenuEntry<TargetConfigMenu> CONFIG_TARGET =
			REGISTRATE.menu("config_target", TargetConfigMenu::fromNetwork, () -> TargetConfigScreen::new)
					.register();

	public static final MenuEntry<PathConfigMenu> CONFIG_PATH =
			REGISTRATE.menu("config_path", PathConfigMenu::fromNetwork, () -> PathConfigScreen::new)
					.register();

	public static void register() {
	}

}
