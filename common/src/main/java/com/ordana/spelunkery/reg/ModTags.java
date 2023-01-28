package com.ordana.spelunkery.reg;

import com.ordana.spelunkery.Spelunkery;
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
    public static final TagKey<Block> OCEAN_TARGET = registerBlockTag("ocean_target");
    public static final TagKey<Block> SALT_BLOCKS = registerBlockTag("salt_blocks");
    public static final TagKey<Block> CHISELABLE = registerBlockTag("chiselable");
    public static final TagKey<Block> CAN_BOIL_WATER = registerBlockTag("can_boil_water");

    //items
    public static final TagKey<Item> KEEP_ON_DEATH = registerItemTag("keep_on_death");
    public static final TagKey<Item> GRINDABLE = registerItemTag("grindable");
    public static final TagKey<Item> DIAMOND_GRINDABLE = registerItemTag("diamond_grindable");

    //biomes
    public static final TagKey<Biome> HAS_LUSH_NOISE = registerBiomeTag("has_lush_noise");
    public static final TagKey<Biome> HAS_STONE_NOISE = registerBiomeTag("has_stone_noise");
    public static final TagKey<Biome> HAS_DIRT_NOISE = registerBiomeTag("has_dirt_noise");
    public static final TagKey<Biome> HAS_OCEAN_NOISE = registerBiomeTag("has_ocean_noise");
    public static final TagKey<Biome> HAS_DESERT_NOISE = registerBiomeTag("has_desert_noise");

    //entities
    public static final TagKey<EntityType<?>> HURT_BY_SALT = registerEntityTag("hurt_by_salt");


    private ModTags() {
    }

    private static TagKey<Block> registerBlockTag(String id) {
        return TagKey.create(Registry.BLOCK_REGISTRY, Spelunkery.res(id));
    }

    private static TagKey<Item> registerItemTag(String id) {
        return TagKey.create(Registry.ITEM_REGISTRY, Spelunkery.res(id));
    }

    private static TagKey<Biome> registerBiomeTag(String id) {
        return TagKey.create(Registry.BIOME_REGISTRY, Spelunkery.res(id));
    }

    private static TagKey<EntityType<?>> registerEntityTag(String id) {
        return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, Spelunkery.res(id));
    }
}
