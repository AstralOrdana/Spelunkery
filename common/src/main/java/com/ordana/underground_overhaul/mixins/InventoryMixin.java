package com.ordana.underground_overhaul.mixins;

import com.ordana.underground_overhaul.mixininterfaces.PlayerInventoryExt;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Iterator;
import java.util.List;

@Mixin(Inventory.class)
public class InventoryMixin {

    @Redirect(method = "dropAll",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z"))
    private boolean dontDropCompass(ItemStack itemStack) {
        return (itemStack.is(Items.RECOVERY_COMPASS));
    }

    /*
    @Final
    @Shadow
    private List<NonNullList<ItemStack>> compartments;

    @Final
    @Shadow
    public Player player;

    @Override
    public void dropExceptCompass() {
        Iterator var1 = this.compartments.iterator();

        while(var1.hasNext()) {
            List<ItemStack> list = (List)var1.next();

            for(int i = 0; i < list.size(); ++i) {
                ItemStack itemStack = (ItemStack)list.get(i);
                if (!itemStack.isEmpty() && !itemStack.is(Items.RECOVERY_COMPASS)) {
                    this.player.drop(itemStack, true, false);
                    list.set(i, ItemStack.EMPTY);
                }
            }
        }
    }

     */

}
