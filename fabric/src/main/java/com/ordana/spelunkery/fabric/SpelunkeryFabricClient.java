package com.ordana.spelunkery.fabric;

import com.ordana.spelunkery.SpelunkeryClient;
import net.fabricmc.api.ClientModInitializer;

public class SpelunkeryFabricClient implements ClientModInitializer {


    public static void initClient() {
        SpelunkeryClient.init();
        SpelunkeryClient.setup();
    }

    @Override
    public void onInitializeClient() {
        //no
    }
}
