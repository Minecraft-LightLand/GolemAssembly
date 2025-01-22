package dev.xkmc.modulargolems.content.recipe;

import dev.xkmc.l2core.serial.recipe.AbstractSmithingRecipe;
import dev.xkmc.modulargolems.content.item.data.GolemUpgrade;
import dev.xkmc.modulargolems.content.item.golem.GolemHolder;
import dev.xkmc.modulargolems.content.item.upgrade.AddSlotTemplate;
import dev.xkmc.modulargolems.init.registrate.GolemMiscs;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.level.Level;

public class GolemSmithAddSlotRecipe extends AbstractSmithingRecipe<GolemSmithAddSlotRecipe> {

	public final Ingredient template, base, addition;

	public GolemSmithAddSlotRecipe(Ingredient template, Ingredient base, Ingredient addition, ItemStack result) {
		super(template, base, addition, result);
		this.template = template;
		this.base = base;
		this.addition = addition;
	}

	@Override
	public boolean matches(SmithingRecipeInput input, Level level) {
		if (!template.test(input.template())) return false;
		if (!base.test(input.base())) return false;
		var ing = GolemHolder.getHealingMaterial(input.base());
		if (ing == null || !ing.test(input.addition())) return false;
		var upgrade = GolemHolder.getUpgrades(input.base());
		return !upgrade.contains(input.template().getItem());
	}

	@Override
	public ItemStack assemble(SmithingRecipeInput input, HolderLookup.Provider pvd) {
		ItemStack stack = input.base().copy();
		GolemUpgrade.add(stack, (AddSlotTemplate) input.template().getItem());
		return stack;
	}

	@Override
	public Serializer<GolemSmithAddSlotRecipe> getSerializer() {
		return GolemMiscs.SMITH_ADD_SLOT.get();
	}

}
