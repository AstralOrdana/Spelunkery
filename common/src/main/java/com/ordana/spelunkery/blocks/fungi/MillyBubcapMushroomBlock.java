package com.ordana.spelunkery.blocks.fungi;

import com.ordana.spelunkery.reg.ModBlockProperties;
import com.ordana.spelunkery.reg.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class MillyBubcapMushroomBlock extends BushBlock implements BonemealableBlock {
    public static final int MAX_CAPS = 8;
    public static final IntegerProperty CAPS;
    protected static final VoxelShape ONE_AABB;
    protected static final VoxelShape TWO_AABB;
    protected static final VoxelShape THREE_AABB;
    protected static final VoxelShape FOUR_AABB;

    public MillyBubcapMushroomBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(CAPS, 1));
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockState = context.getLevel().getBlockState(context.getClickedPos());
        if (blockState.is(this)) {
            return blockState.setValue(CAPS, Math.min(8, blockState.getValue(CAPS) + 1));
        } else {
            return super.getStateForPlacement(context);
        }
    }

    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return !state.getCollisionShape(level, pos).getFaceShape(Direction.UP).isEmpty() || state.isFaceSturdy(level, pos, Direction.UP);
    }

    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockPos = pos.below();
        return this.mayPlaceOn(level.getBlockState(blockPos), level, blockPos);
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        if (!state.canSurvive(level, currentPos)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
        }
    }

    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        return !useContext.isSecondaryUseActive() && useContext.getItemInHand().is(this.asItem()) && state.getValue(CAPS) < 8 || super.canBeReplaced(state, useContext);
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(CAPS)) {
            default -> ONE_AABB;
            case 2 -> TWO_AABB;
            case 3 -> THREE_AABB;
            case 4, 5, 6, 7, 8 -> FOUR_AABB;
        };
    }


    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CAPS);
    }

    public boolean isValidBonemealTarget(BlockGetter level, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {

            int j = 1;
            int l = 0;
            int m = pos.getX() - 2;
            int n = 0;

            for(int o = 0; o < 5; ++o) {
                for(int p = 0; p < j; ++p) {
                    int q = 2 + pos.getY() - 1;

                    for(int r = q - 2; r < q; ++r) {
                        BlockPos blockPos = new BlockPos(m + o, r, pos.getZ() - n + p);
                        if (blockPos != pos && random.nextInt(6) == 0 && level.getBlockState(blockPos).isAir()) {
                            BlockState blockState = level.getBlockState(blockPos.below());
                            if (blockState.is(BlockTags.MUSHROOM_GROW_BLOCK)) {
                                level.setBlock(blockPos, ModBlocks.MILLY_BUBCAP.get().defaultBlockState().setValue(CAPS, random.nextInt(8) + 1), 3);
                            }
                        }
                    }
                }

                if (l < 2) {
                    j += 2;
                    ++n;
                } else {
                    j -= 2;
                    --n;
                }

                ++l;
            }

            level.setBlock(pos, state.setValue(CAPS, 8), 2);


    }

    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }

    static {
        CAPS = ModBlockProperties.CAPS;
        ONE_AABB = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 6.0D, 10.0D);
        TWO_AABB = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 6.0D, 13.0D);
        THREE_AABB = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 6.0D, 14.0D);
        FOUR_AABB = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 7.0D, 14.0D);
    }
}
