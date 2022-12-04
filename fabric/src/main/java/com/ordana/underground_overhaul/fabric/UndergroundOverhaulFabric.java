package com.ordana.underground_overhaul.fabric;

import net.fabricmc.api.ModInitializer;
import com.ordana.underground_overhaul.UndergroundOverhaul;
import com.ordana.underground_overhaul.UndergroundOverhaulClient;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.mehvahdjukaar.moonlight.fabric.FabricSetupCallbacks;

public class UndergroundOverhaulFabric implements ModInitializer {

    @Override
    public void onInitialize() {

        UndergroundOverhaul.commonInit();

        if (PlatformHelper.getEnv().isClient()) {
            FabricSetupCallbacks.CLIENT_SETUP.add(UndergroundOverhaulClient::init);
        }
    }
}
