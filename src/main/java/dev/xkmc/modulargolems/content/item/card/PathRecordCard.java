package dev.xkmc.modulargolems.content.item.card;

import dev.xkmc.l2library.util.nbt.ItemCompoundTag;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import dev.xkmc.modulargolems.content.client.outline.BlockOutliner;
import dev.xkmc.modulargolems.init.data.MGLangData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PathRecordCard extends Item {

	private static final String KEY = "RecordedPath";

	public static List<Pos> getList(ItemStack stack) {
		List<Pos> ans = new ArrayList<>();
		var root = stack.getTag();
		if (root == null) return ans;
		if (!root.contains(KEY)) return ans;
		ListTag list = root.getList(KEY, Tag.TAG_COMPOUND);
		for (int i = 0; i < list.size(); i++) {
			ans.add(TagCodec.valueFromTag(list.getCompound(i), Pos.class));
		}
		return ans;
	}

	public static void addPos(ItemStack stack, Pos pos) {
		ItemCompoundTag.of(stack).getSubList(KEY, Tag.TAG_COMPOUND)
				.addCompound().setTag((CompoundTag) TagCodec.valueToTag(pos));
	}

	public static void setList(ItemStack stack, List<Pos> pos) {
		ItemCompoundTag.of(stack).getSubList(KEY, Tag.TAG_COMPOUND).clear();
		for (var e : pos) {
			addPos(stack, e);
		}
	}

	public static boolean togglePos(ItemStack stack, Pos pos) {
		var ans = getList(stack);
		if (!ans.contains(pos)) {
			addPos(stack, pos);
			return true;
		}
		ans.remove(pos);
		setList(stack, ans);
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
			if (togglePos(stack, new Pos(level.dimension().location(), pos))) {
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
			BlockOutliner.drawOutlines(player, getList(stack));
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		list.add(MGLangData.PATH_COUNT.get(Component.literal("" + getList(stack).size())
				.withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.GRAY));
		list.add(MGLangData.PATH.get().withStyle(ChatFormatting.GRAY));
	}

	public record Pos(ResourceLocation level, BlockPos pos) {

	}

}
