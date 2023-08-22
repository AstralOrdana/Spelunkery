package com.ordana.spelunkery.forge;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.SpelunkeryClient;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.minecraftforge.fml.common.Mod;

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

}

