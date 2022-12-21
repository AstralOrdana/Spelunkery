package com.ordana.underground_overhaul.blocks;

import com.ordana.underground_overhaul.reg.ModBlockProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public class RopeLadderBlock extends Block implements SimpleWaterloggedBlock {
    private static final int TICK_DELAY = 1;
    private static final VoxelShape BELOW_BLOCK = Shapes.block().move(0.0D, -1.0D, 0.0D);
    public static final DirectionProperty FACING;
    public static final BooleanProperty WATERLOGGED;
    public static final BooleanProperty TOP;

    protected RopeLadderBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false).setValue(TOP, false));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, TOP);
    }

    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        return useContext.getItemInHand().is(this.asItem());
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockPos = context.getClickedPos();
        LevelReader levelReader = context.getLevel();
        BlockState blockState = this.defaultBlockState().setValue(TOP, this.isTop(levelReader, blockPos)).setValue(WATERLOGGED, levelReader.getFluidState(blockPos).getType() == Fluids.WATER);
        Direction[] directions = context.getNearestLookingDirections();
        Direction[] var6 = directions;
        int var7 = directions.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            Direction direction = var6[var8];
            if (direction.getAxis().isHorizontal()) {
                Direction direction2 = direction.getOpposite();
                blockState = blockState.setValue(FACING, direction2);
                if (blockState.canSurvive(levelReader, blockPos)) {
                    return blockState;
                }
            }
        }

        return null;
    }

    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide) {
            level.scheduleTick(pos, this, 1);
        }

    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        if (!level.isClientSide()) {
            level.scheduleTick(currentPos, this, 1);
        }

        return state;
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        return direction.getOpposite() == state.getValue(FACING) && !state.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        switch(state.getValue(FACING)) {
            case EAST:
            default:
                return EAST_AABB;
            case WEST:
                return WEST_AABB;
            case SOUTH:
                return SOUTH_AABB;
            case NORTH:
                return NORTH_AABB;
        }
    }

    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }


    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockState blockState = state.setValue(TOP, this.isTop(level, pos));
        if (state != blockState) {
            level.setBlock(pos, blockState, 3);
        }

    }

    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockPos blockPos = pos.relative(direction.getOpposite());
        BlockState blockState = level.getBlockState(blockPos);
        return direction.getAxis().isHorizontal() && blockState.isFaceSturdy(level, blockPos, direction) || level.getBlockState(pos.above()).is(this);
    }

    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (context.isAbove(Shapes.block(), pos, true) && !context.isDescending()) {
            return STABLE_SHAPE;
        } else {
            return (Integer)state.getValue(DISTANCE) != 0 && (Boolean)state.getValue(BOTTOM) && context.isAbove(BELOW_BLOCK, pos, true) ? UNSTABLE_SHAPE_BOTTOM : Shapes.empty();
        }
    }

    public FluidState getFluidState(BlockState state) {
        return (Boolean)state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    private boolean isTop(BlockGetter level, BlockPos pos) {
        return !level.getBlockState(pos.above()).is(this);
    }

    public static int getDistance(BlockGetter level, BlockPos pos) {
        BlockPos.MutableBlockPos mutableBlockPos = pos.mutable().move(Direction.DOWN);
        BlockState blockState = level.getBlockState(mutableBlockPos);
        int i = 7;
        if (blockState.is(Blocks.SCAFFOLDING)) {
            i = (Integer)blockState.getValue(DISTANCE);
        } else if (blockState.isFaceSturdy(level, mutableBlockPos, Direction.UP)) {
            return 0;
        }

        Iterator var5 = Direction.Plane.HORIZONTAL.iterator();

        while(var5.hasNext()) {
            Direction direction = (Direction)var5.next();
            BlockState blockState2 = level.getBlockState(mutableBlockPos.setWithOffset(pos, direction));
            if (blockState2.is(Blocks.SCAFFOLDING)) {
                i = Math.min(i, (Integer)blockState2.getValue(DISTANCE) + 1);
                if (i == 1) {
                    break;
                }
            }
        }

        return i;
    }

    static {
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        TOP = ModBlockProperties.TOP;
    }
}
