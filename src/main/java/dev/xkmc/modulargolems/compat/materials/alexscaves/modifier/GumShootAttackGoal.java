package dev.xkmc.modulargolems.compat.materials.alexscaves.modifier;

import com.github.alexmodguy.alexscaves.server.entity.item.GumballEntity;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.ranged.GolemShooterHelper;
import dev.xkmc.modulargolems.content.modifier.special.BaseRangedAttackGoal;
import dev.xkmc.modulargolems.init.data.MGConfig;
import net.minecraft.world.entity.LivingEntity;

public class GumShootAttackGoal extends BaseRangedAttackGoal {

	public GumShootAttackGoal(int waitTime, int near, int far, AbstractGolemEntity<?, ?> golem, int lv) {
		super(waitTime, near, far, golem, lv);
	}

	@Override
	protected void performAttack(LivingEntity target) {
		var proj = new GumballEntity(target.level(), golem);
		proj.setPos(golem.getEyePosition());
		proj.setDamage((float) (MGConfig.COMMON.candyDamage.get() * lv));
		GolemShooterHelper.shootAimHelper(target, proj, 1.5, 0.08);
		golem.level().addFreshEntity(proj);
	}
}
