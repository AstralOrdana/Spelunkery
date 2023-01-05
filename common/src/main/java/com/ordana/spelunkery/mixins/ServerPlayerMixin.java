package com.ordana.spelunkery.mixins;


import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

    @Redirect(method = "restoreFrom",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;isSpectator()Z"))
    private boolean dontDropCompass(ServerPlayer instance) {
        return true;
    }

}
