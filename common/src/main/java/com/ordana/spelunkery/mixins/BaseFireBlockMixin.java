package com.ordana.spelunkery.mixins;

import com.ordana.spelunkery.configs.CommonConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseFireBlock.class)
public class BaseFireBlockMixin {

    @Inject(method = "onPlace", at = @At(value = "INVOKE", target = "Ljava/util/Optional;get()Ljava/lang/Object;", shift = At.Shift.AFTER))
    public void playSound(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving, CallbackInfo ci) {
        if (CommonConfigs.FlINT_AND_STEEL_PORTAL_LIGHTING.get() && CommonConfigs.PORTAL_CREATION_SOUND.get()) level.playSound(null, pos, SoundEvents.RESPAWN_ANCHOR_SET_SPAWN, SoundSource.BLOCKS, 1.0f, 1.0f);
    }

    @Inject(method = "onPlace", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;", shift = At.Shift.AFTER), cancellable = true)
    public void disablePortals(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving, CallbackInfo ci) {
        if (!CommonConfigs.FlINT_AND_STEEL_PORTAL_LIGHTING.get()) {
            if (!state.canSurvive(level, pos)) {
                level.removeBlock(pos, false);
            }
            ci.cancel();
        }
    }
}
