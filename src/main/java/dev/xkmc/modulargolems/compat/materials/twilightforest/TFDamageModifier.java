package dev.xkmc.modulargolems.compat.materials.twilightforest;

import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.data.MGConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import twilightforest.init.TFDimension;

import java.util.List;

public class TFDamageModifier extends GolemModifier {

	public TFDamageModifier() {
		super(StatFilterType.ATTACK, MAX_LEVEL);
	}

	@Override
	public void onHurtTarget(AbstractGolemEntity<?, ?> entity, DamageData.Offence cache, int level) {
		if (entity.level().dimension().equals(TFDimension.DIMENSION_KEY)) {
			double dmg = MGConfig.COMMON.compatTFDamage.get() * level;
			cache.addHurtModifier(DamageModifier.multTotal(1 + (float) dmg, getRegistryName()));
		}

	}

	public List<MutableComponent> getDetail(int v) {
		int bonus = (int) Math.round((MGConfig.COMMON.compatTFDamage.get() * v) * 100);
		return List.of(Component.translatable(getDescriptionId() + ".desc", bonus).withStyle(ChatFormatting.GREEN));
	}

}
