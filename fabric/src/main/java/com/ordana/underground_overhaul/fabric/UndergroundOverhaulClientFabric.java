package com.ordana.underground_overhaul.fabric;

import com.ordana.underground_overhaul.UndergroundOverhaul;
import com.ordana.underground_overhaul.UndergroundOverhaulClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.mehvahdjukaar.moonlight.fabric.FabricSetupCallbacks;
import net.minecraft.server.MinecraftServer;

public class UndergroundOverhaulClientFabric implements ClientModInitializer {


    public static void initClient() {
        UndergroundOverhaulClient.init();
    }


    @Override
    public void onInitializeClient() {
        //no
    }
}
