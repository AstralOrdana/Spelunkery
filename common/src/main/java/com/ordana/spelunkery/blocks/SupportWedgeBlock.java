package com.ordana.spelunkery.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SupportWedgeBlock extends Block implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING;
    public static final IntegerProperty ORIENTATION = IntegerProperty.create("orientation", 1, 4);
    public static final BooleanProperty WATERLOGGED;
    protected static final VoxelShape NORTH_1_AABB;
    protected static final VoxelShape SOUTH_1_AABB;
    protected static final VoxelShape EAST_1_AABB;
    protected static final VoxelShape WEST_1_AABB;
    protected static final VoxelShape NORTH_2_AABB;
    protected static final VoxelShape SOUTH_2_AABB;
    protected static final VoxelShape EAST_2_AABB;
    protected static final VoxelShape WEST_2_AABB;
    protected static final VoxelShape NORTH_3_AABB;
    protected static final VoxelShape SOUTH_3_AABB;
    protected static final VoxelShape EAST_3_AABB;
    protected static final VoxelShape WEST_3_AABB;
    protected static final VoxelShape NORTH_4_AABB;
    protected static final VoxelShape SOUTH_4_AABB;
    protected static final VoxelShape EAST_4_AABB;
    protected static final VoxelShape WEST_4_AABB;


    public SupportWedgeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(ORIENTATION, 1).setValue(WATERLOGGED, false));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, FACING, ORIENTATION);
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(FACING) == Direction.NORTH) {
            return switch (state.getValue(ORIENTATION)) {
                default -> NORTH_1_AABB;
                case 2 -> NORTH_2_AABB;
                case 3 -> NORTH_3_AABB;
                case 4 -> NORTH_4_AABB;
            };
        }
        if (state.getValue(FACING) == Direction.SOUTH) {
            return switch (state.getValue(ORIENTATION)) {
                default -> SOUTH_1_AABB;
                case 2 -> SOUTH_2_AABB;
                case 3 -> SOUTH_3_AABB;
                case 4 -> SOUTH_4_AABB;
            };
        }
        if (state.getValue(FACING) == Direction.EAST) {
            return switch (state.getValue(ORIENTATION)) {
                default -> EAST_1_AABB;
                case 2 -> EAST_2_AABB;
                case 3 -> EAST_3_AABB;
                case 4 -> EAST_4_AABB;
            };
        }
        if (state.getValue(FACING) == Direction.WEST) {
            return switch (state.getValue(ORIENTATION)) {
                default -> WEST_1_AABB;
                case 2 -> WEST_2_AABB;
                case 3 -> WEST_3_AABB;
                case 4 -> WEST_4_AABB;
            };
        } else return NORTH_1_AABB;
    }

    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockState;
        if (!context.replacingClickedOnBlock()) {
            blockState = context.getLevel().getBlockState(context.getClickedPos().relative(context.getClickedFace().getOpposite()));
            if (blockState.is(this) && blockState.getValue(FACING) == context.getClickedFace()) {
                return null;
            }
        }

        blockState = this.defaultBlockState();
        LevelReader levelReader = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        Direction[] var6 = context.getNearestLookingDirections();

        for (Direction direction : var6) {
            if (direction.getAxis().isHorizontal()) {


                var location = 1;
                var xPos = context.getClickLocation().x - (double) blockPos.getX() - 0.5;
                var yPos = context.getClickLocation().y - (double) blockPos.getY() - 0.5;
                var zPos = context.getClickLocation().z - (double) blockPos.getZ() - 0.5;
                if (context.getClickedFace() == Direction.NORTH || context.getClickedFace() == Direction.SOUTH) {
                    boolean bottomleft = xPos < -yPos;
                    boolean topleft = xPos < yPos;
                    if (!bottomleft && topleft) location = 1;
                    if (!bottomleft && !topleft) location = 2;
                    if (bottomleft && !topleft) location = 3;
                    if (bottomleft && topleft) location = 4;
                }
                if (context.getClickedFace() == Direction.EAST || context.getClickedFace() == Direction.WEST) {
                    boolean bottomleft = zPos < -yPos;
                    boolean topleft = zPos < yPos;
                    if (!bottomleft && topleft) location = 1;
                    if (!bottomleft && !topleft) location = 2;
                    if (bottomleft && !topleft) location = 3;
                    if (bottomleft && topleft) location = 4;
                }
                if (context.getClickedFace() == Direction.UP) {
                    location = 3;
                }

                blockState = blockState.setValue(FACING, direction.getOpposite()).setValue(ORIENTATION, location);
                if (blockState.canSurvive(levelReader, blockPos)) {
                    return blockState.setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
                }
            }
        }

        return null;
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

    static {
        FACING = HorizontalDirectionalBlock.FACING;
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        NORTH_1_AABB = Shapes.or(Block.box(5.0D, 7.0D, 14.0D, 11.0D, 14.0D, 16.0D), Block.box(5.0D, 14.0D, 7.0D, 11.0D, 16.0D, 16.0D));
        NORTH_2_AABB = Shapes.or(Block.box(7.0D, 5.0D, 14.0D, 14.0D, 11.0D, 16.0D), Block.box(14.0D, 5.0D, 7.0D, 16.0D, 11.0D, 16.0D));
        NORTH_3_AABB = Shapes.or(Block.box(5.0D, 2.0D, 14.0D, 11.0D, 9.0D, 16.0D), Block.box(5.0D, 0.0D, 7.0D, 11.0D, 2.0D, 16.0D));
        NORTH_4_AABB = Shapes.or(Block.box(2.0D, 5.0D, 14.0D, 9.0D, 11.0D, 16.0D), Block.box(0.0D, 5.0D, 7.0D, 2.0D, 11.0D, 16.0D));

        SOUTH_1_AABB = Shapes.or(Block.box(5.0D, 7.0D, 0.0D, 11.0D, 14.0D, 2.0D), Block.box(5.0D, 14.0D, 0.0D, 11.0D, 16.0D, 9.0D));
        SOUTH_2_AABB = Shapes.or(Block.box(7.0D, 5.0D, 0.0D, 14.0D, 11.0D, 2.0D), Block.box(14.0D, 5.0D, 0.0D, 16.0D, 11.0D, 9.0D));
        SOUTH_3_AABB = Shapes.or(Block.box(5.0D, 2.0D, 0.0D, 11.0D, 9.0D, 2.0D), Block.box(5.0D, 0.0D, 0.0D, 11.0D, 2.0D, 9.0D));
        SOUTH_4_AABB = Shapes.or(Block.box(2.0D, 5.0D, 0.0D, 9.0D, 11.0D, 2.0D), Block.box(0.0D, 5.0D, 0.0D, 2.0D, 11.0D, 9.0D));

        EAST_1_AABB = Shapes.or(Block.box(0.0D, 7.0D, 5.0D, 2.0D, 14.0D, 11.0D), Block.box(0.0D, 14.0D, 5.0D, 9.0D, 16.0D, 11.0D));
        EAST_2_AABB = Shapes.or(Block.box(0.0D, 5.0D, 7.0D, 2.0D, 11.0D, 14.0D), Block.box(0.0D, 5.0D, 14.0D, 9.0D, 11.0D, 16.0D));
        EAST_3_AABB = Shapes.or(Block.box(0.0D, 2.0D, 5.0D, 2.0D, 9.0D, 11.0D), Block.box(0.0D, 0.0D, 5.0D, 9.0D, 2.0D, 11.0D));
        EAST_4_AABB = Shapes.or(Block.box(0.0D, 5.0D, 2.0D, 2.0D, 11.0D, 9.0D), Block.box(0.0D, 5.0D, 0.0D, 9.0D, 11.0D, 2.0D));

        WEST_1_AABB = Shapes.or(Block.box(14.0D, 7.0D, 5.0D, 16.0D, 14.0D, 11.0D), Block.box(7.0D, 14.0D, 5.0D, 16.0D, 16.0D, 11.0D));
        WEST_2_AABB = Shapes.or(Block.box(14.0D, 5.0D, 7.0D, 16.0D, 11.0D, 14.0D), Block.box(7.0D, 5.0D, 14.0D, 16.0D, 11.0D, 16.0D));
        WEST_3_AABB = Shapes.or(Block.box(14.0D, 2.0D, 5.0D, 16.0D, 9.0D, 11.0D), Block.box(7.0D, 0.0D, 5.0D, 16.0D, 2.0D, 11.0D));
        WEST_4_AABB = Shapes.or(Block.box(14.0D, 5.0D, 2.0D, 16.0D, 11.0D, 9.0D), Block.box(7.0D, 5.0D, 0.0D, 16.0D, 11.0D, 2.0D));
    }
}
