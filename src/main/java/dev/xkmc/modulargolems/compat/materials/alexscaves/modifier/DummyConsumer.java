package dev.xkmc.modulargolems.compat.materials.alexscaves.modifier;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;

import java.util.function.Predicate;

public class DummyConsumer extends ProjectileWeaponItem {

	private final TagKey<Item> ammo;

	public DummyConsumer(TagKey<Item> ammo) {
		super(new Properties());
		this.ammo = ammo;
	}

	public boolean isValid(ItemStack stack) {
		return stack.is(ammo);
	}

	@Override
	public Predicate<ItemStack> getAllSupportedProjectiles() {
		return this::isValid;
	}

	@Override
	public int getDefaultProjectileRange() {
		return 0;
	}

}
