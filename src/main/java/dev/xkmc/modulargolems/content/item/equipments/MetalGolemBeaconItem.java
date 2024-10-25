package dev.xkmc.modulargolems.content.item.equipments;

import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class MetalGolemBeaconItem extends GolemEquipmentItem implements TickEquipmentItem {
	private final int beaconLevel;

	public MetalGolemBeaconItem(Properties properties, int beaconLevel) {
		super(properties, EquipmentSlot.FEET, GolemTypes.ENTITY_GOLEM::get, builder -> {
		});
		this.beaconLevel = beaconLevel;
	}

	public int getBeaconLevel() {
		return this.beaconLevel;
	}

	@Override
	public void tick(ItemStack stack, Level level, Entity entity) {
		if (level.getGameTime() % 80D != 0)
			return;
		if (stack.getItem() instanceof MetalGolemBeaconItem beacon && entity instanceof AbstractGolemEntity<?, ?> golem) {
			double range = this.getBeaconLevel() * 10D + 10D;
			int time = (9 + this.getBeaconLevel() * 2) * 20;
			AABB aabb = golem.getBoundingBox().inflate(range).expandTowards(0D, level.getHeight(), 0D);
			for (LivingEntity entity1 : level.getEntitiesOfClass(LivingEntity.class, aabb)) {
				if (entity1 == golem.getOwner()) {
					entity1.addEffect(new MobEffectInstance(MobEffects.REGENERATION, time, this.getBeaconLevel() - 1, true, true));
				} else if (entity1 instanceof OwnableEntity ownable) {
					if (golem.getOwner() == ownable.getOwner())
						entity1.addEffect(new MobEffectInstance(MobEffects.REGENERATION, time, this.getBeaconLevel() - 1, true, true));
				}
			}
		}
	}
}
