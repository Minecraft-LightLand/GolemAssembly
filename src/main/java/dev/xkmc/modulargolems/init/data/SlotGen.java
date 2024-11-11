package dev.xkmc.modulargolems.init.data;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import top.theillusivec4.curios.api.CuriosDataProvider;

import java.util.concurrent.CompletableFuture;

public class SlotGen extends CuriosDataProvider {

	public SlotGen(PackOutput output, ExistingFileHelper helper, CompletableFuture<HolderLookup.Provider> pvd) {
		super(ModularGolems.MODID, output, helper, pvd);
	}

	@Override
	public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper) {
		createSlot("golem_skin").icon(ModularGolems.loc("slot/empty_skin_slot")).order(1000);
		createSlot("golem_route").icon(ModularGolems.loc("slot/empty_route_slot")).order(1100);

		createEntities("golem_skin").addSlots("golem_skin").addEntities(GolemTypes.TYPE_HUMANOID.get().type());
		createEntities("golem_curios").addSlots("head", "back", "ring", "charm", "hands", "golem_route")
				.addEntities(GolemTypes.TYPE_GOLEM.get().type(),
						GolemTypes.TYPE_HUMANOID.get().type(),
						GolemTypes.TYPE_DOG.get().type());

		createEntities("golem_artifacts").addSlots("artifact_head", "artifact_necklace", "artifact_bracelet", "artifact_body", "artifact_belt")
				.addEntities(GolemTypes.TYPE_GOLEM.get().type(),
						GolemTypes.TYPE_HUMANOID.get().type(),
						GolemTypes.TYPE_DOG.get().type())
				.addCondition(new ModLoadedCondition("l2artifacts"));

		createEntities("maid_curios").addSlots("head", "back", "ring", "charm", "hands")
				.addEntities(InitEntities.MAID.get())
				.addCondition(new ModLoadedCondition(TouhouLittleMaid.MOD_ID));

		createEntities("maid_artifacts").addSlots("artifact_head", "artifact_necklace", "artifact_bracelet", "artifact_body", "artifact_belt")
				.addEntities(InitEntities.MAID.get())
				.addCondition(new ModLoadedCondition(TouhouLittleMaid.MOD_ID))
				.addCondition(new ModLoadedCondition("l2artifacts"));
	}
}
