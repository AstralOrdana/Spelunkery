package com.ordana.spelunkery.mixins;

import com.mojang.authlib.GameProfile;
import com.ordana.spelunkery.items.ParachuteItem;
import com.ordana.spelunkery.reg.ModItems;
import com.ordana.spelunkery.utils.IParachuteEntity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer implements IParachuteEntity {


    public LocalPlayerMixin(ClientLevel clientLevel, GameProfile gameProfile) {
        super(clientLevel, gameProfile);
    }

    @Shadow
    @Override
    public abstract InteractionHand getUsedItemHand();

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