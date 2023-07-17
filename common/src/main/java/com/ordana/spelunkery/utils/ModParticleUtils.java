package com.ordana.spelunkery.utils;

import com.ordana.spelunkery.reg.ModBlockProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ModParticleUtils {

    public static void spawnMineomiteParticles(Level level, BlockPos pos, BlockState state, ParticleOptions particleOptions, Direction direction, IntProvider intProvider) {
        Vec3 vec32 = Vec3.atCenterOf(pos);
        var norm = direction.getNormal();
        var vec = vec32.add(norm.getX() / 2f, norm.getY() / 2f, norm.getZ() / 2f);
        var sticks = state.getValue(ModBlockProperties.STICKS);


        var x = (vec.x == 0 ? vec.x + 0.5 : vec.x);
        var y = (vec.y == 0 ? vec.y + 0.5 : vec.y);
        var z = (vec.z == 0 ? vec.z + 0.5 : vec.z);

        int i = intProvider.sample(level.random);

        for(int j = 0; j < i; ++j) {
            var xSpeed = (norm.getX() != 0 ? (norm.getX() > 0 ? norm.getX() - Mth.nextDouble(level.random, 0.8D, 0.9D) : norm.getX() + Mth.nextDouble(level.random, 0.8D, 0.9D)) : Mth.nextDouble(level.random, -0.1D, 0.1D));
            var ySpeed = (norm.getY() != 0 ? (norm.getY() > 0 ? norm.getY() - Mth.nextDouble(level.random, 0.8D, 0.9D) : norm.getY() + Mth.nextDouble(level.random, 0.8D, 0.9D)) : Mth.nextDouble(level.random, -0.1D, 0.1D));
            var zSpeed = (norm.getZ() != 0 ? (norm.getZ() > 0 ? norm.getZ() - Mth.nextDouble(level.random, 0.8D, 0.9D) : norm.getZ() + Mth.nextDouble(level.random, 0.8D, 0.9D)) : Mth.nextDouble(level.random, -0.1D, 0.1D));

            level.addAlwaysVisibleParticle(particleOptions, true, x, y, z, xSpeed, ySpeed, zSpeed);

            if (sticks >= 2) {
                var x2 = (norm.getX() != 0 ? x : x + 0.35);
                //y = (norm.getY() != 0 ? y : y + 0.35);
                var z2 = (norm.getZ() != 0 ? z : z + 0.35);
                level.addAlwaysVisibleParticle(particleOptions, true, x2, y, z2, xSpeed, ySpeed, zSpeed);

                if (sticks >= 3) {
                    //var x3 = (norm.getX() != 0 ? x : z + 0.35);
                    var y3 = (norm.getY() != 0 ? y : y - 0.35);
                    var z3 = (norm.getZ() != 0 ? z : z - 0.35);
                    level.addAlwaysVisibleParticle(particleOptions, true, x, y3, z3, xSpeed, ySpeed, zSpeed);
                }
                /*
                if (sticks >= 3) {
                    level.addAlwaysVisibleParticle(particleOptions, true, x, y, z - 0.35, xSpeed, ySpeed, zSpeed);
                    if (sticks >= 4) {
                        level.addAlwaysVisibleParticle(particleOptions, true, x - 0.35, y, z, xSpeed, ySpeed, zSpeed);
                        if (sticks >= 5) {
                            level.addAlwaysVisibleParticle(particleOptions, true, x, y, z + 0.35, xSpeed, ySpeed, zSpeed);
                            if (sticks >= 6) {
                                level.addAlwaysVisibleParticle(particleOptions, true, x - 0.35, y, z - 0.35, xSpeed, ySpeed, zSpeed);
                                if (sticks >= 7) {
                                    level.addAlwaysVisibleParticle(particleOptions, true, x + 0.35, y, z - 0.35, xSpeed, ySpeed, zSpeed);
                                    if (sticks >= 8) {
                                        level.addAlwaysVisibleParticle(particleOptions, true, x - 0.35, y, z + 0.35, xSpeed, ySpeed, zSpeed);
                                        if (sticks >= 9) {
                                            level.addAlwaysVisibleParticle(particleOptions, true, x + 0.35, y, z + 0.35, xSpeed, ySpeed, zSpeed);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                 */
            }
        }
    }
}
