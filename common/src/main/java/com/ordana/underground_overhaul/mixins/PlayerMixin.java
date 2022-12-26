package com.ordana.underground_overhaul.mixins;

import com.ordana.underground_overhaul.mixininterfaces.PlayerInventoryExt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Player.class, priority = 999999)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    @Final
    public Inventory inventory;

    @Inject(method = "dropEquipment", at = @At("HEAD"), cancellable = true)
    public void safeInv(CallbackInfo ci){
        ((PlayerInventoryExt)this.inventory).dropInventory();
        ci.cancel();
    }
}
