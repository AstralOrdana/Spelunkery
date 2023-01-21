package com.ordana.spelunkery.mixins;

import com.ordana.spelunkery.features.util.ModVeinType;
import com.ordana.spelunkery.reg.ModTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.OreVeinifier;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(OreVeinifier.class)
public class OreVeinifierMixin {
    /*
    @ModifyVariable(
            method = "method_40547(Lnet/minecraft/world/level/levelgen/DensityFunction;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/levelgen/PositionalRandomFactory;Lnet/minecraft/world/level/levelgen/DensityFunction;Lnet/minecraft/world/level/levelgen/DensityFunction;Lnet/minecraft/world/level/levelgen/DensityFunction$FunctionContext;)Lnet/minecraft/world/level/block/state/BlockState;",
            at = @At("STORE"), index = 9)
    private static OreVeinifier.VeinType mixin(OreVeinifier.VeinType in) {
        return ModVeinType.VeinType.GOLD;
    }

     */

}
