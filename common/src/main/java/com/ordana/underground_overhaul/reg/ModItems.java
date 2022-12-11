package com.ordana.underground_overhaul.reg;

import com.ordana.underground_overhaul.UndergroundOverhaul;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.mehvahdjukaar.moonlight.api.set.BlockSetAPI;
import net.mehvahdjukaar.moonlight.api.set.leaves.LeavesType;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
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

}
