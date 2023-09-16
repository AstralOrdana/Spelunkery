package com.ordana.spelunkery.blocks;

import com.ordana.spelunkery.reg.ModBlockProperties;
import com.ordana.spelunkery.reg.ModBlocks;
import dev.architectury.injectables.annotations.PlatformOnly;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ChannelBlock extends Block {
    public static final BooleanProperty NORTH;
    public static final BooleanProperty SOUTH;
    public static final BooleanProperty EAST;
    public static final BooleanProperty WEST;
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

    public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION;

    public ChannelBlock(Properties properties) {
        super(properties);
        this.registerDefaultState((this.stateDefinition.any()).setValue(NORTH, true).setValue(SOUTH, true).setValue(EAST, true).setValue(WEST, true).setValue(SUPPORTED, false));
    }

    //@Override
    @PlatformOnly(PlatformOnly.FORGE)
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return state.is(ModBlocks.WOODEN_CHANNEL.get());
    }


    //@Override
    @PlatformOnly(PlatformOnly.FORGE)
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return state.is(ModBlocks.WOODEN_CHANNEL.get()) ? 20 : 0;
    }

    //@Override
    @PlatformOnly(PlatformOnly.FORGE)
    public int getFireSpread(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return state.is(ModBlocks.WOODEN_CHANNEL.get()) ? 5 : 0;
    }


    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SUPPORTED, NORTH, EAST, SOUTH, WEST);
    }

    public boolean checkNeighborsForChannel(BlockPos pos, LevelAccessor level, Direction dir) {
        return level.getBlockState(pos.relative(dir)).getBlock() instanceof ChannelBlock
                && level.getBlockState(pos.relative(dir.getOpposite())).getBlock() instanceof ChannelBlock;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        var belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);
        boolean bl = belowState.isFaceSturdy(level, belowPos, Direction.UP) || belowState.getBlock() instanceof ChannelBlock;
        if (checkNeighborsForChannel(pos, level, Direction.NORTH) || checkNeighborsForChannel(pos, level, Direction.WEST)) bl = false;
        level.setBlock(pos, state.setValue(SUPPORTED, bl), 3);
        super.tick(state, level, pos, random);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockGetter blockGetter = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockPos belowPos = pos.below();
        BlockState belowState = blockGetter.getBlockState(belowPos);
        boolean bl = belowState.isFaceSturdy(blockGetter, belowPos, Direction.UP) || belowState.getBlock() instanceof ChannelBlock;
        if (checkNeighborsForChannel(pos, context.getLevel(), Direction.NORTH) || checkNeighborsForChannel(pos, context.getLevel(), Direction.WEST)) bl = false;

        return this.defaultBlockState().setValue(NORTH, !(blockGetter.getBlockState(pos.north()).getBlock() instanceof ChannelBlock)).setValue(EAST, !(blockGetter.getBlockState(pos.east()).getBlock() instanceof ChannelBlock)).setValue(SOUTH, !(blockGetter.getBlockState(pos.south()).getBlock() instanceof ChannelBlock)).setValue(WEST, !(blockGetter.getBlockState(pos.west()).getBlock() instanceof ChannelBlock)).setValue(SUPPORTED, bl);
    }

    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        level.scheduleTick(pos, this, 1);
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        level.scheduleTick(pos, this, 1);
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);

        boolean bl = belowState.isFaceSturdy(level, belowPos, Direction.UP) || belowState.getBlock() instanceof ChannelBlock;
        if (checkNeighborsForChannel(pos, level, Direction.NORTH) || checkNeighborsForChannel(pos, level, Direction.WEST)) bl = false;
        state.setValue(SUPPORTED, bl);
        return neighborState.getBlock() instanceof ChannelBlock && direction != Direction.UP && direction != Direction.DOWN ? state.setValue(PROPERTY_BY_DIRECTION.get(direction), false) : super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);
        Item item = itemStack.getItem();
        var dir = hit.getDirection();
        boolean stone = state.is(ModBlocks.STONE_CHANNEL.get());
        boolean tool = stone ? itemStack.is(ItemTags.PICKAXES) : itemStack.is(ItemTags.AXES);

        if (dir == Direction.UP || dir == Direction.DOWN || !tool) {
            return super.use(state, level, pos, player, hand, hit);
        } else {
            var propDir = PROPERTY_BY_DIRECTION.get(dir);
            var check = state.getValue(propDir);
            level.setBlock(pos, state.setValue(propDir, !check), 3);
            if (!stone) level.playSound(null, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.playSound(null, pos, check ? (stone ? SoundEvents.STONE_BREAK : SoundEvents.WOOD_BREAK) : (stone ? SoundEvents.STONE_PLACE : SoundEvents.WOOD_PLACE), SoundSource.BLOCKS, 1.0F, 1.0F);
            ParticleUtils.spawnParticlesOnBlockFaces(level, pos, new BlockParticleOption(ParticleTypes.BLOCK, this.defaultBlockState()), UniformInt.of(3, 5));
            if (!level.getFluidState(pos.relative(dir).above()).is(Fluids.EMPTY)) level.setBlock(pos.relative(dir).above(), Blocks.AIR.defaultBlockState(), 3);

                if (!player.isCreative()) {
                    itemStack.hurtAndBreak(1, player, (playerx)
                            -> playerx.broadcastBreakEvent(hand));
                }

                player.awardStat(Stats.ITEM_USED.get(item));
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            //return InteractionResult.SUCCESS;

    }

    @Override
    public VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        var north = state.getValue(NORTH);
        var south = state.getValue(SOUTH);
        var east = state.getValue(EAST);
        var west = state.getValue(WEST);
        var model = SHAPE_NONE;

        if (north && south && east && west) {
            return SHAPE_NESW;
        }
        else if (north) {
            model = SHAPE_N;

            if (east) {
                model = SHAPE_NE;

                if (west) {
                    return SHAPE_WNE;
                }
            }

            if (west) {
                model = SHAPE_NW;

                if (south) {
                    return SHAPE_SWN;
                }
            }

            if (south) {
                model = SHAPE_NS;

                if (east) {
                    return SHAPE_NES;
                }
            }
        }

        else if (east) {
            model = SHAPE_E;

            if (south) {
                model = SHAPE_SE;

                if (west) {
                    return SHAPE_ESW;
                }
            }

            else if (west) {
                return SHAPE_EW;
            }
        }

        else if (south) {
            model = SHAPE_S;

            if (west) {
                return SHAPE_SW;
            }
        }

        else if (west) {
            return SHAPE_W;
        }
        return model;
    }


    static {
        NORTH = PipeBlock.NORTH;
        EAST = PipeBlock.EAST;
        SOUTH = PipeBlock.SOUTH;
        WEST = PipeBlock.WEST;
        SUPPORTED = ModBlockProperties.SUPPORTED;
        PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION;
        SHAPE_BASE = Shapes.or(Block.box(0.0D, 12.0D, 0.0D, 16.0D, 16.0D, 16.0D));
        SHAPE_LEGS = Shapes.or(Block.box(-2.0D, 12.0D, -2.0D, 0.0D, 22.0D, 0.0D), Block.box(-2.0D, 12.0D, 16.0D, 0.0D, 22.0D, 18.0D), Block.box(16.0D, 12.0D, -2.0D, 18.0D, 22.0D, 0.0D), Block.box(16.0D, 12.0D, 16.0D, 18.0D, 22.0D, 18.0D));

        SHAPE_NONE = Shapes.or(SHAPE_LEGS, SHAPE_BASE);

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

    }
}
