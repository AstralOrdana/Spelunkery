package com.ordana.spelunkery.blocks;

import com.ordana.spelunkery.blocks.entity.FallingLayerEntity;
import com.ordana.spelunkery.reg.ModBlocks;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;

/**
 * Author: MehVahdJukaar
 */

public class FallingLayerBlock extends FallingBlock {
    public static final IntegerProperty LAYERS_8 = BlockStateProperties.LAYERS;
    private static final VoxelShape[] SHAPE_BY_LAYER = new VoxelShape[8 + 1];

    private final int min;
    private final int max;

    static {
        Arrays.setAll(SHAPE_BY_LAYER, l -> Block.box(0.0D, 0.0D, 0.0D, 16.0D, l * 2, 16.0D));
        SHAPE_BY_LAYER[0] = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 0.1f, 16.0D);
    }

    public FallingLayerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(layerProperty(), 1));
        this.min = Collections.min(this.layerProperty().getPossibleValues());
        this.max = Collections.max(this.layerProperty().getPossibleValues());
    }

    public final int getMaxLayers() {
        return max;
    }
    public final int getMinLayers() {
        return min;
    }

    public IntegerProperty layerProperty() {
        return LAYERS_8;
    }

    public int getLayers(BlockState state) {
        return state.getValue(layerProperty());
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (state.getBlock() != oldState.getBlock())
            worldIn.scheduleTick(pos, this, this.getDelayAfterPlace());
    }

    public VoxelShape getDefaultShape(BlockState state) {
        return SHAPE_BY_LAYER[state.getValue(LAYERS_8)];
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return getDefaultShape(pState);
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState pState, BlockGetter pReader, BlockPos pPos) {
        return getDefaultShape(pState);
    }

    @Override
    public VoxelShape getVisualShape(BlockState pState, BlockGetter pReader, BlockPos pPos, CollisionContext pContext) {
        return getDefaultShape(pState);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
        return switch (type) {
            case LAND -> getLayers(state) < 5;
            case WATER -> getLayers(state) == 0;
            case AIR -> false;
        };
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    //ugly but works
    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos otherPos) {
        if (world instanceof ServerLevel serverLevel) {
            BlockPos pos = currentPos.above();
            BlockState state1 = world.getBlockState(pos);

            while (state1.is(this)) {
                serverLevel.scheduleTick(pos, this, this.getDelayAfterPlace());
                pos = pos.above();
                state1 = serverLevel.getBlockState(pos);
            }
        }
        return super.updateShape(state, direction, facingState, world, currentPos, otherPos);
    }


    public boolean shouldFall(BlockState belowState) {
        return (belowState.isAir() || belowState.getMaterial().isReplaceable()) && !(belowState.is(this));
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource pRand) {
        BlockState below = level.getBlockState(pos.below());
        if ((this.shouldFall(below) || hasIncompletePileBelow(below)) && pos.getY() >= level.getMinBuildHeight()) {

            while (state.is(this)) {
                FallingBlockEntity fallingblockentity = FallingLayerEntity.fall(level, pos, state);
                this.falling(fallingblockentity);

                pos = pos.above();
                state = level.getBlockState(pos);
            }
        }
    }

    private boolean hasIncompletePileBelow(BlockState state) {
        return state.is(this) && getLayers(state) != getMaxLayers();
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos());
        if (blockstate.is(this)) {
            int i = getLayers(blockstate);
            return blockstate.setValue(layerProperty(), Math.min(getMaxLayers(), i + 1));
        } else {
            return super.getStateForPlacement(context);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(layerProperty());
    }

    @Override
    public boolean canBeReplaced(BlockState pState, BlockPlaceContext pUseContext) {
        int i = getLayers(pState);
        if (pUseContext.getItemInHand().is(this.asItem()) && i < getMaxLayers()) {
            return true;
        } else {
            return i == 1;
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return this.getShape(state, level, pos, context);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float height) {
        super.fallOn(level, state, pos, entity, height);
    }

}