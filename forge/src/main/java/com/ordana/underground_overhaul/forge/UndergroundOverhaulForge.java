package com.ordana.underground_overhaul.forge;

import com.ordana.underground_overhaul.UndergroundOverhaul;
import com.ordana.underground_overhaul.UndergroundOverhaulClient;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(UndergroundOverhaul.MOD_ID)
public class UndergroundOverhaulForge {
    public static final String MOD_ID = UndergroundOverhaul.MOD_ID;

    public UndergroundOverhaulForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.register(this);

        UndergroundOverhaul.commonInit();

        if (PlatformHelper.getEnv().isClient()) {
            UndergroundOverhaulClient.init();
        }
    }
}

