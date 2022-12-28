package com.ordana.underground_overhaul.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ordana.underground_overhaul.blocks.fungi.FloorAndSidesMushroomBlock;
import com.ordana.underground_overhaul.reg.ModBlocks;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.MultifaceGrowthConfiguration;

import java.util.Objects;

public class WallMushroomFeatureConfig implements FeatureConfiguration {

    public static final Codec<WallMushroomFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(Registry.BLOCK.byNameCodec().fieldOf("block").flatXmap(WallMushroomFeatureConfig::apply, DataResult::success).orElse((FloorAndSidesMushroomBlock) ModBlocks.CONK_FUNGUS.get()).forGetter((wallMushroomFeatureConfig) -> {
            return wallMushroomFeatureConfig.placeBlock;
        }), Codec.BOOL.fieldOf("can_place_on_floor").orElse(false).forGetter((wallMushroomFeatureConfig) -> {
            return wallMushroomFeatureConfig.canPlaceOnFloor;
        }), RegistryCodecs.homogeneousList(Registry.BLOCK_REGISTRY).fieldOf("can_be_placed_on").forGetter((wallMushroomFeatureConfig) -> {
            return wallMushroomFeatureConfig.canBePlacedOn;
        })).apply(instance, WallMushroomFeatureConfig::new);
    });

    public final FloorAndSidesMushroomBlock placeBlock;
    public final boolean canPlaceOnFloor;
    public final HolderSet<Block> canBePlacedOn;

    private static DataResult<FloorAndSidesMushroomBlock> apply(Block block) {
        DataResult var10000;
        if (block instanceof FloorAndSidesMushroomBlock) {
            FloorAndSidesMushroomBlock block1 = (FloorAndSidesMushroomBlock)block;
            var10000 = DataResult.success(block1);
        } else {
            var10000 = DataResult.error("Growth block should be a multiface block");
        }

        return var10000;
    }

    public WallMushroomFeatureConfig(FloorAndSidesMushroomBlock floorAndSidesMushroomBlock, Boolean bl, HolderSet<Block> holderSet) {
        this.placeBlock = floorAndSidesMushroomBlock;
        this.canPlaceOnFloor = bl;
        this.canBePlacedOn = holderSet;
    }
}
