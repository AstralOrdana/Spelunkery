package com.ordana.spelunkery.reg;

import net.minecraft.world.damagesource.DamageSource;

public class ModDamageSource extends DamageSource {

    public static final DamageSource STONECUTTER = new ModDamageSource("stonecutter").bypassArmor();

    protected ModDamageSource(String name) {
        super(name);
    }
}