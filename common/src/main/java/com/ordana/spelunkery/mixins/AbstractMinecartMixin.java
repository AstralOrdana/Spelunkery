package com.ordana.spelunkery.mixins;

import com.ordana.spelunkery.reg.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartMixin extends Entity {

    @Shadow
    @Final
    public abstract int getHurtTime();

    @Shadow
    @Final
    public abstract void setHurtTime(int i);

    @Shadow
    @Final
    public abstract int getHurtDir();

    @Shadow
    @Final
    public abstract void setHurtDir(int i);

    @Shadow
    @Final
    public abstract void setDamage(float v);

    public AbstractMinecartMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/vehicle/AbstractMinecart;moveAlongTrack(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V",
            shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILSOFT)
    public void rattleMinecart(CallbackInfo ci, int i, int j, int k, BlockPos blockPos, BlockState blockState) {
        if (blockState.is(ModBlocks.WOODEN_RAIL.get()) && random.nextInt(8) == 0) {
            if (this.getHurtTime() == 0) {
                this.setHurtDir(-this.getHurtDir());
                this.setHurtTime(10);
                this.setDamage(random.nextInt(5 ,10));
                this.markHurt();
            }
        }
    }


    /*
    @Inject(method = "getBlockSpeedFactor", at = @At("HEAD"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    public void slowRails(CallbackInfoReturnable<Float> cir) {
        BlockState blockState = this.level.getBlockState(this.blockPosition());
        if (blockState.is(ModBlocks.WOODEN_RAIL.get())) cir.setReturnValue(0.8f);
    }
     */



    @Shadow
    @Final
    protected abstract void defineSynchedData();

    @Shadow
    @Final
    protected abstract void readAdditionalSaveData(CompoundTag compound);

    @Shadow
    @Final
    protected abstract void addAdditionalSaveData(CompoundTag compound);

    @Shadow
    @Final
    public abstract Packet<?> getAddEntityPacket();
}
