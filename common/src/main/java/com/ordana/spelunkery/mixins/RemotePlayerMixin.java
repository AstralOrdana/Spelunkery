package com.ordana.spelunkery.mixins;

import com.mojang.authlib.GameProfile;
import com.ordana.spelunkery.items.ParachuteItem;
import com.ordana.spelunkery.utils.IParachuteEntity;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.ProfilePublicKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RemotePlayer.class)
public abstract class RemotePlayerMixin extends Player implements IParachuteEntity {

    @Unique
    private ItemStack parachute = ItemStack.EMPTY;

    protected RemotePlayerMixin(Level level, BlockPos blockPos, float f, GameProfile gameProfile, @Nullable ProfilePublicKey profilePublicKey) {
        super(level, blockPos, f, gameProfile, profilePublicKey);
    }

    //this isn't optimal but still better than checking every render tick the whole inventory
    @Inject(method = "tick",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/player/AbstractClientPlayer;tick()V",
                    shift = At.Shift.AFTER)
    )
    private void checkIfHasParachute(CallbackInfo ci) {
        parachute = ParachuteItem.getParachute(this);
    }

    @Override
    public ItemStack getParachute() {
        return parachute;
    }

    @Override
    public void setParachute(ItemStack parachute) {
        this.parachute = parachute;
    }
}
