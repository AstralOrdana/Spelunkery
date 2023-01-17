package com.ordana.spelunkery.items;

import com.ordana.spelunkery.configs.CommonConfigs;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MagnetItem extends Item {

    public MagnetItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        toggleMagnet(player, stack, level);
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }

    public static void toggleMagnet(Player player, ItemStack stack, Level level) {
        if(!player.level.isClientSide && stack.getItem() instanceof MagnetItem){
            boolean active = stack.getOrCreateTag().contains("active") && stack.getOrCreateTag().getBoolean("active");
            if (active) level.playSound(player, player.getOnPos(), SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1.0f, 1.5f);
            if (!active) level.playSound(player, player.getOnPos(), SoundEvents.BEACON_DEACTIVATE, SoundSource.BLOCKS, 1.0f, 1.5f);
            stack.getOrCreateTag().putBoolean("active", !active);
        }
    }

    @Override
    public boolean isFoil(ItemStack stack){
        return stack.getOrCreateTag().contains("active") && stack.getOrCreateTag().getBoolean("active");
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if(entity.isSpectator())
            return;

        CompoundTag tag = stack.getOrCreateTag();
        if(tag.contains("active") && tag.getBoolean("active")){
            int r = CommonConfigs.MAGNET_RANGE.get();
            AABB area = new AABB(entity.position().add(-r, -r, -r), entity.position().add(r, r, r));

            List<ItemEntity> items = level.getEntities(EntityType.ITEM, area,
                    item -> item.isAlive() && (!level.isClientSide || item.tickCount > 1) &&
                            (item.getThrower() == null || !item.getThrower().equals(entity.getUUID()) || !item.hasPickUpDelay()) &&
                            !item.getItem().isEmpty() /*&& !item.getPersistentData().contains("PreventRemoteMovement") && this.canPickupStack(tag, item.getItem())*/
            );
            items.forEach(item -> item.setPos(entity.getX(), entity.getY(), entity.getZ()));
        }
    }
}
