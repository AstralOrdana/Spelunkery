package com.ordana.underground_overhaul;

import com.ordana.underground_overhaul.configs.ClientConfigs;
import com.ordana.underground_overhaul.reg.ModBlocks;
import net.mehvahdjukaar.moonlight.api.platform.ClientPlatformHelper;
import net.minecraft.client.renderer.RenderType;

public class UndergroundOverhaulClient {
    
    public static void init() {
        ClientConfigs.init();
    }

    public static void setup() {
        ClientPlatformHelper.registerRenderType(ModBlocks.ROCK_SALT.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.SALT.get(), RenderType.cutout());
    }

}