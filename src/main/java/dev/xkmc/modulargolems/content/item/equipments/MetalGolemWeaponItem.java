package dev.xkmc.modulargolems.content.item.equipments;

import dev.xkmc.modulargolems.init.material.GolemWeaponType;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.common.ForgeMod;

public class MetalGolemWeaponItem extends GolemEquipmentItem {
	protected final GolemWeaponType gwt;
	public MetalGolemWeaponItem(Properties properties, int attackDamage, double percentAttack, float range, float sweep, GolemWeaponType gwt) {
		super(properties, EquipmentSlot.MAINHAND, GolemTypes.ENTITY_GOLEM::get, builder -> {
			var uuid = UUID.get(EquipmentSlot.MAINHAND);
			if (attackDamage > 0) {
				builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(uuid, "Weapon modifier", attackDamage, AttributeModifier.Operation.ADDITION));
			}
			if (percentAttack > 0) {
				builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(uuid, "Weapon modifier", percentAttack, AttributeModifier.Operation.MULTIPLY_BASE));
			}
			if (range > 0) {
				builder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(uuid, "spear_range", range, AttributeModifier.Operation.ADDITION));
			}
			if (sweep > 0) {
				builder.put(GolemTypes.GOLEM_SWEEP.get(), new AttributeModifier(uuid, "spear_sweep", sweep, AttributeModifier.Operation.ADDITION));
			}
		});
		this.gwt = gwt;
	}
	@Override
	public boolean isEnchantable(ItemStack stack) {
		return true;
	}
	@Override
	public int getEnchantmentValue() {
		return 15;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		if (enchantment.category == EnchantmentCategory.WEAPON) {
			return true;
		}
		return super.canApplyAtEnchantingTable(stack, enchantment);
	}
	public GolemWeaponType getGolemWeaponType(MetalGolemWeaponItem i) {
      return i.gwt;
	}
}
