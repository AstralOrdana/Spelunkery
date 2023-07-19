package com.ordana.spelunkery.mixins;

import com.mojang.authlib.GameProfile;
import com.ordana.spelunkery.items.ParachuteItem;
import com.ordana.spelunkery.reg.ModItems;
import com.ordana.spelunkery.utils.IParachuteEntity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.ProfilePublicKey;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer implements IParachuteEntity {


    @Shadow
    @Override
    public abstract InteractionHand getUsedItemHand();

    protected LocalPlayerMixin(ClientLevel clientLevel, GameProfile gameProfile, @Nullable ProfilePublicKey profilePublicKey) {
        super(clientLevel, gameProfile, profilePublicKey);
    }

    //hack. this will be ugly. Prevents parachute from slowing down

    @Inject(method = "aiStep",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z",
                    shift = At.Shift.BEFORE),
            require = 3)
    private void cancelParachuteSlow(CallbackInfo ci) {
        this.cancelUsingParachute = true;
    }

    @Inject(method = "aiStep",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z",
                    shift = At.Shift.AFTER),
            require = 3)
    private void reset(CallbackInfo ci) {
        this.cancelUsingParachute = false;
    }

    @Inject(method = "isUsingItem",
            at = @At("HEAD"), cancellable = true)
    private void isUsingItem(CallbackInfoReturnable<Boolean> cir) {
        if (cancelUsingParachute && this.getUseItem().getItem() == ModItems.PARACHUTE.get()) {
            cir.setReturnValue(false);
        }
    }

    @Unique
    private boolean cancelUsingParachute = false;

    @Unique
    private ItemStack parachute = ItemStack.EMPTY;

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