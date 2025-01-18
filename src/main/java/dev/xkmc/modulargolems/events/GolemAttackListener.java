package dev.xkmc.modulargolems.events;

import dev.xkmc.l2damagetracker.contents.attack.AttackListener;
import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import net.minecraft.tags.DamageTypeTags;

public class GolemAttackListener implements AttackListener {

	@Override
	public void onCreateSource(CreateSourceEvent event) {
		if (event.getAttacker() instanceof AbstractGolemEntity<?, ?> golem) {
			for (var e : golem.getModifiers().entrySet()) {
				e.getKey().modifySource(golem, event, e.getValue());
			}
		}
	}

	@Override
	public boolean onAttack(DamageData.Attack data) {
		if (data.getTarget() instanceof AbstractGolemEntity<?, ?> golem) {
			if (data.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY)) return false;
			for (var e : golem.getModifiers().entrySet()) {
				if (e.getKey().onAttacked(golem, data, e.getValue())) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void onHurt(DamageData.Offence data) {
		if (data.getAttacker() instanceof AbstractGolemEntity<?, ?> golem) {
			for (var entry : golem.getModifiers().entrySet()) {
				entry.getKey().onHurtTarget(golem, data, entry.getValue());
			}
		}
	}

	@Override
	public void onHurtMaximized(DamageData.OffenceMax data) {

	}

	@Override
	public void onDamage(DamageData.Defence data) {
		if (data.getTarget() instanceof AbstractGolemEntity<?, ?> golem) {
			if (data.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY)) return;
			for (var e : golem.getModifiers().entrySet()) {
				e.getKey().onDamaged(golem, data, e.getValue());
			}
		}
	}

	@Override
	public void onDamageFinalized(DamageData.DefenceMax data) {
		if (data.getAttacker() instanceof AbstractGolemEntity<?, ?> golem) {
			for (var entry : golem.getModifiers().entrySet()) {
				entry.getKey().postHurtTarget(golem, data, entry.getValue());
			}
			var owner = golem.getOwner();
			if (owner != null) {
				data.getTarget().setLastHurtByPlayer(owner);
			}
		}
	}

}
