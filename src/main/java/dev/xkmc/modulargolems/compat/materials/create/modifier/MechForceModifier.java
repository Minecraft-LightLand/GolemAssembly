package dev.xkmc.modulargolems.compat.materials.create.modifier;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.data.MGConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;

public class MechForceModifier extends GolemModifier {

	public MechForceModifier() {
		super(StatFilterType.ATTACK, 5);
	}

	public List<MutableComponent> getDetail(int v) {
		int reduce = (int) Math.round(v * MGConfig.COMMON.mechAttack.get() * 100);
		return List.of(Component.translatable(getDescriptionId() + ".desc", reduce).withStyle(ChatFormatting.GREEN));
	}

}
