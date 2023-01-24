package com.ordana.spelunkery.mixins;

import com.ordana.spelunkery.configs.CommonConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class NetherPortalSoundMixin {
    @Shadow
    protected abstract Block asBlock();

    private static final SoundType NETHER_PORTAL = new SoundType(1.0F, 0.9F, SoundEvents.RESPAWN_ANCHOR_DEPLETE, SoundEvents.GLASS_STEP, SoundEvents.RESPAWN_ANCHOR_SET_SPAWN, SoundEvents.GLASS_HIT, SoundEvents.GLASS_FALL);

    @Inject(method = "getSoundType", at = @At("TAIL"), cancellable = true)
    private void getSoundGroupMixin(CallbackInfoReturnable<SoundType> cir){
        if (CommonConfigs.PORTAL_DESTRUCTION_SOUND.get()) {
            String blockId = Registry.BLOCK.getKey(this.asBlock()).getPath();
            if (blockId.equals("nether_portal")) cir.setReturnValue(NETHER_PORTAL);
        }
    }
}