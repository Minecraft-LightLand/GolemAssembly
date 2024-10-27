package dev.xkmc.modulargolems.compat.materials.alexscaves.modifier;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.data.MGConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.List;
import java.util.function.BiConsumer;

public class CandyShootModifier extends GolemModifier {

	public CandyShootModifier() {
		super(StatFilterType.HEAD, 1);
	}

	@Override
	public void onRegisterGoals(AbstractGolemEntity<?, ?> entity, int lv, BiConsumer<Integer, Goal> addGoal) {
		addGoal.accept(2, new GumShootAttackGoal(20, 2, 16, entity, lv));
	}

	@Override
	public List<MutableComponent> getDetail(int v) {
		int num = (int) Math.round(MGConfig.COMMON.candyDamage.get() * v);
		var val = Component.literal("" + num).withStyle(ChatFormatting.AQUA);
		return List.of(Component.translatable(getDescriptionId() + ".desc", val).withStyle(ChatFormatting.GREEN));
	}

}
