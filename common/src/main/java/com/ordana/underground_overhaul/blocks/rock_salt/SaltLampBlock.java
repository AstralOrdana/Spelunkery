package com.ordana.underground_overhaul.blocks.rock_salt;

import com.ordana.underground_overhaul.reg.ModBlockProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SaltLampBlock extends LanternBlock implements RockSalt {
    protected static final VoxelShape SHAPE;
    protected static final VoxelShape HANGING_SHAPE;

    public SaltLampBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(ILLUMINATED, false).setValue(HANGING, false).setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        super.createBlockStateDefinition(stateManager);
        stateManager.add(ILLUMINATED);
    }


    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!level.isClientSide) {
            state = state.cycle(ILLUMINATED);
            level.setBlock(pos, state, 3);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }


    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(HANGING) ? HANGING_SHAPE : SHAPE;
    }

    static {
        SHAPE = Shapes.or(Block.box(5.0D, 0.0D, 5.0D, 11.0D, 2.0D, 11.0D), Block.box(3.0D, 2.0D, 3.0D, 13.0D, 15.0D, 13.0D));
        HANGING_SHAPE = Shapes.or(Block.box(5.0D, 14.0D, 5.0D, 11.0D, 16.0D, 11.0D), Block.box(3.0D, 1.0D, 3.0D, 13.0D, 14.0D, 13.0D));
    }
}
