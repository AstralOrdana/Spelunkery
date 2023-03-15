package com.ordana.spelunkery.features;

import com.mojang.serialization.Codec;
import com.ordana.spelunkery.configs.CommonConfigs;
import com.ordana.spelunkery.features.util.FastNoiseLite;
import com.ordana.spelunkery.features.util.StoneEntry;
import com.ordana.spelunkery.features.util.StonePattern;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.List;
import java.util.Random;

public class BlockStripeFeature extends Feature<BlockStripeFeatureConfig> {

    private final List<StoneEntry> entryListStone;
    private final List<StoneEntry> entryListDeepslate;
    private final TagKey<Biome> biomes;
    private final boolean useBiomeFilter;
    private final float chanceOfBlankPatch;
    private final TagKey<Block> target1;
    private final TagKey<Block> target2;
    private final boolean useHeightFilter;
    private final int surfaceOffset;
    private final int bottomOffset;

    public BlockStripeFeature(Codec<BlockStripeFeatureConfig> codec, List<StoneEntry> entryListStone, List<StoneEntry> entryListDeepslate, TagKey<Biome> biomes, boolean useBiomeFilter, float chanceOfBlankPatch, TagKey<Block> target1, TagKey<Block> target2, boolean useHeightFilter, int surfaceOffset , int bottomOffset) {
        super(codec);
        this.entryListDeepslate = entryListDeepslate;
        this.entryListStone = entryListStone;
        this.biomes = biomes;
        this.useBiomeFilter = useBiomeFilter;
        this.chanceOfBlankPatch = chanceOfBlankPatch;
        this.target1 = target1;
        this.target2 = target2;
        this.useHeightFilter = useHeightFilter;
        this.surfaceOffset = surfaceOffset;
        this.bottomOffset = bottomOffset;
    }

    public static BlockStripeFeature featureWithDeepslateFiltered(Codec<BlockStripeFeatureConfig> codec, List<StoneEntry> blockListStone, List<StoneEntry> blockListDeepslate, TagKey<Biome> biomes, float chanceOfBlankPatch, TagKey<Block> target1, TagKey<Block> target2, boolean useHeightFilter, int surfaceOffset, int bottomOffset) {
        return new BlockStripeFeature(codec, blockListStone, blockListDeepslate, biomes, true, chanceOfBlankPatch, target1, target2, useHeightFilter, surfaceOffset, bottomOffset);
    }

    public static BlockStripeFeature featureWithDeepslateUnfiltered(Codec<BlockStripeFeatureConfig> codec, List<StoneEntry> blockListStone, List<StoneEntry> blockListDeepslate, float chanceOfBlankPatch, TagKey<Block> target1, TagKey<Block> target2, boolean useHeightFilter, int surfaceOffset, int bottomOffset) {
        return new BlockStripeFeature(codec, blockListStone, blockListDeepslate, null, false, chanceOfBlankPatch, target1, target2, useHeightFilter, surfaceOffset, bottomOffset);
    }

    public static BlockStripeFeature featureNoDeepslateFiltered(Codec<BlockStripeFeatureConfig> codec, List<StoneEntry> blockListStone, TagKey<Biome> biomes, float chanceOfBlankPatch, TagKey<Block> target1, boolean useHeightFilter, int surfaceOffset, int bottomOffset) {
        return new BlockStripeFeature(codec, blockListStone, blockListStone, biomes, true, chanceOfBlankPatch, target1, null, useHeightFilter, surfaceOffset, bottomOffset);
    }

    public static BlockStripeFeature featureNoDeepslateUnfiltered(Codec<BlockStripeFeatureConfig> codec, List<StoneEntry> blockListStone, float chanceOfBlankPatch, TagKey<Block> target1, boolean useHeightFilter, int surfaceOffset, int bottomOffset) {
        return new BlockStripeFeature(codec, blockListStone, blockListStone, null, false, chanceOfBlankPatch, target1, null, useHeightFilter, surfaceOffset, bottomOffset);
    }

    @Override
    public boolean place(FeaturePlaceContext<BlockStripeFeatureConfig> featurePlaceContext) {

        if (CommonConfigs.STONE_STRIPE_FEATURES.get()) {
            RandomSource random = featurePlaceContext.random();
            BlockPos originPos = featurePlaceContext.origin();
            WorldGenLevel worldGenLevel = featurePlaceContext.level();
            ChunkGenerator chunkGenerator = featurePlaceContext.chunkGenerator();

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
            noise.SetDomainWarpAmp(5F);

            FastNoiseLite.Vector3 domainWarpedVector = new FastNoiseLite.Vector3(originPos.getX(), originPos.getY(), originPos.getZ());

            noise.SetFractalOctaves(1);
            var getX = (originPos.getX() & ~15);
            var getZ = (originPos.getZ() & ~15);

            for (int x = getX; x < getX + 16; x++) {
                for (int z = getZ; z < getZ + 16; z++) {
                    var heightmap = worldGenLevel.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, x, z);
                    for (int y = chunkGenerator.getMinY(); y < heightmap - surfaceOffset; y++) {

                        domainWarpedVector.x = x;
                        domainWarpedVector.y = y;
                        domainWarpedVector.z = z;
                        noise.DomainWarp(domainWarpedVector);
                        float cellValue = cellNoise.GetNoise(domainWarpedVector.x, domainWarpedVector.y, domainWarpedVector.z);
                        cellValue = (cellValue * 0.5F + 0.5F);
                        int stoneIndex = Mth.floor((cellValue) * this.entryListStone.size());

                        float seed = cellValue * 1000000;
                        Random patchRandom = new Random((long) seed);
                        boolean isBlankPatch = (patchRandom.nextFloat() < this.chanceOfBlankPatch) || cellBufferNoise.GetNoise(domainWarpedVector.x, domainWarpedVector.y, domainWarpedVector.z) > -0.1;

                        BlockPos currentPos = new BlockPos(x, y, z);
                        BlockState currentState = cachedChunk.getBlockState(currentPos);

                        boolean passesBiomeFilter = true;
                        if (this.biomes != null && this.useBiomeFilter) {
                            if (!worldGenLevel.getBiome(currentPos).is(biomes)) {
                                passesBiomeFilter = false;
                            }
                        }

                        if (passesBiomeFilter) {
                            if (!isBlankPatch) {
                                if (!useHeightFilter || (y > (cachedChunk.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, x, z) - bottomOffset))) {

                                    List<StoneEntry> patternList = null;
                                    if (target2 != null && currentState.is(target2)) patternList = this.entryListDeepslate;
                                    else if (currentState.is(target1)) patternList = this.entryListStone;

                                    if (patternList != null) {
                                        StoneEntry stoneEntry = patternList.get(stoneIndex);
                                        StonePattern stonePattern = stoneEntry.getStonePattern();

                                        if (stonePattern.shouldPlaceSecondaryStone(currentPos)) {
                                            worldGenLevel.setBlock(currentPos, stoneEntry.getSecondaryStoneState(), Block.UPDATE_CLIENTS);
                                        } else if (stonePattern.shouldPlacePrimaryStone(currentPos)) {
                                            worldGenLevel.setBlock(currentPos, stoneEntry.getPrimaryStoneState(), Block.UPDATE_CLIENTS);
                                        }
                                    }

                                }
                            }
                        }
                        if (CommonConfigs.CROSS_SECTION.get()) {
                            if (z < 0 && y < 128)
                                cachedChunk.setBlockState(currentPos, Blocks.BARRIER.defaultBlockState(), false);
                        }
                    }
                }
            }
        }

        return false;
    }
}