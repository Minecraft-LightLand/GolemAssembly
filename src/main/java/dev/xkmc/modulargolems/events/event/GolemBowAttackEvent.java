package dev.xkmc.modulargolems.events.event;

import dev.xkmc.modulargolems.content.entity.humanoid.HumanoidGolemEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;

public class GolemBowAttackEvent extends GolemItemUseEvent {

	private Projectile entity;
	private double speed = 3, gravity = 0.05;

	public GolemBowAttackEvent(HumanoidGolemEntity golem, ItemStack stack, InteractionHand hand, Projectile entity) {
		super(golem, stack, hand);
		this.entity = entity;
	}

	public void setArrow(Projectile entity) {
		this.entity = entity;
	}

	public void setParams(double speed, double gravity) {
		this.speed = speed;
		this.gravity = gravity;
	}

	public Projectile getArrow() {
		return entity;
	}

	public double speed() {
		return speed;
	}

	public double gravity() {
		return gravity;
	}

}
