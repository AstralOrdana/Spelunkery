package com.ordana.underground_overhaul.blocks.fungi;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ConkMushroomBlock extends FloorAndSidesMushroomBlock {
    public static final DirectionProperty FACING;
    private static final Map<Direction, VoxelShape> SHAPES;

    public ConkMushroomBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(FLOOR, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, FLOOR);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockPos blockPos = pos.relative(direction.getOpposite());
        BlockState blockState = level.getBlockState(blockPos);
        return blockState.isFaceSturdy(level, blockPos, direction);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockState = super.getStateForPlacement(context);
        LevelReader levelReader = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        Direction[] directions = context.getNearestLookingDirections();
        Direction[] var6 = directions;
        int var7 = directions.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            Direction direction = var6[var8];
            if (direction.getAxis().isHorizontal()) {
                blockState = blockState.setValue(FACING, direction.getOpposite());
                if (blockState.canSurvive(levelReader, blockPos)) {
                    return blockState;
                }
            }
        }

        return null;
    }

    static {
        FACING = HorizontalDirectionalBlock.FACING;
        SHAPES = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.box(0.0D, 4.0D, 5.0D, 16.0D, 12.0D, 16.0D), Direction.SOUTH, Block.box(0.0D, 4.0D, 0.0D, 16.0D, 12.0D, 11.0D), Direction.WEST, Block.box(5.0D, 4.0D, 0.0D, 16.0D, 12.0D, 16.0D), Direction.EAST, Block.box(0.0D, 4.0D, 0.0D, 11.0D, 12.0D, 16.0D)));
    }
}
