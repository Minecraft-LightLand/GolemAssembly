package dev.xkmc.modulargolems.content.item.golem;

import com.mojang.datafixers.util.Pair;
import com.tterrag.registrate.util.CreativeModeTabModifier;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.modulargolems.content.capability.GolemConfigStorage;
import dev.xkmc.modulargolems.content.config.GolemMaterial;
import dev.xkmc.modulargolems.content.config.GolemMaterialConfig;
import dev.xkmc.modulargolems.content.core.GolemType;
import dev.xkmc.modulargolems.content.core.IGolemPart;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.item.data.GolemConfigKey;
import dev.xkmc.modulargolems.content.item.data.GolemHolderMaterial;
import dev.xkmc.modulargolems.content.item.data.GolemIcon;
import dev.xkmc.modulargolems.content.item.data.GolemUpgrade;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.data.MGConfig;
import dev.xkmc.modulargolems.init.data.MGLangData;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class GolemHolder<T extends AbstractGolemEntity<T, P>, P extends IGolemPart<P>> extends Item {

	public static ArrayList<GolemMaterial> getMaterial(ItemStack stack) {
		var ans = GolemItems.HOLDER_MAT.get(stack);
		return ans == null ? new ArrayList<>() : ans.toList();
	}

	public static GolemUpgrade getUpgrades(ItemStack stack) {
		return GolemItems.UPGRADE.getOrDefault(stack, GolemUpgrade.EMPTY);
	}

	public static Optional<GolemConfigKey> getGolemConfig(ItemStack stack) {
		return Optional.ofNullable(GolemItems.CONFIG_KEY.get(stack));
	}

	public static void setGolemConfig(ItemStack stack, UUID id, int color) {
		GolemItems.CONFIG_KEY.set(stack, new GolemConfigKey(id, color));
	}

	public static <T extends AbstractGolemEntity<T, P>, P extends IGolemPart<P>> ItemStack setEntity(T entity) {
		GolemHolder<T, P> holder = GolemType.getGolemHolder(entity.getType());
		ItemStack stack = new ItemStack(holder);
		var config = entity.getConfigEntry(null);
		if (config != null) {
			setGolemConfig(stack, config.getID(), config.getColor());
		}
		GolemItems.HOLDER_MAT.set(stack, GolemHolderMaterial.parse(entity.getMaterials()));
		GolemItems.UPGRADE.set(stack, entity.getUpgrades());
		CustomData.update(GolemItems.ENTITY.get(), stack, entity::save);
		Optional.ofNullable(entity.getCustomName()).ifPresent(e -> stack.set(DataComponents.CUSTOM_NAME, e));
		return stack;
	}

	public static float getHealth(ItemStack stack) {
		return Optional.ofNullable(stack.get(GolemItems.ENTITY))
				.map(e -> e.getUnsafe()).map(e -> e.getFloat("Health")).orElse(-1f);
	}

	public static float getMaxHealth(ItemStack stack) {
		return Optional.ofNullable(stack.get(GolemItems.ENTITY))
				.map(e -> e.getUnsafe()).flatMap(e -> e.getList("attributes", Tag.TAG_COMPOUND).stream()
						.map(t -> ((CompoundTag) t))
						.filter(t -> t.getString("id").equals("minecraft:generic.max_health"))
						.findAny()).map(e -> e.getFloat("base")).orElse(-1f);
	}

	public static void setHealth(ItemStack result, float health) {
		CustomData.update(GolemItems.ENTITY.get(), result, e -> e.putFloat("Health", health));
	}

	public static ItemStack toEntityIcon(ItemStack golem, ItemStack... equipments) {
		return GolemItems.DC_ICON.set(golem, new GolemIcon(new ArrayList<>(List.of(equipments))));
	}

	private final Val<GolemType<T, P>> type;

	public GolemHolder(Properties props, Val<GolemType<T, P>> type) {
		super(props.stacksTo(1));
		this.type = type;
		GolemType.GOLEM_TYPE_TO_ITEM.put(type.id(), this);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
		var data = GolemItems.ENTITY.get(stack);
		if (data == null) return;
		if (entity.tickCount % 20 == 0) {
			var health = getHealth(stack);
			var maxHealth = getMaxHealth(stack);
			if (health > 0 && health < maxHealth) {
				var mats = getMaterial(stack);
				var upgrades = getUpgrades(stack);
				var attr = GolemMaterial.collectAttributes(mats, upgrades);
				var modifiers = GolemMaterial.collectModifiers(mats, upgrades);
				double heal = attr.getOrDefault(GolemTypes.GOLEM_REGEN.holder(), Pair.of(GolemTypes.STAT_REGEN.get(), 0d)).getSecond();
				var ctx = new GolemModifier.HealingContext(health, maxHealth, entity);
				for (var entry : modifiers.entrySet()) {
					heal = entry.getKey().onInventoryHealTick(heal, ctx, entry.getValue());
				}
				if (heal > 0) {
					setHealth(stack, Math.min(maxHealth, (float) heal + health));
				}
			}
		}
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
		Level level = player.level();
		Vec3 pos = target.position();
		if (summon(stack, level, pos, player, e -> e.checkRide(target))) {
			if (!level.isClientSide()) {
				player.setItemInHand(hand, ItemStack.EMPTY);
			}
			return InteractionResult.CONSUME;
		} else {
			return InteractionResult.FAIL;
		}
	}

	@Override
	public boolean canGrindstoneRepair(ItemStack stack) {
		return true;
	}

	@Override
	public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
		Level level = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		Direction direction = context.getClickedFace();
		BlockState blockstate = level.getBlockState(blockpos);
		BlockPos spawnPos;
		if (blockstate.getCollisionShape(level, blockpos).isEmpty()) {
			spawnPos = blockpos;
		} else {
			spawnPos = blockpos.relative(direction);
		}
		Vec3 pos = new Vec3(spawnPos.getX() + 0.5, spawnPos.getY() + 0.05, spawnPos.getZ() + 0.5);
		if (summon(stack, level, pos, context.getPlayer(), null)) {
			if (context.getPlayer() != null && !context.getLevel().isClientSide())
				context.getPlayer().setItemInHand(context.getHand(), ItemStack.EMPTY);
			return InteractionResult.CONSUME;
		} else {
			return InteractionResult.FAIL;
		}
	}

	public boolean summon(ItemStack stack, Level level, Vec3 pos, @Nullable Player player, @Nullable Consumer<AbstractGolemEntity<?, ?>> callback) {
		var data = GolemItems.ENTITY.get(stack);
		if (data != null && getMaxHealth(stack) >= 0) {
			if (getHealth(stack) <= 0)
				return false;
			if (!level.isClientSide()) {
				AbstractGolemEntity<?, ?> golem = type.get().create((ServerLevel) level, data.getUnsafe());
				UUID id = player == null ? null : player.getUUID();
				golem.updateAttributes(getMaterial(stack), getUpgrades(stack), id);
				golem.moveTo(pos);
				getGolemConfig(stack).ifPresent(e -> golem.setConfigCard(e.id(), e.color()));
				Optional.ofNullable(stack.get(DataComponents.CUSTOM_NAME)).ifPresent(golem::setCustomName);
				if (!golem.initMode(player)) {
					return false;
				}
				level.addFreshEntity(golem);
				stack.remove(GolemItems.ENTITY);
				stack.shrink(1);
				if (callback != null) {
					callback.accept(golem);
				}
			}
			return true;
		}
		var mat = GolemItems.HOLDER_MAT.get(stack);
		if (mat != null) {
			if (!level.isClientSide()) {
				AbstractGolemEntity<?, ?> golem = type.get().create(level);
				golem.moveTo(pos);
				UUID id = player == null ? null : player.getUUID();
				golem.onCreate(getMaterial(stack), getUpgrades(stack), id);
				getGolemConfig(stack).ifPresent(e -> golem.setConfigCard(e.id(), e.color()));
				Optional.ofNullable(stack.get(DataComponents.CUSTOM_NAME)).ifPresent(golem::setCustomName);
				if (!golem.initMode(player)) {
					return false;
				}
				level.addFreshEntity(golem);
				if (player == null || !player.getAbilities().instabuild) {
					stack.shrink(1);
				}
				if (callback != null) {
					callback.accept(golem);
				}
			}
			return true;
		}
		return false;
	}

	@Nullable
	public T createDummy(ItemStack stack, Level level) {
		T golem;
		var data = GolemItems.ENTITY.get(stack);
		var mat = GolemItems.HOLDER_MAT.get(stack);
		if (data != null) {
			golem = type.get().create((ServerLevel) level, data.getUnsafe());
			golem.updateAttributes(getMaterial(stack), getUpgrades(stack), null);
		} else if (mat != null) {
			golem = type.get().create(level);
			golem.onCreate(getMaterial(stack), getUpgrades(stack), null);
		} else return null;
		getGolemConfig(stack).ifPresent(e -> golem.setConfigCard(e.id(), e.color()));
		Optional.ofNullable(stack.get(DataComponents.CUSTOM_NAME)).ifPresent(golem::setCustomName);
		return golem;
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		if (GolemItems.DC_DISP_HP.get(stack) != null)
			return true;
		return getMaxHealth(stack) >= 0;
	}

	@Override
	public int getBarColor(ItemStack stack) {
		float f;
		Double hp = GolemItems.DC_DISP_HP.get(stack);
		if (hp != null) f = hp.floatValue();
		else {
			float health = getHealth(stack);
			float maxHealth = getMaxHealth(stack);
			f = Mth.clamp(health / maxHealth, 0f, 1f);
		}
		return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		Double hp = GolemItems.DC_DISP_HP.get(stack);
		if (hp != null) {
			return Math.round(hp.floatValue() * 13.0F);
		}
		return Math.round(Mth.clamp(getHealth(stack) / getMaxHealth(stack), 0, 1) * 13.0F);
	}

	public GolemType<T, P> getEntityType() {
		return type.get();
	}

	public void fillItemCategory(CreativeModeTabModifier tab) {
		for (ResourceLocation rl : GolemMaterialConfig.get().getAllMaterials()) {
			ItemStack stack = new ItemStack(this);
			ArrayList<GolemHolderMaterial.Entry> mats = new ArrayList<>();
			for (P part : getEntityType().values()) {
				mats.add(new GolemHolderMaterial.Entry(part.toItem(), rl));
			}
			tab.accept(GolemItems.HOLDER_MAT.set(stack, new GolemHolderMaterial(mats)));
		}
	}

	public ItemStack withUniformMaterial(ResourceLocation rl) {
		ItemStack stack = new ItemStack(this);
		ArrayList<GolemHolderMaterial.Entry> list = new ArrayList<>();
		for (P part : getEntityType().values()) {
			list.add(new GolemHolderMaterial.Entry(part.toItem(), rl));
		}
		return GolemItems.HOLDER_MAT.set(stack, new GolemHolderMaterial(list));
	}

	public int getRemaining(ArrayList<GolemMaterial> mats, GolemUpgrade upgrades) {
		int base = getEntityType().values().length;
		if (type.get() == GolemTypes.TYPE_GOLEM.get()) {
			base = MGConfig.COMMON.largeGolemSlot.get();
		} else if (type.get() == GolemTypes.TYPE_HUMANOID.get()) {
			base = MGConfig.COMMON.humanoidGolemSlot.get();
		} else if (type.get() == GolemTypes.TYPE_DOG.get()) {
			base = MGConfig.COMMON.dogGolemSlot.get();
		}
		base -= upgrades.upgrades().size();
		var modifiers = GolemMaterial.collectModifiers(mats, upgrades);
		var list = upgrades.upgradeItems();
		for (var ent : modifiers.entrySet()) {
			base += ent.getKey().addSlot(list, ent.getValue());
		}
		return base;
	}

	@Override
	public void onDestroyed(ItemEntity entity, DamageSource source) {
		if (source.is(DamageTypeTags.IS_EXPLOSION)) {
			for (var e : getUpgrades(entity.getItem()).upgrades()) {
				entity.level().addFreshEntity(new ItemEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ(), e.getDefaultInstance()));
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> list, TooltipFlag flag) {
		if (flag.hasAltDown()) {
			NBTAnalytic.analyze(stack, list);
			return;
		}
		var level = ctx.level();
		if (!flag.hasShiftDown()) {
			float max = getMaxHealth(stack);
			if (max >= 0) {
				float health = getHealth(stack);
				float f = Mth.clamp(health / max, 0f, 1f);
				int color = Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
				MutableComponent hc = Component.literal("" + Math.round(health)).setStyle(Style.EMPTY.withColor(color));
				list.add(MGLangData.HEALTH.get(hc, Math.round(max)).withStyle(health <= 0 ? ChatFormatting.RED : ChatFormatting.AQUA));
			}
			var config = getGolemConfig(stack);

			if (level == null || config.isEmpty()) {
				list.add(MGLangData.NO_CONFIG.get());
			} else {
				var id = config.get().id();
				var color = config.get().color();
				var entry = GolemConfigStorage.get(level)
						.getOrCreateStorage(id, color, MGLangData.LOADING.get());
				entry.clientTick(level, false);
				list.add(entry.getDisplayName());
			}
			var mats = getMaterial(stack);
			var upgrades = getUpgrades(stack);
			var parts = getEntityType().values();
			if (mats.size() == parts.length) {
				for (int i = 0; i < parts.length; i++) {
					list.add(parts[i].getDesc(mats.get(i).getDesc()));
				}
			}
			list.add(MGLangData.SLOT.get(getRemaining(mats, upgrades)).withStyle(ChatFormatting.AQUA));
			var modifiers = GolemMaterial.collectModifiers(mats, upgrades);
			if (modifiers.size() > 8) {
				list.add(MGLangData.UPGRADE_COUNT.get(modifiers.size(), upgrades.upgrades().size()));
			} else {
				modifiers.forEach((k, v) -> list.add(k.getTooltip(v)));
			}
			GolemMaterial.collectAttributes(mats, upgrades).forEach((k, v) -> {
				if (Math.abs(v.getSecond()) > 1e-3) {
					list.add(v.getFirst().getTotalTooltip(v.getSecond()));
				}
			});
			list.add(MGLangData.SHIFT.get());
		} else {
			var mats = getMaterial(stack);
			var upgrades = getUpgrades(stack);
			var map = GolemMaterial.collectModifiers(mats, upgrades);
			int size = map.size();
			int index = 0;
			for (var entry : map.entrySet()) {
				index++;
				var k = entry.getKey();
				var v = entry.getValue();
				list.add(k.getTooltip(v));
				if (size > 12) {
					continue;
				}
				if (size > 4 && level != null) {
					if (level.getGameTime() / 30 % size != index - 1) continue;
				}
				list.addAll(k.getDetail(v));
			}
		}
	}

}
