package com.ordana.underground_overhaul.reg;

import com.ordana.underground_overhaul.UndergroundOverhaul;
import com.ordana.underground_overhaul.items.*;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

import static com.ordana.underground_overhaul.reg.ModCreativeTab.getTab;

public class ModItems {

    public static void init() {
    }

    public static <T extends Item> Supplier<T> regItem(String name, Supplier<T> itemSup) {
        return RegHelper.registerItem(UndergroundOverhaul.res(name), itemSup);
    }

    public static final Supplier<Item> CINNABAR = regItem("cinnabar", () ->
            new Item(new Item.Properties().tab(getTab(CreativeModeTab.TAB_MATERIALS))));
    public static final Supplier<Item> ROUGH_CINNABAR = regItem("rough_cinnabar", () ->
            new Item(new Item.Properties().tab(getTab(CreativeModeTab.TAB_MATERIALS))));
    public static final Supplier<Item> ROUGH_LAZURITE = regItem("rough_lazurite", () ->
            new Item(new Item.Properties().tab(getTab(CreativeModeTab.TAB_MATERIALS))));
    public static final Supplier<Item> ROUGH_EMERALD = regItem("rough_emerald", () ->
            new Item(new Item.Properties().tab(getTab(CreativeModeTab.TAB_MATERIALS))));
    public static final Supplier<Item> ROUGH_DIAMOND = regItem("rough_diamond", () ->
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


    public static final Supplier<Item> GLOWSTICK = regItem("glowstick", () ->
            new GlowstickItem(ModBlocks.GLOWSTICK.get(), new Item.Properties().tab(getTab(CreativeModeTab.TAB_TOOLS))));
    public static final Supplier<Item> ROPE_LADDER = regItem("rope_ladder", () ->
            new RopeLadderBlockItem(ModBlocks.ROPE_LADDER.get(), new Item.Properties().tab(getTab(CreativeModeTab.TAB_TOOLS))));
    public static final Supplier<Item> PORTAL_FLUID_BOTTLE = regItem("portal_fluid_bottle", () ->
            new PortalFluidBottle(new Item.Properties().tab(getTab(CreativeModeTab.TAB_TOOLS)).food(PortalFluidBottle.PORTAL_FLUID).stacksTo(16)));

}
