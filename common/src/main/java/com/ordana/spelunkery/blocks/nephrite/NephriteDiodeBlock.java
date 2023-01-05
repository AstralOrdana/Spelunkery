package com.ordana.spelunkery.blocks.nephrite;

import com.ordana.spelunkery.blocks.entity.CarvedNephriteBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class NephriteDiodeBlock extends DirectionalBlock {
    public static final BooleanProperty POWERED;

    public NephriteDiodeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.SOUTH).setValue(POWERED, false));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED);
    }

    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    public void jumpCharge(BlockState state, Level level, BlockPos pos) {
        Direction dir = state.getValue(FACING);
        if (level.getBlockEntity(pos.relative(dir)) instanceof CarvedNephriteBlockEntity takeTile && level.getBlockEntity(pos.relative(dir.getOpposite())) instanceof CarvedNephriteBlockEntity receiveTile) {
            if (takeTile.getCharge() > 0 && receiveTile.getCharge() < 100) {
                takeTile.setCharge(takeTile.getCharge() - 1);
                receiveTile.setCharge(receiveTile.getCharge() + 1);
            }
        }
    }


    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide) {
            boolean bl = state.getValue(POWERED);
            if (bl != level.hasNeighborSignal(pos)) {
                if (bl) {
                    level.scheduleTick(pos, this, 2);
                } else {
                    level.setBlock(pos, state.cycle(POWERED), 2);
                }
            }

        }
    }

    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(POWERED) && !level.hasNeighborSignal(pos)) {
            level.setBlock(pos, state.cycle(POWERED), 2);
        }
        this.jumpCharge(state, level, pos);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite().getOpposite()).setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    static {
        POWERED = BlockStateProperties.POWERED;
    }
}
