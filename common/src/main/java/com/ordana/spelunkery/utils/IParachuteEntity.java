package com.ordana.spelunkery.utils;

import net.minecraft.world.item.ItemStack;

public interface IParachuteEntity {

    //only used for rendering for player and both for skeletons
    ItemStack getParachute();

    default boolean hasParachute(){
        return !this.getParachute().isEmpty();
    }

    void setParachute(ItemStack parachute);

    int getParachuteTicks();
}
