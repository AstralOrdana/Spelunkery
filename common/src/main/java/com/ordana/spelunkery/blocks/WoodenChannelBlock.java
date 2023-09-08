package com.ordana.spelunkery.blocks;

import com.ordana.spelunkery.reg.ModBlockProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;
import java.util.Optional;

public class WoodenChannelBlock extends Block implements BucketPickup, LiquidBlockContainer {
    public static final BooleanProperty NORTH;
    public static final BooleanProperty SOUTH;
    public static final BooleanProperty EAST;
    public static final BooleanProperty WEST;
    public static final BooleanProperty HOLE;
    public static final BooleanProperty FLOWING;
    public static final BooleanProperty SUPPORTED;

    protected static final VoxelShape SHAPE_N;
    protected static final VoxelShape SHAPE_E;
    protected static final VoxelShape SHAPE_S;
    protected static final VoxelShape SHAPE_W;

    protected static final VoxelShape SHAPE_NE;
    protected static final VoxelShape SHAPE_SE;
    protected static final VoxelShape SHAPE_NW;
    protected static final VoxelShape SHAPE_SW;
    protected static final VoxelShape SHAPE_EW;
    protected static final VoxelShape SHAPE_NS;

    protected static final VoxelShape SHAPE_NES;
    protected static final VoxelShape SHAPE_ESW;
    protected static final VoxelShape SHAPE_SWN;
    protected static final VoxelShape SHAPE_WNE;
    protected static final VoxelShape SHAPE_NESW;
    protected static final VoxelShape SHAPE_LEGS;
    protected static final VoxelShape SHAPE_BASE;
    protected static final VoxelShape SHAPE_NONE;

    protected static final VoxelShape SHAPE_HOLE_N;
    protected static final VoxelShape SHAPE_HOLE_E;
    protected static final VoxelShape SHAPE_HOLE_S;
    protected static final VoxelShape SHAPE_HOLE_W;
    
    protected static final VoxelShape SHAPE_HOLE_NE;
    protected static final VoxelShape SHAPE_HOLE_SE;
    protected static final VoxelShape SHAPE_HOLE_NW;
    protected static final VoxelShape SHAPE_HOLE_SW;
    protected static final VoxelShape SHAPE_HOLE_EW;
    protected static final VoxelShape SHAPE_HOLE_NS;
    
    protected static final VoxelShape SHAPE_HOLE_NES;
    protected static final VoxelShape SHAPE_HOLE_ESW;
    protected static final VoxelShape SHAPE_HOLE_SWN;
    protected static final VoxelShape SHAPE_HOLE_WNE;
    protected static final VoxelShape SHAPE_HOLE_NESW;
    protected static final VoxelShape SHAPE_HOLE_NONE;
    public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION;

    public WoodenChannelBlock(Properties properties) {
        super(properties);
        this.registerDefaultState((this.stateDefinition.any()).setValue(NORTH, true).setValue(SOUTH, true).setValue(EAST, true).setValue(WEST, true).setValue(HOLE, false).setValue(FLOWING, false).setValue(SUPPORTED, false));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HOLE, FLOWING, SUPPORTED, NORTH, EAST, SOUTH, WEST);
    }


    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        var belowPos = pos.below();
        var belowState = level.getBlockState(belowPos);
        var aboveState = level.getFluidState(pos.above());
        var fluidState = level.getFluidState(pos);
        if (!aboveState.is(Fluids.WATER) && !aboveState.is(Fluids.FLOWING_WATER) && fluidState.is(Fluids.FLOWING_WATER)) {
            //fluidState = Fluids.EMPTY.defaultFluidState();
            //level.setBlock(pos, fluidState.createLegacyBlock(), 3);
            state.setValue(FLOWING, false);
            //fluidState.setValue(FlowingFluid.LEVEL, 1).setValue(FlowingFluid.FALLING, false);
        }
        level.setBlock(pos, state.setValue(SUPPORTED, belowState.isFaceSturdy(level, belowPos, Direction.UP) || belowState.getBlock() instanceof WoodenChannelBlock), 3);
        super.tick(state, level, pos, random);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockGetter blockGetter = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        BlockPos belowPos = blockPos.below();
        BlockState belowState = blockGetter.getBlockState(belowPos);
        return this.defaultBlockState().setValue(NORTH, !blockGetter.getBlockState(blockPos.north()).is(this)).setValue(EAST, !blockGetter.getBlockState(blockPos.east()).is(this)).setValue(SOUTH, !blockGetter.getBlockState(blockPos.south()).is(this)).setValue(WEST, !blockGetter.getBlockState(blockPos.west()).is(this)).setValue(SUPPORTED, belowState.isFaceSturdy(blockGetter, belowPos, Direction.UP) || belowState.getBlock() instanceof WoodenChannelBlock);
    }

    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        var fluidState = level.getFluidState(pos);
        level.scheduleTick(pos, this, 1);
        level.scheduleTick(pos.above(), level.getBlockState(pos.above()).getBlock(), 1);
        level.scheduleTick(pos.below(), level.getBlockState(pos.below()).getBlock(), 1);
        if (state.getValue(FLOWING)) level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(level));
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        var fluidState = level.getFluidState(pos);
        if (state.getValue(FLOWING)) level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(level));
        if (direction == Direction.DOWN && (neighborState.isFaceSturdy(level, neighborPos, direction) || neighborState.getBlock() instanceof WoodenChannelBlock)) state.setValue(SUPPORTED, true);
        level.scheduleTick(pos, this, 1);
        level.scheduleTick(pos.above(), level.getBlockState(pos.above()).getBlock(), 1);
        level.scheduleTick(pos.below(), level.getBlockState(pos.below()).getBlock(), 1);
        return neighborState.is(this) && direction != Direction.UP && direction != Direction.DOWN ? state.setValue(PROPERTY_BY_DIRECTION.get(direction), false) : super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);
        Item item = itemStack.getItem();
        if (!(item instanceof AxeItem)) {
            return super.use(state, level, pos, player, hand, hit);
        } else {
            level.setBlock(pos, state.setValue(HOLE, true), 3);
            level.playSound(null, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.playSound(null, pos, SoundEvents.WOOD_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);

            if (!player.isCreative()) {
                itemStack.hurtAndBreak(1, player, (playerx) -> {
                    playerx.broadcastBreakEvent(hand);
                });
            }

            player.awardStat(Stats.ITEM_USED.get(item));
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }

    @Deprecated
    public FluidState getFluidState(BlockState state) {
        if (state.getValue(FLOWING)) return Fluids.FLOWING_WATER.getFlowing(1, false);
        return super.getFluidState(state);
    }

    @Override
    public ItemStack pickupBlock(LevelAccessor level, BlockPos pos, BlockState state) {
        return null;
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Optional.empty();
    }

    @Override
    public boolean canPlaceLiquid(BlockGetter level, BlockPos pos, BlockState state, Fluid fluid) {
        return state.getValue(HOLE) && fluid == Fluids.FLOWING_WATER;
    }

    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
        if (state.getValue(HOLE) && fluidState.getType() == Fluids.FLOWING_WATER) {
            if (!level.isClientSide()) {
                level.setBlock(pos, state.setValue(FLOWING, true), 3);
                //level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
                level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(level));
            }

            return true;
        } else {
            return false;
        }
    }

    public VoxelShape getHoly(VoxelShape input) {
        if (input == SHAPE_N) return SHAPE_HOLE_N;
        else if (input == SHAPE_E) return SHAPE_HOLE_E;
        else if (input == SHAPE_S) return SHAPE_HOLE_S;
        else if (input == SHAPE_W) return SHAPE_HOLE_W;

        else if (input == SHAPE_NE) return SHAPE_HOLE_NE;
        else if (input == SHAPE_NW) return SHAPE_HOLE_NW;
        else if (input == SHAPE_SE) return SHAPE_HOLE_SE;
        else if (input == SHAPE_SW) return SHAPE_HOLE_SW;
        else if (input == SHAPE_NS) return SHAPE_HOLE_NS;
        else if (input == SHAPE_EW) return SHAPE_HOLE_EW;

        else if (input == SHAPE_NES) return SHAPE_HOLE_NES;
        else if (input == SHAPE_ESW) return SHAPE_HOLE_ESW;
        else if (input == SHAPE_SWN) return SHAPE_HOLE_SWN;
        else if (input == SHAPE_WNE) return SHAPE_HOLE_WNE;

        else if (input == SHAPE_NESW) return SHAPE_HOLE_NESW;
        else if (input == SHAPE_NONE) return SHAPE_HOLE_NONE;

        return SHAPE_NESW;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        var north = state.getValue(NORTH);
        var south = state.getValue(SOUTH);
        var east = state.getValue(EAST);
        var west = state.getValue(WEST);
        var hole = state.getValue(HOLE);
        var model = SHAPE_NONE;


        if (north && south && east && west) {
            return hole ? SHAPE_HOLE_NESW : SHAPE_NESW;
        }
        else if (north) {
            model = SHAPE_N;

            if (east) {
                model = SHAPE_NE;

                if (west) {
                    return hole ? SHAPE_HOLE_WNE : SHAPE_WNE;
                }
            }

            if (west) {
                model = SHAPE_NW;

                if (south) {
                    return hole ? SHAPE_HOLE_SWN : SHAPE_SWN;
                }
            }

            if (south) {
                model = SHAPE_NS;

                if (east) {
                    return hole ? SHAPE_HOLE_NES : SHAPE_NES;
                }
            }
        }

        else if (east) {
            model = SHAPE_E;

            if (south) {
                model = SHAPE_SE;

                if (west) {
                    return hole ? SHAPE_HOLE_ESW : SHAPE_ESW;
                }
            }

            else if (west) {
                return hole ? SHAPE_HOLE_EW : SHAPE_EW;
            }
        }

        else if (south) {
            model = SHAPE_S;

            if (west) {
                return hole ? SHAPE_HOLE_SW : SHAPE_SW;
            }
        }

        else if (west) {
            return hole ? SHAPE_HOLE_W : SHAPE_W;
        }
        return hole ? getHoly(model) : model;
    }


    static {
        NORTH = PipeBlock.NORTH;
        EAST = PipeBlock.EAST;
        SOUTH = PipeBlock.SOUTH;
        WEST = PipeBlock.WEST;
        HOLE = ModBlockProperties.HOLE;
        FLOWING = ModBlockProperties.FLOWING;
        SUPPORTED = ModBlockProperties.SUPPORTED;
        PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION;
        SHAPE_BASE = Shapes.or(Block.box(0.0D, 12.0D, 0.0D, 16.0D, 16.0D, 16.0D));
        SHAPE_LEGS = Shapes.or(Block.box(-2.0D, 12.0D, -2.0D, 0.0D, 22.0D, 0.0D), Block.box(-2.0D, 12.0D, 16.0D, 0.0D, 22.0D, 18.0D), Block.box(16.0D, 12.0D, -2.0D, 18.0D, 22.0D, 0.0D), Block.box(16.0D, 12.0D, 16.0D, 18.0D, 22.0D, 18.0D));

        SHAPE_NONE = Shapes.or(SHAPE_LEGS, SHAPE_BASE);
        SHAPE_HOLE_NONE = Shapes.or(SHAPE_LEGS);

        SHAPE_N = Shapes.or(SHAPE_LEGS, SHAPE_BASE, Block.box(0.0D, 12.0D, -2.0D, 16.0D, 22.0D, 0.0D));
        SHAPE_E = Shapes.or(SHAPE_LEGS, SHAPE_BASE, Block.box(16.0D, 12.0D, 0.0D, 18.0D, 22.0D, 16.0D));
        SHAPE_S = Shapes.or(SHAPE_LEGS, SHAPE_BASE, Block.box(0.0D, 12.0D, 16.0D, 16.0D, 22.0D, 18.0D));
        SHAPE_W = Shapes.or(SHAPE_LEGS, SHAPE_BASE, Block.box(-2.0D, 12.0D, 0.0D, 0.0D, 22.0D, 16.0D));

        SHAPE_NE = Shapes.or(SHAPE_N, SHAPE_E);
        SHAPE_SE = Shapes.or(SHAPE_S, SHAPE_E);
        SHAPE_NW = Shapes.or(SHAPE_N, SHAPE_W);
        SHAPE_SW = Shapes.or(SHAPE_S, SHAPE_W);
        SHAPE_EW = Shapes.or(SHAPE_E, SHAPE_W);
        SHAPE_NS = Shapes.or(SHAPE_N, SHAPE_S);

        SHAPE_NES = Shapes.or(SHAPE_N, SHAPE_E, SHAPE_S);
        SHAPE_ESW = Shapes.or(SHAPE_E, SHAPE_S, SHAPE_W);
        SHAPE_SWN = Shapes.or(SHAPE_S, SHAPE_W, SHAPE_N);
        SHAPE_WNE = Shapes.or(SHAPE_W, SHAPE_N, SHAPE_E);
        SHAPE_NESW = Shapes.or(SHAPE_N, SHAPE_E, SHAPE_S, SHAPE_W);

        SHAPE_HOLE_N = Shapes.or(SHAPE_LEGS, Block.box(0.0D, 12.0D, -2.0D, 16.0D, 22.0D, 0.0D));
        SHAPE_HOLE_E = Shapes.or(SHAPE_LEGS, Block.box(16.0D, 12.0D, 0.0D, 18.0D, 22.0D, 16.0D));
        SHAPE_HOLE_S = Shapes.or(SHAPE_LEGS, Block.box(0.0D, 12.0D, 16.0D, 16.0D, 22.0D, 18.0D));
        SHAPE_HOLE_W = Shapes.or(SHAPE_LEGS, Block.box(-2.0D, 12.0D, 0.0D, 0.0D, 22.0D, 16.0D));

        SHAPE_HOLE_NE = Shapes.or(SHAPE_HOLE_N, SHAPE_HOLE_E);
        SHAPE_HOLE_SE = Shapes.or(SHAPE_HOLE_S, SHAPE_HOLE_E);
        SHAPE_HOLE_NW = Shapes.or(SHAPE_HOLE_N, SHAPE_HOLE_W);
        SHAPE_HOLE_SW = Shapes.or(SHAPE_HOLE_S, SHAPE_HOLE_W);
        SHAPE_HOLE_EW = Shapes.or(SHAPE_HOLE_E, SHAPE_HOLE_W);
        SHAPE_HOLE_NS = Shapes.or(SHAPE_HOLE_N, SHAPE_HOLE_S);

        SHAPE_HOLE_NES = Shapes.or(SHAPE_HOLE_N, SHAPE_HOLE_E, SHAPE_HOLE_S);
        SHAPE_HOLE_ESW = Shapes.or(SHAPE_HOLE_E, SHAPE_HOLE_S, SHAPE_HOLE_W);
        SHAPE_HOLE_SWN = Shapes.or(SHAPE_HOLE_S, SHAPE_HOLE_W, SHAPE_HOLE_N);
        SHAPE_HOLE_WNE = Shapes.or(SHAPE_HOLE_W, SHAPE_HOLE_N, SHAPE_HOLE_E);
        SHAPE_HOLE_NESW = Shapes.or(SHAPE_HOLE_N, SHAPE_HOLE_E, SHAPE_HOLE_S, SHAPE_HOLE_W);

    }
}
