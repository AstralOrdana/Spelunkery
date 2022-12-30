package com.ordana.underground_overhaul.features;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BuddingAmethystBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.material.FluidState;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class UpdateGeodeFeature extends Feature<GeodeConfiguration> {
    private static final Direction[] DIRECTIONS = Direction.values();

    public UpdateGeodeFeature(Codec<GeodeConfiguration> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext<GeodeConfiguration> context) {
        GeodeConfiguration geodeConfiguration = (GeodeConfiguration)context.config();
        RandomSource randomSource = context.random();
        BlockPos blockPos = context.origin();
        WorldGenLevel worldGenLevel = context.level();
        int i = geodeConfiguration.minGenOffset;
        int j = geodeConfiguration.maxGenOffset;
        List<Pair<BlockPos, Integer>> list = Lists.newLinkedList();
        int k = geodeConfiguration.distributionPoints.sample(randomSource);
        WorldgenRandom worldgenRandom = new WorldgenRandom(new LegacyRandomSource(worldGenLevel.getSeed()));
        NormalNoise normalNoise = NormalNoise.create(worldgenRandom, -4, new double[]{1.0D});
        List<BlockPos> list2 = Lists.newLinkedList();
        double d = (double)k / (double)geodeConfiguration.outerWallDistance.getMaxValue();
        GeodeLayerSettings geodeLayerSettings = geodeConfiguration.geodeLayerSettings;
        GeodeBlockSettings geodeBlockSettings = geodeConfiguration.geodeBlockSettings;
        GeodeCrackSettings geodeCrackSettings = geodeConfiguration.geodeCrackSettings;
        double e = 1.0D / Math.sqrt(geodeLayerSettings.filling);
        double f = 1.0D / Math.sqrt(geodeLayerSettings.innerLayer + d);
        double g = 1.0D / Math.sqrt(geodeLayerSettings.middleLayer + d);
        double h = 1.0D / Math.sqrt(geodeLayerSettings.outerLayer + d);
        double l = 1.0D / Math.sqrt(geodeCrackSettings.baseCrackSize + randomSource.nextDouble() / 2.0D + (k > 3 ? d : 0.0D));
        boolean bl = (double)randomSource.nextFloat() < geodeCrackSettings.generateCrackChance;
        int m = 0;

        int n;
        int o;
        BlockPos blockPos2;
        BlockState blockState;
        for(n = 0; n < k; ++n) {
            o = geodeConfiguration.outerWallDistance.sample(randomSource);
            int p = geodeConfiguration.outerWallDistance.sample(randomSource);
            int q = geodeConfiguration.outerWallDistance.sample(randomSource);
            blockPos2 = blockPos.offset(o, p, q);
            blockState = worldGenLevel.getBlockState(blockPos2);
            if (blockState.isAir() || blockState.is(BlockTags.GEODE_INVALID_BLOCKS)) {
                ++m;
                if (m > geodeConfiguration.invalidBlocksThreshold) {
                    return false;
                }
            }

            list.add(Pair.of(blockPos2, geodeConfiguration.pointOffset.sample(randomSource)));
        }

        if (bl) {
            n = randomSource.nextInt(4);
            o = k * 2 + 1;
            if (n == 0) {
                list2.add(blockPos.offset(o, 7, 0));
                list2.add(blockPos.offset(o, 5, 0));
                list2.add(blockPos.offset(o, 1, 0));
            } else if (n == 1) {
                list2.add(blockPos.offset(0, 7, o));
                list2.add(blockPos.offset(0, 5, o));
                list2.add(blockPos.offset(0, 1, o));
            } else if (n == 2) {
                list2.add(blockPos.offset(o, 7, o));
                list2.add(blockPos.offset(o, 5, o));
                list2.add(blockPos.offset(o, 1, o));
            } else {
                list2.add(blockPos.offset(0, 7, 0));
                list2.add(blockPos.offset(0, 5, 0));
                list2.add(blockPos.offset(0, 1, 0));
            }
        }

        List<BlockPos> list3 = Lists.newArrayList();
        Predicate<BlockState> predicate = isReplaceable(geodeConfiguration.geodeBlockSettings.cannotReplace);
        Iterator var48 = BlockPos.betweenClosed(blockPos.offset(i, i, i), blockPos.offset(j, j, j)).iterator();

        while(true) {
            while(true) {
                double s;
                double t;
                BlockPos blockPos3;
                do {
                    if (!var48.hasNext()) {
                        List<BlockState> list4 = geodeBlockSettings.innerPlacements;
                        Iterator var51 = list3.iterator();

                        while(true) {
                            while(var51.hasNext()) {
                                blockPos2 = (BlockPos)var51.next();
                                blockState = (BlockState) Util.getRandom(list4, randomSource);
                                Direction[] var53 = DIRECTIONS;
                                int var37 = var53.length;

                                for(int var54 = 0; var54 < var37; ++var54) {
                                    Direction direction2 = var53[var54];
                                    if (blockState.hasProperty(BlockStateProperties.FACING)) {
                                        blockState = (BlockState)blockState.setValue(BlockStateProperties.FACING, direction2);
                                    }

                                    BlockPos blockPos6 = blockPos2.relative(direction2);
                                    BlockState blockState2 = worldGenLevel.getBlockState(blockPos6);
                                    if (blockState.hasProperty(BlockStateProperties.WATERLOGGED)) {
                                        blockState = (BlockState)blockState.setValue(BlockStateProperties.WATERLOGGED, blockState2.getFluidState().isSource());
                                    }

                                    if (BuddingAmethystBlock.canClusterGrowAtState(blockState2)) {
                                        this.safeSetBlock(worldGenLevel, blockPos6, blockState, predicate);
                                        break;
                                    }
                                }
                            }

                            return true;
                        }
                    }

                    blockPos3 = (BlockPos)var48.next();
                    double r = normalNoise.getValue((double)blockPos3.getX(), (double)blockPos3.getY(), (double)blockPos3.getZ()) * geodeConfiguration.noiseMultiplier;
                    s = 0.0D;
                    t = 0.0D;

                    Iterator var40;
                    Pair pair;
                    for(var40 = list.iterator(); var40.hasNext(); s += Mth.fastInvSqrt(blockPos3.distSqr((Vec3i)pair.getFirst()) + (double)(Integer)pair.getSecond()) + r) {
                        pair = (Pair)var40.next();
                    }

                    BlockPos blockPos4;
                    for(var40 = list2.iterator(); var40.hasNext(); t += Mth.fastInvSqrt(blockPos3.distSqr(blockPos4) + (double)geodeCrackSettings.crackPointOffset) + r) {
                        blockPos4 = (BlockPos)var40.next();
                    }
                } while(s < h);

                if (bl && t >= l && s < e) {
                    this.safeSetBlock(worldGenLevel, blockPos3, Blocks.AIR.defaultBlockState(), predicate);
                    Direction[] var56 = DIRECTIONS;
                    int var59 = var56.length;

                    for(int var42 = 0; var42 < var59; ++var42) {
                        Direction direction = var56[var42];
                        BlockPos blockPos5 = blockPos3.relative(direction);
                        FluidState fluidState = worldGenLevel.getFluidState(blockPos5);
                        if (!fluidState.isEmpty()) {
                            worldGenLevel.scheduleTick(blockPos5, fluidState.getType(), 0);
                        }
                    }
                } else if (s >= e) {
                    this.safeSetBlock(worldGenLevel, blockPos3, geodeBlockSettings.fillingProvider.getState(randomSource, blockPos3), predicate);
                } else if (s >= f) {
                    boolean bl2 = (double)randomSource.nextFloat() < geodeConfiguration.useAlternateLayer0Chance;
                    if (bl2) {
                        this.safeSetBlock(worldGenLevel, blockPos3, geodeBlockSettings.alternateInnerLayerProvider.getState(randomSource, blockPos3), predicate);
                    } else {
                        this.safeSetBlock(worldGenLevel, blockPos3, geodeBlockSettings.innerLayerProvider.getState(randomSource, blockPos3), predicate);
                    }

                    if ((!geodeConfiguration.placementsRequireLayer0Alternate || bl2) && (double)randomSource.nextFloat() < geodeConfiguration.usePotentialPlacementsChance) {
                        list3.add(blockPos3.immutable());
                    }
                } else if (s >= g) {
                    this.safeSetBlock(worldGenLevel, blockPos3, geodeBlockSettings.middleLayerProvider.getState(randomSource, blockPos3), predicate);
                } else if (s >= h) {
                    this.safeSetBlock(worldGenLevel, blockPos3, geodeBlockSettings.outerLayerProvider.getState(randomSource, blockPos3), predicate);
                }
            }
        }
    }

    protected void safeSetBlock(WorldGenLevel level, BlockPos pos, BlockState state, Predicate<BlockState> oldState) {
        if (oldState.test(level.getBlockState(pos))) {
            level.setBlock(pos, state, 2);
            level.scheduleTick(pos, state.getBlock(), 0);
        }
    }
}
