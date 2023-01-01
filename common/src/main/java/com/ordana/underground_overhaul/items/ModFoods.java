package com.ordana.underground_overhaul.items;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Items;

public class ModFoods {
    public static final FoodProperties GRILLED_PORTABELLA = (new FoodProperties.Builder())
            .nutrition(6).saturationMod(0.8F
            ).effect(new MobEffectInstance(MobEffects.DIG_SPEED, 1800, 2), 1F)
            .build();

    public static final FoodProperties PORTABELLA = (new FoodProperties.Builder())
            .nutrition(3).saturationMod(0.4F
            ).effect(new MobEffectInstance(MobEffects.DIG_SPEED, 300, 1), 0.5F)
            .build();

    public static final FoodProperties CRIMINI = (new FoodProperties.Builder())
            .nutrition(2).saturationMod(1.5F)
            .build();

    public static final FoodProperties BUTTON_MUSHROOM = (new FoodProperties.Builder())
            .nutrition(2).saturationMod(0.3F
            ).effect(new MobEffectInstance(MobEffects.DIG_SPEED, 18000, 2), 0.01F)
            .build();
}
