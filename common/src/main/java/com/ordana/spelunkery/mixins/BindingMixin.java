package com.ordana.spelunkery.mixins;

import com.ordana.spelunkery.reg.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class BindingMixin {

    @Inject(method = "hasBindingCurse", at = @At("HEAD"), cancellable = true)
    private static void injectBinding(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.is(ModItems.TRUE_CROWN.get())) cir.setReturnValue(true);
    }
}
