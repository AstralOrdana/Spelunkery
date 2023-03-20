package com.ordana.spelunkery.blocks;

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
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;

public class WoodenRailBlock extends RailBlock {
    public static final EnumProperty<RailShape> SHAPE;
    public static final int STABILITY_MAX_DISTANCE = 7;
    public static final IntegerProperty DISTANCE;
    public static final BooleanProperty BOTTOM;

    public WoodenRailBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(SHAPE, RailShape.NORTH_SOUTH).setValue(WATERLOGGED, false).setValue(BOTTOM, true).setValue(DISTANCE, 0));
    }

    private static boolean isBottom(BlockGetter level, BlockPos pos) {
        return canSupportRigidBlock(level, pos.below());
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockPos = context.getClickedPos();
        Level level = context.getLevel();
        int i = getDistance(level, blockPos);
        Direction direction = context.getHorizontalDirection();
        boolean bl2 = direction == Direction.EAST || direction == Direction.WEST;
        return this.defaultBlockState().setValue(this.getShapeProperty(), bl2 ? RailShape.EAST_WEST : RailShape.NORTH_SOUTH).setValue(WATERLOGGED, level.getFluidState(blockPos).getType() == Fluids.WATER).setValue(DISTANCE, i).setValue(BOTTOM, isBottom(level, blockPos));
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
        return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
    }

    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int i = getDistance(level, pos);
        BlockState blockState = state.setValue(DISTANCE, i).setValue(BOTTOM, this.isBottom(level, pos));
        if (blockState.getValue(DISTANCE) == STABILITY_MAX_DISTANCE || shouldBeRemoved(pos, level, blockState.getValue(SHAPE))) {
            level.destroyBlock(pos, true);
        } else if (state != blockState) {
            level.setBlock(pos, blockState, 3);
        }

    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return getDistance(level, pos) < STABILITY_MAX_DISTANCE || isBottom(level, pos);
    }

    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide && level.getBlockState(pos).is(this)) {
            RailShape railShape = state.getValue(this.getShapeProperty());
            if (shouldBeRemoved(pos, level, railShape)) {
                dropResources(state, level, pos);
                level.removeBlock(pos, isMoving);
            } else {
                this.updateState(state, level, pos, block);
            }

        }
    }

    private static boolean shouldBeRemoved(BlockPos pos, Level level, RailShape railShape) {
        if (!isBottom(level, pos)) {
            List<BlockState> neighbors = new ArrayList<>();
            var counter = 0;
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                BlockState neighborState = level.getBlockState(pos.relative(direction));
                if (neighborState.getBlock() instanceof WoodenRailBlock) {
                    neighbors.add(neighborState);
                }
            }

            for (BlockState state : neighbors) {
                var neighborRailShape = state.getValue(WoodenRailBlock.SHAPE);
                if (railShape == neighborRailShape) counter = counter + 1;
            }

            return counter == 0;
        }

        return !isBottom(level, pos);

        /*
        if (railShape == RailShape.NORTH_SOUTH && (level.getBlockState(pos.relative(Direction.NORTH)).is(BlockTags.RAILS) || level.getBlockState(pos.relative(Direction.SOUTH)).is(BlockTags.RAILS))) {
            return level.getBlockState(pos.relative(Direction.NORTH)).getValue(RailBlock.SHAPE) != RailShape.NORTH_SOUTH && level.getBlockState(pos.relative(Direction.SOUTH)).getValue(RailBlock.SHAPE) != RailShape.NORTH_SOUTH;
        }
        else if (railShape == RailShape.EAST_WEST && (level.getBlockState(pos.relative(Direction.EAST)).is(BlockTags.RAILS) || level.getBlockState(pos.relative(Direction.WEST)).is(BlockTags.RAILS))) {
            return level.getBlockState(pos.relative(Direction.EAST)).getValue(RailBlock.SHAPE) != RailShape.EAST_WEST && level.getBlockState(pos.relative(Direction.WEST)).getValue(RailBlock.SHAPE) != RailShape.EAST_WEST;
        }
         */

        //return !canSupportRigidBlock(level, pos.below()) && !level.getBlockState(pos.below()).isAir();
        /*
        else {
            switch(railShape) {
                case ASCENDING_EAST:
                    return !canSupportRigidBlock(level, pos.east());
                case ASCENDING_WEST:
                    return !canSupportRigidBlock(level, pos.west());
                case ASCENDING_NORTH:
                    return !canSupportRigidBlock(level, pos.north());
                case ASCENDING_SOUTH:
                    return !canSupportRigidBlock(level, pos.south());
                default:
                    return false;
            }
        }
         */
    }

    public static int getDistance(BlockGetter level, BlockPos pos) {
        BlockPos.MutableBlockPos mutableBlockPos = pos.mutable().move(Direction.DOWN);
        int i = STABILITY_MAX_DISTANCE;
        if (isBottom(level, pos)) return 0;
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockState blockState2 = level.getBlockState(mutableBlockPos.setWithOffset(pos, direction));
            if (blockState2.is(ModBlocks.WOODEN_RAIL.get())) {
                i = Math.min(i, blockState2.getValue(DISTANCE) + 1);
                if (i == 1) {
                    break;
                }
            }
        }

        return i;
    }

    @Override
    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }

    public BlockState rotate(BlockState state, Rotation rotation) {
        switch(rotation) {
            case CLOCKWISE_180:
                switch(state.getValue(SHAPE)) {
                    case ASCENDING_EAST:
                        return state.setValue(SHAPE, RailShape.ASCENDING_WEST);
                    case ASCENDING_WEST:
                        return state.setValue(SHAPE, RailShape.ASCENDING_EAST);
                    case ASCENDING_NORTH:
                        return state.setValue(SHAPE, RailShape.ASCENDING_SOUTH);
                    case ASCENDING_SOUTH:
                        return state.setValue(SHAPE, RailShape.ASCENDING_NORTH);
                    case SOUTH_EAST:
                        return state.setValue(SHAPE, RailShape.NORTH_WEST);
                    case SOUTH_WEST:
                        return state.setValue(SHAPE, RailShape.NORTH_EAST);
                    case NORTH_WEST:
                        return state.setValue(SHAPE, RailShape.SOUTH_EAST);
                    case NORTH_EAST:
                        return state.setValue(SHAPE, RailShape.SOUTH_WEST);
                }
            case COUNTERCLOCKWISE_90:
                switch(state.getValue(SHAPE)) {
                    case NORTH_SOUTH:
                        return state.setValue(SHAPE, RailShape.EAST_WEST);
                    case EAST_WEST:
                        return state.setValue(SHAPE, RailShape.NORTH_SOUTH);
                    case ASCENDING_EAST:
                        return state.setValue(SHAPE, RailShape.ASCENDING_NORTH);
                    case ASCENDING_WEST:
                        return state.setValue(SHAPE, RailShape.ASCENDING_SOUTH);
                    case ASCENDING_NORTH:
                        return state.setValue(SHAPE, RailShape.ASCENDING_WEST);
                    case ASCENDING_SOUTH:
                        return state.setValue(SHAPE, RailShape.ASCENDING_EAST);
                    case SOUTH_EAST:
                        return state.setValue(SHAPE, RailShape.NORTH_EAST);
                    case SOUTH_WEST:
                        return state.setValue(SHAPE, RailShape.SOUTH_EAST);
                    case NORTH_WEST:
                        return state.setValue(SHAPE, RailShape.SOUTH_WEST);
                    case NORTH_EAST:
                        return state.setValue(SHAPE, RailShape.NORTH_WEST);
                }
            case CLOCKWISE_90:
                switch(state.getValue(SHAPE)) {
                    case NORTH_SOUTH:
                        return state.setValue(SHAPE, RailShape.EAST_WEST);
                    case EAST_WEST:
                        return state.setValue(SHAPE, RailShape.NORTH_SOUTH);
                    case ASCENDING_EAST:
                        return state.setValue(SHAPE, RailShape.ASCENDING_SOUTH);
                    case ASCENDING_WEST:
                        return state.setValue(SHAPE, RailShape.ASCENDING_NORTH);
                    case ASCENDING_NORTH:
                        return state.setValue(SHAPE, RailShape.ASCENDING_EAST);
                    case ASCENDING_SOUTH:
                        return state.setValue(SHAPE, RailShape.ASCENDING_WEST);
                    case SOUTH_EAST:
                        return state.setValue(SHAPE, RailShape.SOUTH_WEST);
                    case SOUTH_WEST:
                        return state.setValue(SHAPE, RailShape.NORTH_WEST);
                    case NORTH_WEST:
                        return state.setValue(SHAPE, RailShape.NORTH_EAST);
                    case NORTH_EAST:
                        return state.setValue(SHAPE, RailShape.SOUTH_EAST);
                }
            default:
                return state;
        }
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        RailShape railShape = state.getValue(SHAPE);
        switch(mirror) {
            case LEFT_RIGHT:
                switch(railShape) {
                    case ASCENDING_NORTH:
                        return state.setValue(SHAPE, RailShape.ASCENDING_SOUTH);
                    case ASCENDING_SOUTH:
                        return state.setValue(SHAPE, RailShape.ASCENDING_NORTH);
                    case SOUTH_EAST:
                        return state.setValue(SHAPE, RailShape.NORTH_EAST);
                    case SOUTH_WEST:
                        return state.setValue(SHAPE, RailShape.NORTH_WEST);
                    case NORTH_WEST:
                        return state.setValue(SHAPE, RailShape.SOUTH_WEST);
                    case NORTH_EAST:
                        return state.setValue(SHAPE, RailShape.SOUTH_EAST);
                    default:
                        return super.mirror(state, mirror);
                }
            case FRONT_BACK:
                switch(railShape) {
                    case ASCENDING_EAST:
                        return state.setValue(SHAPE, RailShape.ASCENDING_WEST);
                    case ASCENDING_WEST:
                        return state.setValue(SHAPE, RailShape.ASCENDING_EAST);
                    case ASCENDING_NORTH:
                    case ASCENDING_SOUTH:
                    default:
                        break;
                    case SOUTH_EAST:
                        return state.setValue(SHAPE, RailShape.SOUTH_WEST);
                    case SOUTH_WEST:
                        return state.setValue(SHAPE, RailShape.SOUTH_EAST);
                    case NORTH_WEST:
                        return state.setValue(SHAPE, RailShape.NORTH_EAST);
                    case NORTH_EAST:
                        return state.setValue(SHAPE, RailShape.NORTH_WEST);
                }
        }

        return super.mirror(state, mirror);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DISTANCE, SHAPE, WATERLOGGED, BOTTOM);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Block.box(0.0D, -1.0D, 0.0D, 16.0D, 0D, 16.0D);
    }

    static {
        SHAPE = BlockStateProperties.RAIL_SHAPE;
        DISTANCE = BlockStateProperties.STABILITY_DISTANCE;
        BOTTOM = BlockStateProperties.BOTTOM;
    }
}
