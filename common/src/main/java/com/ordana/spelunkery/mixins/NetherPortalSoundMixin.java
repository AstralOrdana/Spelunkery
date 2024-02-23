package com.ordana.spelunkery.mixins;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.configs.CommonConfigs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class NetherPortalSoundMixin {
    @Shadow
    protected abstract Block asBlock();

    private static final SoundType NETHER_PORTAL = new SoundType(1.0F, 0.9F, SoundEvents.RESPAWN_ANCHOR_DEPLETE.value(), SoundEvents.GLASS_STEP, SoundEvents.RESPAWN_ANCHOR_SET_SPAWN, SoundEvents.GLASS_HIT, SoundEvents.GLASS_FALL);

    @Inject(method = "getSoundType", at = @At("TAIL"), cancellable = true)
    private void getSoundGroupMixin(CallbackInfoReturnable<SoundType> cir){
        if (Spelunkery.isInitiated()) {
            if (CommonConfigs.PORTAL_DESTRUCTION_SOUND.get()) {
                String blockId = BuiltInRegistries.BLOCK.getKey(this.asBlock()).getPath();
                if (blockId.equals("nether_portal")) cir.setReturnValue(NETHER_PORTAL);
            }
        }
    }
}