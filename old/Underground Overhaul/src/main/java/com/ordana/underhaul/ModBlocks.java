package com.ordana.underhaul;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {

    public static final Block RAW_DIAMOND_BLOCK = new Block(FabricBlockSettings.of(Material.STONE, MapColor.DIAMOND_BLUE).requiresTool().strength(5f, 6f));
    public static final Block RAW_EMERALD_BLOCK = new Block(FabricBlockSettings.of(Material.STONE, MapColor.EMERALD_GREEN).requiresTool().strength(5f, 6f));

    public static final Block ROUGH_DIAMOND_BLOCK = new Block(FabricBlockSettings.of(Material.STONE, MapColor.DIAMOND_BLUE).requiresTool().strength(5f, 6f));
    public static final Block ROUGH_EMERALD_BLOCK = new Block(FabricBlockSettings.of(Material.STONE, MapColor.EMERALD_GREEN).requiresTool().strength(5f, 6f));
    public static final Block ROUGH_QUARTZ_BLOCK = new Block(FabricBlockSettings.of(Material.STONE, MapColor.OFF_WHITE).requiresTool().strength(0.8F));
    public static final Block ROUGH_LAPIS_BLOCK = new Block(FabricBlockSettings.of(Material.STONE, MapColor.LAPIS_BLUE).requiresTool().strength(3.0F, 3.0F));

    public static void registerBlocks() {

        Registry.register(Registry.BLOCK, new Identifier(Underhaul.MOD_ID, "raw_diamond_block"), RAW_DIAMOND_BLOCK);
        Registry.register(Registry.BLOCK, new Identifier(Underhaul.MOD_ID, "raw_emerald_block"), RAW_EMERALD_BLOCK);

        Registry.register(Registry.BLOCK, new Identifier(Underhaul.MOD_ID, "rough_diamond_block"), ROUGH_DIAMOND_BLOCK);
        Registry.register(Registry.BLOCK, new Identifier(Underhaul.MOD_ID, "rough_emerald_block"), ROUGH_EMERALD_BLOCK);
        Registry.register(Registry.BLOCK, new Identifier(Underhaul.MOD_ID, "rough_quartz_block"), ROUGH_QUARTZ_BLOCK);
        Registry.register(Registry.BLOCK, new Identifier(Underhaul.MOD_ID, "rough_lapis_block"), ROUGH_LAPIS_BLOCK);

    }
}
