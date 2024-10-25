package dev.xkmc.modulargolems.compat.materials.botania;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.init.data.MGConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.List;
import java.util.function.BiConsumer;

public class ManaBurstModifier extends ManaModifier {

	public ManaBurstModifier() {
		super(StatFilterType.ATTACK, MAX_LEVEL);
	}

	@Override
	public void onRegisterGoals(AbstractGolemEntity<?, ?> entity, int lv, BiConsumer<Integer, Goal> addGoal) {
		addGoal.accept(5, new ManaBurstAttackGoal(entity, lv));
	}

	public List<MutableComponent> getDetail(int v) {
		int prob = (int) Math.round((MGConfig.COMMON.manaBurstDamage.get() * v) * 100);
		int manaCost = MGConfig.COMMON.manaBurstCost.get() * v;
		return List.of(Component.translatable(getDescriptionId() + ".desc", prob, manaCost).withStyle(ChatFormatting.GREEN));
	}

}
