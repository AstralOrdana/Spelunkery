package com.ordana.spelunkery.blocks.fungi;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.ordana.spelunkery.reg.ModBlockProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class FloorAndSidesMushroomBlock extends Block {
    public static final BooleanProperty FLOOR;
    public static final DirectionProperty FACING;
    private static final Map<Direction, VoxelShape> FACING_TO_SHAPE;
    private static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 11.0D, 14.0D);

    public FloorAndSidesMushroomBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(FLOOR, true));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, FLOOR);
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(FLOOR)) {
            return SHAPE;
        }
        else return FACING_TO_SHAPE.get(state.getValue(FACING));
    }

    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        if (!state.canSurvive(level, currentPos)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
        }
    }

    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        if(state.getValue(FLOOR)) direction = Direction.UP;
        BlockPos blockPos = pos.relative(direction.getOpposite());
        BlockState blockState = level.getBlockState(blockPos);
        return blockState.isFaceSturdy(level, blockPos, direction);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState blockState = super.getStateForPlacement(ctx);
        LevelReader level = ctx.getLevel();
        BlockPos blockPos = ctx.getClickedPos();

        Direction dir = ctx.getClickedFace();
        if(dir==Direction.DOWN) blockState = blockState.setValue(FLOOR, true);
        else if(dir!=Direction.UP) blockState = blockState.setValue(FACING, dir).setValue(FLOOR, false);
        if (blockState.canSurvive(level, blockPos)) {
            return blockState;
        }
        return null;
    }

    static {
        FLOOR = ModBlockProperties.FLOOR;
        FACING = HorizontalDirectionalBlock.FACING;
        FACING_TO_SHAPE = Maps.newEnumMap(ImmutableMap.of(
                Direction.NORTH, Block.box(2.0D, 2.0D, 5.0D, 14.0D, 14.0D, 16.0D),
                Direction.SOUTH, Block.box(2.0D, 2.0D, 0.0D, 14.0D, 14.0D, 11.0D),
                Direction.WEST, Block.box(5.0D, 2.0D, 2.0D, 16.0D, 14.0D, 14.0D),
                Direction.EAST, Block.box(0.0D, 2.0D, 2.0D, 11.0D, 14.0D, 14.0D)));
    }
}