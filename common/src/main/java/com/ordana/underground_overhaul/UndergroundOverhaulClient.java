package com.ordana.underground_overhaul;

import com.ordana.underground_overhaul.configs.ClientConfigs;
import com.ordana.underground_overhaul.reg.ModBlocks;
import com.ordana.underground_overhaul.reg.ModEntities;
import net.mehvahdjukaar.moonlight.api.client.renderer.FallingBlockRendererGeneric;
import net.mehvahdjukaar.moonlight.api.platform.ClientPlatformHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class UndergroundOverhaulClient {
    
    public static void init() {
        ClientConfigs.init();
        ClientPlatformHelper.addEntityRenderersRegistration(UndergroundOverhaulClient::registerEntityRenderers);
    }

    public static void setup() {
        ClientPlatformHelper.registerRenderType(ModBlocks.SALT_LAMP.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.SALT.get(), RenderType.cutout());
    }

    private static void registerEntityRenderers(ClientPlatformHelper.EntityRendererEvent event) {
        event.register(ModEntities.GLOWSTICK.get(), context -> new ThrownItemRenderer<>(context, 1, false));
    }

}