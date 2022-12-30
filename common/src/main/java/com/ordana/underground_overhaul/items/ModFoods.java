package com.ordana.underground_overhaul.items;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties GRILLED_PORTABELLA = (new FoodProperties.Builder())
            .nutrition(6).saturationMod(0.8F
            ).effect(new MobEffectInstance(MobEffects.DIG_SPEED, 1800, 2), 1F)
            .build();
}
