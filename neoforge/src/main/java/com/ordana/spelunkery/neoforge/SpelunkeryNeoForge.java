package com.ordana.spelunkery.neoforge;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.SpelunkeryClient;
import com.ordana.spelunkery.reg.ModSetup;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(Spelunkery.MOD_ID)
public class SpelunkeryNeoForge {
    public static final String MOD_ID = Spelunkery.MOD_ID;

    public SpelunkeryNeoForge(IEventBus eventBus, ModContainer container) {
        RegHelper.startRegisteringFor(eventBus);

        Spelunkery.commonInit();
        SpelunkeryPlatformImpl.GAME_EVENTS.register(eventBus);

        if (PlatHelper.getPhysicalSide().isClient()) {
            ClientEventsNeoForge.init();
            SpelunkeryClient.init();
        }
    }

    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ModSetup::setup);
    }

}

