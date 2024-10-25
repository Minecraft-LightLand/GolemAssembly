package dev.xkmc.modulargolems.content.entity.humanoid.skin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.SkullBlockEntity;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.TreeMap;

public class ClientProfileManager {

	private static final Map<String, GameProfile> CACHE = new TreeMap<>();

	@Nullable
	public static SpecialRenderProfile get(String name) {
		var profile = getProfile(name);
		if (profile == null) return null;
		var skins = Minecraft.getInstance().getSkinManager();
		var skin = skins.getInsecureSkinInformation(profile).get(MinecraftProfileTexture.Type.SKIN);
		if (skin == null) return null;
		var skinModel = skin.getMetadata("model");
		boolean slim = skinModel != null && skinModel.equals("slim");
		ResourceLocation texture = skins.getInsecureSkinLocation(profile);
		if (texture.equals(new ResourceLocation("missingno")))
			return null;
		return new SpecialRenderProfile(slim, texture);
	}

	@Nullable
	private static GameProfile getProfile(String name) {
		if (!CACHE.containsKey(name)) {
			CACHE.put(name, null);
			SkullBlockEntity.updateGameprofile(new GameProfile(null, name), e -> CACHE.put(name, e));
		}
		return CACHE.get(name);
	}

}
