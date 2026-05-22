package com.ordana.spelunkery.items;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.ordana.spelunkery.configs.ClientConfigs;
import com.ordana.spelunkery.reg.ModBlocks;
import com.ordana.spelunkery.reg.ModComponents;
import com.ordana.spelunkery.reg.ModGameEvents;
import com.ordana.spelunkery.utils.TranslationUtils;
import dev.architectury.injectables.annotations.PlatformOnly;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MagneticCompassItem extends Item {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String TAG_LODESTONE_POS = "LodestonePos";
    public static final String TAG_LODESTONE_DIMENSION = "LodestoneDimension";
    public static final String TAG_LODESTONE_TRACKED = "LodestoneTracked";

    public MagneticCompassItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable TooltipContext level, @NotNull List<Component> tooltip, @NotNull TooltipFlag context) {
        if (ClientConfigs.ENABLE_TOOLTIPS.get()) {
            tooltip.add(Component.translatable("tooltip.spelunkery.player_pos", getPlayerX(stack), getPlayerZ(stack)).setStyle(Style.EMPTY.applyFormat(ChatFormatting.DARK_GREEN)));
            if (stack.has(ModComponents.MAGNETITE_POS.get())) {
                BlockPos blockPos = stack.get(ModComponents.MAGNETITE_POS.get()).pos();
                tooltip.add(Component.translatable("tooltip.spelunkery.magnetite_pos", blockPos.getX(), blockPos.getZ()).setStyle(Style.EMPTY.applyFormat(ChatFormatting.DARK_GREEN)));
            }
            if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), Minecraft.getInstance().options.keyShift.key.getValue())) {
                tooltip.add(Component.translatable("tooltip.spelunkery.magnetic_compass_2").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
                tooltip.add(Component.translatable("tooltip.spelunkery.magnetic_compass_3").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
                tooltip.add(TranslationUtils.MAGNETIC_COMPASS_4.component());
            } else {
                tooltip.add(TranslationUtils.CROUCH.component());
            }
        }
    }

    public void setPlayerX(ItemStack stack, int amount) {
        var pos = stack.getOrDefault(ModComponents.PLAYER_POS.get(), BlockPos.ZERO);
        stack.set(ModComponents.PLAYER_POS.get(), new BlockPos(amount, pos.getY(), pos.getZ()));
    }

    public int getPlayerX(ItemStack stack) {
        return stack.getOrDefault(ModComponents.PLAYER_POS.get(), BlockPos.ZERO).getX();
    }

    public void setPlayerZ(ItemStack stack, int amount) {
        var pos = stack.getOrDefault(ModComponents.PLAYER_POS.get(), BlockPos.ZERO);
        stack.set(ModComponents.PLAYER_POS.get(), new BlockPos(pos.getX(), pos.getY(), amount));
    }

    public int getPlayerZ(ItemStack stack) {
        return stack.getOrDefault(ModComponents.PLAYER_POS.get(), BlockPos.ZERO).getZ();
    }

    //Override
//    @PlatformOnly(PlatformOnly.NEOFORGE)
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    //Override
    @PlatformOnly(PlatformOnly.FABRIC)
    public boolean allowComponentsUpdateAnimation(Player player, InteractionHand hand, ItemStack originalStack, ItemStack updatedStack) {
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.has(ModComponents.MAGNETITE_POS.get())) {
            BlockPos blockPos = stack.get(ModComponents.MAGNETITE_POS.get()).pos();
            player.displayClientMessage(Component.translatable("tooltip.spelunkery.magnetite_pos", blockPos.getX(), blockPos.getZ()).setStyle(Style.EMPTY.applyFormat(ChatFormatting.DARK_GREEN)), true);
        } else player.displayClientMessage(Component.translatable("tooltip.spelunkery.player_pos", getPlayerX(stack), getPlayerZ(stack)).setStyle(Style.EMPTY.applyFormat(ChatFormatting.DARK_GREEN)), true);
        if (player.isSecondaryUseActive()) {
            stack.remove(ModComponents.MAGNETITE_POS.get());
            level.gameEvent(player, ModGameEvents.COMPASS_PING_EVENT, player.blockPosition());
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }

    private static Optional<ResourceKey<Level>> getDimension(CompoundTag compoundTag) {
        return Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, compoundTag.get("magnetiteDimension")).result();
    }

    private int tickCounter = 0;
    public int setTickCounter(int tick) {
        return tickCounter = tick;
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide()) {
            if (entity instanceof ServerPlayer player){
                this.setPlayerX(stack, player.getBlockX());
                this.setPlayerZ(stack, player.getBlockZ());

            }

            tickCounter++;
            if (tickCounter == 100) {
                if (!stack.has(ModComponents.MAGNETITE_POS.get())) level.gameEvent(entity, ModGameEvents.COMPASS_PING_EVENT, entity.blockPosition());
                setTickCounter(0);
            }



            if (stack.has(ModComponents.MAGNETITE_POS.get())) {
                var component = stack.get(ModComponents.MAGNETITE_POS.get());
                BlockPos blockPos = component.pos();
                if (!level.isInWorldBounds(blockPos) || !level.getBlockState(blockPos).is(ModBlocks.RAW_MAGNETITE_BLOCK.get())) {
                    stack.remove(ModComponents.MAGNETITE_POS.get());
                    level.gameEvent(entity, ModGameEvents.COMPASS_PING_EVENT, entity.blockPosition());
                }
            }

        }
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return isMagnetiteNearby(stack) || super.isFoil(stack);
    }

    public static void addMagnetiteTags(ResourceKey<Level> lodestoneDimension, BlockPos pos, CompoundTag compoundTag) {
        if (!compoundTag.contains("magnetitePos")) compoundTag.put("magnetitePos", NbtUtils.writeBlockPos(pos));
        DataResult<Tag> var10000 = Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, lodestoneDimension);
        Logger var10001 = LOGGER;
        Objects.requireNonNull(var10001);
        var10000.resultOrPartial(var10001::error).ifPresent(tag -> compoundTag.put("magnetiteDimension", tag));
    }

    public static boolean isMagnetiteNearby(ItemStack stack) {
        return stack.has(ModComponents.MAGNETITE_POS.get());
    }

    @Nullable
    public static GlobalPos getNorthPosition(Level level) {
        return level.dimensionType().natural() ? GlobalPos.of(level.dimension(), new BlockPos(0, 0, -10000000)) : null;
    }

    @Nullable
    public static GlobalPos getMagnetitePos(ItemStack stack) {
        return stack.getOrDefault(ModComponents.MAGNETITE_POS.get(), null);
    }
}
