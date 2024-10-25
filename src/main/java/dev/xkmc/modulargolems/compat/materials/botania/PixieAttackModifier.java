package dev.xkmc.modulargolems.compat.materials.botania;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.data.MGConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import vazkii.botania.common.entity.PixieEntity;

import java.util.List;

public class PixieAttackModifier extends GolemModifier {

	public PixieAttackModifier() {
		super(StatFilterType.ATTACK, MAX_LEVEL);
	}

	@Override
	public void onHurtTarget(AbstractGolemEntity<?, ?> entity, LivingHurtEvent event, int level) {
		double prob = MGConfig.COMMON.pixieCounterattackProb.get() * level;
		if (entity.getRandom().nextDouble() < prob) {
			PixieEntity pixie = new PixieEntity(entity.level());
			pixie.setPos(entity.getX(), entity.getY() + 2, entity.getZ());
			float dmg = 4 + 2 * level;
			pixie.setProps(event.getEntity(), entity, 0, dmg);
			pixie.finalizeSpawn((ServerLevelAccessor) entity.level(), entity.level().getCurrentDifficultyAt(pixie.blockPosition()),
					MobSpawnType.EVENT, null, null);
			entity.level().addFreshEntity(pixie);
		}
	}

	public List<MutableComponent> getDetail(int v) {
		int prob = (int) Math.round((MGConfig.COMMON.pixieAttackProb.get() * v) * 100);
		double extraDmg = 2 * v;
		return List.of(Component.translatable(getDescriptionId() + ".desc", prob, extraDmg).withStyle(ChatFormatting.GREEN));
	}

}
