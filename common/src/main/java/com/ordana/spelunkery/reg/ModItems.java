package com.ordana.spelunkery.reg;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.items.*;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;

import java.util.Objects;
import java.util.function.Supplier;

import static com.ordana.spelunkery.reg.ModCreativeTab.getTab;

public class ModItems {

    public static void init() {
    }

    public static <T extends Item> Supplier<T> regItem(String name, Supplier<T> itemSup) {
        return RegHelper.registerItem(Spelunkery.res(name), itemSup);
    }

    private static boolean isCompatItemEanbled(String requiredMod) {
        if(Objects.equals(requiredMod, "create")) return requiredMod.equals("create");
        return PlatformHelper.isModLoaded(requiredMod);
    }

    public static final Supplier<Item> ROCK_SALT = regItem("rock_salt", () ->
            new RockSaltBlockItem(ModBlocks.ROCK_SALT.get(), new Item.Properties().tab(getTab(CreativeModeTab.TAB_DECORATIONS))));
    public static final Supplier<Item> SALT = regItem("salt", () ->
            new SaltBlockItem(ModBlocks.SALT.get(), new Item.Properties().tab(getTab(CreativeModeTab.TAB_MATERIALS))));
    public static final Supplier<Item> NEPHRITE_CHUNK = regItem("nephrite_chunk", () ->
            new Item(new Item.Properties().tab(getTab(CreativeModeTab.TAB_MATERIALS))));

    public static final Supplier<Item> RAW_IRON_NUGGET = regItem("raw_iron_nugget", () ->
            new Item(new Item.Properties().tab(getTab(CreativeModeTab.TAB_MATERIALS))));
    public static final Supplier<Item> RAW_COPPER_NUGGET = regItem("raw_copper_nugget", () ->
            new Item(new Item.Properties().tab(getTab(isCompatItemEanbled("create") ? getTab(CreativeModeTab.TAB_MATERIALS) : null))));
    public static final Supplier<Item> RAW_GOLD_NUGGET = regItem("raw_gold_nugget", () ->
            new Item(new Item.Properties().tab(getTab(CreativeModeTab.TAB_MATERIALS))));
    public static final Supplier<Item> RAW_ZINC_NUGGET = regItem("raw_zinc_nugget", () ->
            new Item(new Item.Properties().tab(getTab(isCompatItemEanbled("create") ? getTab(CreativeModeTab.TAB_MATERIALS) : null))));

    public static final Supplier<Item> ROUGH_CINNABAR = regItem("rough_cinnabar", () ->
            new Item(new Item.Properties().tab(getTab(CreativeModeTab.TAB_MATERIALS))));
    public static final Supplier<Item> ROUGH_LAZURITE = regItem("rough_lazurite", () ->
            new Item(new Item.Properties().tab(getTab(CreativeModeTab.TAB_MATERIALS))));
    public static final Supplier<Item> ROUGH_EMERALD = regItem("rough_emerald", () ->
            new Item(new Item.Properties().tab(getTab(CreativeModeTab.TAB_MATERIALS))));
    public static final Supplier<Item> ROUGH_DIAMOND = regItem("rough_diamond", () ->
            new Item(new Item.Properties().tab(getTab(CreativeModeTab.TAB_MATERIALS))));
    public static final Supplier<Item> CINNABAR = regItem("cinnabar", () ->
            new Item(new Item.Properties().tab(getTab(CreativeModeTab.TAB_MATERIALS))));


    public static final Supplier<Item> NEPHRITE_CHARM = regItem("nephrite_charm", () ->
            new NephriteCharmItem(new Item.Properties().tab(getTab(CreativeModeTab.TAB_TOOLS))));

    public static final Supplier<Item> CARVED_NEPHRITE = regItem("carved_nephrite", () ->
            new CarvedNephriteItem(ModBlocks.CARVED_NEPHRITE.get(), new Item.Properties().tab(getTab(CreativeModeTab.TAB_REDSTONE))));
    public static final Supplier<Item> NEPHRITE_SIPHON = regItem("nephrite_siphon", () ->
            new NephriteSiphonItem(ModBlocks.NEPHRITE_SIPHON.get(), new Item.Properties().tab(getTab(CreativeModeTab.TAB_REDSTONE))));
    public static final Supplier<Item> NEPHRITE_SPOUT = regItem("nephrite_spout", () ->
            new NephriteSpoutItem(ModBlocks.NEPHRITE_SPOUT.get(), new Item.Properties().tab(getTab(CreativeModeTab.TAB_REDSTONE))));
    public static final Supplier<Item> NEPHRITE_DIODE = regItem("nephrite_diode", () ->
            new NephriteDiodeItem(ModBlocks.NEPHRITE_DIODE.get(), new Item.Properties().tab(getTab(CreativeModeTab.TAB_REDSTONE))));

    //food
    public static final Supplier<Item> BUTTON_MUSHROOM = regItem("button_mushroom", () ->
            new BlockItem(ModBlocks.BUTTON_MUSHROOM.get(), new Item.Properties().tab(getTab(CreativeModeTab.TAB_DECORATIONS)).food(ModFoods.BUTTON_MUSHROOM)));
    public static final Supplier<Item> CRIMINI = regItem("crimini", () ->
            new BlockItem(ModBlocks.CRIMINI.get(), new Item.Properties().tab(getTab(CreativeModeTab.TAB_DECORATIONS)).food(ModFoods.CRIMINI)));
    public static final Supplier<Item> PORTABELLA = regItem("portabella", () ->
            new BlockItem(ModBlocks.PORTABELLA.get(), new Item.Properties().tab(getTab(CreativeModeTab.TAB_DECORATIONS)).food(ModFoods.PORTABELLA)));
    public static final Supplier<Item> GRILLED_PORTABELLA = regItem("grilled_portabella", () ->
            new Item( new Item.Properties().tab(getTab(CreativeModeTab.TAB_FOOD)).food(ModFoods.GRILLED_PORTABELLA)));


    //mining gear
    public static final Supplier<Item> ROPE_LADDER = regItem("rope_ladder", () ->
            new RopeLadderBlockItem(ModBlocks.ROPE_LADDER.get(), new Item.Properties().tab(getTab(CreativeModeTab.TAB_TOOLS))));
    public static final Supplier<Item> WOODEN_RAIL = regItem("wooden_rail", () ->
            new WoodenRailBlockItem(ModBlocks.WOODEN_RAIL.get(), new Item.Properties().tab(getTab(CreativeModeTab.TAB_TRANSPORTATION))));
    public static final Supplier<Item> PORTAL_FLUID_BOTTLE = regItem("portal_fluid_bottle", () ->
            new PortalFluidBottle(new Item.Properties().tab(getTab(CreativeModeTab.TAB_TOOLS)).food(PortalFluidBottle.PORTAL_FLUID).stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final Supplier<Item> DEPTH_GAUGE = regItem("depth_gauge", () ->
            new DepthGaugeItem(new Item.Properties().tab(getTab(CreativeModeTab.TAB_TOOLS))));
    public static final Supplier<Item> GLOWSTICK = regItem("glowstick", () ->
            new GlowstickItem(null, ModBlocks.GLOWSTICK.get(), new Item.Properties().tab(getTab(CreativeModeTab.TAB_TOOLS))));

    //glowsticks
    public static final Supplier<Item> RED_GLOWSTICK = regItem("red_glowstick", () ->
            new GlowstickItem(DyeColor.RED, ModBlocks.RED_GLOWSTICK.get(), new Item.Properties().tab(getTab(CreativeModeTab.TAB_TOOLS))));
    public static final Supplier<Item> ORANGE_GLOWSTICK = regItem("orange_glowstick", () ->
            new GlowstickItem(DyeColor.ORANGE, ModBlocks.ORANGE_GLOWSTICK.get(), new Item.Properties().tab(getTab(CreativeModeTab.TAB_TOOLS))));
    public static final Supplier<Item> YELLOW_GLOWSTICK = regItem("yellow_glowstick", () ->
            new GlowstickItem(DyeColor.YELLOW, ModBlocks.YELLOW_GLOWSTICK.get(), new Item.Properties().tab(getTab(CreativeModeTab.TAB_TOOLS))));
    public static final Supplier<Item> LIME_GLOWSTICK = regItem("lime_glowstick", () ->
            new GlowstickItem(DyeColor.LIME, ModBlocks.LIME_GLOWSTICK.get(), new Item.Properties().tab(getTab(CreativeModeTab.TAB_TOOLS))));
    public static final Supplier<Item> GREEN_GLOWSTICK = regItem("green_glowstick", () ->
            new GlowstickItem(DyeColor.GREEN, ModBlocks.GREEN_GLOWSTICK.get(), new Item.Properties().tab(getTab(CreativeModeTab.TAB_TOOLS))));
    public static final Supplier<Item> CYAN_GLOWSTICK = regItem("cyan_glowstick", () ->
            new GlowstickItem(DyeColor.CYAN, ModBlocks.CYAN_GLOWSTICK.get(), new Item.Properties().tab(getTab(CreativeModeTab.TAB_TOOLS))));
    public static final Supplier<Item> LIGHT_BLUE_GLOWSTICK = regItem("light_blue_glowstick", () ->
            new GlowstickItem(DyeColor.LIGHT_BLUE, ModBlocks.LIGHT_BLUE_GLOWSTICK.get(), new Item.Properties().tab(getTab(CreativeModeTab.TAB_TOOLS))));
    public static final Supplier<Item> BLUE_GLOWSTICK = regItem("blue_glowstick", () ->
            new GlowstickItem(DyeColor.BLUE, ModBlocks.BLUE_GLOWSTICK.get(), new Item.Properties().tab(getTab(CreativeModeTab.TAB_TOOLS))));
    public static final Supplier<Item> PURPLE_GLOWSTICK = regItem("purple_glowstick", () ->
            new GlowstickItem(DyeColor.PURPLE, ModBlocks.PURPLE_GLOWSTICK.get(), new Item.Properties().tab(getTab(CreativeModeTab.TAB_TOOLS))));
    public static final Supplier<Item> MAGENTA_GLOWSTICK = regItem("magenta_glowstick", () ->
            new GlowstickItem(DyeColor.MAGENTA, ModBlocks.MAGENTA_GLOWSTICK.get(), new Item.Properties().tab(getTab(CreativeModeTab.TAB_TOOLS))));
    public static final Supplier<Item> PINK_GLOWSTICK = regItem("pink_glowstick", () ->
            new GlowstickItem(DyeColor.PINK, ModBlocks.PINK_GLOWSTICK.get(), new Item.Properties().tab(getTab(CreativeModeTab.TAB_TOOLS))));
    public static final Supplier<Item> BROWN_GLOWSTICK = regItem("brown_glowstick", () ->
            new GlowstickItem(DyeColor.BROWN, ModBlocks.BROWN_GLOWSTICK.get(), new Item.Properties().tab(getTab(CreativeModeTab.TAB_TOOLS))));
    public static final Supplier<Item> BLACK_GLOWSTICK = regItem("black_glowstick", () ->
            new GlowstickItem(DyeColor.BLACK, ModBlocks.BLACK_GLOWSTICK.get(), new Item.Properties().tab(getTab(CreativeModeTab.TAB_TOOLS))));
    public static final Supplier<Item> WHITE_GLOWSTICK = regItem("white_glowstick", () ->
            new GlowstickItem(DyeColor.WHITE, ModBlocks.WHITE_GLOWSTICK.get(), new Item.Properties().tab(getTab(CreativeModeTab.TAB_TOOLS))));
    public static final Supplier<Item> GRAY_GLOWSTICK = regItem("gray_glowstick", () ->
            new GlowstickItem(DyeColor.GRAY, ModBlocks.GRAY_GLOWSTICK.get(), new Item.Properties().tab(getTab(CreativeModeTab.TAB_TOOLS))));
    public static final Supplier<Item> LIGHT_GRAY_GLOWSTICK = regItem("light_gray_glowstick", () ->
            new GlowstickItem(DyeColor.LIGHT_GRAY, ModBlocks.LIGHT_GRAY_GLOWSTICK.get(), new Item.Properties().tab(getTab(CreativeModeTab.TAB_TOOLS))));

}
