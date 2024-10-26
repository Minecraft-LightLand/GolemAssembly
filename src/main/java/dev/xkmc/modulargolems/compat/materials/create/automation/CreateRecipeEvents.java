package dev.xkmc.modulargolems.compat.materials.create.automation;

import com.simibubi.create.content.kinetics.deployer.DeployerRecipeSearchEvent;
import dev.xkmc.modulargolems.content.core.IGolemPart;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.item.card.ConfigCard;
import dev.xkmc.modulargolems.content.item.equipments.GolemEquipmentItem;
import dev.xkmc.modulargolems.content.item.golem.GolemHolder;
import dev.xkmc.modulargolems.content.item.upgrade.UpgradeItem;
import dev.xkmc.modulargolems.events.CraftEventListeners;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.Curios;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Optional;

public class CreateRecipeEvents {

	@SubscribeEvent
	public static void addRecipe(DeployerRecipeSearchEvent event) {
		ItemStack first = event.getInventory().getItem(0);
		ItemStack second = event.getInventory().getItem(1);
		if (!(first.getItem() instanceof GolemHolder<?, ?> holder)) return;
		ItemStack result;
		Level level = event.getBlockEntity().getLevel();
		if (!(level instanceof ServerLevel sl)) return;
		if (second.getItem() instanceof ConfigCard card) {
			var id = ConfigCard.getUUID(second);
			if (id == null) return;
			result = first.copy();
			GolemHolder.setGolemConfig(result, id, card.getColor());
		} else if (second.getItem() instanceof UpgradeItem upgrade) {
			result = CraftEventListeners.appendUpgrade(first, holder, upgrade);
		} else if (isGolemCurio(holder, second)) {
			result = equipCurio(holder, first, second, sl);
		} else if (holder.getEntityType() == GolemTypes.TYPE_GOLEM.get()) {
			if (!(second.getItem() instanceof GolemEquipmentItem equipment)) return;
			if (!equipment.isFor(GolemTypes.ENTITY_GOLEM.get())) return;
			EquipmentSlot slot = equipment.getSlot();
			result = equip(holder, first, second, slot, sl);
		} else if (holder.getEntityType() == GolemTypes.TYPE_HUMANOID.get()) {
			EquipmentSlot slot = LivingEntity.getEquipmentSlotForItem(second);
			result = equip(holder, first, second, slot, sl);
		} else return;
		if (result.isEmpty()) return;
		event.addRecipe(() -> Optional.of(new DeployerUpgradeRecipe(result)), 1000);
	}

	private static boolean isGolemCurio(GolemHolder<?, ?> holder, ItemStack stack) {
		if (!ModList.get().isLoaded(Curios.MODID)) return false;
		var set = CuriosApi.getEntitySlots(holder.getEntityType().type()).keySet();
		return ForgeRegistries.ITEMS.tags().getReverseTag(stack.getItem())
				.map(e -> e.getTagKeys().anyMatch(t ->
						t.location().getNamespace().equals(Curios.MODID) &&
								set.contains(t.location().getPath())))
				.orElse(false);
	}

	private static <T extends AbstractGolemEntity<T, P>, P extends IGolemPart<P>>
	ItemStack equipCurio(GolemHolder<T, P> holder, ItemStack golem, ItemStack equip, Level level) {
		if (!ModList.get().isLoaded(Curios.MODID)) return ItemStack.EMPTY;
		T entity = holder.createDummy(golem, level);
		if (entity == null) return ItemStack.EMPTY;
		var opt = CuriosApi.getCuriosInventory(entity).resolve();
		if (opt.isEmpty()) return ItemStack.EMPTY;
		equip = equip.copy();
		equip.setCount(1);
		for (var slot : CuriosApi.getItemStackSlots(equip, entity).keySet()) {
			var handler = opt.get().getStacksHandler(slot);
			if (handler.isEmpty()) continue;
			var stacks = handler.get().getStacks();
			for (int i = 0; i < stacks.getSlots(); i++) {
				if (stacks.getStackInSlot(i).isEmpty()) {
					stacks.setStackInSlot(i, equip);
					return GolemHolder.setEntity(entity);
				}
			}
		}
		return ItemStack.EMPTY;
	}

	private static <T extends AbstractGolemEntity<T, P>, P extends IGolemPart<P>>
	ItemStack equip(GolemHolder<T, P> holder, ItemStack golem, ItemStack equip, EquipmentSlot slot, Level level) {
		T entity = holder.createDummy(golem, level);
		if (entity == null) return ItemStack.EMPTY;
		if (!entity.getItemBySlot(slot).isEmpty()) return ItemStack.EMPTY;
		equip = equip.copy();
		equip.setCount(1);
		entity.setItemSlot(slot, equip);
		return GolemHolder.setEntity(entity);
	}

}
