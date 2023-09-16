package com.ordana.spelunkery.forge;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.SpelunkeryClient;
import com.ordana.spelunkery.reg.ModSetup;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(Spelunkery.MOD_ID)
public class SpelunkeryForge {
    public static final String MOD_ID = Spelunkery.MOD_ID;

    public SpelunkeryForge() {
        Spelunkery.commonInit();

        if (PlatHelper.getPhysicalSide().isClient()) {
            ClientEventsForge.init();
            SpelunkeryClient.init();
        }
    }

    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ModSetup::setup);
    }

}

