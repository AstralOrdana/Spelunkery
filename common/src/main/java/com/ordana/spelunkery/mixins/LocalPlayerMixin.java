package com.ordana.spelunkery.mixins;

import com.mojang.authlib.GameProfile;
import com.ordana.spelunkery.items.ParachuteItem;
import com.ordana.spelunkery.utils.IParachuteEntity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.ProfilePublicKey;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer implements IParachuteEntity {


    public LocalPlayerMixin(ClientLevel clientLevel, GameProfile gameProfile, @Nullable ProfilePublicKey profilePublicKey) {
        super(clientLevel, gameProfile, profilePublicKey);
    }

    @Unique
    private ItemStack spelunkery$parachute = ItemStack.EMPTY;

    @Unique
    private int spelunkery$parachuteTicks = 0;

    //this isn't optimal but still better than checking every render tick the whole inventory
    @Inject(method = "tick",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/player/AbstractClientPlayer;tick()V",
                    shift = At.Shift.AFTER)
    )
    private void checkIfHasParachute(CallbackInfo ci) {
        spelunkery$parachute = ParachuteItem.getParachute(this);
        if (!spelunkery$parachute.isEmpty() && ParachuteItem.getActive(spelunkery$parachute)) {
            spelunkery$parachuteTicks++;
        } else{
            spelunkery$parachuteTicks =0;
        }
    }

    @Override
    public ItemStack getParachute() {
        return spelunkery$parachute;
    }

    @Override
    public void setParachute(ItemStack parachute) {
        this.spelunkery$parachute = parachute;
    }

    @Override
    public int getParachuteTicks() {
        return spelunkery$parachuteTicks;
    }
}