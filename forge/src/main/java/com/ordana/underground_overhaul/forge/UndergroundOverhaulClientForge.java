package com.ordana.underground_overhaul.forge;

import com.ordana.underground_overhaul.UndergroundOverhaulClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.loading.ClientModLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = UndergroundOverhaulForge.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class UndergroundOverhaulClientForge extends ClientModLoader {

    @SubscribeEvent
    public static void init(final FMLClientSetupEvent event) {
        event.enqueueWork(UndergroundOverhaulClient::setup);
    }
}
