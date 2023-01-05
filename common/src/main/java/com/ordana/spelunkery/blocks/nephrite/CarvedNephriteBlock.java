package com.ordana.spelunkery.blocks.nephrite;

import com.ordana.spelunkery.blocks.entity.CarvedNephriteBlockEntity;
import com.ordana.spelunkery.reg.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class CarvedNephriteBlock extends BaseEntityBlock {

    public static final EnumProperty<ChargeState> CHARGE = EnumProperty.create("charge", ChargeState.class);

    public CarvedNephriteBlock(Properties properties) {

        super(properties.lightLevel((state) ->
                switch (state.getValue(CarvedNephriteBlock.CHARGE)) {
                    case EMPTY -> 0;
                    case ONE_TO_TWENTY -> 1;
                    case TWENTYONE_TO_FOURTY -> 2;
                    case FOURTYONE_TO_SIXTY -> 3;
                    case SIXTYONE_TO_EIGHTY -> 4;
                    case EIGHTYONE_TO_NINETYNINE -> 5;
                    case FULL -> 6;
                }
        ));

        this.registerDefaultState(this.stateDefinition.any().setValue(CHARGE, ChargeState.EMPTY));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        super.createBlockStateDefinition(stateManager);
        stateManager.add(CHARGE);
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

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (!level.isClientSide) {
            return createTickerHelper(blockEntityType, ModEntities.NEPHRITE_TILE.get(), CarvedNephriteBlockEntity::tickBlock);
        } else {
            return super.getTicker(level, state, blockEntityType);
        }
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public enum ChargeState implements StringRepresentable {
        EMPTY,
        ONE_TO_TWENTY,
        TWENTYONE_TO_FOURTY,
        FOURTYONE_TO_SIXTY,
        SIXTYONE_TO_EIGHTY,
        EIGHTYONE_TO_NINETYNINE,
        FULL;

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (level instanceof ServerLevel && level.getBlockEntity(pos) instanceof CarvedNephriteBlockEntity selfTile) {
            this.popExperience((ServerLevel) level, pos, selfTile.getCharge());
        }
        super.playerWillDestroy(level, pos, state, player);
    }
}
