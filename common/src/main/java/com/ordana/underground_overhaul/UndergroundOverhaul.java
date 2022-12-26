package com.ordana.underground_overhaul;

import com.ordana.underground_overhaul.configs.ClientConfigs;
import com.ordana.underground_overhaul.configs.CommonConfigs;
import com.ordana.underground_overhaul.reg.*;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UndergroundOverhaul {

    public static final String MOD_ID = "underground_overhaul";
    public static final Logger LOGGER = LogManager.getLogger();

    public static ResourceLocation res(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    public static void commonInit() {

        CommonConfigs.init();
        if(PlatformHelper.getEnv().isClient()){
            ClientConfigs.init();
        }

        ModFeatures.init();
        ModBlocks.init();
        ModItems.init();
        ModEntities.init();
        //ModItemProperties.init();
        //ModRecipes.init();
    }

}