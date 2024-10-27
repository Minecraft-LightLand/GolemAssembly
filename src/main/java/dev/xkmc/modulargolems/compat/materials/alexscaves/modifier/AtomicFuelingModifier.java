package dev.xkmc.modulargolems.compat.materials.alexscaves.modifier;

import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import dev.xkmc.l2complements.init.L2Complements;
import dev.xkmc.l2complements.init.registrate.LCEffects;
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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;

import java.util.List;
import java.util.function.BiConsumer;

public class AtomicFuelingModifier extends GolemModifier {

	private static int time() {
		return MGConfig.COMMON.atomicDuration.get();
	}

	private static float health() {
		return (float) (double) MGConfig.COMMON.atomicHeal.get();
	}

	public AtomicFuelingModifier() {
		super(StatFilterType.HEALTH, 2);
	}

	private void perform(AbstractGolemEntity<?, ?> golem, int level) {
		float max = golem.getMaxHealth();
		golem.heal(max * health() * level);
		var ins = golem.getEffect(ACCompatRegistry.EFF_ATOMIC.get());
		int t = ins == null ? 0 : ins.getDuration();
		golem.addEffect(new MobEffectInstance(ACCompatRegistry.EFF_ATOMIC.get(), t + time(), level));
		float f1 = 1 + (golem.getRandom().nextFloat() - golem.getRandom().nextFloat()) * 0.2F;
		golem.playSound(SoundEvents.IRON_GOLEM_REPAIR, 1, f1);
	}

	@Override
	public void onAiStep(AbstractGolemEntity<?, ?> golem, int level) {
		if (golem.tickCount % 5 != 0) return;
		float hp = golem.getHealth();
		float max = golem.getMaxHealth();
		if (max * (1 - health()) <= hp) return;
		ItemStack uranium = golem.getProjectile(ACCompatRegistry.DUMMY_URANIUM.asStack());
		if (uranium.isEmpty()) return;
		uranium.shrink(1);
		perform(golem, level);
	}

	@Override
	public InteractionResult interact(Player player, AbstractGolemEntity<?, ?> golem, InteractionHand hand, int level) {
		ItemStack stack = player.getItemInHand(hand);
		if (!ACCompatRegistry.DUMMY_URANIUM.get().isValid(stack)) return InteractionResult.PASS;
		float hp = golem.getHealth();
		float max = golem.getMaxHealth();
		if (max <= hp) return InteractionResult.FAIL;
		if (!player.level().isClientSide()) {
			if (!player.isCreative()) stack.shrink(1);
			perform(golem, level);
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public void onRegisterGoals(AbstractGolemEntity<?, ?> entity, int lv, BiConsumer<Integer, Goal> addGoal) {
		entity.effectImmunity.add(ACEffectRegistry.IRRADIATED.get());
		if (ModList.get().isLoaded(L2Complements.MODID)) {
			entity.effectImmunity.add(LCEffects.CURSE.get());
		}
	}

	@Override
	public List<MutableComponent> getDetail(int v) {
		var item = ACItemRegistry.URANIUM_SHARD.get().getDefaultInstance().getHoverName();
		var heal = Component.literal(Math.round(health() * 100 * v) + "%").withStyle(ChatFormatting.AQUA);
		var boost = Component.literal("20%").withStyle(ChatFormatting.AQUA);
		var time = Component.literal(time() / 20 + "").withStyle(ChatFormatting.AQUA);
		return List.of(Component.translatable(getDescriptionId() + ".desc", item, heal, boost, time).withStyle(ChatFormatting.GREEN));
	}


}
