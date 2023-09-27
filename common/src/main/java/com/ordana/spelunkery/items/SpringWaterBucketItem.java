package com.ordana.spelunkery.items;

import dev.architectury.injectables.annotations.PlatformOnly;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;

import java.lang.reflect.Field;

public class SpringWaterBucketItem extends BucketItem {

    private static final Field CONTENT = PlatformHelper.findField(BucketItem.class, "content");

    public SpringWaterBucketItem(Fluid fluid, Properties properties) {
        super(fluid, properties);
    }

    //Override
    @PlatformOnly(PlatformOnly.FORGE)
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    //Override
    @PlatformOnly(PlatformOnly.FABRIC)
    public boolean allowNbtUpdateAnimation(Player player, InteractionHand hand, ItemStack originalStack, ItemStack updatedStack) {
        return false;
    }

    @Override
    protected void playEmptySound(@org.jetbrains.annotations.Nullable Player player, LevelAccessor level, BlockPos pos) {
        SoundEvent soundEvent = SoundEvents.BUCKET_EMPTY;
        level.playSound(player, pos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.gameEvent(player, GameEvent.FLUID_PLACE, pos);
    }
}
