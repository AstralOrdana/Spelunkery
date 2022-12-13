package com.ordana.underground_overhaul.blocks.nephrite;

import com.ordana.underground_overhaul.blocks.entity.CarvedNephriteBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

public class NephriteSpout extends Block {
    public static final BooleanProperty ACTIVE;

    public NephriteSpout(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(ACTIVE, false));
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(ACTIVE, context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide) {
            boolean bl = state.getValue(ACTIVE);
            if (bl != level.hasNeighborSignal(pos)) {
                if (bl) {
                    level.scheduleTick(pos, this, 4);
                } else {
                    level.setBlock(pos, state.cycle(ACTIVE), 2);
                }
            }

        }
    }

    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(ACTIVE) && !level.hasNeighborSignal(pos)) {
            level.setBlock(pos, state.cycle(ACTIVE), 2);
        }
        for (Direction direction : Direction.values()) {
            if (direction.equals(Direction.UP)) continue;

            while (level.getBlockEntity(pos.relative(direction)) instanceof CarvedNephriteBlockEntity neighborTile && neighborTile.getCharge() > 0) {
                neighborTile.setCharge(neighborTile.getCharge() - 1);
                this.popExperience(level, pos.relative(direction.getOpposite()), 1);
            }
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }

    static {
        ACTIVE = RedstoneTorchBlock.LIT;
    }
}
