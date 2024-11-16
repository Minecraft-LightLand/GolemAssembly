package dev.xkmc.modulargolems.compat.materials.l2hostility;

import dev.xkmc.l2core.serial.config.ConfigDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;

import java.util.concurrent.CompletableFuture;

public class LHConfigGen extends ConfigDataProvider {

	public LHConfigGen(DataGenerator generator, CompletableFuture<HolderLookup.Provider> pvd) {
		super(generator, pvd, "L2Hostility config provider");
	}

	@Override
	public void add(Collector collector) {

	}

}
