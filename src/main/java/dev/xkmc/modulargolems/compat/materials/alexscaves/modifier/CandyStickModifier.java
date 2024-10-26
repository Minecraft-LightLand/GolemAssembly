package dev.xkmc.modulargolems.compat.materials.alexscaves.modifier;

import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.MeltedCaramelEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class CandyStickModifier extends GolemModifier {

	private static int time() {
		return 100;//TODO
	}

	public CandyStickModifier() {
		super(StatFilterType.ATTACK, 2);
	}

	@Override
	public void onHurtTarget(AbstractGolemEntity<?, ?> entity, LivingHurtEvent event, int level) {
		var target = event.getEntity();
		var caramel = new MeltedCaramelEntity(ACEntityRegistry.MELTED_CARAMEL.get(), entity.level());
		Vec3 vec3 = new Vec3(target.getX(), target.getY() + 0.02, target.getZ());
		caramel.setPos(ACMath.getGroundBelowPosition(entity.level(), vec3));
		caramel.setDespawnsIn(time() * level);
		entity.level().addFreshEntity(caramel);
	}

}
