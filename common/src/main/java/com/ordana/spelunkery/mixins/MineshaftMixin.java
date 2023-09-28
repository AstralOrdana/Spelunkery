package com.ordana.spelunkery.mixins;

import com.ordana.spelunkery.worldgen.structures.MineshaftDustCorridor;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftPieces;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftStructure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MineshaftPieces.class)
public abstract class MineshaftMixin {

    @Inject(method = "createRandomShaftPiece", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftCorridor;findCorridorSize(Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;)Lnet/minecraft/world/level/levelgen/structure/BoundingBox;", shift = At.Shift.BEFORE),
            cancellable = true)
    private static void addDustCorridor(StructurePieceAccessor pieces, RandomSource random, int x, int y, int z, Direction orientation, int genDepth, MineshaftStructure.Type type, CallbackInfoReturnable<MineshaftPieces.MineShaftPiece> cir) {
        var shaft = MineshaftDustCorridor.findCorridorSize(
                pieces, random, x, y, z, orientation);
        if (shaft != null && random.nextBoolean()) cir.setReturnValue(new MineshaftDustCorridor(genDepth, random, shaft, orientation, type));
    }

}