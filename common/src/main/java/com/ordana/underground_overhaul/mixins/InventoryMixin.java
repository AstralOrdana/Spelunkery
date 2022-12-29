package com.ordana.underground_overhaul.mixins;

import com.ordana.underground_overhaul.reg.ModTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Inventory.class)
public class InventoryMixin {

    @Redirect(method = "dropAll",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z"))
    private boolean dontDropCompass(ItemStack itemStack) {
        return (itemStack.is(ModTags.KEEP_ON_DEATH));
    }

}
