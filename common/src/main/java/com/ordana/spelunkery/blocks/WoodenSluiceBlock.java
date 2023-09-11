package com.ordana.spelunkery.blocks;

import com.ordana.spelunkery.blocks.entity.WoodenSluiceBlockEntity;
import com.ordana.spelunkery.reg.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class WoodenSluiceBlock extends ModBaseEntityBlock {
    public WoodenSluiceBlock(Properties properties) {
        super(properties);
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);
        boolean planks = itemStack.is(ItemTags.PLANKS);
        boolean axes = itemStack.is(ItemTags.AXES);
        if (planks || axes) {
            return super.use(state, level, pos, player, hand, hit);
        } else if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        else {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof WoodenSluiceBlockEntity) {
                player.openMenu((WoodenSluiceBlockEntity)blockEntity);
            }

            return InteractionResult.CONSUME;
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (context instanceof EntityCollisionContext c && c.getEntity() instanceof ItemEntity) return SHAPE_NESW;
        return super.getShape(state, level, pos, context);
    }

    public static int getFlow(Level level, BlockState state, BlockPos pos) {
        int flowCount = 0;
        for (var direction : Direction.Plane.HORIZONTAL) {
            BlockPos neighborPos = pos.relative(direction);
            if (level.getFluidState(neighborPos).is(Fluids.FLOWING_WATER) && state.getValue(WoodenChannelBlock.PROPERTY_BY_DIRECTION.get(direction))) {
                flowCount += 1;
            }
        }
        return flowCount;
    }

    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof Container) {
                Containers.dropContents(level, pos, (Container)blockEntity);
                level.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, level, pos, newState, movedByPiston);
        }
    }

    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof WoodenSluiceBlockEntity) {
            ((WoodenSluiceBlockEntity)blockEntity).recheckOpen();
        }

    }

    @org.jetbrains.annotations.Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WoodenSluiceBlockEntity(pos, state);
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @org.jetbrains.annotations.Nullable LivingEntity placer, ItemStack stack) {
        if (stack.hasCustomHoverName()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof WoodenSluiceBlockEntity) {
                ((WoodenSluiceBlockEntity)blockEntity).setCustomName(stack.getHoverName());
            }
        }

    }

    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
    }
    public boolean triggerEvent(BlockState state, Level level, BlockPos pos, int id, int param) {
        super.triggerEvent(state, level, pos, id, param);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity != null && blockEntity.triggerEvent(id, param);
    }

    @javax.annotation.Nullable
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity instanceof MenuProvider ? (MenuProvider)blockEntity : null;
    }


    @org.jetbrains.annotations.Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (!level.isClientSide) {
            return createTickerHelper(blockEntityType, ModEntities.WOODEN_SLUICE.get(), WoodenSluiceBlockEntity::pushItemsTick);
        } else {
            return super.getTicker(level, state, blockEntityType);
        }
    }

}
