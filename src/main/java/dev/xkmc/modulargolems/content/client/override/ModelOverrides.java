package dev.xkmc.modulargolems.content.client.override;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

public class ModelOverrides {

	private static final HashMap<ResourceLocation, ModelOverride> OVERRIDES = new HashMap<>();

	public static synchronized void registerOverride(ResourceLocation id, ModelOverride override) {
		OVERRIDES.put(id, override);
	}

	public static synchronized void reload() {
		for (var e : OVERRIDES.values()) {
			e.clear();
		}
	}

	public static synchronized ModelOverride getOverride(ResourceLocation id) {
		var ans = OVERRIDES.get(id);
		if (ans == null) {
			ans = new ModelOverride();
			registerOverride(id, ans);
		}
		return ans;
	}

}
