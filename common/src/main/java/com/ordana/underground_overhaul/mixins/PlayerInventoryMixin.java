package com.ordana.underground_overhaul.mixins;

import com.ordana.underground_overhaul.mixininterfaces.PlayerInventoryExt;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Inventory.class)
public abstract class PlayerInventoryMixin implements PlayerInventoryExt {

    @Shadow @Final public Player player;

    /**
     * method that sorts a specific inventory
     * @param stacks the inventory that you want to sort
     */
    @Unique
    @Override
    public void configureDrop(NonNullList<ItemStack> stacks) {
        for (int i = 0; i < stacks.size(); i++) {
            ItemStack stack = stacks.get(i);
            boolean bl = !stack.is(Items.RECOVERY_COMPASS);
            if (!stack.isEmpty()) {
                if (EnchantmentHelper.hasVanishingCurse(stack)) {
                    stacks.set(i, ItemStack.EMPTY);
                } else if (bl) {
                    if (stack.getCount() == 1) {
                            this.player.drop(stack, false);
                            stacks.set(i, ItemStack.EMPTY);
                    } else {
                        ItemStack copyStack;
                        copyStack = stack.copy();
                        ItemEntity entity = this.player.drop(copyStack, false);
                        stacks.set(i, stack);
                    }
                }
            }
        }
    }

//    //coming soon
//    @Unique
//    /**
//     * A method that sorts your offhand based on the stack.
//     * @param stack that you want sorted
//     */
//
//
//    @Override
//    public void sortOffHand(ItemStack stack) {
//        if (stack != null){
//            if (this.indexOf(stack) != -1){
//                int item = indexOf(stack);
//                ItemStack currentOffStack = this.offHand.get(0);
//                ItemStack wantedStack = this.main.get(item);
//                this.main.set(item, currentOffStack);
//                this.offHand.set(0, wantedStack);
//            }
//            else {
//                int slot = this.indexOfArmor(stack);
//                if (slot != -1) {
//                    ItemStack currentOffHandStack = this.offHand.get(0);
//                    ItemStack wantedStack = this.armor.get(slot);
//                    this.armor.set(slot, currentOffHandStack);
//                    this.offHand.set(0, wantedStack);
//                }
//            }
//        }
//    }
//
//    /**
//     * a method that searches through a pregenerated list based on the cycle num
//     */
//    @Unique
//    @Override
//    public void sortOffHand() {
//
//    }
//    @Unique
//    @Override
//    public void sortHotBar(NonNullList<ItemStack> stack) {
//
//    }
//
//    /**
//     *  this only searches on the main inventory
//     * @param stack wanted stack
//     * @return index of stack in the main slot
//     */
//    @Unique
//    @Override
//    public int indexOf(ItemStack stack) {
//        for (int i = 0; i < this.main.size(); i++){
//            ItemStack itemStack = this.main.get(i);
//            if (itemStack.sameItem(stack)){
//                return i;
//            }
//        }
//        return -1;
//    }
//
//    /**
//     * only searches armor inventory
//     * @param stack wanted stack
//     * @return index of the stack in armor slots
//     */
//    @Unique
//    @Override
//    public int indexOfArmor(ItemStack stack){
//        for (int i = 0; i < this.armor.size(); i++){
//            ItemStack stacker = this.armor.get(i);
//            if (stacker.sameItem(stack)){
//                return i;
//            }
//        }
//        return -1;
//    }
//
//    /**
//     * Searches through the main inventory and returns the slot number of a food item
//     * @return the int of the slot that the stack that is a food is in
//     */
//    @Unique
//    @Override
//    public int indexOfFood() {
//        for (int i = 0; i < this.main.size(); i++){
//            ItemStack stack = this.main.get(i);
//            Item item = stack.getItem();
//            if (item.isEdible()){
//                return i;
//            }
//        }
//
//        return -1;
//    }
}
