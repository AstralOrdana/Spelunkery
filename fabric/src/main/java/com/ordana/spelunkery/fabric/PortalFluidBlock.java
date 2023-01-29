package com.ordana.spelunkery.fabric;

import com.mlib.LevelHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

public class PortalFluidBlock extends LiquidBlock {

    public PortalFluidBlock(FlowingFluid flowingFluid, Properties properties) {
        super(flowingFluid, properties);
    }

    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!entity.isPassenger() && !entity.isVehicle() && entity.canChangeDimensions() && !pos.equals(level.getSharedSpawnPos())) {
            if (entity.isInWater()) {
                if (entity instanceof ServerPlayer player) {
                    if (!player.isSecondaryUseActive() || !Screen.hasControlDown()) LevelHelper.teleportToSpawnPosition(player);
                }
                else {
                    LevelHelper.teleportToWorldspawn(level, entity);
                    level.playSound(null, entity.blockPosition(), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.BLOCKS, 1.0f, 1.0f);
                }
            }
        }
    }

}
