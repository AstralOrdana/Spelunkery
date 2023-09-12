package com.ordana.spelunkery.mixins;

import com.ordana.spelunkery.reg.ModSoundEvents;
import com.ordana.spelunkery.utils.LevelHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class PortalFluidMixin {

    @Shadow public Level level;
    @Shadow public abstract int getDimensionChangingDelay();
    @Shadow public abstract boolean isPassenger();


    private int fluidCooldown;
    protected boolean isInsideFluid;
    protected BlockPos fluidEntrancePos;
    protected int fluidTime;


    @Inject(method = "baseTick",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;handleNetherPortal()V",
                    shift = At.Shift.AFTER))
    public void insertFluidTick(CallbackInfo ci) {
        this.handlePortalFluid();
    }



    public Level level() {
        return this.level;
    }

    public void setFluidCooldown() {
        this.fluidCooldown = this.getDimensionChangingDelay();
    }

    public boolean isOnFluidCooldown() {
        return this.fluidCooldown > 0;
    }

    protected void processFluidCooldown() {
        if (this.isOnFluidCooldown()) {
            --this.fluidCooldown;
        }

    }

    public int getPortalWaitTime() {
        return 0;
    }


    public void handleInsideFluid(BlockPos pos) {
        if (this.isOnFluidCooldown()) {
            this.setFluidCooldown();
        } else {
            if (!this.level().isClientSide && !pos.equals(this.fluidEntrancePos)) {
                this.fluidEntrancePos = pos.immutable();
            }

            this.isInsideFluid = true;
        }
    }

    protected void handlePortalFluid() {
        if (this.level() instanceof ServerLevel) {
            int i = this.getPortalWaitTime();

            if (this.isInsideFluid) {

                if (!this.isPassenger() && this.fluidTime++ >= i) {
                    this.fluidTime = i;
                    this.setFluidCooldown();

                    var entity = (Entity)(Object)this;

                    if (entity instanceof ServerPlayer player) LevelHelper.teleportToSpawnPosition(player);
                    else LevelHelper.teleportToWorldspawn(level, entity);
                    entity.playSound(ModSoundEvents.PORTAL_FLUID_TELEPORT.get(), 1.0f, 1.0f);

                }

                this.isInsideFluid = false;
            } else {
                if (this.fluidTime > 0) {
                    this.fluidTime -= 4;
                }

                if (this.fluidTime < 0) {
                    this.fluidTime = 0;
                }
            }

            this.processFluidCooldown();
        }
    }

}
