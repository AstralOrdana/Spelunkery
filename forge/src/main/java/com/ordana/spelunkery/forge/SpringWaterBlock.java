package com.ordana.spelunkery.forge;

import com.ordana.spelunkery.reg.ModBlocks;
import com.ordana.spelunkery.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

import java.util.function.Supplier;

public class SpringWaterBlock extends LiquidBlock {

    public SpringWaterBlock(Supplier<FlowingFluid> flowingFluid, Properties properties) {
        super(flowingFluid, properties);
    }

    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 5, 0, true, false, true));
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        var aboveState = level.getBlockState(pos.above());
        var belowState = level.getBlockState(pos.below());
        var fluidState = level.getFluidState(pos);
        if (!fluidState.getValue(FlowingFluid.FALLING) && fluidState.isSource() &&
                (belowState.is(ModTags.SPRING_GEYSER_SOURCE) || aboveState.is(BlockTags.OVERWORLD_CARVER_REPLACEABLES)) || aboveState.is(BlockTags.SNOW)) {
            level.setBlockAndUpdate(pos.above(), ModBlocks.SPRING_WATER.get().defaultBlockState());
        }
    }

    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        level.scheduleTick(pos, this, 2);
        level.scheduleTick(pos.above(), this, 4);
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        level.scheduleTick(pos, this, 2);
        level.scheduleTick(pos.above(), this, 4);
        return state;
    }

}