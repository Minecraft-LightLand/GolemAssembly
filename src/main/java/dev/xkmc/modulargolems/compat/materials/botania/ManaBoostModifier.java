package dev.xkmc.modulargolems.compat.materials.botania;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.init.data.MGConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;

public class ManaBoostModifier extends ManaModifier {

	public ManaBoostModifier() {
		super(StatFilterType.ATTACK, MAX_LEVEL);
	}

	@Override
	public void modifyDamage(AttackCache cache, AbstractGolemEntity<?, ?> entity, int level) {
		int manaCost = MGConfig.COMMON.manaBoostingCost.get() * level;
		double damageBoost = MGConfig.COMMON.manaBoostingDamage.get() * level;
		int consumed = new BotUtils(entity).consumeMana(manaCost);
		if (consumed <= 0) return;
		if (consumed < manaCost) damageBoost = damageBoost * consumed / manaCost;
		cache.addHurtModifier(DamageModifier.multTotal(1 + (float) damageBoost));

	}

	public List<MutableComponent> getDetail(int v) {
		int bonus = (int) Math.round((MGConfig.COMMON.manaBoostingDamage.get() * v) * 100);
		int manaCost = MGConfig.COMMON.manaBoostingCost.get() * v;
		return List.of(Component.translatable(getDescriptionId() + ".desc", bonus, manaCost).withStyle(ChatFormatting.GREEN));
	}

}
