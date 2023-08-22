package com.ordana.spelunkery.loot_modifiers;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.configs.CommonConfigs;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class ModLootInjects {

    private static final List<String> oreStoneDrops = List.of(
            "diamond_ore",
            "emerald_ore",
            "redstone_ore",
            "lapis_ore",
            "iron_ore",
            "copper_ore",
            "gold_ore",
            "coal_ore"
    );

    private static final List<String> oreDeepslateDrops = List.of(
            "deepslate_diamond_ore",
            "deepslate_emerald_ore",
            "deepslate_redstone_ore",
            "deepslate_lapis_ore",
            "deepslate_iron_ore",
            "deepslate_copper_ore",
            "deepslate_gold_ore",
            "deepslate_coal_ore"
    );

    private static final List<String> oreGraniteDrops = List.of(
            "granite_diamond_ore",
            "granite_emerald_ore",
            "granite_redstone_ore",
            "granite_lapis_ore",
            "granite_iron_ore",
            "granite_copper_ore",
            "granite_gold_ore",
            "granite_coal_ore"
    );

    private static final List<String> oreDioriteDrops = List.of(
            "diorite_diamond_ore",
            "diorite_emerald_ore",
            "diorite_redstone_ore",
            "diorite_lapis_ore",
            "diorite_iron_ore",
            "diorite_copper_ore",
            "diorite_gold_ore",
            "diorite_coal_ore"
    );

    private static final List<String> oreAndesiteDrops = List.of(
            "andesite_diamond_ore",
            "andesite_emerald_ore",
            "andesite_redstone_ore",
            "andesite_lapis_ore",
            "andesite_iron_ore",
            "andesite_copper_ore",
            "andesite_gold_ore",
            "andesite_coal_ore"
    );

    private static final List<String> oreTuffDrops = List.of(
            "tuff_diamond_ore",
            "tuff_emerald_ore",
            "tuff_redstone_ore",
            "tuff_lapis_ore",
            "tuff_iron_ore",
            "tuff_copper_ore",
            "tuff_gold_ore",
            "tuff_coal_ore"
    );

    private static final List<String> oreStoneDropsOther = List.of(
            "smooth_basalt_diamond_ore",
            "calcite_redstone_ore",
            "sandstone_lapis_ore"
    );

    private static final List<String> oreNetherDrops = List.of(
            "nether_gold_ore",
            "nether_quartz_ore"
    );

    private static final List<String> lootChests = List.of(
            "abandoned_mineshaft",
            "ruined_portal",
            "stronghold_library",
            "ancient_city"
    );

    public static void onLootInject(RegHelper.LootInjectEvent event) {
        if (!CommonConfigs.ORE_STONE_DROPS.get()) return;

        ResourceLocation name = event.getTable();
        String path = name.getNamespace();
        if (!path.equals("minecraft") && !path.equals("spelunkery")) return;


        for (var loot : lootChests) {
            if (name.equals(new ResourceLocation("minecraft", "chests/" + loot))) {
                event.addTableReference(Spelunkery.res("injects/" + loot));
            }
        }

        if (PlatHelper.getPlatform() == PlatHelper.Platform.FORGE) return;

        if (PlatHelper.isModLoaded("ditr")) {
            if (name.equals(new ResourceLocation("ditr", "blocks/obsidian_diamond_ore"))) {
                event.addTableReference(Spelunkery.res("injects/ores/obsidian"));
            }

        }

        for (var loot : oreNetherDrops) {
            if (name.equals(new ResourceLocation("minecraft", "blocks/" + loot))) {
                event.addTableReference(Spelunkery.res("injects/ores/netherrack"));
            }
        }

        for (var loot : oreStoneDropsOther) {
            if (name.equals(new ResourceLocation("spelunkery", "blocks/" + loot))) {
                event.addTableReference(Spelunkery.res("injects/ores/" + loot));
            }
        }

        for (var loot : oreStoneDrops) {
            if (name.equals(new ResourceLocation("minecraft", "blocks/" + loot))) {
                event.addTableReference(Spelunkery.res("injects/ores/stone"));
            }
        }

        for (var loot : oreDeepslateDrops) {
            if (name.equals(new ResourceLocation("minecraft", "blocks/" + loot))) {
                event.addTableReference(Spelunkery.res("injects/ores/deepslate"));
            }
        }

        for (var loot : oreAndesiteDrops) {
            if (name.equals(new ResourceLocation("spelunkery", "blocks/" + loot))) {
                event.addTableReference(Spelunkery.res("injects/ores/andesite"));

            }
        }

        for (var loot : oreDioriteDrops) {
            if (name.equals(new ResourceLocation("spelunkery", "blocks/" + loot))) {
                event.addTableReference(Spelunkery.res("injects/ores/diorite"));
            }
        }

        for (var loot : oreGraniteDrops) {
            if (name.equals(new ResourceLocation("spelunkery", "blocks/" + loot))) {
                event.addTableReference(Spelunkery.res("injects/ores/granite"));

            }
        }

        for (var loot : oreTuffDrops) {
            if (name.equals(new ResourceLocation("spelunkery", "blocks/" + loot))) {
                event.addTableReference(Spelunkery.res("injects/ores/tuff"));
            }
        }
    }
}
