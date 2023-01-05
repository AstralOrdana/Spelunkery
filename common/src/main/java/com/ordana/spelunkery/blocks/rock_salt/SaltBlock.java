package com.ordana.spelunkery.blocks.rock_salt;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.ordana.spelunkery.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class SaltBlock extends Block {

    public static final EnumProperty<RedstoneSide> NORTH = BlockStateProperties.NORTH_REDSTONE;
    public static final EnumProperty<RedstoneSide> EAST = BlockStateProperties.EAST_REDSTONE;
    public static final EnumProperty<RedstoneSide> SOUTH = BlockStateProperties.SOUTH_REDSTONE;
    public static final EnumProperty<RedstoneSide> WEST = BlockStateProperties.WEST_REDSTONE;
    //public static final IntegerProperty BURNING = ModBlockProperties.BURNING;


    public static final Map<Direction, EnumProperty<RedstoneSide>> PROPERTY_BY_DIRECTION = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, NORTH, Direction.EAST, EAST, Direction.SOUTH, SOUTH, Direction.WEST, WEST));
    private static final VoxelShape SHAPE_DOT = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 1.0D, 13.0D);
    private static final Map<Direction, VoxelShape> SHAPES_FLOOR = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.box(3.0D, 0.0D, 0.0D, 13.0D, 1.0D, 13.0D), Direction.SOUTH, Block.box(3.0D, 0.0D, 3.0D, 13.0D, 1.0D, 16.0D), Direction.EAST, Block.box(3.0D, 0.0D, 3.0D, 16.0D, 1.0D, 13.0D), Direction.WEST, Block.box(0.0D, 0.0D, 3.0D, 13.0D, 1.0D, 13.0D)));
    private static final Map<Direction, VoxelShape> SHAPES_UP = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Shapes.or(SHAPES_FLOOR.get(Direction.NORTH), Block.box(3.0D, 0.0D, 0.0D, 13.0D, 16.0D, 1.0D)), Direction.SOUTH, Shapes.or(SHAPES_FLOOR.get(Direction.SOUTH), Block.box(3.0D, 0.0D, 15.0D, 13.0D, 16.0D, 16.0D)), Direction.EAST, Shapes.or(SHAPES_FLOOR.get(Direction.EAST), Block.box(15.0D, 0.0D, 3.0D, 16.0D, 16.0D, 13.0D)), Direction.WEST, Shapes.or(SHAPES_FLOOR.get(Direction.WEST), Block.box(0.0D, 0.0D, 3.0D, 1.0D, 16.0D, 13.0D))));

    private final Map<BlockState, VoxelShape> SHAPES_CACHE;
    private final BlockState crossState;


    public SaltBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, RedstoneSide.NONE)
                .setValue(EAST, RedstoneSide.NONE).setValue(SOUTH, RedstoneSide.NONE)
                .setValue(WEST, RedstoneSide.NONE));
        this.crossState = this.defaultBlockState().setValue(NORTH, RedstoneSide.SIDE)
                .setValue(EAST, RedstoneSide.SIDE).setValue(SOUTH, RedstoneSide.SIDE)
                .setValue(WEST, RedstoneSide.SIDE);

        ImmutableMap.Builder<BlockState, VoxelShape> builder = ImmutableMap.builder();
        for (BlockState blockstate : this.getStateDefinition().getPossibleStates()) {
                builder.put(blockstate, this.calculateVoxelShape(blockstate));
        }
        this.SHAPES_CACHE = builder.build();

    }

    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (context instanceof EntityCollisionContext c) {
            var e = c.getEntity();
            if (e instanceof LivingEntity livingEntity && livingEntity.isInvertedHealAndHarm()) {
                return Block.box(0.0D, 0.0D, 0.0D, 16.0D, 24.0D, 16.0D);
            }
        }
        return Shapes.empty();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST);
    }

    private VoxelShape calculateVoxelShape(BlockState state) {
        VoxelShape voxelshape = SHAPE_DOT;

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            RedstoneSide redstoneside = state.getValue(PROPERTY_BY_DIRECTION.get(direction));
            if (redstoneside == RedstoneSide.SIDE) {
                voxelshape = Shapes.or(voxelshape, SHAPES_FLOOR.get(direction));
            } else if (redstoneside == RedstoneSide.UP) {
                voxelshape = Shapes.or(voxelshape, SHAPES_UP.get(direction));
            }
        }
        return voxelshape;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return this.SHAPES_CACHE.get(state);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.getConnectionState(context.getLevel(), this.crossState, context.getClickedPos());
    }

    @Override
    public boolean canBeReplaced(BlockState state, Fluid fluid) {
        return this.material.isReplaceable() || !this.material.isSolid();
    }

    //-----connection logic------

    private BlockState getConnectionState(BlockGetter world, BlockState state, BlockPos pos) {
        boolean flag = isDot(state);
        state = this.getMissingConnections(world, this.defaultBlockState(), pos);
        if (!flag || !isDot(state)) {
            boolean flag1 = state.getValue(NORTH).isConnected();
            boolean flag2 = state.getValue(SOUTH).isConnected();
            boolean flag3 = state.getValue(EAST).isConnected();
            boolean flag4 = state.getValue(WEST).isConnected();
            boolean flag5 = !flag1 && !flag2;
            boolean flag6 = !flag3 && !flag4;
            if (!flag4 && flag5) {
                state = state.setValue(WEST, RedstoneSide.SIDE);
            }

            if (!flag3 && flag5) {
                state = state.setValue(EAST, RedstoneSide.SIDE);
            }

            if (!flag1 && flag6) {
                state = state.setValue(NORTH, RedstoneSide.SIDE);
            }

            if (!flag2 && flag6) {
                state = state.setValue(SOUTH, RedstoneSide.SIDE);
            }
        }
        return state;
    }

    private BlockState getMissingConnections(BlockGetter world, BlockState state, BlockPos pos) {
        boolean flag = !world.getBlockState(pos.above()).isRedstoneConductor(world, pos);

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (!state.getValue(PROPERTY_BY_DIRECTION.get(direction)).isConnected()) {
                RedstoneSide redstoneside = this.getConnectingSide(world, pos, direction, flag);
                state = state.setValue(PROPERTY_BY_DIRECTION.get(direction), redstoneside);
            }
        }
        return state;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState otherState, LevelAccessor world, BlockPos pos, BlockPos otherPos) {
        //should be server only
        BlockState newState;
        if (direction == Direction.DOWN) {
            newState = this.canSurvive(state, world, pos) ? state : Blocks.AIR.defaultBlockState();
        } else if (direction == Direction.UP) {
            newState = this.getConnectionState(world, state, pos);
        } else {
            RedstoneSide redstoneside = this.getConnectingSide(world, pos, direction);
            newState = redstoneside.isConnected() == state.getValue(PROPERTY_BY_DIRECTION.get(direction)).isConnected() && !isCross(state) ?
                    state.setValue(PROPERTY_BY_DIRECTION.get(direction), redstoneside) :
                    this.getConnectionState(world, this.crossState.setValue(PROPERTY_BY_DIRECTION.get(direction), redstoneside), pos);
        }
        return newState;
    }

    private static boolean isCross(BlockState state) {
        return state.getValue(NORTH).isConnected() && state.getValue(SOUTH).isConnected() && state.getValue(EAST).isConnected() && state.getValue(WEST).isConnected();
    }

    private static boolean isDot(BlockState state) {
        return !state.getValue(NORTH).isConnected() && !state.getValue(SOUTH).isConnected() && !state.getValue(EAST).isConnected() && !state.getValue(WEST).isConnected();
    }

    //used to connect diagonally
    @Override
    public void updateIndirectNeighbourShapes(BlockState state, LevelAccessor world, BlockPos pos, int var1, int var2) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            RedstoneSide redstoneside = state.getValue(PROPERTY_BY_DIRECTION.get(direction));
            if (redstoneside != RedstoneSide.NONE && !world.getBlockState(mutable.setWithOffset(pos, direction)).is(this)) {
                mutable.move(Direction.DOWN);
                BlockState blockstate = world.getBlockState(mutable);
                if (!blockstate.is(Blocks.OBSERVER)) {
                    BlockPos blockpos = mutable.relative(direction.getOpposite());
                    BlockState blockstate1 = blockstate.updateShape(direction.getOpposite(), world.getBlockState(blockpos), world, mutable, blockpos);
                    updateOrDestroy(blockstate, blockstate1, world, mutable, var1, var2);
                }

                mutable.setWithOffset(pos, direction).move(Direction.UP);
                BlockState blockstate3 = world.getBlockState(mutable);
                if (!blockstate3.is(Blocks.OBSERVER)) {
                    BlockPos pos1 = mutable.relative(direction.getOpposite());
                    BlockState blockstate2 = blockstate3.updateShape(direction.getOpposite(), world.getBlockState(pos1), world, mutable, pos1);
                    updateOrDestroy(blockstate3, blockstate2, world, mutable, var1, var2);
                }
            }
        }

    }

    //gets connection to blocks diagonally above
    private RedstoneSide getConnectingSide(BlockGetter world, BlockPos pos, Direction dir) {
        return this.getConnectingSide(world, pos, dir, !world.getBlockState(pos.above()).isRedstoneConductor(world, pos));
    }

    private RedstoneSide getConnectingSide(BlockGetter world, BlockPos pos, Direction dir, boolean canClimbUp) {
        BlockPos blockpos = pos.relative(dir);
        BlockState blockstate = world.getBlockState(blockpos);
        if (canClimbUp) {
            boolean flag = this.canSurviveOn(world, blockpos, blockstate);
            if (flag && canConnectTo(world.getBlockState(blockpos.above()), world, blockpos.above(), null)) {
                if (blockstate.isFaceSturdy(world, blockpos, dir.getOpposite())) {
                    return RedstoneSide.UP;
                }
                return RedstoneSide.SIDE;
            }
        }
        return !canConnectTo(blockstate, world, blockpos, dir) && (blockstate.isRedstoneConductor(world, blockpos) || !canConnectTo(world.getBlockState(blockpos.below()), world, blockpos.below(), null)) ? RedstoneSide.NONE : RedstoneSide.SIDE;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState blockstate = world.getBlockState(blockpos);
        return this.canSurviveOn(world, blockpos, blockstate);
    }

    private boolean canSurviveOn(BlockGetter world, BlockPos pos, BlockState state) {
        return state.isFaceSturdy(world, pos, Direction.UP) || state.is(Blocks.HOPPER);
    }

    @SuppressWarnings("ConstantConditions")
    protected boolean canConnectTo(BlockState state, BlockGetter world, BlockPos pos, @Nullable Direction dir) {
        Block b = state.getBlock();
        return state.is(ModTags.SALT_BLOCKS) || state.is(this);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return switch (rotation) {
            case CLOCKWISE_180 ->
                    state.setValue(NORTH, state.getValue(SOUTH)).setValue(EAST, state.getValue(WEST)).setValue(SOUTH, state.getValue(NORTH)).setValue(WEST, state.getValue(EAST));
            case COUNTERCLOCKWISE_90 ->
                    state.setValue(NORTH, state.getValue(EAST)).setValue(EAST, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(WEST)).setValue(WEST, state.getValue(NORTH));
            case CLOCKWISE_90 ->
                    state.setValue(NORTH, state.getValue(WEST)).setValue(EAST, state.getValue(NORTH)).setValue(SOUTH, state.getValue(EAST)).setValue(WEST, state.getValue(SOUTH));
            default -> state;
        };
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return switch (mirror) {
            case LEFT_RIGHT -> state.setValue(NORTH, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(NORTH));
            case FRONT_BACK -> state.setValue(EAST, state.getValue(WEST)).setValue(WEST, state.getValue(EAST));
            default -> super.mirror(state, mirror);
        };
    }


    //-----redstone------

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean moving) {
        if (!oldState.is(state.getBlock()) && !world.isClientSide) {
            //doesn't ignite immediately
            world.scheduleTick(pos, this, 2);

            for (Direction direction : Direction.Plane.VERTICAL) {
                world.updateNeighborsAt(pos.relative(direction), this);
            }

            this.updateNeighborsOfNeighboringWires(world, pos);
        }
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!isMoving && !state.is(newState.getBlock())) {
            super.onRemove(state, world, pos, newState, isMoving);
            if (!world.isClientSide) {
                for (Direction direction : Direction.values()) {
                    world.updateNeighborsAt(pos.relative(direction), this);
                }
                this.updateNeighborsOfNeighboringWires(world, pos);
            }
        }
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which
     * neighbor changed (coordinates passed are their own) Args: x, y, z,
     * neighbor Block
     */
    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean moving) {
        super.neighborChanged(state, world, pos, neighborBlock, neighborPos, moving);
        if (!world.isClientSide) {
            world.scheduleTick(pos, this, 2);
        }
    }

    private void updateNeighborsOfNeighboringWires(Level world, BlockPos pos) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            this.checkCornerChangeAt(world, pos.relative(direction));
        }

        for (Direction direction1 : Direction.Plane.HORIZONTAL) {
            BlockPos blockpos = pos.relative(direction1);
            if (world.getBlockState(blockpos).isRedstoneConductor(world, blockpos)) {
                this.checkCornerChangeAt(world, blockpos.above());
            } else {
                this.checkCornerChangeAt(world, blockpos.below());
            }
        }
    }

    private void checkCornerChangeAt(Level world, BlockPos pos) {
        if (world.getBlockState(pos).is(this)) {
            world.updateNeighborsAt(pos, this);

            for (Direction direction : Direction.values()) {
                world.updateNeighborsAt(pos.relative(direction), this);
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        InteractionResult lightUp = super.use(state, world, pos, player, hand, hit);
        if (lightUp.consumesAction()) return lightUp;
        if (player.getAbilities().mayBuild) {
            if (isCross(state) || isDot(state)) {
                BlockState blockstate = isCross(state) ? this.defaultBlockState() : this.crossState;
                blockstate = this.getConnectionState(world, blockstate, pos);
                if (blockstate != state) {
                    world.setBlock(pos, blockstate, 3);
                    this.updatesOnShapeChange(world, pos, state, blockstate);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    private void updatesOnShapeChange(Level world, BlockPos pos, BlockState state, BlockState newState) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos blockpos = pos.relative(direction);
            if (state.getValue(PROPERTY_BY_DIRECTION.get(direction)).isConnected() != newState.getValue(PROPERTY_BY_DIRECTION.get(direction)).isConnected() && world.getBlockState(blockpos).isRedstoneConductor(world, blockpos)) {
                world.updateNeighborsAtExceptFromFacing(blockpos, newState.getBlock(), direction.getOpposite());
            }
        }
    }

    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }

    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity && ((LivingEntity) entity).isInvertedHealAndHarm()) {
            entity.hurt(DamageSource.HOT_FLOOR, 1.0F);
            entity.setRemainingFireTicks(8);
        }
    }
}