package com.ordana.spelunkery.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ordana.spelunkery.reg.ModBlocks;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class CrystalFeatureConfig implements FeatureConfiguration {

    public static final Codec<CrystalFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(Registry.BLOCK.byNameCodec().fieldOf("block").flatXmap(CrystalFeatureConfig::apply, DataResult::success).orElse(ModBlocks.ROCK_SALT.get()).forGetter((crystalFeatureConfig) -> {
            return crystalFeatureConfig.placeBlock;
        }), RegistryCodecs.homogeneousList(Registry.BLOCK_REGISTRY).fieldOf("can_be_placed_on").forGetter((crystalFeatureConfig) -> {
            return crystalFeatureConfig.canBePlacedOn;
        })).apply(instance, CrystalFeatureConfig::new);
    });

    public final Block placeBlock;
    public final HolderSet<Block> canBePlacedOn;

    private static DataResult<Block> apply(Block block) {
        DataResult var10000;
        if (block.getStateDefinition().any().hasProperty(BlockStateProperties.FACING)) {
            Block block1 = block;
            var10000 = DataResult.success(block1);
        } else {
            var10000 = DataResult.error("Block needs to have the FACING property");
        }

        return var10000;
    }

    public CrystalFeatureConfig(Block block, HolderSet<Block> holderSet) {
        this.placeBlock = block;
        this.canBePlacedOn = holderSet;
    }
}
