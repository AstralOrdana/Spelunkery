package com.ordana.underground_overhaul.mixininterfaces;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public interface PlayerInventoryExt {
    void configureDrop(NonNullList<ItemStack> stacks);
    void dropInventory();
}
