package dev.xkmc.modulargolems.content.item.wand;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2library.util.raytrace.IGlowingTarget;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import dev.xkmc.modulargolems.content.capability.GolemConfigEditor;
import dev.xkmc.modulargolems.content.capability.GolemConfigEntry;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.dog.DogGolemEntity;
import dev.xkmc.modulargolems.content.item.card.ConfigCard;
import dev.xkmc.modulargolems.init.data.MGLangData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;


public class SquadWandItem extends BaseWandItem implements GolemInteractItem, IGlowingTarget {
	private static final String KEY_OWNER = "config_captain";
	private static final int RANGE = 64;

	public SquadWandItem(Properties properties, @Nullable ItemEntry<? extends BaseWandItem> base) {
		super(properties, MGLangData.WAND_SQUAD, null, base);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
		if (level.isClientSide() && selected && entity instanceof Player player) {
			RayTraceUtil.clientUpdateTarget(player, RANGE);
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!level.isClientSide()) {
			LivingEntity target = RayTraceUtil.serverGetTarget(player);
			if (target != null) {
				interactLivingEntity(stack, player, target, hand);
			}
		}
		return InteractionResultHolder.success(stack);
	}

	@Override
	public int getDistance(ItemStack itemStack) {
		return RANGE;
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity target, InteractionHand hand) {
		if (!(target instanceof AbstractGolemEntity<?, ?> golem)) {
			return InteractionResult.PASS;
		}
		return choose(target.level(), user, golem) ? InteractionResult.SUCCESS : InteractionResult.FAIL;
	}

	private static boolean choose(Level level, Player user, AbstractGolemEntity<?, ?> golem) {
		if (!ConfigCard.getFilter(user).test(golem)) return false;
		if (!golem.canModify(user)) return false;
		if (level.isClientSide()) return true;
		if (!(golem instanceof DogGolemEntity)) {
			GolemConfigEntry entry = golem.getConfigEntry(null);
			if (entry != null) {
				var editor = new GolemConfigEditor.Writable(level, entry).getSquad();
				UUID capId = editor.getCaptainId();
				UUID golemId = golem.getUUID();
				if (capId != null && capId.equals(golemId)) {
					editor.setCaptainId(null);
				} else {
					editor.setCaptainId(golemId);
				}
				if (user.isShiftKeyDown()) {
					if (capId != null && capId.equals(golemId)){
						golem.blockPosition();
					}else return false;
				}
			} else return false;
		}
		return false;
	}
	@Nullable
	public static UUID getUUID(ItemStack stack) {
		if (stack.getTag() != null && stack.getTag().contains(KEY_OWNER)) {
			return stack.getTag().getUUID(KEY_OWNER);
		}
		return null;
	}
}
/* ._.
————|————
    |
   / \
  /   \ */