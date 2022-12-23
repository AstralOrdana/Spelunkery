package com.ordana.underground_overhaul.fabric;

import com.ordana.underground_overhaul.events.ModLootInjects;
import net.fabricmc.api.ModInitializer;
import com.ordana.underground_overhaul.UndergroundOverhaul;
import com.ordana.underground_overhaul.UndergroundOverhaulClient;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.mehvahdjukaar.moonlight.fabric.FabricSetupCallbacks;
import net.minecraft.server.MinecraftServer;

public class UndergroundOverhaulFabric implements ModInitializer {

    public static MinecraftServer currentServer;

    @Override
    public void onInitialize() {

        ServerLifecycleEvents.SERVER_STARTING.register(s -> currentServer = s);

        UndergroundOverhaul.commonInit();

        LootTableEvents.MODIFY.register((m, t, r, b, s) -> ModLootInjects.onLootInject(t, r, b::withPool));

        if(PlatformHelper.getEnv().isClient()){
            FabricSetupCallbacks.CLIENT_SETUP.add(UndergroundOverhaulClientFabric::initClient);
        }
    }
}
