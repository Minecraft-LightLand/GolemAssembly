package dev.xkmc.modulargolems.compat.materials.alexscaves.modifier;

import dev.xkmc.modulargolems.compat.materials.alexscaves.ACCompatRegistry;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.data.MGConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ReformationModifier extends GolemModifier {

	private static int max(int lv) {
		return lv * MGConfig.COMMON.reformationMax.get();
	}

	private static float health() {
		return (float) (double) MGConfig.COMMON.reformationAbsorption.get();
	}


	private static float heal(int lv) {
		return (float) (MGConfig.COMMON.reformationHealing.get() * lv);
	}

	public ReformationModifier() {
		super(StatFilterType.HEALTH, 2);
	}

	@Override
	public void onAiStep(AbstractGolemEntity<?, ?> golem, int level) {
		if (golem.tickCount % 5 != 0) return;
		float hp = golem.getAbsorptionAmount();
		float max = max(level) * health();
		if (max <= hp) return;
		ItemStack iron = golem.getProjectile(ACCompatRegistry.DUMMY_IRON.asStack());
		if (iron.isEmpty()) return;
		iron.shrink(1);
		golem.setAbsorptionAmount(hp + health());
		golem.heal(heal(level));
		float f1 = 1 + (golem.getRandom().nextFloat() - golem.getRandom().nextFloat()) * 0.2F;
		golem.playSound(SoundEvents.IRON_GOLEM_REPAIR, 1, f1);
		//TODO visual effects
	}

	@Override
	public InteractionResult interact(Player player, AbstractGolemEntity<?, ?> golem, InteractionHand hand, int level) {
		ItemStack stack = player.getItemInHand(hand);
		if (!ACCompatRegistry.DUMMY_IRON.get().isValid(stack)) return InteractionResult.PASS;
		float hp = golem.getAbsorptionAmount();
		float max = max(level) * health();
		if (max <= hp) return InteractionResult.FAIL;
		if (!player.level().isClientSide()) {
			if (!player.isCreative()) stack.shrink(1);
			golem.setAbsorptionAmount(hp + health());
			golem.heal(heal(level));
			float f1 = 1 + (golem.getRandom().nextFloat() - golem.getRandom().nextFloat()) * 0.2F;
			golem.playSound(SoundEvents.IRON_GOLEM_REPAIR, 1, f1);
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public List<MutableComponent> getDetail(int v) {
		int nab = Math.round(health());
		int nhp = Math.round(heal(v));
		int nmax = nab * max(v);
		var cab = Component.literal("" + nab).withStyle(ChatFormatting.AQUA);
		var chp = Component.literal("" + nhp).withStyle(ChatFormatting.AQUA);
		var cmax = Component.literal("" + nmax).withStyle(ChatFormatting.AQUA);
		return List.of(Component.translatable(getDescriptionId() + ".desc", cab, chp, cmax).withStyle(ChatFormatting.GREEN));
	}


}
