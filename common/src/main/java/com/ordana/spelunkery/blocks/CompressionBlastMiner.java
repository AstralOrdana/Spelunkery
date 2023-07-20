package com.ordana.spelunkery.blocks;

import com.ordana.spelunkery.reg.ModBlockProperties;
import com.ordana.spelunkery.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class CompressionBlastMiner extends DirectionalBlock {
    public static final BooleanProperty POWERED;
    public static final BooleanProperty PRIMED;

    public CompressionBlastMiner(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.SOUTH).setValue(POWERED, false).setValue(PRIMED, false));
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(Items.TNT) && !state.getValue(PRIMED)) {
            level.setBlockAndUpdate(pos, state.setValue(PRIMED, true));
            if (!player.getAbilities().instabuild) itemStack.shrink(1);
            return InteractionResult.SUCCESS;
        }
        if (player.isSecondaryUseActive() && state.getValue(PRIMED)) {
            level.setBlockAndUpdate(pos, state.setValue(PRIMED, false));
            Block.popResourceFromFace(level, pos, hit.getDirection(), Items.TNT.getDefaultInstance());
            return InteractionResult.SUCCESS;
        }
        else return itemStack.getItem() instanceof BlockItem && (new BlockPlaceContext(player, hand, itemStack, hit)).canPlace() ? InteractionResult.PASS : InteractionResult.SUCCESS;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED, PRIMED);
    }

    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    public void blast(BlockState state, Level level, BlockPos pos) {
        Direction dir = state.getValue(FACING);
        if (state.getValue(PRIMED) && !level.getBlockState(pos.relative(dir)).is(ModTags.BLAST_MINER_IMMUNE)) {
            level.setBlockAndUpdate(pos, state.setValue(PRIMED, false));
            level.destroyBlock(pos.relative(dir), false);
            level.explode(null, level.damageSources().badRespawnPointExplosion(pos.getCenter()), null, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, 5.0F, false, Level.ExplosionInteraction.TNT);
        }
    }


    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        boolean bl = level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.above());
        boolean bl2 = state.getValue(POWERED);
        if (bl && !bl2) {
            level.scheduleTick(pos, this, 4);
            level.setBlock(pos, state.setValue(POWERED, true), 4);
        } else if (!bl && bl2) {
            level.setBlock(pos, state.setValue(POWERED, false), 4);
        }

    }

    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.blast(state, level, pos);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite().getOpposite()).setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    static {
        POWERED = BlockStateProperties.POWERED;
        PRIMED = ModBlockProperties.PRIMED;
    }

}
