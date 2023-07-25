package com.ordana.spelunkery.loot_modifiers;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.configs.CommonConfigs;
import net.mehvahdjukaar.moonlight.api.platform.ForgeHelper;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;

import java.util.List;
import java.util.function.Consumer;

public class ModLootInjects {

    public static void onLootInject(LootTables lootManager, ResourceLocation name, Consumer<LootPool.Builder> builderConsumer) {

        var lootChests = List.of(
                "abandoned_mineshaft",
                "ruined_portal",
                "stronghold_library",
                "ancient_city"
        );


        for (var loot : lootChests) {
            if (name.equals(new ResourceLocation("minecraft", "chests/" + loot))) {
                {
                    LootPool.Builder pool = LootPool.lootPool();
                    pool.add(LootTableReference.lootTableReference(Spelunkery.res("injects/" + loot)));
                    ForgeHelper.setPoolName(pool, "spelunkery_" + loot);
                    builderConsumer.accept(pool);
                }
            }
        }

        var oreStoneDrops = List.of(
                "diamond_ore",
                "emerald_ore",
                "redstone_ore",
                "lapis_ore",
                "iron_ore",
                "copper_ore",
                "gold_ore",
                "coal_ore"
        );

        var oreDeepslateDrops = List.of(
                "deepslate_diamond_ore",
                "deepslate_emerald_ore",
                "deepslate_redstone_ore",
                "deepslate_lapis_ore",
                "deepslate_iron_ore",
                "deepslate_copper_ore",
                "deepslate_gold_ore",
                "deepslate_coal_ore"
        );

        var oreGraniteDrops = List.of(
                "granite_diamond_ore",
                "granite_emerald_ore",
                "granite_redstone_ore",
                "granite_lapis_ore",
                "granite_iron_ore",
                "granite_copper_ore",
                "granite_gold_ore",
                "granite_coal_ore"
        );

        var oreDioriteDrops = List.of(
                "diorite_diamond_ore",
                "diorite_emerald_ore",
                "diorite_redstone_ore",
                "diorite_lapis_ore",
                "diorite_iron_ore",
                "diorite_copper_ore",
                "diorite_gold_ore",
                "diorite_coal_ore"
        );

        var oreAndesiteDrops = List.of(
                "andesite_diamond_ore",
                "andesite_emerald_ore",
                "andesite_redstone_ore",
                "andesite_lapis_ore",
                "andesite_iron_ore",
                "andesite_copper_ore",
                "andesite_gold_ore",
                "andesite_coal_ore"
        );

        var oreTuffDrops = List.of(
                "tuff_diamond_ore",
                "tuff_emerald_ore",
                "tuff_redstone_ore",
                "tuff_lapis_ore",
                "tuff_iron_ore",
                "tuff_copper_ore",
                "tuff_gold_ore",
                "tuff_coal_ore"
        );

        var oreStoneDropsOther = List.of(
                "smooth_basalt_diamond_ore",
                "calcite_redstone_ore",
                "sandstone_lapis_ore"
        );

        var oreNetherDrops = List.of(
                "nether_gold_ore",
                "nether_quartz_ore"
        );

        if (PlatformHelper.getPlatform() == PlatformHelper.Platform.FORGE) return;

        if (CommonConfigs.ORE_STONE_DROPS.get()) {

            for (var loot : oreNetherDrops) {
                if (name.equals(new ResourceLocation("minecraft", "blocks/" + loot))) {
                    {
                        LootPool.Builder pool = LootPool.lootPool();
                        pool.add(LootTableReference.lootTableReference(Spelunkery.res("injects/ores/netherrack")));
                        ForgeHelper.setPoolName(pool, "spelunkery_" + loot);
                        builderConsumer.accept(pool);
                    }
                }
            }

            for (var loot : oreStoneDropsOther) {
                if (name.equals(new ResourceLocation("spelunkery", "blocks/" + loot))) {
                    {
                        LootPool.Builder pool = LootPool.lootPool();
                        pool.add(LootTableReference.lootTableReference(Spelunkery.res("injects/ores/" + loot)));
                        ForgeHelper.setPoolName(pool, "spelunkery_" + loot);
                        builderConsumer.accept(pool);
                    }
                }
            }

            for (var loot : oreStoneDrops) {
                if (name.equals(new ResourceLocation("minecraft", "blocks/" + loot))) {
                    {
                        LootPool.Builder pool = LootPool.lootPool();
                        pool.add(LootTableReference.lootTableReference(Spelunkery.res("injects/ores/stone")));
                        ForgeHelper.setPoolName(pool, "spelunkery_" + loot);
                        builderConsumer.accept(pool);
                    }
                }
            }

            for (var loot : oreDeepslateDrops) {
                if (name.equals(new ResourceLocation("minecraft", "blocks/" + loot))) {
                    {
                        LootPool.Builder pool = LootPool.lootPool();
                        pool.add(LootTableReference.lootTableReference(Spelunkery.res("injects/ores/deepslate")));
                        ForgeHelper.setPoolName(pool, "spelunkery_" + loot);
                        builderConsumer.accept(pool);
                    }
                }
            }

            for (var loot : oreAndesiteDrops) {
                if (name.equals(new ResourceLocation("spelunkery", "blocks/" + loot))) {
                    {
                        LootPool.Builder pool = LootPool.lootPool();
                        pool.add(LootTableReference.lootTableReference(Spelunkery.res("injects/ores/andesite")));
                        ForgeHelper.setPoolName(pool, "spelunkery_" + loot);
                        builderConsumer.accept(pool);
                    }
                }
            }

            for (var loot : oreDioriteDrops) {
                if (name.equals(new ResourceLocation("spelunkery", "blocks/" + loot))) {
                    {
                        LootPool.Builder pool = LootPool.lootPool();
                        pool.add(LootTableReference.lootTableReference(Spelunkery.res("injects/ores/diorite")));
                        ForgeHelper.setPoolName(pool, "spelunkery_" + loot);
                        builderConsumer.accept(pool);
                    }
                }
            }

            for (var loot : oreGraniteDrops) {
                if (name.equals(new ResourceLocation("spelunkery", "blocks/" + loot))) {
                    {
                        LootPool.Builder pool = LootPool.lootPool();
                        pool.add(LootTableReference.lootTableReference(Spelunkery.res("injects/ores/granite")));
                        ForgeHelper.setPoolName(pool, "spelunkery_" + loot);
                        builderConsumer.accept(pool);
                    }
                }
            }

            for (var loot : oreTuffDrops) {
                if (name.equals(new ResourceLocation("spelunkery", "blocks/" + loot))) {
                    {
                        LootPool.Builder pool = LootPool.lootPool();
                        pool.add(LootTableReference.lootTableReference(Spelunkery.res("injects/ores/tuff")));
                        ForgeHelper.setPoolName(pool, "spelunkery_" + loot);
                        builderConsumer.accept(pool);
                    }
                }
            }
        }

    }
}
