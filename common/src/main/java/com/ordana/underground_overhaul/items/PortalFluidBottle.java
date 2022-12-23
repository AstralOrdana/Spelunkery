package com.ordana.underground_overhaul.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class PortalFluidBottle extends Item {
    public PortalFluidBottle(Properties properties) {
        super(properties);
    }

    public static final FoodProperties PORTAL_FLUID = (new FoodProperties.Builder()).nutrition(0).saturationMod(0F).alwaysEat().build();

    @Override
    public SoundEvent getDrinkingSound() {
        return SoundEvents.HONEY_DRINK;
    }

    @Override
    public SoundEvent getEatingSound() {
        return SoundEvents.HONEY_DRINK;
    }


    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (livingEntity instanceof ServerPlayer) teleportTargetToPlayerSpawn((ServerPlayer) livingEntity);
        ItemStack itemStack = super.finishUsingItem(stack, level, livingEntity);
        if (livingEntity instanceof Player player && player.getAbilities().instabuild) {
            return itemStack;
        }
        return new ItemStack(Items.GLASS_BOTTLE);
    }

    void teleportTargetToPlayerSpawn(ServerPlayer player){
        BlockPos spawn = player.getRespawnPosition();
        ServerLevel serverWorld = (ServerLevel) player.level;
        ResourceKey<Level> spawnDimension = player.getRespawnDimension();
        ServerLevel destination = ((ServerLevel) player.level).getServer().getLevel(spawnDimension);

        // If recalling in a dimension different from the player's spawn dimension, or null for some reason, fail.
        if (destination == null || !(spawnDimension.equals(serverWorld.dimension()))) {
            Vec3 pos = player.position();
            player.level.playSound(null, pos.x(), pos.y(), pos.z(), SoundEvents.REDSTONE_TORCH_BURNOUT, SoundSource.PLAYERS, 1f, 1f);
            return;
        }

        if (spawn == null) {
            spawn = player.level.getSharedSpawnPos();
        }
        Optional<Vec3> a = Player.findRespawnPositionAndUseSpawnBlock(destination, spawn, 0, true, true);
        if(a.isPresent()){
            BlockState blockState = destination.getBlockState(spawn);
            if(blockState.is(BlockTags.BEDS)){
                spawn = new BlockPos(a.get());
            }
            else {
                Optional<Vec3> b = Player.findRespawnPositionAndUseSpawnBlock(destination, player.level.getSharedSpawnPos(), 0, true, true);
                spawn = b.map(BlockPos::new).orElseGet(() -> player.level.getSharedSpawnPos());
                player.setRespawnPosition(serverWorld.dimension(), spawn, 0, true, false);
            }
        }

        player.stopRiding();
        player.fallDistance = 0;
        player.teleportTo(spawn.getX() + 0.5F,spawn.getY()+0.6F,spawn.getZ()+ 0.5F);
        player.level.playSound(null, spawn.getX() + 0.5F, spawn.getY()+0.6F, spawn.getZ() + 0.5F, SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 1f, 1f);
    }

}
