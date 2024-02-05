package com.ordana.spelunkery.blocks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.blocks.entity.SluiceBlockEntity;
import com.ordana.spelunkery.reg.ModBlockProperties;
import com.ordana.spelunkery.reg.ModBlocks;
import com.ordana.spelunkery.reg.ModEntities;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
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
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;
import java.util.Objects;

public class SluiceBlock extends ModBaseEntityBlock {
    public static final BooleanProperty GRATE_NORTH;
    public static final BooleanProperty GRATE_SOUTH;
    public static final BooleanProperty GRATE_EAST;
    public static final BooleanProperty GRATE_WEST;

    protected static final VoxelShape SHAPE_TALL_N;
    protected static final VoxelShape SHAPE_TALL_E;
    protected static final VoxelShape SHAPE_TALL_S;
    protected static final VoxelShape SHAPE_TALL_W;

    protected static final VoxelShape SHAPE_TALL_NE;
    protected static final VoxelShape SHAPE_TALL_SE;
    protected static final VoxelShape SHAPE_TALL_NW;
    protected static final VoxelShape SHAPE_TALL_SW;
    protected static final VoxelShape SHAPE_TALL_EW;
    protected static final VoxelShape SHAPE_TALL_NS;

    protected static final VoxelShape SHAPE_TALL_NES;
    protected static final VoxelShape SHAPE_TALL_ESW;
    protected static final VoxelShape SHAPE_TALL_SWN;
    protected static final VoxelShape SHAPE_TALL_WNE;
    protected static final VoxelShape SHAPE_TALL_NESW;
    protected static final VoxelShape SHAPE_TALL_NONE;

    public static final Map<Direction, BooleanProperty> GRATE_PROPERTY_BY_DIRECTION;

    public SluiceBlock(Properties properties) {
        super(properties);
        super.registerDefaultState((this.stateDefinition.any()).setValue(GRATE_NORTH, false).setValue(GRATE_SOUTH, false).setValue(GRATE_EAST, false).setValue(GRATE_WEST, false));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SUPPORTED, NORTH, EAST, SOUTH, WEST, GRATE_NORTH, GRATE_EAST, GRATE_SOUTH, GRATE_WEST);
    }

    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(GRATE_NORTH) || state.getValue(GRATE_EAST) || state.getValue(GRATE_WEST) || state.getValue(GRATE_SOUTH);
    }

    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int flow = SluiceBlock.getFlow(level, state, pos);
        if (!(level.getBlockEntity(pos) instanceof SluiceBlockEntity sluice) || flow == 0) return;

        var fluidName = Utils.getID(level.getFluidState(pos.above()).getType()).getPath();

        if (!Objects.equals(fluidName, "empty")) {

            if (fluidName.contains("flowing_")) fluidName = fluidName.replace("flowing_", "");

            var tablePath = Spelunkery.res("gameplay/sluice/" + fluidName + "/passive");
            var lootTable = Objects.requireNonNull(level.getServer()).getLootTables().get(tablePath);

            LootContext.Builder builder = (new LootContext.Builder(level))
                    .withParameter(LootContextParams.BLOCK_STATE, level.getBlockState(pos))
                    .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                    .withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
                    .withOptionalParameter(LootContextParams.BLOCK_ENTITY, sluice);

            for (int i = 0; i < flow; ++i) {
                var lootItem = lootTable.getRandomItems(builder.create(LootContextParamSets.BLOCK));
                if (lootItem.isEmpty()) return;

                if (lootItem.iterator().next().getItem() instanceof SpawnEggItem egg) {

                    Entity eggEntity = egg.getType(lootItem.iterator().next().getTag()).create(level);
                    if (eggEntity != null) {
                        eggEntity.moveTo(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
                        level.addFreshEntity(eggEntity);
                        return;
                    }
                }

                var bl = SluiceBlockEntity.suckInItems(sluice, lootItem.iterator().next());
                SluiceBlockEntity.tryFilterItems(level, pos, state, sluice, flow, () -> bl);

            }
        }
    }


    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        boolean stone = state.is(ModBlocks.STONE_SLUICE.get());
        boolean tool = stack.getItem().isCorrectToolForDrops(state);
        var dir = hit.getDirection();

        if (tool  && (dir != Direction.DOWN && dir != Direction.UP)) {
            if (state.getValue(PROPERTY_BY_DIRECTION.get(dir))) {
                level.setBlock(pos, state.setValue(PROPERTY_BY_DIRECTION.get(dir), false).setValue(GRATE_PROPERTY_BY_DIRECTION.get(dir), true), 3);
                if (!stone) level.playSound(null, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.playSound(null, pos, stone ? SoundEvents.STONE_BREAK : SoundEvents.WOOD_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.playSound(null, pos, SoundEvents.CHAIN_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                ParticleUtils.spawnParticlesOnBlockFaces(level, pos, new BlockParticleOption(ParticleTypes.BLOCK, this.defaultBlockState()), UniformInt.of(3, 5));
            }
            else if (state.getValue(GRATE_PROPERTY_BY_DIRECTION.get(dir))) {
                level.setBlock(pos, state.setValue(GRATE_PROPERTY_BY_DIRECTION.get(dir), false), 3);
                if (!stone) level.playSound(null, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.playSound(null, pos, SoundEvents.CHAIN_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
                ParticleUtils.spawnParticlesOnBlockFaces(level, pos, new BlockParticleOption(ParticleTypes.BLOCK, this.defaultBlockState()), UniformInt.of(3, 5));
            }
            else if (!state.getValue(GRATE_PROPERTY_BY_DIRECTION.get(dir))) {
                level.setBlock(pos, state.setValue(PROPERTY_BY_DIRECTION.get(dir), true), 3);
                level.playSound(null, pos, stone ? SoundEvents.STONE_PLACE : SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (!level.getFluidState(pos.relative(dir).above()).is(Fluids.EMPTY)) level.setBlock(pos.relative(dir).above(), Blocks.AIR.defaultBlockState(), 3);
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
            if (blockEntity instanceof SluiceBlockEntity) {
                player.openMenu((SluiceBlockEntity)blockEntity);
            }

            return InteractionResult.CONSUME;
        }
    }


    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (context instanceof EntityCollisionContext c && c.getEntity() instanceof ItemEntity) {
            var north = state.getValue(NORTH) || state.getValue(GRATE_NORTH);
            var south = state.getValue(SOUTH) || state.getValue(GRATE_SOUTH);
            var east = state.getValue(EAST) || state.getValue(GRATE_EAST);
            var west = state.getValue(WEST) || state.getValue(GRATE_WEST);
            var model = SHAPE_TALL_NONE;

            if (north && south && east && west) {
                return SHAPE_TALL_NESW;
            }
            else if (north) {
                model = SHAPE_TALL_N;

                if (east) {
                    model = SHAPE_TALL_NE;

                    if (west) {
                        return SHAPE_TALL_WNE;
                    }
                }

                if (west) {
                    model = SHAPE_TALL_NW;

                    if (south) {
                        return SHAPE_TALL_SWN;
                    }
                }

                if (south) {
                    model = SHAPE_TALL_NS;

                    if (east) {
                        return SHAPE_TALL_NES;
                    }
                }
            }

            else if (east) {
                model = SHAPE_TALL_E;

                if (south) {
                    model = SHAPE_TALL_SE;

                    if (west) {
                        return SHAPE_TALL_ESW;
                    }
                }

                else if (west) {
                    return SHAPE_TALL_EW;
                }
            }

            else if (south) {
                model = SHAPE_TALL_S;

                if (west) {
                    return SHAPE_TALL_SW;
                }
            }

            else if (west) {
                return SHAPE_TALL_W;
            }
            return model;
        }
        return super.getShape(state, level, pos, context);
    }

    public static int getFlow(Level level, BlockState state, BlockPos pos) {
        int flowCount = 0;
        for (var direction : Direction.Plane.HORIZONTAL) {
            BlockPos neighborPos = pos.relative(direction);
            if (!level.getFluidState(neighborPos).is(Fluids.EMPTY) && state.getValue(GRATE_PROPERTY_BY_DIRECTION.get(direction))) {
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
        if (blockEntity instanceof SluiceBlockEntity sluiceBlockEntity) {
            sluiceBlockEntity.recheckOpen();
        }
        super.tick(state, level, pos, random);

    }

    @org.jetbrains.annotations.Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SluiceBlockEntity(pos, state);
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @org.jetbrains.annotations.Nullable LivingEntity placer, ItemStack stack) {
        if (stack.hasCustomHoverName()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof SluiceBlockEntity sluiceBlockEntity) {
                sluiceBlockEntity.setCustomName(stack.getHoverName());
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
            return createTickerHelper(blockEntityType, (state.is(ModBlocks.WOODEN_SLUICE.get()) ? ModEntities.WOODEN_SLUICE.get() : ModEntities.STONE_SLUICE.get()), SluiceBlockEntity::pushItemsTick);
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


        SHAPE_TALL_NONE = Shapes.or(SHAPE_LEGS, SHAPE_BASE);

        SHAPE_TALL_N = Shapes.or(SHAPE_LEGS, SHAPE_BASE, Block.box(0.0D, 12.0D, -2.0D, 16.0D, 32.0D, 0.0D));
        SHAPE_TALL_E = Shapes.or(SHAPE_LEGS, SHAPE_BASE, Block.box(16.0D, 12.0D, 0.0D, 18.0D, 32.0D, 16.0D));
        SHAPE_TALL_S = Shapes.or(SHAPE_LEGS, SHAPE_BASE, Block.box(0.0D, 12.0D, 16.0D, 16.0D, 32.0D, 18.0D));
        SHAPE_TALL_W = Shapes.or(SHAPE_LEGS, SHAPE_BASE, Block.box(-2.0D, 12.0D, 0.0D, 0.0D,  32.0D, 16.0D));

        SHAPE_TALL_NE = Shapes.or(SHAPE_TALL_N, SHAPE_TALL_E);
        SHAPE_TALL_SE = Shapes.or(SHAPE_TALL_S, SHAPE_TALL_E);
        SHAPE_TALL_NW = Shapes.or(SHAPE_TALL_N, SHAPE_TALL_W);
        SHAPE_TALL_SW = Shapes.or(SHAPE_TALL_S, SHAPE_TALL_W);
        SHAPE_TALL_EW = Shapes.or(SHAPE_TALL_E, SHAPE_TALL_W);
        SHAPE_TALL_NS = Shapes.or(SHAPE_TALL_N, SHAPE_TALL_S);

        SHAPE_TALL_NES = Shapes.or(SHAPE_TALL_N, SHAPE_TALL_E, SHAPE_TALL_S);
        SHAPE_TALL_ESW = Shapes.or(SHAPE_TALL_E, SHAPE_TALL_S, SHAPE_TALL_W);
        SHAPE_TALL_SWN = Shapes.or(SHAPE_TALL_S, SHAPE_TALL_W, SHAPE_TALL_N);
        SHAPE_TALL_WNE = Shapes.or(SHAPE_TALL_W, SHAPE_TALL_N, SHAPE_TALL_E);
        SHAPE_TALL_NESW = Shapes.or(SHAPE_TALL_N, SHAPE_TALL_E, SHAPE_TALL_S, SHAPE_TALL_W);
    }

}
