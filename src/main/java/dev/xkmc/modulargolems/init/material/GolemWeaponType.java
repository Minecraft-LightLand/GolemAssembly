package dev.xkmc.modulargolems.init.material;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.modulargolems.content.item.equipments.MetalGolemWeaponItem;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.Locale;

import static dev.xkmc.modulargolems.init.ModularGolems.REGISTRATE;
interface MGFunction{
	MetalGolemWeaponItem create(Item.Properties properties, int rank, GolemWeaponType type);
}
public enum GolemWeaponType {
	SPEAR("item/long_weapon", (p, i,t) -> new MetalGolemWeaponItem(p, i, 0, 2, 0,t),"TII", " SI", "S T"),
	AXE("item/battle_axe", (p, i,t) -> new MetalGolemWeaponItem(p, 0, i * 0.05, 0, 2,t), "III", "IS ", "TST"),
	SWORD("item/sword", (p, i,t) -> new MetalGolemWeaponItem(p, i, 0, 1, 2,t), "TII", "ISI", "SIT"),;

	private final MGFunction function;
	private final String[] pattern;
	private final String model;

	GolemWeaponType(String model, MGFunction function, String... pattern) {
		this.model = model;
		this.function = function;
		this.pattern = pattern;
	}

	public String getName() {
		return "golem_" + name().toLowerCase(Locale.ROOT);
	}

	public ItemEntry<MetalGolemWeaponItem> buildItem(IGolemWeaponMaterial material) {
		return REGISTRATE.item(material.getName() + "_" + getName(), p -> function.create(material.modify(p.stacksTo(1)), material.getDamage(),this))
				.model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile(pvd.modLoc(model)))
						.texture("layer0", pvd.modLoc("item/equipments/" + ctx.getName())))
				.defaultLang().register();
	}

	public static ItemEntry<MetalGolemWeaponItem>[][] build(IGolemWeaponMaterial[] values) {
		ItemEntry<MetalGolemWeaponItem>[][] ans = new ItemEntry[GolemWeaponType.values().length][values.length];
		for (int i = 0; i < GolemWeaponType.values().length; i++) {
			GolemWeaponType type = GolemWeaponType.values()[i];
			for (int j = 0; j < values.length; j++) {
				IGolemWeaponMaterial mat = values[j];
				ans[i][j] = type.buildItem(mat);
			}
		}
		return ans;
	}

	public ShapedRecipeBuilder pattern(ShapedRecipeBuilder unlock) {
		for (String str : pattern) {
			unlock.pattern(str);
		}
		return unlock;
	}
}
