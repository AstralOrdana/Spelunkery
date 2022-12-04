package com.ordana.underhaul;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {

    public static final BlockItem RAW_DIAMOND_BLOCK = new BlockItem(ModBlocks.RAW_DIAMOND_BLOCK, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS));
    public static final BlockItem RAW_EMERALD_BLOCK = new BlockItem(ModBlocks.RAW_EMERALD_BLOCK, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS));

    public static final BlockItem ROUGH_DIAMOND_BLOCK = new BlockItem(ModBlocks.ROUGH_DIAMOND_BLOCK, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS));
    public static final BlockItem ROUGH_EMERALD_BLOCK = new BlockItem(ModBlocks.ROUGH_EMERALD_BLOCK, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS));
    public static final BlockItem ROUGH_QUARTZ_BLOCK = new BlockItem(ModBlocks.ROUGH_QUARTZ_BLOCK, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS));
    public static final BlockItem ROUGH_LAPIS_BLOCK = new BlockItem(ModBlocks.ROUGH_LAPIS_BLOCK, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS));

    public static final Item RAW_DIAMOND = new Item(new Item.Settings().group(ItemGroup.MATERIALS));
    public static final Item RAW_EMERALD = new Item(new Item.Settings().group(ItemGroup.MATERIALS));

    public static final Item ROUGH_DIAMOND = new Item(new Item.Settings().group(ItemGroup.MATERIALS));
    public static final Item ROUGH_EMERALD = new Item(new Item.Settings().group(ItemGroup.MATERIALS));
    public static final Item ROUGH_QUARTZ = new Item(new Item.Settings().group(ItemGroup.MATERIALS));
    public static final Item ROUGH_LAPIS_LAZULI = new Item(new Item.Settings().group(ItemGroup.MATERIALS));

    public static void registerItems() {

        Registry.register(Registry.ITEM, new Identifier(Underhaul.MOD_ID, "raw_diamond_block"), RAW_DIAMOND_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(Underhaul.MOD_ID, "raw_emerald_block"), RAW_EMERALD_BLOCK);

        Registry.register(Registry.ITEM, new Identifier(Underhaul.MOD_ID, "rough_diamond_block"), ROUGH_DIAMOND_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(Underhaul.MOD_ID, "rough_emerald_block"), ROUGH_EMERALD_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(Underhaul.MOD_ID, "rough_quartz_block"), ROUGH_QUARTZ_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(Underhaul.MOD_ID, "rough_lapis_block"), ROUGH_LAPIS_BLOCK);

        Registry.register(Registry.ITEM, new Identifier(Underhaul.MOD_ID, "raw_diamond"), RAW_DIAMOND);
        Registry.register(Registry.ITEM, new Identifier(Underhaul.MOD_ID, "raw_emerald"), RAW_EMERALD);

        Registry.register(Registry.ITEM, new Identifier(Underhaul.MOD_ID, "rough_diamond"), ROUGH_DIAMOND);
        Registry.register(Registry.ITEM, new Identifier(Underhaul.MOD_ID, "rough_emerald"), ROUGH_EMERALD);
        Registry.register(Registry.ITEM, new Identifier(Underhaul.MOD_ID, "rough_lapis_lazuli"), ROUGH_LAPIS_LAZULI);
        Registry.register(Registry.ITEM, new Identifier(Underhaul.MOD_ID, "rough_quartz"), ROUGH_QUARTZ);
    }
}