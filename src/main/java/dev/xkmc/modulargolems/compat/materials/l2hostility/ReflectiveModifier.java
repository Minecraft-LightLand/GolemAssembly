package dev.xkmc.modulargolems.compat.materials.l2hostility;

import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;

public class ReflectiveModifier extends GolemModifier {

	public ReflectiveModifier(StatFilterType type, int maxLevel) {
		super(type, maxLevel);
	}

	@Override
	public void onDamaged(AbstractGolemEntity<?, ?> entity, DamageData.Defence event, int level) {
		LHTraits.REFLECT.get().onHurtByMax(level, entity, event);
	}

	@Override
	public List<MutableComponent> getDetail(int v) {
		List<MutableComponent> ans = new ArrayList<>();
		ans.add(Component.translatable(LHTraits.REFLECT.get().getDescriptionId() + ".desc",
						Component.literal((int) Math.round(100 * (1 + v * LHConfig.SERVER.reflectFactor.get())) + "")
								.withStyle(ChatFormatting.AQUA))
				.withStyle(ChatFormatting.GREEN));
		return ans;
	}

}
