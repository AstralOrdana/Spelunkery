package com.ordana.spelunkery.blocks;

import com.ordana.spelunkery.blocks.entity.MagnetiteBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEventListener;
import org.jetbrains.annotations.Nullable;

public class MagnetiteBlock extends BaseEntityBlock {
    public MagnetiteBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MagnetiteBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> GameEventListener getListener(ServerLevel serverLevel, T blockEntity) {
        return blockEntity instanceof MagnetiteBlockEntity t ? t : null;
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
