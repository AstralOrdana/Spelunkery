package com.ordana.underground_overhaul.blocks.nephrite;

import com.ordana.underground_overhaul.blocks.entity.CarvedNephriteBlockEntity;
import com.ordana.underground_overhaul.reg.ModBlockProperties;
import com.ordana.underground_overhaul.reg.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Properties;

public class CarvedNephriteBlock extends BaseEntityBlock {

    public static final EnumProperty<ChargeState> CHARGE = EnumProperty.create("charge", ChargeState.class);
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public CarvedNephriteBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(CHARGE, ChargeState.EMPTY).setValue(ACTIVE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        super.createBlockStateDefinition(stateManager);
        stateManager.add(CHARGE, ACTIVE);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        /*
        if (state.getValue(ACTIVE)) {
            return new CarvedNephriteBlockEntity(pos, state);
        }
        return null;
         */

        return new CarvedNephriteBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!level.isClientSide) {
            if (level.getBlockEntity(pos) instanceof CarvedNephriteBlockEntity selfTile) {
                if (selfTile.getCharge(pos, level) < 100) {
                    selfTile.setCharge(100);
                    level.setBlockAndUpdate(pos, state.setValue(CHARGE, ChargeState.FULL));
                }

                if (selfTile.getCharge(pos, level) == 100) level.setBlockAndUpdate(pos, state.setValue(CHARGE, ChargeState.FULL));
                else if (selfTile.getCharge(pos, level) > 0) level.setBlockAndUpdate(pos, state.setValue(CHARGE, ChargeState.HALF_FULL));
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (!level.isClientSide) {
            return createTickerHelper(blockEntityType, ModEntities.NEPHRITE_TILE.get(), CarvedNephriteBlockEntity::tickBlock);
        } else {
            return super.getTicker(level, state, blockEntityType);
        }
    }


    enum ChargeState implements StringRepresentable {
        EMPTY,
        FULL,
        HALF_FULL;

        public boolean isEmpty() {
            return this == EMPTY;
        }

        public boolean isFull() {
            return this == FULL;
        }

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}
