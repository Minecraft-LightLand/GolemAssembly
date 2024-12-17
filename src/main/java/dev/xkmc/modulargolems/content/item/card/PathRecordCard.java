package dev.xkmc.modulargolems.content.item.card;

import dev.xkmc.modulargolems.content.client.outline.BlockOutliner;
import dev.xkmc.modulargolems.init.data.MGLangData;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.ModList;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PathRecordCard extends Item {

	@Nullable
	public static Pos getList(ItemStack stack) {
		return GolemItems.DC_PATH.get(stack);
	}

	public static void addPos(ItemStack stack, Level level, BlockPos pos) {
		var id = level.dimension().location();
		Pos old = getList(stack);
		if (old != null && old.level.equals(id)) {
			old = old.copy();
			old.pos.add(pos);
			GolemItems.DC_PATH.set(stack, old);
		} else {
			GolemItems.DC_PATH.set(stack, new Pos(id, new ArrayList<>(List.of(pos))));
		}
	}

	public static boolean togglePos(ItemStack stack, Level level, BlockPos pos) {
		var id = level.dimension().location();
		var ans = getList(stack);
		if (ans == null || !ans.level().equals(id) || !ans.pos().contains(pos)) {
			addPos(stack, level, pos);
			return true;
		}
		ans = ans.copy();
		ans.pos().remove(pos);
		GolemItems.DC_PATH.set(stack, ans);
		return false;
	}

	public PathRecordCard(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		ItemStack stack = ctx.getItemInHand();
		Level level = ctx.getLevel();
		if (!level.isClientSide()) {
			BlockPos pos = ctx.getClickedPos();
			BlockState state = level.getBlockState(pos);
			if (!state.getShape(level, pos).isEmpty()) {
				pos = pos.relative(ctx.getClickedFace());
			}
			Player player = ctx.getPlayer();
			if (togglePos(stack, level, pos)) {
				if (player != null) {
					player.sendSystemMessage(MGLangData.PATH_ADD.get());
				}
			} else {
				if (player != null) {
					player.sendSystemMessage(MGLangData.PATH_REMOVE.get());
				}
			}
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
		if (selected && entity instanceof Player player && level.isClientSide()) {
			var pos = getList(stack);
			if (pos != null && pos.level().equals(level.dimension().location())) {
				BlockOutliner.drawOutlines(player, pos.pos);
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		int size = Optional.ofNullable(getList(stack)).map(e -> e.pos().size()).orElse(0);
		list.add(MGLangData.PATH_COUNT.get(Component.literal("" + size)
				.withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.GRAY));
		list.add(MGLangData.PATH.get().withStyle(ChatFormatting.GRAY));
	}

	public record Pos(ResourceLocation level, ArrayList<BlockPos> pos) {

		public Pos copy() {
			return new Pos(level, new ArrayList<>(pos));
		}

		public boolean match(Level level) {
			return level().equals(level.dimension().location());
		}

	}

}
