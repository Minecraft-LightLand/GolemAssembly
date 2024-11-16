package dev.xkmc.modulargolems.events;

import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.common.GolemFlags;
import dev.xkmc.modulargolems.content.item.card.ClickEntityFilterCard;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.data.MGConfig;
import dev.xkmc.modulargolems.init.registrate.GolemModifiers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;

@EventBusSubscriber(modid = ModularGolems.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ModifierEventListeners {

	@SubscribeEvent
	public static void onGolemSpawn(EntityJoinLevelEvent event) {
		if (event.getLevel().isClientSide()) return;
		if (event.getEntity() instanceof AbstractGolemEntity<?, ?> entity) {
			entity.getModifiers().forEach((k, v) -> k.onGolemSpawn(entity, v));
		}
	}

	@SubscribeEvent
	public static void onExplosion(ExplosionEvent.Detonate event) {
		for (var e : event.getAffectedEntities()) {
			if (e instanceof AbstractGolemEntity<?, ?> golem) {
				if (golem.getModifiers().getOrDefault(GolemModifiers.EXPLOSION_RES.get(), 0) > 0) {
					event.getAffectedBlocks().clear();
					return;
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
		if (event.getLevel().isClientSide()) return;
		if (!(event.getEntity() instanceof Mob mob)) return;
		if (!MGConfig.COMMON.doEnemyAggro.get()) return;
		if (!(mob instanceof Enemy) || mob instanceof Creeper) return;
		int priority = 0;
		TargetGoal ans = null;
		for (var goal : mob.targetSelector.getAvailableGoals()) {
			if (goal.getGoal() instanceof NearestAttackableTargetGoal<?> target) {
				if (target.targetType == IronGolem.class) {
					priority = goal.getPriority();
					ans = new NearestAttackableTargetGoal<>(mob, AbstractGolemEntity.class,
							target.randomInterval, target.mustSee, target.mustReach, null);
					break;
				}
			}
		}
		if (ans != null) {
			mob.targetSelector.addGoal(priority, ans);
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onLivingDrop(LivingDropsEvent event) {
		if (event.getSource().getEntity() instanceof AbstractGolemEntity<?, ?> e) {
			if (e.hasFlag(GolemFlags.PICKUP)) {
				event.getDrops().forEach(x -> x.moveTo(e.position()));
			}
		}
	}

	@SubscribeEvent
	public static void onTargetCardClick(PlayerInteractEvent.EntityInteract event) {
		if (event.getItemStack().getItem() instanceof ClickEntityFilterCard<?>) {
			if (event.getTarget() instanceof LivingEntity le) {
				event.setCancellationResult(event.getItemStack().interactLivingEntity(event.getEntity(),
						le, event.getHand()));
				event.setCanceled(true);
			}
		}
	}

}
