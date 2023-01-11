package com.ordana.spelunkery.blocks.rock_salt;

import com.ordana.spelunkery.blocks.nephrite.CarvedNephriteBlock;
import com.ordana.spelunkery.reg.ModBlockProperties;
import com.ordana.spelunkery.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

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
        if (entity instanceof LivingEntity && entity.getType().is(ModTags.HURT_BY_SALT)) {
            if (((LivingEntity) entity).isInvertedHealAndHarm()) entity.setRemainingFireTicks(8);
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
