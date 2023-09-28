package com.ordana.spelunkery.worldgen.feature_configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ordana.spelunkery.blocks.fungi.FloorAndSidesMushroomBlock;
import com.ordana.spelunkery.reg.ModBlocks;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class WallMushroomFeatureConfig implements FeatureConfiguration {

    public static final Codec<WallMushroomFeatureConfig> CODEC = RecordCodecBuilder.create((instance)
            -> instance.group(BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").flatXmap(WallMushroomFeatureConfig::apply, DataResult::success).orElse((FloorAndSidesMushroomBlock) ModBlocks.CONK_FUNGUS.get()).forGetter((wallMushroomFeatureConfig)
                            -> wallMushroomFeatureConfig.placeBlock),

                    Codec.BOOL.fieldOf("can_place_on_floor").orElse(false).forGetter((wallMushroomFeatureConfig)
                            -> wallMushroomFeatureConfig.canPlaceOnFloor),

                    RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("can_be_placed_on").forGetter((wallMushroomFeatureConfig)
                            -> wallMushroomFeatureConfig.canBePlacedOn))

            .apply(instance, WallMushroomFeatureConfig::new));

    public final FloorAndSidesMushroomBlock placeBlock;
    public final boolean canPlaceOnFloor;
    public final HolderSet<Block> canBePlacedOn;

    private static DataResult<FloorAndSidesMushroomBlock> apply(Block block) {
        DataResult var10000;
        if (block instanceof FloorAndSidesMushroomBlock) {
            FloorAndSidesMushroomBlock block1 = (FloorAndSidesMushroomBlock)block;
            var10000 = DataResult.success(block1);
        } else {
            var10000 = DataResult.error(() -> "Growth block should be a multiface block");
        }

        return var10000;
    }

    public WallMushroomFeatureConfig(FloorAndSidesMushroomBlock floorAndSidesMushroomBlock, Boolean bl, HolderSet<Block> holderSet) {
        this.placeBlock = floorAndSidesMushroomBlock;
        this.canPlaceOnFloor = bl;
        this.canBePlacedOn = holderSet;
    }
}
