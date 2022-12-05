package com.ordana.underground_overhaul.reg;

import com.ordana.underground_overhaul.UndergroundOverhaul;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class ModTags {

    //blocks
    public static final TagKey<Block> STONE_TARGET = registerBlockTag("stone_target");
    public static final TagKey<Block> DEEPSLATE_TARGET = registerBlockTag("deepslate_target");
    public static final TagKey<Block> RIVER_TARGET = registerBlockTag("river_target");
    //blocks
    public static final TagKey<Block> OCEAN_TARGET = registerBlockTag("ocean_target");

    //items
    public static final TagKey<Item> ITEM = registerItemTag("item");


    //biomes
    public static final TagKey<Biome> HAS_LUSH_NOISE = registerBiomeTag("has_lush_noise");
    public static final TagKey<Biome> HAS_STONE_NOISE = registerBiomeTag("has_stone_noise");
    public static final TagKey<Biome> HAS_DIRT_NOISE = registerBiomeTag("has_dirt_noise");
    public static final TagKey<Biome> HAS_OCEAN_NOISE = registerBiomeTag("has_ocean_noise");
    public static final TagKey<Biome> HAS_DESERT_NOISE = registerBiomeTag("has_desert_noise");

    //entities
    public static final TagKey<EntityType<?>> LIGHT_FREEZE_IMMUNE = registerEntityTag("light_freeze_immune");


    private ModTags() {
    }

    private static TagKey<Block> registerBlockTag(String id) {
        return TagKey.create(Registry.BLOCK_REGISTRY, UndergroundOverhaul.res(id));
    }

    private static TagKey<Item> registerItemTag(String id) {
        return TagKey.create(Registry.ITEM_REGISTRY, UndergroundOverhaul.res(id));
    }

    private static TagKey<Biome> registerBiomeTag(String id) {
        return TagKey.create(Registry.BIOME_REGISTRY, UndergroundOverhaul.res(id));
    }

    private static TagKey<EntityType<?>> registerEntityTag(String id) {
        return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, UndergroundOverhaul.res(id));
    }
}
