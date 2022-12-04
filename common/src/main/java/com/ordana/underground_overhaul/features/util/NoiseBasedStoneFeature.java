package com.ordana.underground_overhaul.features.util;

import com.ibm.icu.impl.UCharacterUtility;
import com.mojang.serialization.Codec;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeResolver;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.lwjgl.system.CallbackI;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
import java.util.Random;

public class NoiseBasedStoneFeature extends Feature<NoneFeatureConfiguration> {

    private static final BlockState STONE = Blocks.STONE.defaultBlockState();
    private static final BlockState DEEPSLATE = Blocks.DEEPSLATE.defaultBlockState();
    private static final BlockState CLAY = Blocks.CLAY.defaultBlockState();

    private final List<StoneEntry> entryListStone;
    private final List<StoneEntry> entryListDeepslate;
    private final List<ResourceKey<Biome>> biomes;
    private final boolean useBiomeFilter;
    private final float chanceOfBlankPatch;

    public NoiseBasedStoneFeature(Codec<NoneFeatureConfiguration> codec, List<StoneEntry> entryListStone, List<StoneEntry> entryListDeepslate, List<ResourceKey<Biome>> biomes, boolean useBiomeFilter, float chanceOfBlankPatch) {
        super(codec);
        this.entryListDeepslate = entryListDeepslate;
        this.entryListStone = entryListStone;
        this.biomes = biomes;
        this.useBiomeFilter = useBiomeFilter;
        this.chanceOfBlankPatch = chanceOfBlankPatch;
    }

    public static NoiseBasedStoneFeature featureWithDeepslateFiltered(Codec<NoneFeatureConfiguration> codec, List<StoneEntry> blockListStone, List<StoneEntry> blockListDeepslate, List<ResourceKey<Biome>> biomes, float chanceOfBlankPatch) {
        return new NoiseBasedStoneFeature(codec, blockListStone, blockListDeepslate, biomes, true, chanceOfBlankPatch);
    }

    public static NoiseBasedStoneFeature featureWithDeepslateUnfiltered(Codec<NoneFeatureConfiguration> codec, List<StoneEntry> blockListStone, List<StoneEntry> blockListDeepslate, float chanceOfBlankPatch) {
        return new NoiseBasedStoneFeature(codec, blockListStone, blockListDeepslate, null, false, chanceOfBlankPatch);
    }

    public static NoiseBasedStoneFeature featureNoDeepslateFiltered(Codec<NoneFeatureConfiguration> codec, List<StoneEntry> blockListStone, List<ResourceKey<Biome>> biomes, float chanceOfBlankPatch) {
        return new NoiseBasedStoneFeature(codec, blockListStone, blockListStone, biomes, true, chanceOfBlankPatch);
    }

    public static NoiseBasedStoneFeature featureNoDeepslateUniltered(Codec<NoneFeatureConfiguration> codec, List<StoneEntry> blockListStone, float chanceOfBlankPatch) {
        return new NoiseBasedStoneFeature(codec, blockListStone, blockListStone, null, false, chanceOfBlankPatch);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
        RandomSource random = featurePlaceContext.random();
        BlockPos originPos = featurePlaceContext.origin();
        WorldGenLevel worldGenLevel = featurePlaceContext.level();
        ChunkGenerator chunkGenerator = featurePlaceContext.chunkGenerator();

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
        noise.SetFractalOctaves(1);

        for (int x = (int) (Math.floor(originPos.getX() / 16F) * 16); x < (int) (Math.floor(originPos.getX() / 16F) * 16) + 16; x++) {
            for (int z = (int) (Math.floor(originPos.getZ() / 16F) * 16); z < (int) (Math.floor(originPos.getZ() / 16F) * 16) + 16; z++) {
                for (int y = chunkGenerator.getMinY(); y < chunkGenerator.getGenDepth() + chunkGenerator.getMinY(); y++) {

                    float domainWarpValue = noise.GetNoise(x, y, z) * 5F;
                    float cellValue = cellNoise.GetNoise(x + domainWarpValue, y + domainWarpValue, z + domainWarpValue);
                    cellValue = (cellValue * 0.5F + 0.5F);
                    int stoneIndex = Mth.floor((cellValue) * this.entryListStone.size());

                    float seed = cellValue * 1000000;
                    Random patchRandom = new Random((long) seed);
                    boolean isBlankPatch = (patchRandom.nextFloat() < this.chanceOfBlankPatch) || cellBufferNoise.GetNoise(x + domainWarpValue, y + domainWarpValue, z + domainWarpValue) > -0.1;

                    BlockPos currentPos = new BlockPos(x, y, z);
                    BlockState currentState = worldGenLevel.getBlockState(currentPos);

                    boolean passesBiomeFilter = true;
                    /*
                    if(this.biomes != null && this.useBiomeFilter){
                        if(!biomes.contains(featurePlaceContext.level().registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getKey(worldGenLevel.getBiome(currentPos)))) {
                            passesBiomeFilter = false;
                        }
                    }
                     */

                    if(passesBiomeFilter){
                        if((worldGenLevel.dimensionType().hasCeiling() || y < worldGenLevel.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z) - 10) && !isBlankPatch){
                            if(currentState == DEEPSLATE){

                                boolean shouldPlacePrimaryStone = this.entryListDeepslate.get(stoneIndex).getStonePattern().shouldPlacePrimaryStone(currentPos);
                                boolean shouldPlaceSecondaryStone = this.entryListDeepslate.get(stoneIndex).getStonePattern().shouldPlaceSecondaryStone(currentPos);

                                if (shouldPlacePrimaryStone && !shouldPlaceSecondaryStone){
                                    worldGenLevel.setBlock(currentPos, this.entryListDeepslate.get(stoneIndex).getPrimaryStoneState(), Block.UPDATE_CLIENTS);
                                } else if (shouldPlaceSecondaryStone){
                                    worldGenLevel.setBlock(currentPos, this.entryListDeepslate.get(stoneIndex).getSecondaryStoneState(), Block.UPDATE_CLIENTS);
                                }
                            } else if(currentState == STONE){

                                boolean shouldPlacePrimaryStone = this.entryListStone.get(stoneIndex).getStonePattern().shouldPlacePrimaryStone(currentPos);
                                boolean shouldPlaceSecondaryStone = this.entryListStone.get(stoneIndex).getStonePattern().shouldPlaceSecondaryStone(currentPos);

                                if (shouldPlacePrimaryStone && !shouldPlaceSecondaryStone){
                                    worldGenLevel.setBlock(currentPos, this.entryListStone.get(stoneIndex).getPrimaryStoneState(), Block.UPDATE_CLIENTS);
                                } else if (shouldPlaceSecondaryStone){
                                    worldGenLevel.setBlock(currentPos, this.entryListStone.get(stoneIndex).getSecondaryStoneState(), Block.UPDATE_CLIENTS);
                                }
                            }
                        }
                    }


                    if(z < 0 && y < 128){
                        worldGenLevel.setBlock(currentPos, Blocks.BARRIER.defaultBlockState(), 2);
                    }

                }
            }
        }

        return false;
    }

    /*
    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
        Random random = featurePlaceContext.random();
        BlockPos originPos = featurePlaceContext.origin();
        WorldGenLevel worldGenLevel = featurePlaceContext.level();
        ChunkGenerator chunkGenerator = featurePlaceContext.chunkGenerator();

        testRockNoise = new FastNoiseLite(9512);
        testRockNoise.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        testRockNoise.SetRotationType3D(FastNoiseLite.RotationType3D.ImproveXZPlanes);
        testRockNoise.SetFrequency(BASE_FREQUENCY);
        testRockNoise.SetFractalOctaves(2);
        testRockNoise.SetFractalType(FastNoiseLite.FractalType.Ridged);


        domainWarpNoise = new FastNoiseLite(95121);
        domainWarpNoise.SetDomainWarpType(FastNoiseLite.DomainWarpType.OpenSimplex2);
        domainWarpNoise.SetRotationType3D(FastNoiseLite.RotationType3D.ImproveXZPlanes);
        domainWarpNoise.SetFrequency(BASE_FREQUENCY / 3F);
        domainWarpNoise.SetDomainWarpAmp(1.0F);
        domainWarpNoise.SetFractalOctaves(1);

        FastNoiseLite.Vector3 warpCoord = new FastNoiseLite.Vector3(0, 0, 0);

        //Iterate over every block
        for(int x = (int) (Math.floor(originPos.getX() / 16F) * 16); x < (int) (Math.floor(originPos.getX() / 16F) * 16) + 16; x++){
            for(int z = (int) (Math.floor(originPos.getZ() / 16F) * 16); z < (int) (Math.floor(originPos.getZ() / 16F) * 16) + 16; z++){
                for(int y = chunkGenerator.getMinY(); y < chunkGenerator.getGenDepth() + chunkGenerator.getMinY(); y++){
                    BlockPos currentPos = new BlockPos(x, y, z);

                    warpCoord.x = x;
                    warpCoord.y = y;
                    warpCoord.z = z;
                    domainWarpNoise.DomainWarp(warpCoord);

                    float testGradient = Mth.clamp(y, -32, 32) / 32F;
                    float testNoiseValue = testRockNoise.GetNoise(warpCoord.x, warpCoord.y, warpCoord.z) + testGradient;
                    BlockState blockState = testNoiseValue > 0 ? STONE : DEEPSLATE;
                    blockState = worldGenLevel.getBiome(currentPos).getBiomeCategory() == Biome.BiomeCategory.UNDERGROUND ? CLAY : blockState;

                    worldGenLevel.getBiome(currentPos) == Registry.;

                    setBlockReplace(worldGenLevel, currentPos, STONE, DEEPSLATE);
                    setBlockReplace(worldGenLevel, currentPos, blockState, STONE);
                    if(z < 0){
                        worldGenLevel.setBlock(currentPos, Blocks.BARRIER.defaultBlockState(), 2);
                    }
                }
            }
        }
        return false;
    }

    private void setBlockReplace(WorldGenLevel worldGenLevel, BlockPos blockPos, BlockState blockState, BlockState replaceState){
        if(worldGenLevel.getBlockState(blockPos) == replaceState){
            worldGenLevel.setBlock(blockPos, blockState, 2);
        }
    }

     */


}


//blockState = worldGenLevel.getBiome(currentPos).getBiomeCategory() == Biome.BiomeCategory.UNDERGROUND ? CLAY : blockState;

//featurePlaceContext.level().registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getKey(worldGenLevel.getBiome(currentPos))
