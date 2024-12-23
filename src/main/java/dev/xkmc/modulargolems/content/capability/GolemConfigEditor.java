package dev.xkmc.modulargolems.content.capability;

import dev.xkmc.l2core.util.Proxy;
import dev.xkmc.modulargolems.content.entity.mode.GolemMode;
import dev.xkmc.modulargolems.content.entity.mode.GolemModes;
import dev.xkmc.modulargolems.init.data.MGLangData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import java.util.UUID;

public interface GolemConfigEditor {

	static GolemConfigEditor readable(UUID id, int color) {
		return new Readable(Proxy.getLevel(), id, color);
	}

	Level level();

	GolemConfigEntry entry();

	default Component getDisplayName() {
		return entry().getDisplayName();
	}

	default GolemMode getDefaultMode() {
		return GolemModes.get(entry().defaultMode);
	}

	default void setDefaultMode(GolemMode mode) {
		entry().defaultMode = mode.getID();
		sync();
	}

	default boolean summonToPosition() {
		return entry().summonToPosition;
	}

	default void setSummonToPosition(boolean bool) {
		entry().summonToPosition = bool;
		sync();
	}

	default boolean locked() {
		return entry().locked;
	}

	default void setLocked(boolean lock) {
		entry().locked = lock;
		sync();
	}

	default void sync() {
		entry().sync(level());
	}

	default PickupFilterEditor getFilter() {
		return new PickupFilterEditor(this);
	}

	default TargetFilterEditor target() {
		return new TargetFilterEditor(this);
	}

	default SquadEditor getSquad() {
		return new SquadEditor(this);
	}

	default PathEditor getPath() {
		return new PathEditor(this);
	}

	record Writable(Level level, GolemConfigEntry entry) implements GolemConfigEditor {

	}

	record Readable(Level level, UUID id, int color) implements GolemConfigEditor {

		@Override
		public GolemConfigEntry entry() {
			return GolemConfigStorage.get(level).getOrCreateStorage(id, color, MGLangData.LOADING.get());
		}

	}

}
