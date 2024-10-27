package dev.xkmc.modulargolems.compat.materials.alexscaves.modifier;

import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.data.MGConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.StringUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;

public class RadiationModifier extends GolemModifier {

	private static int time() {
		return MGConfig.COMMON.radiationDuration.get();
	}

	private static float atk() {
		return (float) (double) MGConfig.COMMON.radiationDamage.get();
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

	@Override
	public void modifyDamage(AttackCache cache, AbstractGolemEntity<?, ?> entity, int level) {
		var ins = cache.getAttackTarget().getEffect(ACEffectRegistry.IRRADIATED.get());
		if (ins == null) return;
		cache.addHurtModifier(DamageModifier.multTotal(1 + (ins.getAmplifier() + 1) * atk()));
	}

	@Override
	public List<MutableComponent> getDetail(int v) {
		var val = Component.translatable(ACEffectRegistry.IRRADIATED.get().getDescriptionId());
		val = Component.translatable("potion.withAmplifier", val, Component.translatable("potion.potency." + (v * 2 - 1)));
		val = Component.translatable("potion.withDuration", val, Component.literal(StringUtil.formatTickDuration(time())));
		val = val.withStyle(ChatFormatting.RED);
		var atk = Component.literal(Math.round(atk() * 100) + "%").withStyle(ChatFormatting.AQUA);
		return List.of(Component.translatable(getDescriptionId() + ".desc", val, atk).withStyle(ChatFormatting.GREEN));
	}

}
