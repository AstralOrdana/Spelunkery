package com.ordana.spelunkery.blocks;

import com.ordana.spelunkery.reg.ModBlockProperties;
import com.ordana.spelunkery.reg.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EndRodBlock;
import net.minecraft.world.level.block.RodBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Properties;

public class MineomiteBlock extends RodBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED;
    public static final BooleanProperty PRIMED;
    public static final IntegerProperty STICKS;
    protected static final VoxelShape ONE_AABB;
    protected static final VoxelShape TWO_AABB;
    protected static final VoxelShape THREE_AABB;
    protected static final VoxelShape FOUR_AABB;
    protected static final VoxelShape FIVE_AABB;
    protected static final VoxelShape SIX_AABB;
    protected static final VoxelShape SEVEN_AABB;
    protected static final VoxelShape EIGHT_AABB;
    protected static final VoxelShape NINE_AABB;
    protected static final VoxelShape ONE_AABB_X;
    protected static final VoxelShape TWO_AABB_X;
    protected static final VoxelShape THREE_AABB_X;
    protected static final VoxelShape FOUR_AABB_X;
    protected static final VoxelShape FIVE_AABB_X;
    protected static final VoxelShape SIX_AABB_X;
    protected static final VoxelShape SEVEN_AABB_X;
    protected static final VoxelShape EIGHT_AABB_X;
    protected static final VoxelShape NINE_AABB_X;
    protected static final VoxelShape ONE_AABB_Z;
    protected static final VoxelShape TWO_AABB_Z;
    protected static final VoxelShape THREE_AABB_Z;
    protected static final VoxelShape FOUR_AABB_Z;
    protected static final VoxelShape FIVE_AABB_Z;
    protected static final VoxelShape SIX_AABB_Z;
    protected static final VoxelShape SEVEN_AABB_Z;
    protected static final VoxelShape EIGHT_AABB_Z;
    protected static final VoxelShape NINE_AABB_Z;

    public MineomiteBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(((this.stateDefinition.any()).setValue(FACING, Direction.UP)).setValue(WATERLOGGED, false).setValue(STICKS, 1).setValue(PRIMED, false));
    }

    public static int getSticks(Level level, BlockPos pos) {
        return level.getBlockState(pos).getValue(STICKS);
    }

    /*
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(PRIMED)) {
            ModParticleUtils.spawnMineomiteParticles(level, pos, level.getBlockState(pos), ParticleTypes.FIREWORK, state.getValue(FACING), UniformInt.of(10, 15));
            //ParticleUtils.spawnParticlesAlongAxis(state.getValue(FACING).getAxis(), level, pos, 0.125D, ParticleTypes.FIREWORK, UniformInt.of(1, 2));
        }
    }
     */



    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!oldState.is(state.getBlock())) {
            if (level.hasNeighborSignal(pos)) {
                level.setBlock(pos, state.setValue(PRIMED, true), 3);
                level.scheduleTick(pos, this, 60);
            }
        }
    }

    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (level.hasNeighborSignal(pos)) {
            level.setBlock(pos, state.setValue(PRIMED, true), 3);
            level.scheduleTick(pos, this, 60);
        }
    }

    public void wasExploded(Level level, BlockPos pos, Explosion explosion) {
        if (!level.isClientSide) {

            /*
            PrimedMineomiteEntity primedTnt = new PrimedMineomiteEntity(level, 2, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, explosion.getSourceMob());
            int i = primedTnt.getFuse();
            primedTnt.setFuse((short)(level.random.nextInt(i / 4) + i / 8));
            level.addFreshEntity(primedTnt);
             */

            level.explode(null, pos.getX(), pos.getY(), pos.getZ(), 2, Explosion.BlockInteraction.BREAK);

        }
    }

    public static void explode(Level level, BlockPos pos) {
        explode(level, pos, null);
    }

    private static void explode(Level level, BlockPos pos, @Nullable LivingEntity entity) {
        if (!level.isClientSide) {
            float f = level.getBlockState(pos).getValue(STICKS) * 2;
            level.explode(entity, pos.getX(), pos.getY(), pos.getZ(), f, Explosion.BlockInteraction.BREAK);
            level.gameEvent(entity, GameEvent.PRIME_FUSE, pos);
        }
    }


    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (player.isSecondaryUseActive() && state.getValue(STICKS) > 1) {
            level.setBlockAndUpdate(pos, state.setValue(STICKS, state.getValue(STICKS) - 1));
            Block.popResourceFromFace(level, pos, hit.getDirection(), new ItemStack(ModBlocks.MINEOMITE.get()));
            return InteractionResult.SUCCESS;
        }
        if (!itemStack.is(Items.FLINT_AND_STEEL) && !itemStack.is(Items.FIRE_CHARGE)) {
            return super.use(state, level, pos, player, hand, hit);
        } else {
            level.setBlock(pos, state.setValue(PRIMED, true), 3);
            level.playSound(null, pos, SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.scheduleTick(pos, this, 60);
            Item item = itemStack.getItem();
            if (!player.isCreative()) {
                if (itemStack.is(Items.FLINT_AND_STEEL)) {
                    itemStack.hurtAndBreak(1, player, (playerx) -> {
                        playerx.broadcastBreakEvent(hand);
                    });
                } else {
                    itemStack.shrink(1);
                }
            }

            player.awardStat(Stats.ITEM_USED.get(item));
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }

    public void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        if (!level.isClientSide) {
            BlockPos blockPos = hit.getBlockPos();
            if (projectile.isOnFire() && projectile.mayInteract(level, blockPos)) {

                level.playSound(null, hit.getBlockPos(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.setBlock(blockPos, state.setValue(PRIMED, true), 3);
                level.scheduleTick(blockPos, this, 60);
            }
        }
    }

    public boolean dropFromExplosion(Explosion explosion) {
        return false;
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        BlockState blockState = context.getLevel().getBlockState(context.getClickedPos());
        boolean bl = fluidState.getType() == Fluids.WATER;
        if (blockState.is(this)) {
            return blockState.setValue(STICKS, Math.min(9, blockState.getValue(STICKS) + 1));
        } else return this.defaultBlockState().setValue(FACING, context.getClickedFace()).setValue(WATERLOGGED, bl);
    }

    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        return !useContext.isSecondaryUseActive() && useContext.getItemInHand().is(this.asItem()) && state.getValue(STICKS) < 9 || super.canBeReplaced(state, useContext);
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(FACING).getAxis() == Direction.Axis.X) {
            return switch (state.getValue(STICKS)) {
                default -> ONE_AABB_X;
                case 2 -> TWO_AABB_X;
                case 3 -> THREE_AABB_X;
                case 4 -> FOUR_AABB_X;
                case 5 -> FIVE_AABB_X;
                case 6 -> SIX_AABB_X;
                case 7 -> SEVEN_AABB_X;
                case 8 -> EIGHT_AABB_X;
                case 9 -> NINE_AABB_X;
            };
        }
        else if (state.getValue(FACING).getAxis() == Direction.Axis.Y) {
            return switch (state.getValue(STICKS)) {
                default -> ONE_AABB;
                case 2 -> TWO_AABB;
                case 3 -> THREE_AABB;
                case 4 -> FOUR_AABB;
                case 5 -> FIVE_AABB;
                case 6 -> SIX_AABB;
                case 7 -> SEVEN_AABB;
                case 8 -> EIGHT_AABB;
                case 9 -> NINE_AABB;
            };
        }
        else if (state.getValue(FACING).getAxis() == Direction.Axis.Z) {
            return switch (state.getValue(STICKS)) {
                default -> ONE_AABB_Z;
                case 2 -> TWO_AABB_Z;
                case 3 -> THREE_AABB_Z;
                case 4 -> FOUR_AABB_Z;
                case 5 -> FIVE_AABB_Z;
                case 6 -> SIX_AABB_Z;
                case 7 -> SEVEN_AABB_Z;
                case 8 -> EIGHT_AABB_Z;
                case 9 -> NINE_AABB_Z;
            };
        }
        else return ONE_AABB;
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
    }

    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    private void updateNeighbours(BlockState state, Level level, BlockPos pos) {
        level.updateNeighborsAt(pos.relative(state.getValue(FACING).getOpposite()), this);
    }

    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(PRIMED)) {
            explode(level, pos);
            level.removeBlock(pos, false);
        }
        this.updateNeighbours(state, level, pos);
    }

    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, STICKS, PRIMED);
    }

    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }

    public boolean isSignalSource(BlockState state) {
        return false;
    }

    static {
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        PRIMED = ModBlockProperties.PRIMED;
        STICKS = ModBlockProperties.STICKS;
        ONE_AABB_X = Shapes.or(Block.box(0.0D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D));
        TWO_AABB_X = Shapes.or(Block.box(0.0D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D), Block.box(0.0D, 6.0D, 11.0D, 16.0D, 10.0D, 15.0D));
        THREE_AABB_X = Shapes.or(Block.box(0.0D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D), Block.box(0.0D, 6.0D, 11.0D, 16.0D, 10.0D, 15.0D), Block.box(0.0D, 1.0D, 6.0D, 16.0D, 5.0D, 10.0D));
        FOUR_AABB_X = Shapes.or(Block.box(0.0D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D), Block.box(0.0D, 6.0D, 11.0D, 16.0D, 10.0D, 15.0D), Block.box(0.0D, 1.0D, 6.0D, 16.0D, 5.0D, 10.0D), Block.box(0.0D, 6.0D, 1.0D, 16.0D, 10.0D, 5.0D));
        FIVE_AABB_X = Shapes.or(Block.box(0.0D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D), Block.box(0.0D, 6.0D, 11.0D, 16.0D, 10.0D, 15.0D), Block.box(0.0D, 1.0D, 6.0D, 16.0D, 5.0D, 10.0D), Block.box(0.0D, 6.0D, 1.0D, 16.0D, 10.0D, 5.0D), Block.box(0.0D, 11.0D, 6.0D, 16.0D, 15.0D, 10.0D));
        SIX_AABB_X = Shapes.or(Block.box(0.0D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D), Block.box(0.0D, 6.0D, 11.0D, 16.0D, 10.0D, 15.0D), Block.box(0.0D, 1.0D, 6.0D, 16.0D, 5.0D, 10.0D), Block.box(0.0D, 6.0D, 1.0D, 16.0D, 10.0D, 5.0D), Block.box(0.0D, 11.0D, 6.0D, 16.0D, 15.0D, 10.0D), Block.box(0.0D, 1.0D, 1.0D, 16.0D, 5.0D, 5.0D));
        SEVEN_AABB_X = Shapes.or(Block.box(0.0D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D), Block.box(0.0D, 6.0D, 11.0D, 16.0D, 10.0D, 15.0D), Block.box(0.0D, 1.0D, 6.0D, 16.0D, 5.0D, 10.0D), Block.box(0.0D, 6.0D, 1.0D, 16.0D, 10.0D, 5.0D), Block.box(0.0D, 11.0D, 6.0D, 16.0D, 15.0D, 10.0D), Block.box(0.0D, 1.0D, 1.0D, 16.0D, 5.0D, 5.0D), Block.box(0.0D, 1.0D, 11.0D, 16.0D, 5.0D, 15.0D));
        EIGHT_AABB_X = Shapes.or(Block.box(0.0D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D), Block.box(0.0D, 6.0D, 11.0D, 16.0D, 10.0D, 15.0D), Block.box(0.0D, 1.0D, 6.0D, 16.0D, 5.0D, 10.0D), Block.box(0.0D, 6.0D, 1.0D, 16.0D, 10.0D, 5.0D), Block.box(0.0D, 11.0D, 6.0D, 16.0D, 15.0D, 10.0D), Block.box(0.0D, 1.0D, 1.0D, 16.0D, 5.0D, 5.0D), Block.box(0.0D, 1.0D, 11.0D, 16.0D, 5.0D, 15.0D), Block.box(0.0D, 11.0D, 1.0D, 16.0D, 15.0D, 5.0D));
        NINE_AABB_X = Shapes.or(Block.box(0.0D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D), Block.box(0.0D, 6.0D, 11.0D, 16.0D, 10.0D, 15.0D), Block.box(0.0D, 1.0D, 6.0D, 16.0D, 5.0D, 10.0D), Block.box(0.0D, 6.0D, 1.0D, 16.0D, 10.0D, 5.0D), Block.box(0.0D, 11.0D, 6.0D, 16.0D, 15.0D, 10.0D), Block.box(0.0D, 1.0D, 1.0D, 16.0D, 5.0D, 5.0D), Block.box(0.0D, 1.0D, 11.0D, 16.0D, 5.0D, 15.0D), Block.box(0.0D, 11.0D, 1.0D, 16.0D, 15.0D, 5.0D), Block.box(0.0D, 11.0D, 11.0D, 16.0D, 15.0D, 15.0D));

        ONE_AABB = Shapes.or(Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D));
        TWO_AABB = Shapes.or(Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D), Block.box(11.0D, 0.0D, 6.0D, 15.0D, 16.0D, 10.0D));
        THREE_AABB = Shapes.or(Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D), Block.box(11.0D, 0.0D, 6.0D, 15.0D, 16.0D, 10.0D), Block.box(6.0D, 0.0D, 1.0D, 10.0D, 16.0D, 5.0D));
        FOUR_AABB = Shapes.or(Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D), Block.box(11.0D, 0.0D, 6.0D, 15.0D, 16.0D, 10.0D), Block.box(6.0D, 0.0D, 1.0D, 10.0D, 16.0D, 5.0D), Block.box(1.0D, 0.0D, 6.0D, 5.0D, 16.0D, 10.0D));
        FIVE_AABB = Shapes.or(Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D), Block.box(11.0D, 0.0D, 6.0D, 15.0D, 16.0D, 10.0D), Block.box(6.0D, 0.0D, 1.0D, 10.0D, 16.0D, 5.0D), Block.box(1.0D, 0.0D, 6.0D, 5.0D, 16.0D, 10.0D), Block.box(6.0D, 0.0D, 11.0D, 10.0D, 16.0D, 15.0D));
        SIX_AABB = Shapes.or(Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D), Block.box(11.0D, 0.0D, 6.0D, 15.0D, 16.0D, 10.0D), Block.box(6.0D, 0.0D, 1.0D, 10.0D, 16.0D, 5.0D), Block.box(1.0D, 0.0D, 6.0D, 5.0D, 16.0D, 10.0D), Block.box(6.0D, 0.0D, 11.0D, 10.0D, 16.0D, 15.0D), Block.box(1.0D, 0.0D, 1.0D, 5.0D, 16.0D, 5.0D));
        SEVEN_AABB = Shapes.or(Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D), Block.box(11.0D, 0.0D, 6.0D, 15.0D, 16.0D, 10.0D), Block.box(6.0D, 0.0D, 1.0D, 10.0D, 16.0D, 5.0D), Block.box(1.0D, 0.0D, 6.0D, 5.0D, 16.0D, 10.0D), Block.box(6.0D, 0.0D, 11.0D, 10.0D, 16.0D, 15.0D), Block.box(1.0D, 0.0D, 1.0D, 5.0D, 16.0D, 5.0D), Block.box(11.0D, 0.0D, 1.0D, 15.0D, 16.0D, 5.0D));
        EIGHT_AABB = Shapes.or(Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D), Block.box(11.0D, 0.0D, 6.0D, 15.0D, 16.0D, 10.0D), Block.box(6.0D, 0.0D, 1.0D, 10.0D, 16.0D, 5.0D), Block.box(1.0D, 0.0D, 6.0D, 5.0D, 16.0D, 10.0D), Block.box(6.0D, 0.0D, 11.0D, 10.0D, 16.0D, 15.0D), Block.box(1.0D, 0.0D, 1.0D, 5.0D, 16.0D, 5.0D), Block.box(11.0D, 0.0D, 1.0D, 15.0D, 16.0D, 5.0D), Block.box(1.0D, 0.0D, 11.0D, 5.0D, 16.0D, 15.0D));
        NINE_AABB = Shapes.or(Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D), Block.box(11.0D, 0.0D, 6.0D, 15.0D, 16.0D, 10.0D), Block.box(6.0D, 0.0D, 1.0D, 10.0D, 16.0D, 5.0D), Block.box(1.0D, 0.0D, 6.0D, 5.0D, 16.0D, 10.0D), Block.box(6.0D, 0.0D, 11.0D, 10.0D, 16.0D, 15.0D), Block.box(1.0D, 0.0D, 1.0D, 5.0D, 16.0D, 5.0D), Block.box(11.0D, 0.0D, 1.0D, 15.0D, 16.0D, 5.0D), Block.box(1.0D, 0.0D, 11.0D, 5.0D, 16.0D, 15.0D), Block.box(11.0D, 0.0D, 11.0D, 15.0D, 16.0D, 15.0D));

        ONE_AABB_Z =   Shapes.or(Block.box(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 16.0D));
        TWO_AABB_Z =   Shapes.or(Block.box(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 16.0D), Block.box(11.0D, 6.0D, 0.0D, 15.0D, 10.0D, 16.0D));
        THREE_AABB_Z = Shapes.or(Block.box(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 16.0D), Block.box(11.0D, 6.0D, 0.0D, 15.0D, 10.0D, 16.0D), Block.box(6.0D, 1.0D, 0.0D, 10.0D, 5.0D, 16.0D));
        FOUR_AABB_Z =  Shapes.or(Block.box(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 16.0D), Block.box(11.0D, 6.0D, 0.0D, 15.0D, 10.0D, 16.0D), Block.box(6.0D, 1.0D, 0.0D, 10.0D, 5.0D, 16.0D), Block.box(1.0D, 6.0D, 0.0D, 5.0D, 10.0D, 16.0D));
        FIVE_AABB_Z =  Shapes.or(Block.box(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 16.0D), Block.box(11.0D, 6.0D, 0.0D, 15.0D, 10.0D, 16.0D), Block.box(6.0D, 1.0D, 0.0D, 10.0D, 5.0D, 16.0D), Block.box(1.0D, 6.0D, 0.0D, 5.0D, 10.0D, 16.0D), Block.box(6.0D, 11.0D, 0.0D, 10.0D, 15.0D, 16.0D));
        SIX_AABB_Z =   Shapes.or(Block.box(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 16.0D), Block.box(11.0D, 6.0D, 0.0D, 15.0D, 10.0D, 16.0D), Block.box(6.0D, 1.0D, 0.0D, 10.0D, 5.0D, 16.0D), Block.box(1.0D, 6.0D, 0.0D, 5.0D, 10.0D, 16.0D), Block.box(6.0D, 11.0D, 0.0D, 10.0D, 15.0D, 16.0D), Block.box(1.0D, 1.0D, 0.0D, 5.0D, 5.0D, 16.0D));
        SEVEN_AABB_Z = Shapes.or(Block.box(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 16.0D), Block.box(11.0D, 6.0D, 0.0D, 15.0D, 10.0D, 16.0D), Block.box(6.0D, 1.0D, 0.0D, 10.0D, 5.0D, 16.0D), Block.box(1.0D, 6.0D, 0.0D, 5.0D, 10.0D, 16.0D), Block.box(6.0D, 11.0D, 0.0D, 10.0D, 15.0D, 16.0D), Block.box(1.0D, 1.0D, 0.0D, 5.0D, 5.0D, 16.0D), Block.box(11.0D, 1.0D, 0.0D, 15.0D, 5.0D, 16.0D));
        EIGHT_AABB_Z = Shapes.or(Block.box(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 16.0D), Block.box(11.0D, 6.0D, 0.0D, 15.0D, 10.0D, 16.0D), Block.box(6.0D, 1.0D, 0.0D, 10.0D, 5.0D, 16.0D), Block.box(1.0D, 6.0D, 0.0D, 5.0D, 10.0D, 16.0D), Block.box(6.0D, 11.0D, 0.0D, 10.0D, 15.0D, 16.0D), Block.box(1.0D, 1.0D, 0.0D, 5.0D, 5.0D, 16.0D), Block.box(11.0D, 1.0D, 0.0D, 15.0D, 5.0D, 16.0D), Block.box(1.0D, 11.0D, 0.0D, 5.0D, 15.0D, 16.0D));
        NINE_AABB_Z =  Shapes.or(Block.box(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 16.0D), Block.box(11.0D, 6.0D, 0.0D, 15.0D, 10.0D, 16.0D), Block.box(6.0D, 1.0D, 0.0D, 10.0D, 5.0D, 16.0D), Block.box(1.0D, 6.0D, 0.0D, 5.0D, 10.0D, 16.0D), Block.box(6.0D, 11.0D, 0.0D, 10.0D, 15.0D, 16.0D), Block.box(1.0D, 1.0D, 0.0D, 5.0D, 5.0D, 16.0D), Block.box(11.0D, 1.0D, 0.0D, 15.0D, 5.0D, 16.0D), Block.box(1.0D, 11.0D, 0.0D, 5.0D, 15.0D, 16.0D), Block.box(11.0D, 11.0D, 0.0D, 15.0D, 15.0D, 16.0D));
    }
}


