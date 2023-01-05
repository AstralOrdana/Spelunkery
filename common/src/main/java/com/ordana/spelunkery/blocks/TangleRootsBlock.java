package com.ordana.spelunkery.blocks;

import com.ordana.spelunkery.reg.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TangleRootsBlock extends GrowingPlantHeadBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED;
    protected static final VoxelShape SHAPE = Block.box(4.0D, 9.0D, 4.0D, 12.0D, 16.0D, 12.0D);

    public TangleRootsBlock(BlockBehaviour.Properties properties) {
        super(properties, Direction.DOWN, SHAPE, false, 0.1D);
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false));
    }

    protected int getBlocksToGrowWhenBonemealed(RandomSource random) {
        return NetherVines.getBlocksToGrowWhenBonemealed(random);
    }


    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
    }

    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, AGE);
    }

    protected Block getBodyBlock() {
        return ModBlocks.TANGLE_ROOTS_PLANT.get();
    }

    protected boolean canGrowInto(BlockState state) {
        return NetherVines.isValidGrowthState(state) || state.getFluidState().is(Fluids.WATER);
    }

    static {
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
    }
}
