package com.ordana.spelunkery.forge;

import com.ordana.spelunkery.reg.ModSoundEvents;
import com.ordana.spelunkery.utils.LevelHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

import java.util.function.Supplier;

public class PortalFluidBlock extends LiquidBlock {

    public PortalFluidBlock(Supplier<FlowingFluid> flowingFluid, Properties properties) {
        super(flowingFluid, properties);
    }

    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        setTickCounter(0);
    }

    private int tickCounter = 0;

    public int setTickCounter(int tick) {
        return tickCounter = tick;
    }


    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {

        if (!entity.isInWater() || entity.isPassenger() || entity.isVehicle() || !entity.canChangeDimensions() || pos.equals(level.getSharedSpawnPos())) return;
        if (entity instanceof ServerPlayer player && player.isSecondaryUseActive()) return;

        tickCounter++;

        if (this.tickCounter < 1) {
            entity.playSound(ModSoundEvents.PORTAL_FLUID_ENTER.get(), 1.0f, 1.0f);
        }
        level.scheduleTick(pos, this, 120);
        if (this.tickCounter >= 100) {


            if (entity instanceof ServerPlayer player) LevelHelper.teleportToSpawnPosition(player);
            else LevelHelper.teleportToWorldspawn(level, entity);
            entity.playSound(ModSoundEvents.PORTAL_FLUID_TELEPORT.get(), 1.0f, 1.0f);


            setTickCounter(0);

        }
    }

}
