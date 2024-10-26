package dev.xkmc.modulargolems.compat.materials.alexscaves.modifier;

import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class RadiationModifier extends GolemModifier {

	private static int time() {
		return 200;//TODO
	}

	public RadiationModifier() {
		super(StatFilterType.ATTACK, 2);
	}

	@Override
	public void onHurtTarget(AbstractGolemEntity<?, ?> entity, LivingHurtEvent event, int level) {
		event.getEntity().addEffect(new MobEffectInstance(
				ACEffectRegistry.IRRADIATED.get(), time(), level * 2 - 1
		));
	}

}
