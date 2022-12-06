package com.ordana.underground_overhaul.blocks.rock_salt;

import com.ordana.underground_overhaul.reg.ModBlockProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Containers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface RockSalt {

    BooleanProperty ILLUMINATED = ModBlockProperties.ILLUMINATED;

    default void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighborPos, boolean isMoving) {
        if (!level.isClientSide) {
            boolean bl = state.getValue(ILLUMINATED);
            if (bl != this.neighborIsBright(pos, level)) {
                if (bl) {
                    level.scheduleTick(pos, (Block) this, 4);
                } else {
                    level.setBlock(pos, state.cycle(ILLUMINATED), 2);
                }
            }

        }
    }

    default void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(ILLUMINATED) && !this.neighborIsBright(pos, level)) {
            level.setBlock(pos, state.cycle(ILLUMINATED), 2);
        }
    }


    default void onEntityStepOn(BlockState state, Entity entity) {
        if (entity instanceof LivingEntity && ((LivingEntity) entity).isInvertedHealAndHarm()) {
            entity.hurt(DamageSource.HOT_FLOOR, 1.0F);
        }
    }

    default boolean neighborIsBright(BlockPos pos, Level level) {
        if (level.getLightEmission(pos.below()) > 12) {
            return true;
        } else if (level.getLightEmission(pos.above()) > 12) {
            return true;
        } else if (level.getLightEmission(pos.north()) > 12) {
            return true;
        } else if (level.getLightEmission(pos.south()) > 12) {
            return true;
        } else if (level.getLightEmission(pos.east()) > 12) {
            return true;
        } else {
            return level.getLightEmission(pos.west()) > 12;
        }
    }

}
