package com.ordana.spelunkery.features;

import com.mojang.serialization.Codec;
import com.ordana.spelunkery.configs.CommonConfigs;
import com.ordana.spelunkery.features.util.FastNoiseLite;
import com.ordana.spelunkery.features.util.StoneEntry;
import com.ordana.spelunkery.features.util.StonePattern;
import com.ordana.spelunkery.reg.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.List;
import java.util.Random;

public class BlockStripeFeature extends Feature<BlockStripeFeatureConfig> {

    public BlockStripeFeature(Codec<BlockStripeFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<BlockStripeFeatureConfig> context) {

        if (CommonConfigs.STONE_STRIPE_FEATURES.get()) {
            BlockStripeFeatureConfig config = context.config();

            RandomSource random = context.random();
            BlockPos originPos = context.origin();
            WorldGenLevel worldGenLevel = context.level();
            ChunkGenerator chunkGenerator = context.chunkGenerator();

            ChunkAccess cachedChunk = worldGenLevel.getChunk(originPos);

            FastNoiseLite cellNoise = new FastNoiseLite();


            cellNoise.SetNoiseType(FastNoiseLite.NoiseType.Cellular);
            cellNoise.SetCellularReturnType(FastNoiseLite.CellularReturnType.CellValue);
            cellNoise.SetDomainWarpType(FastNoiseLite.DomainWarpType.OpenSimplex2);
            cellNoise.SetRotationType3D(FastNoiseLite.RotationType3D.ImproveXZPlanes);
            cellNoise.SetFrequency(0.01F);
            cellNoise.SetFractalOctaves(1);

            FastNoiseLite cellBufferNoise = new FastNoiseLite();
            cellBufferNoise.SetNoiseType(FastNoiseLite.NoiseType.Cellular);
            cellBufferNoise.SetCellularReturnType(FastNoiseLite.CellularReturnType.Distance2Div);
            cellBufferNoise.SetDomainWarpType(FastNoiseLite.DomainWarpType.OpenSimplex2);
            cellBufferNoise.SetRotationType3D(FastNoiseLite.RotationType3D.ImproveXZPlanes);
            cellBufferNoise.SetFrequency(0.01F);
            cellBufferNoise.SetFractalOctaves(1);

            FastNoiseLite noise = new FastNoiseLite();
            noise.SetDomainWarpType(FastNoiseLite.DomainWarpType.OpenSimplex2);
            noise.SetRotationType3D(FastNoiseLite.RotationType3D.ImproveXZPlanes);
            noise.SetFrequency(0.03F);
            noise.SetDomainWarpAmp(1.5F);

            FastNoiseLite.Vector3 domainWarpedVector = new FastNoiseLite.Vector3(0, 0, 0);

            noise.SetFractalOctaves(1);
            var getX = (originPos.getX() & ~15);
            var getZ = (originPos.getZ() & ~15);
            var minY = chunkGenerator.getMinY();

            for (int x = getX; x < getX + 16; x++) {
                for (int z = getZ; z < getZ + 16; z++) {
                    if (originPos.getY() == minY && CommonConfigs.PORTAL_FLUID_OCEAN.get()) {
                        BlockPos currentPos = new BlockPos(x, minY, z);
                        if (cachedChunk.getBlockState(currentPos).isAir()) {
                            worldGenLevel.setBlock(currentPos, ModBlocks.PORTAL_FLUID.get().defaultBlockState(), Block.UPDATE_CLIENTS);
                        }
                    } else {

                        var heightmap = worldGenLevel.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, x, z);
                        for (int y = chunkGenerator.getMinY(); y < heightmap - config.surfaceOffset; y++) {


                            BlockPos currentPos = new BlockPos(x, y, z);
                            BlockState currentState = cachedChunk.getBlockState(currentPos);


                            boolean isTarget1 = currentState.is(config.firstTarget);
                            boolean isTarget2 = false;
                            if (config.secondTarget != null && config.secondTargetPlacer != null)
                                isTarget2 = config.useSecondTarget && currentState.is(config.secondTarget);
                            if (!isTarget1 && !isTarget2) continue;


                            domainWarpedVector.x = x;
                            domainWarpedVector.y = y;
                            domainWarpedVector.z = z;
                            noise.DomainWarp(domainWarpedVector);
                            float cellValue = cellNoise.GetNoise(domainWarpedVector.x, domainWarpedVector.y, domainWarpedVector.z);
                            cellValue = (cellValue * 0.5F + 0.5F);
                            int stoneIndex = Mth.floor((cellValue) * config.firstTargetPlacer.size());

                            float seed = cellValue * 1000000;
                            Random patchRandom = new Random((long) seed);
                            boolean isBlankPatch = (patchRandom.nextFloat() < config.blankPatchChance) || cellBufferNoise.GetNoise(domainWarpedVector.x, domainWarpedVector.y, domainWarpedVector.z) > -0.1;


                            boolean passesBiomeFilter = true;
                            if (config.biomes != null && config.useBiomeFilter) {
                                var biome = worldGenLevel.getBiome(currentPos);
                                if (!config.biomes.contains(biome)) {
                                    passesBiomeFilter = false;
                                }
                            }


                        /*
                        if (CommonConfigs.CROSS_SECTION.get()) {
                            if (z < 0 && y < 128)
                                cachedChunk.setBlockState(currentPos, Blocks.BARRIER.defaultBlockState(), false);
                        }
                         */
                            if (passesBiomeFilter) {
                                if (!isBlankPatch) {
                                    if (!config.useHeightFilter || (y > (cachedChunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z) - config.bottomOffset))) {

                                        List<StoneEntry> patternList = null;
                                        if (isTarget1) patternList = config.firstTargetPlacer;
                                        else if (isTarget2) patternList = config.secondTargetPlacer;


                                        if (patternList != null) {
                                            StoneEntry stoneEntry = patternList.get(stoneIndex);
                                            StonePattern stonePattern = stoneEntry.getStonePattern();

                                            if (stonePattern.shouldPlacePrimaryStone(currentPos)) {
                                                worldGenLevel.setBlock(currentPos, stoneEntry.getPrimaryStoneState(worldGenLevel, currentPos), Block.UPDATE_CLIENTS);
                                            } else if (stonePattern.shouldPlaceSecondaryStone(currentPos)) {
                                                worldGenLevel.setBlock(currentPos, stoneEntry.getSecondaryStoneState(worldGenLevel, currentPos), Block.UPDATE_CLIENTS);
                                            }
                                        }

                                    }
                                }
                            }

                        }
                    }
                }
            }
        }

        return false;
    }
}