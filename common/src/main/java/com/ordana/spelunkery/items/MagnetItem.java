package com.ordana.spelunkery.items;

import com.mojang.blaze3d.platform.InputConstants;
import com.ordana.spelunkery.configs.ClientConfigs;
import com.ordana.spelunkery.configs.CommonConfigs;
import com.ordana.spelunkery.utils.TranslationUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MagnetItem extends Item {

    public MagnetItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @javax.annotation.Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag context) {
        if (ClientConfigs.ENABLE_TOOLTIPS.get()) {
            CompoundTag compoundTag = stack.getOrCreateTag();
            if (compoundTag.getBoolean("active")) tooltip.add(Component.translatable("tooltip.spelunkery.active").setStyle(Style.EMPTY.applyFormats(ChatFormatting.DARK_GREEN, ChatFormatting.ITALIC)));
            if (!compoundTag.getBoolean("active")) tooltip.add(Component.translatable("tooltip.spelunkery.inactive").setStyle(Style.EMPTY.applyFormats(ChatFormatting.DARK_RED, ChatFormatting.ITALIC)));
            if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), Minecraft.getInstance().options.keyShift.key.getValue())) {
                tooltip.add(Component.translatable("tooltip.spelunkery.item_magnet_1", getMagnetRange()).setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
                tooltip.add(Component.translatable("tooltip.spelunkery.item_magnet_2").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
            } else {
                tooltip.add(TranslationUtils.CROUCH.component());
            }
        }
    }

    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        toggleMagnet(player, stack, level);
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }

    public static void toggleMagnet(Player player, ItemStack stack, Level level) {
        if(!player.level.isClientSide && stack.getItem() instanceof MagnetItem){
            boolean active = stack.getOrCreateTag().contains("active") && stack.getOrCreateTag().getBoolean("active");
            var beaconSound = active ? SoundEvents.BEACON_DEACTIVATE : SoundEvents.BEACON_ACTIVATE;
            level.playSound(null, player.blockPosition(), beaconSound, SoundSource.BLOCKS, 1.0f, 2.0f);
            stack.getOrCreateTag().putBoolean("active", !active);
        }
    }

    @Override
    public boolean isFoil(ItemStack stack){
        return stack.getOrCreateTag().contains("active") && stack.getOrCreateTag().getBoolean("active");
    }

    public int getMagnetRange() {
        return CommonConfigs.MAGNET_RANGE.get();
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, Entity entity, int slotId, boolean isSelected) {
        if(entity.isSpectator()) {
            return;
        }


        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains("active") && tag.getBoolean("active")) {
            int r = getMagnetRange();
            AABB area = new AABB(entity.position().add(-r, -r, -r), entity.position().add(r, r, r));

            List<ItemEntity> items = level.getEntities(EntityType.ITEM, area,
                    item -> item.isAlive() && (!level.isClientSide || item.tickCount > 1) &&
                            (item.getOwner() == null || !item.hasPickUpDelay()) &&
                            !item.getItem().isEmpty() /*&& !item.getPersistentData().contains("PreventRemoteMovement") && this.canPickupStack(tag, item.getItem())*/
            );

            items.forEach(item -> item.setDeltaMovement(item.getDeltaMovement().add(
                    new Vec3((entity.position().x) - item.getX(), (entity.position().y) - item.getY(), (entity.position().z) - item.getZ()).normalize().scale(((1.4D - Math.sqrt(
                            new Vec3((entity.position().x) - item.getX(), (entity.position().y) - item.getY(), (entity.position().z) - item.getZ()).lengthSqr()) / 8.0D) * (1.4D - Math.sqrt(
                            new Vec3((entity.position().x) - item.getX(), (entity.position().y) - item.getY(), (entity.position().z) - item.getZ()).lengthSqr()) / 8.0D)) * 0.1D))));
        }
    }

}
