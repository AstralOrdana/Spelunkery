package com.ordana.underground_overhaul.forge;

import com.ordana.underground_overhaul.UndergroundOverhaul;
import com.ordana.underground_overhaul.UndergroundOverhaulClient;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
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

