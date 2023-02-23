package com.ordana.spelunkery.mixins;

import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin({NoiseBasedChunkGenerator.class})
public class NoiseBasedChunkGeneratorMixin {
    /**
     * @author Andrew6rant (Andrew Grant)
     * @reason Remove the hardcoded -54 lava sea level
     */

    @Overwrite
    private static Aquifer.NoiseBasedAquifer.FluidPicker stable(NoiseGeneratorSettings settings) {
        return (x, y, z) -> new Aquifer.FluidStatus(settings.seaLevel(), settings.defaultFluid());
    }
}
