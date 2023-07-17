package com.ordana.spelunkery.blocks.rock_salt;

import com.ordana.spelunkery.reg.ModBlockProperties;
import com.ordana.spelunkery.reg.ModTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public interface RockSalt {

    BooleanProperty ILLUMINATED = ModBlockProperties.ILLUMINATED;
    IntegerProperty LIGHT = ModBlockProperties.LIGHT;

    default void onEntityStepOn(BlockState state, Entity entity) {
        if (entity instanceof LivingEntity && entity.getType().is(ModTags.HURT_BY_SALT)) {
            if (((LivingEntity) entity).isInvertedHealAndHarm()) entity.setRemainingFireTicks(8);
            entity.hurt(DamageSource.HOT_FLOOR, 1.0F);
        }
    }

}
