package dev.xkmc.modulargolems.content.client.override;

import it.unimi.dsi.fastutil.objects.Object2BooleanLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

public class ModelOverrides {

	private static final HashMap<ResourceLocation, ModelOverride> OVERRIDES = new HashMap<>();
	private static final Object2BooleanMap<ResourceLocation> EMISSIVE = new Object2BooleanLinkedOpenHashMap<>();

	public static synchronized void registerOverride(ResourceLocation id, ModelOverride override) {
		OVERRIDES.put(id, override);
	}

	public static synchronized void reload() {
		EMISSIVE.clear();
	}

	public static synchronized boolean isValid(ResourceLocation id) {
		if (!EMISSIVE.containsKey(id)) {
			boolean present = Minecraft.getInstance().getResourceManager().getResource(id).isPresent();
			EMISSIVE.put(id, present);
			return present;
		}
		return EMISSIVE.getBoolean(id);
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
