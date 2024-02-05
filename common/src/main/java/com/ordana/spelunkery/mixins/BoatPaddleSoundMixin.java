package com.ordana.spelunkery.mixins;

import com.ordana.spelunkery.reg.ModFluids;
import com.ordana.spelunkery.reg.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Boat.class)
public abstract class BoatPaddleSoundMixin extends Entity {


    public BoatPaddleSoundMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "getPaddleSound", at = @At("HEAD"), cancellable = true)
    public void insertFluidTick(CallbackInfoReturnable<SoundEvent> cir) {
        if (isInPortalFluid()) cir.setReturnValue(ModSoundEvents.BOAT_PADDLE_PORTAL_FLUID.get());
    }

    private boolean isInPortalFluid() {
        AABB aabb = this.getBoundingBox();
        int i = Mth.floor(aabb.minX);
        int j = Mth.ceil(aabb.maxX);
        int k = Mth.floor(aabb.minY);
        int l = Mth.ceil(aabb.minY + 0.001D);
        int i1 = Mth.floor(aabb.minZ);
        int j1 = Mth.ceil(aabb.maxZ);
        boolean flag = false;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(int k1 = i; k1 < j; ++k1) {
            for(int l1 = k; l1 < l; ++l1) {
                for(int i2 = i1; i2 < j1; ++i2) {
                    blockpos$mutableblockpos.set(k1, l1, i2);
                    FluidState fluidstate = this.level.getFluidState(blockpos$mutableblockpos);
                    return fluidstate.is(ModFluids.PORTAL_FLUID.get()) || fluidstate.is(ModFluids.FLOWING_PORTAL_FLUID.get());

                }
            }
        }

        return flag;
    }


    @Shadow
    protected void defineSynchedData() {
    }

    @Shadow
    protected void readAdditionalSaveData(CompoundTag compound) {
    }

    @Shadow
    protected void addAdditionalSaveData(CompoundTag compound) {
    }
}
