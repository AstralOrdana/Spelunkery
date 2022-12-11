package com.ordana.underground_overhaul.fabric;

import com.ordana.underground_overhaul.UndergroundOverhaulClient;
import net.fabricmc.api.ClientModInitializer;

public class UndergroundOverhaulClientFabric implements ClientModInitializer {


    public static void initClient() {
        UndergroundOverhaulClient.init();
        UndergroundOverhaulClient.setup();
    }

    @Override
    public void onInitializeClient() {
        //no
    }
}
