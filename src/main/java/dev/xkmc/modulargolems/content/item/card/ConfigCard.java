package dev.xkmc.modulargolems.content.item.card;

import dev.xkmc.l2core.util.Proxy;
import dev.xkmc.modulargolems.content.capability.GolemConfigEditor;
import dev.xkmc.modulargolems.content.capability.GolemConfigEntry;
import dev.xkmc.modulargolems.content.capability.GolemConfigStorage;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.item.wand.GolemInteractItem;
import dev.xkmc.modulargolems.content.menu.config.ConfigMenuProvider;
import dev.xkmc.modulargolems.init.data.MGLangData;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public class ConfigCard extends Item implements GolemInteractItem {

	@Nullable
	public static UUID getUUID(ItemStack stack) {
		return GolemItems.DC_OWNER.get(stack);
	}

	public static Predicate<AbstractGolemEntity<?, ?>> getFilter(Player player) {
		ItemStack stack = player.getOffhandItem();
		if (stack.getItem() instanceof ConfigCard card) {
			UUID uuid = getUUID(stack);
			if (uuid != null) {
				return e -> Optional.ofNullable(e.getConfigEntry(null))
						.map(x -> x.getID().equals(uuid) && x.getColor() == card.color.getId())
						.orElse(true);
			}
		}
		return e -> true;
	}

	private static boolean mayClientEdit(UUID id) {
		LocalPlayer player = Proxy.getClientPlayer();
		return player != null && player.getUUID().equals(id);
	}

	private final DyeColor color;

	public ConfigCard(Properties properties, DyeColor color) {
		super(properties);
		this.color = color;
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
		if (target instanceof AbstractGolemEntity<?, ?> golem) {
			if (!golem.canModify(player)) return InteractionResult.FAIL;
			if (player.level().isClientSide()) return InteractionResult.SUCCESS;
			UUID uuid = getUUID(stack);
			if (uuid == null) {
				GolemItems.DC_OWNER.set(stack, uuid = player.getUUID());
			}
			var old = golem.getConfigEntry(null);
			if (old != null && old.getID().equals(uuid) && old.getColor() == color.getId()) {
				golem.setConfigCard(null, 0);
			} else {
				golem.setConfigCard(uuid, color.getId());
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		UUID uuid = getUUID(stack);
		if (uuid == null) {
			GolemItems.DC_OWNER.set(stack, uuid = player.getUUID());
		}
		if (level instanceof ServerLevel sl) {
			GolemConfigEditor editor;
			GolemConfigEntry entry;
			if (player.getUUID().equals(uuid)) {
				entry = GolemConfigStorage.get(level).getOrCreateStorage(uuid, color.getId(), stack.getHoverName());
				entry.setName(stack.getHoverName(), sl);
				entry.heartBeat(sl, (ServerPlayer) player);
				editor = new GolemConfigEditor.Writable(level, entry);
			} else {
				entry = GolemConfigStorage.get(level).getStorage(uuid, color.getId());
				if (entry != null) {
					editor = new GolemConfigEditor.Writable(level, entry);
				} else {
					editor = null;
				}
			}
			if (editor != null && player instanceof ServerPlayer sp) {
				var pvd = new ConfigMenuProvider(uuid, color.getId(), editor);
				sp.openMenu(pvd, pvd::writeBuffer);
				return InteractionResultHolder.success(stack);
			}
		}
		return InteractionResultHolder.pass(stack);
	}

	public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> list, TooltipFlag flag) {
		var id = getUUID(stack);
		if (id == null) {
			list.add(MGLangData.CONFIG_INIT.get());
		} else {
			var level = ctx.level();
			if (level != null) {
				var entry = GolemConfigStorage.get(level)
						.getOrCreateStorage(id, color.getId(), MGLangData.LOADING.get());
				entry.clientTick(level, false);
				list.add(entry.getDisplayName());
			}
			if (!mayClientEdit(id)) {
				list.add(MGLangData.CONFIG_OTHER.get());
			}
			list.add(MGLangData.CONFIG_CARD.get());
		}
	}

}
