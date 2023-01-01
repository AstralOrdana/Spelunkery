package com.ordana.underground_overhaul.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;

public class WoodenRailBlock extends BaseRailBlock {
    public static final EnumProperty<RailShape> SHAPE;

    public WoodenRailBlock(Properties properties) {
        super(true, properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(SHAPE, RailShape.NORTH_SOUTH).setValue(WATERLOGGED, false));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return canSupportRigidBlock(level, pos.below()) || level.getBlockState(pos.below()).isAir();
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
        return !canSupportRigidBlock(level, pos.below()) && !level.getBlockState(pos.below()).isAir();
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

    @Override
    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }

    public BlockState rotate(BlockState state, Rotation rotation) {
        switch(rotation) {
            case CLOCKWISE_180:
                switch((RailShape)state.getValue(SHAPE)) {
                    case ASCENDING_EAST:
                        return (BlockState)state.setValue(SHAPE, RailShape.ASCENDING_WEST);
                    case ASCENDING_WEST:
                        return (BlockState)state.setValue(SHAPE, RailShape.ASCENDING_EAST);
                    case ASCENDING_NORTH:
                        return (BlockState)state.setValue(SHAPE, RailShape.ASCENDING_SOUTH);
                    case ASCENDING_SOUTH:
                        return (BlockState)state.setValue(SHAPE, RailShape.ASCENDING_NORTH);
                    case SOUTH_EAST:
                        return (BlockState)state.setValue(SHAPE, RailShape.NORTH_WEST);
                    case SOUTH_WEST:
                        return (BlockState)state.setValue(SHAPE, RailShape.NORTH_EAST);
                    case NORTH_WEST:
                        return (BlockState)state.setValue(SHAPE, RailShape.SOUTH_EAST);
                    case NORTH_EAST:
                        return (BlockState)state.setValue(SHAPE, RailShape.SOUTH_WEST);
                }
            case COUNTERCLOCKWISE_90:
                switch((RailShape)state.getValue(SHAPE)) {
                    case NORTH_SOUTH:
                        return (BlockState)state.setValue(SHAPE, RailShape.EAST_WEST);
                    case EAST_WEST:
                        return (BlockState)state.setValue(SHAPE, RailShape.NORTH_SOUTH);
                    case ASCENDING_EAST:
                        return (BlockState)state.setValue(SHAPE, RailShape.ASCENDING_NORTH);
                    case ASCENDING_WEST:
                        return (BlockState)state.setValue(SHAPE, RailShape.ASCENDING_SOUTH);
                    case ASCENDING_NORTH:
                        return (BlockState)state.setValue(SHAPE, RailShape.ASCENDING_WEST);
                    case ASCENDING_SOUTH:
                        return (BlockState)state.setValue(SHAPE, RailShape.ASCENDING_EAST);
                    case SOUTH_EAST:
                        return (BlockState)state.setValue(SHAPE, RailShape.NORTH_EAST);
                    case SOUTH_WEST:
                        return (BlockState)state.setValue(SHAPE, RailShape.SOUTH_EAST);
                    case NORTH_WEST:
                        return (BlockState)state.setValue(SHAPE, RailShape.SOUTH_WEST);
                    case NORTH_EAST:
                        return (BlockState)state.setValue(SHAPE, RailShape.NORTH_WEST);
                }
            case CLOCKWISE_90:
                switch((RailShape)state.getValue(SHAPE)) {
                    case NORTH_SOUTH:
                        return (BlockState)state.setValue(SHAPE, RailShape.EAST_WEST);
                    case EAST_WEST:
                        return (BlockState)state.setValue(SHAPE, RailShape.NORTH_SOUTH);
                    case ASCENDING_EAST:
                        return (BlockState)state.setValue(SHAPE, RailShape.ASCENDING_SOUTH);
                    case ASCENDING_WEST:
                        return (BlockState)state.setValue(SHAPE, RailShape.ASCENDING_NORTH);
                    case ASCENDING_NORTH:
                        return (BlockState)state.setValue(SHAPE, RailShape.ASCENDING_EAST);
                    case ASCENDING_SOUTH:
                        return (BlockState)state.setValue(SHAPE, RailShape.ASCENDING_WEST);
                    case SOUTH_EAST:
                        return (BlockState)state.setValue(SHAPE, RailShape.SOUTH_WEST);
                    case SOUTH_WEST:
                        return (BlockState)state.setValue(SHAPE, RailShape.NORTH_WEST);
                    case NORTH_WEST:
                        return (BlockState)state.setValue(SHAPE, RailShape.NORTH_EAST);
                    case NORTH_EAST:
                        return (BlockState)state.setValue(SHAPE, RailShape.SOUTH_EAST);
                }
            default:
                return state;
        }
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        RailShape railShape = (RailShape)state.getValue(SHAPE);
        switch(mirror) {
            case LEFT_RIGHT:
                switch(railShape) {
                    case ASCENDING_NORTH:
                        return (BlockState)state.setValue(SHAPE, RailShape.ASCENDING_SOUTH);
                    case ASCENDING_SOUTH:
                        return (BlockState)state.setValue(SHAPE, RailShape.ASCENDING_NORTH);
                    case SOUTH_EAST:
                        return (BlockState)state.setValue(SHAPE, RailShape.NORTH_EAST);
                    case SOUTH_WEST:
                        return (BlockState)state.setValue(SHAPE, RailShape.NORTH_WEST);
                    case NORTH_WEST:
                        return (BlockState)state.setValue(SHAPE, RailShape.SOUTH_WEST);
                    case NORTH_EAST:
                        return (BlockState)state.setValue(SHAPE, RailShape.SOUTH_EAST);
                    default:
                        return super.mirror(state, mirror);
                }
            case FRONT_BACK:
                switch(railShape) {
                    case ASCENDING_EAST:
                        return (BlockState)state.setValue(SHAPE, RailShape.ASCENDING_WEST);
                    case ASCENDING_WEST:
                        return (BlockState)state.setValue(SHAPE, RailShape.ASCENDING_EAST);
                    case ASCENDING_NORTH:
                    case ASCENDING_SOUTH:
                    default:
                        break;
                    case SOUTH_EAST:
                        return (BlockState)state.setValue(SHAPE, RailShape.SOUTH_WEST);
                    case SOUTH_WEST:
                        return (BlockState)state.setValue(SHAPE, RailShape.SOUTH_EAST);
                    case NORTH_WEST:
                        return (BlockState)state.setValue(SHAPE, RailShape.NORTH_EAST);
                    case NORTH_EAST:
                        return (BlockState)state.setValue(SHAPE, RailShape.NORTH_WEST);
                }
        }

        return super.mirror(state, mirror);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SHAPE, WATERLOGGED);
    }

    static {
        SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;
    }
}
