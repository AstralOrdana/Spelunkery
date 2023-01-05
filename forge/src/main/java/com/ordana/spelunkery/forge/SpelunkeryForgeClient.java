package com.ordana.spelunkery.forge;

import com.ordana.spelunkery.SpelunkeryClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.loading.ClientModLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = SpelunkeryForge.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpelunkeryForgeClient extends ClientModLoader {

    @SubscribeEvent
    public static void init(final FMLClientSetupEvent event) {
        event.enqueueWork(SpelunkeryClient::setup);
    }
}
