package com.ordana.spelunkery.blocks;

import com.mojang.serialization.MapCodec;
import com.ordana.spelunkery.reg.ModBlockProperties;
import com.ordana.spelunkery.reg.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TangleRootsBodyBlock extends GrowingPlantBodyBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty TOP;
    public static final BooleanProperty WATERLOGGED;
    public static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);

    public TangleRootsBodyBlock(Properties properties) {
        super(properties, Direction.DOWN, SHAPE, false);
        this.registerDefaultState(this.stateDefinition.any().setValue(TOP, true).setValue(WATERLOGGED, false));
    }

    private boolean isTop(BlockGetter level, BlockPos pos) {
        return !level.getBlockState(pos.above()).is(this);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockPos = context.getClickedPos();
        LevelReader levelReader = context.getLevel();
        BlockState blockState = context.getLevel().getBlockState(context.getClickedPos().relative(this.growthDirection));
        return !blockState.is(this.getHeadBlock()) && !blockState.is(this.getBodyBlock()) ?
                this.getStateForPlacement(context.getLevel()) :
                this.getBodyBlock().defaultBlockState()
                        .setValue(TOP, this.isTop(levelReader, blockPos))
                        .setValue(WATERLOGGED, levelReader.getFluidState(blockPos).getType() == Fluids.WATER);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide) {
            level.scheduleTick(pos, this, 1);
        }
    }

    @Override
    protected MapCodec<? extends GrowingPlantBodyBlock> codec() {
        return simpleCodec(TangleRootsBodyBlock::new);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        if (!level.isClientSide()) {
            level.scheduleTick(currentPos, this, 1);
        }

        return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected GrowingPlantHeadBlock getHeadBlock() {
        return (GrowingPlantHeadBlock) ModBlocks.TANGLE_ROOTS.get();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, TOP);
    }

    static {
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        TOP = ModBlockProperties.TOP;
    }
}
