package com.ordana.spelunkery.mixins;

import com.ordana.spelunkery.configs.CommonConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StonecutterBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(StonecutterBlock.class)
public class StonecutterBlockMixin extends Block {

    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (entity instanceof LivingEntity && CommonConfigs.STONECUTTER_DAMAGE.get()) {
            entity.hurt(level.damageSources().stalagmite(), 1.5F);
        }
        super.stepOn(level, pos, state, entity);
    }

    public StonecutterBlockMixin(Properties properties) {
        super(properties);
    }
}
