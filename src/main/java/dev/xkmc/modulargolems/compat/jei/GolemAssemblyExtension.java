package dev.xkmc.modulargolems.compat.jei;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.modulargolems.content.config.GolemMaterial;
import dev.xkmc.modulargolems.content.config.GolemMaterialConfig;
import dev.xkmc.modulargolems.content.item.data.GolemHolderMaterial;
import dev.xkmc.modulargolems.content.item.golem.GolemHolder;
import dev.xkmc.modulargolems.content.item.golem.GolemPart;
import dev.xkmc.modulargolems.content.recipe.GolemAssembleRecipe;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.List;

public record GolemAssemblyExtension(
) implements ICraftingCategoryExtension<GolemAssembleRecipe> {

	@Override
	public void setRecipe(RecipeHolder<GolemAssembleRecipe> holder, IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
		var out = focuses.getItemStackFocuses(RecipeIngredientRole.OUTPUT).findAny();
		if (out.isPresent()) {
			ItemStack outStack = out.get().getTypedValue().getIngredient();
			if (outStack.getItem() instanceof GolemHolder<?, ?> h &&
					GolemHolder.getMaterial(outStack).size() == h.getEntityType().values().length) {
				setRecipeSpecial(holder, builder, craftingGridHelper, outStack);
				return;
			}
		}
		setRecipeAll(holder, builder, craftingGridHelper, focuses);
	}

	private void setRecipeAll(RecipeHolder<GolemAssembleRecipe> holder, IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
		List<List<ItemStack>> inputs = new ArrayList<>();
		for (Ingredient ing : holder.value().getIngredients()) {
			ItemStack[] stacks = ing.getItems();
			if (stacks.length == 1 && stacks[0].getItem() instanceof GolemPart<?, ?> part) {
				List<ItemStack> list = new ArrayList<>();
				for (ResourceLocation rl : GolemMaterialConfig.get().getAllMaterials()) {
					ItemStack stack = new ItemStack(part);
					list.add(GolemPart.setMaterial(stack, rl));
				}
				inputs.add(list);
			} else {
				inputs.add(List.of(stacks));
			}
		}
		ItemStack resultItem = holder.value().getResultItem(Minecraft.getInstance().level.registryAccess());
		List<ItemStack> list = new ArrayList<>();
		if (resultItem.getItem() instanceof GolemHolder<?, ?> golem) {
			Pair<GolemPart<?, ?>, ResourceLocation> fix = null;
			var focus = focuses.getItemStackFocuses(RecipeIngredientRole.INPUT).findAny();
			if (focus.isPresent()) {
				ItemStack stack = focus.get().getTypedValue().getIngredient();
				if (stack.getItem() instanceof GolemPart<?, ?> part) {
					var mat = GolemPart.getMaterial(stack);
					if (mat.isPresent()) {
						fix = Pair.of(part, mat.get());
					}
				}
			}
			for (ResourceLocation rl : GolemMaterialConfig.get().getAllMaterials()) {
				ItemStack stack = new ItemStack(golem);
				ArrayList<GolemHolderMaterial.Entry> mats = new ArrayList<>();
				for (var part : golem.getEntityType().values()) {
					GolemPart<?, ?> partItem = part.toItem();
					if (fix != null && fix.getFirst() == partItem) {
						mats.add(new GolemHolderMaterial.Entry(partItem, fix.getSecond()));
					} else {
						mats.add(new GolemHolderMaterial.Entry(partItem, rl));
					}
				}
				list.add(GolemItems.HOLDER_MAT.set(stack, new GolemHolderMaterial(mats)));
			}
		} else {
			list.add(resultItem);
		}
		int width = getWidth(holder);
		int height = getHeight(holder);
		craftingGridHelper.createAndSetOutputs(builder, list);
		craftingGridHelper.createAndSetInputs(builder, inputs, width, height);
	}

	private void setRecipeSpecial(RecipeHolder<GolemAssembleRecipe> holder, IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, ItemStack focusResult) {
		var mats = GolemHolder.getMaterial(focusResult);
		List<List<ItemStack>> inputs = new ArrayList<>();
		int ind = 0;
		for (Ingredient ing : holder.value().getIngredients()) {
			ItemStack[] stacks = ing.getItems();
			if (stacks.length == 1 && stacks[0].getItem() instanceof GolemPart<?, ?> part) {
				GolemMaterial mat = mats.get(ind++);
				inputs.add(List.of(GolemPart.setMaterial(new ItemStack(part), mat.id())));
			} else inputs.add(List.of(stacks));

		}
		int width = getWidth(holder);
		int height = getHeight(holder);
		craftingGridHelper.createAndSetOutputs(builder, List.of(focusResult));
		craftingGridHelper.createAndSetInputs(builder, inputs, width, height);
	}

	@Override
	public int getWidth(RecipeHolder<GolemAssembleRecipe> recipeHolder) {
		return recipeHolder.value().getWidth();
	}

	@Override
	public int getHeight(RecipeHolder<GolemAssembleRecipe> recipeHolder) {
		return recipeHolder.value().getHeight();
	}

}
