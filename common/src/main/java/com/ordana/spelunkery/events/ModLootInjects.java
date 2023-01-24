package com.ordana.spelunkery.events;

import com.ordana.spelunkery.Spelunkery;
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


        for (var table : lootChests) {
            if (name.equals(new ResourceLocation("minecraft", "chests/" + table))) {
                {
                    LootPool.Builder pool = LootPool.lootPool();
                    pool.add(LootTableReference.lootTableReference(Spelunkery.res("injects/" + table)));
                    ForgeHelper.setPoolName(pool, "spelunkery_" + table);
                    builderConsumer.accept(pool);
                }
            }
        }
    }
}
