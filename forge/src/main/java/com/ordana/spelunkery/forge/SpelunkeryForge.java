package com.ordana.spelunkery.forge;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.SpelunkeryClient;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Spelunkery.MOD_ID)
public class SpelunkeryForge {
    public static final String MOD_ID = Spelunkery.MOD_ID;

    public SpelunkeryForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.register(this);

        Spelunkery.commonInit();

        if (PlatformHelper.getEnv().isClient()) {
            SpelunkeryClient.init();
        }
    }
}

