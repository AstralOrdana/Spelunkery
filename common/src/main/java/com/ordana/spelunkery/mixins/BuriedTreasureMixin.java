package com.ordana.spelunkery.mixins;

import com.ordana.spelunkery.Spelunkery;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.structures.BuriedTreasurePieces;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BuriedTreasurePieces.BuriedTreasurePiece.class)
public class BuriedTreasureMixin {

    @Inject(method = "postProcess", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos$MutableBlockPos;move(III)Lnet/minecraft/core/BlockPos$MutableBlockPos;", shift = At.Shift.BEFORE),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void addSusSand(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos, CallbackInfo ci, int i, BlockPos.MutableBlockPos mutableBlockPos) {
        BlockState blockState = level.getBlockState(mutableBlockPos);
        BlockState blockState2 = level.getBlockState(mutableBlockPos.above());

        if (!blockState.isAir() && !blockState.is(Blocks.WATER) && (blockState2.isAir() || blockState2.is(Blocks.WATER))) level.setBlock(mutableBlockPos, blockState.is(Blocks.SAND) ? Blocks.SUSPICIOUS_SAND.defaultBlockState() : Blocks.SUSPICIOUS_GRAVEL.defaultBlockState(), 3);

        BlockEntity blockEntity = level.getBlockEntity(mutableBlockPos);
        if (blockEntity instanceof BrushableBlockEntity susSand) {
            (susSand).setLootTable(Spelunkery.res("gameplay/buried_treasure_marker"), random.nextLong());
        }
    }

}
