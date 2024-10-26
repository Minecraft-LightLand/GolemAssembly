package dev.xkmc.modulargolems.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.xkmc.modulargolems.compat.materials.create.automation.DeployerUpgradeRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets = "com.simibubi.create.content.kinetics.deployer.BeltDeployerCallbacks")
public class BeltDeployerCallbacksMixin {

	@WrapOperation(method = "activate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isDamageableItem()Z"))
	private static boolean modulargolems$applyItem(ItemStack instance, Operation<Boolean> original, @Local(argsOnly = true) Recipe<?> recipe) {
		if (recipe instanceof DeployerUpgradeRecipe) {
			return false;
		}
		return original.call(instance);
	}

}
