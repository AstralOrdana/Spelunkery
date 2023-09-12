package com.ordana.spelunkery.blocks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.ordana.spelunkery.blocks.entity.WoodenSluiceBlockEntity;
import com.ordana.spelunkery.reg.ModBlockProperties;
import com.ordana.spelunkery.reg.ModEntities;
import net.minecraft.Util;
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
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

public class WoodenSluiceBlock extends ModBaseEntityBlock {
    public static final BooleanProperty GRATE_NORTH;
    public static final BooleanProperty GRATE_SOUTH;
    public static final BooleanProperty GRATE_EAST;
    public static final BooleanProperty GRATE_WEST;

    public static final Map<Direction, BooleanProperty> GRATE_PROPERTY_BY_DIRECTION;

    public WoodenSluiceBlock(Properties properties) {
        super(properties);
        super.registerDefaultState((this.stateDefinition.any()).setValue(GRATE_NORTH, false).setValue(GRATE_SOUTH, false).setValue(GRATE_EAST, false).setValue(GRATE_WEST, false));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SUPPORTED, NORTH, EAST, SOUTH, WEST, GRATE_NORTH, GRATE_EAST, GRATE_SOUTH, GRATE_WEST);
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        boolean axes = stack.is(ItemTags.AXES);
        var dir = hit.getDirection();

        if (axes  && (dir != Direction.DOWN && dir != Direction.UP)) {
            if (state.getValue(PROPERTY_BY_DIRECTION.get(dir))) {
                level.setBlock(pos, state.setValue(PROPERTY_BY_DIRECTION.get(dir), false).setValue(GRATE_PROPERTY_BY_DIRECTION.get(dir), true), 3);
                level.playSound(null, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.playSound(null, pos, SoundEvents.WOOD_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.playSound(null, pos, SoundEvents.CHAIN_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                ParticleUtils.spawnParticlesOnBlockFaces(level, pos, new BlockParticleOption(ParticleTypes.BLOCK, this.defaultBlockState()), UniformInt.of(3, 5));
            }
            else if (state.getValue(GRATE_PROPERTY_BY_DIRECTION.get(dir))) {
                level.setBlock(pos, state.setValue(GRATE_PROPERTY_BY_DIRECTION.get(dir), false), 3);
                level.playSound(null, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.playSound(null, pos, SoundEvents.CHAIN_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
                ParticleUtils.spawnParticlesOnBlockFaces(level, pos, new BlockParticleOption(ParticleTypes.BLOCK, this.defaultBlockState()), UniformInt.of(3, 5));
            }
            else if (!state.getValue(GRATE_PROPERTY_BY_DIRECTION.get(dir))) {
                level.setBlock(pos, state.setValue(PROPERTY_BY_DIRECTION.get(dir), true), 3);
                level.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (!level.getBlockState(pos.relative(dir).above()).isCollisionShapeFullBlock(level, pos)) level.setBlock(pos.relative(dir).above(), Blocks.AIR.defaultBlockState(), 3);
            }
            if (!player.isCreative()) {
                stack.hurtAndBreak(1, player, (playerx)
                        -> playerx.broadcastBreakEvent(hand));
            }

            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        else if (level.isClientSide) {
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
            if (level.getFluidState(neighborPos).is(Fluids.FLOWING_WATER) && !(state.getValue(WoodenChannelBlock.PROPERTY_BY_DIRECTION.get(direction))) && state.getValue(GRATE_PROPERTY_BY_DIRECTION.get(direction))) {
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
        super.tick(state, level, pos, random);

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

    static {
        GRATE_NORTH = ModBlockProperties.GRATE_NORTH;
        GRATE_EAST = ModBlockProperties.GRATE_EAST;
        GRATE_SOUTH = ModBlockProperties.GRATE_SOUTH;
        GRATE_WEST = ModBlockProperties.GRATE_WEST;
        GRATE_PROPERTY_BY_DIRECTION = ImmutableMap.copyOf((Map) Util.make(Maps.newEnumMap(Direction.class), (enumMap) -> {
            enumMap.put(Direction.NORTH, GRATE_NORTH);
            enumMap.put(Direction.EAST, GRATE_EAST);
            enumMap.put(Direction.SOUTH, GRATE_SOUTH);
            enumMap.put(Direction.WEST, GRATE_WEST);
        }));
    }

}
