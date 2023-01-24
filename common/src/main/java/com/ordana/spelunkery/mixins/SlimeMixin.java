package com.ordana.spelunkery.mixins;

import com.ordana.spelunkery.configs.CommonConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slime.class)
public class SlimeMixin extends Mob {

    protected SlimeMixin(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "checkSlimeSpawnRules", at = @At("HEAD"), cancellable = true)
    private static void caveSlimeSpawns(EntityType<Slime> slime, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random, CallbackInfoReturnable<Boolean> cir) {
        if (CommonConfigs.INCREASED_SLIME_SPAWN_RATE.get()) {
            if (pos.getY() < 60 && random.nextInt(124) > (pos.getY() + 64)) {
                cir.setReturnValue(checkMobSpawnRules(slime, level, spawnType, pos, random));
            }
        }
    }
}
