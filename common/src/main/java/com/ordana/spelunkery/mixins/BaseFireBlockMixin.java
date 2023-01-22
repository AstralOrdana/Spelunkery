package com.ordana.spelunkery.mixins;

import com.ordana.spelunkery.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseFireBlock.class)
public class BaseFireBlockMixin {

    @Inject(method = "onPlace", at = @At(value = "INVOKE", target = "Ljava/util/Optional;isPresent()Z", shift = At.Shift.AFTER))
    public void playSound(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving, CallbackInfo ci) {
        level.playSound(null, pos, SoundEvents.RESPAWN_ANCHOR_SET_SPAWN, SoundSource.BLOCKS, 1.0f, 1.0f);
    }
}
