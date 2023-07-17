package com.ordana.spelunkery.blocks.rock_salt;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;

public class RockSaltSlab extends SlabBlock implements RockSalt {
    public RockSaltSlab(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(ILLUMINATED, false).setValue(LIGHT, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        super.createBlockStateDefinition(stateManager);
        stateManager.add(ILLUMINATED, LIGHT);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        level.setBlock(pos, updateDistance(state, level, pos), 3);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        var light = context.getLevel().getLightEmission(context.getClickedPos());
        boolean bl = light > 0;
        BlockState blockState = super.getStateForPlacement(context).setValue(LIGHT, light).setValue(ILLUMINATED, bl);
        return updateDistance(blockState, context.getLevel(), context.getClickedPos());
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        int i = getDistanceAt(neighborState) - 1;
        if (i != 1 || state.getValue(LIGHT) != i) {
            level.scheduleTick(currentPos, this, 1);
        }

        return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
    }

    @Override
    public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
        onEntityStepOn(state, entity);
        super.stepOn(world, pos, state, entity);
    }

    private static BlockState updateDistance(BlockState state, LevelAccessor level, BlockPos pos) {
        int i = 0;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        Direction[] var5 = Direction.values();

        for (Direction direction : var5) {
            mutableBlockPos.setWithOffset(pos, direction);
            i = Math.max(i, getDistanceAt(level.getBlockState(mutableBlockPos)) - 1);
            if (i == 16) {
                break;
            }
        }

        return state.setValue(LIGHT, i).setValue(ILLUMINATED, i > 0);
    }

    private static int getDistanceAt(BlockState neighbor) {
        if (neighbor.getLightEmission() > 1) {
            return neighbor.getLightEmission();
        } else {
            return neighbor.hasProperty(ILLUMINATED) ? neighbor.getValue(LIGHT) : 0;
        }
    }

    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return (state.getValue(LIGHT));
    }

}
