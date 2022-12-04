package com.ordana.underground_overhaul.forge;

import com.ordana.underground_overhaul.UndergroundOverhaul;
import net.minecraftforge.fml.common.Mod;

@Mod(UndergroundOverhaul.MOD_ID)
public class UndergroundOverhaulForge {

    public UndergroundOverhaulForge() {
        UndergroundOverhaul.commonInit();
        /*
        if (PlatformHelper.getEnv().isClient()) {
            ModidClient.init();
        }
        */
    }
}

