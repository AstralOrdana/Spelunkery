package com.ordana.spelunkery;

import com.ordana.spelunkery.configs.ClientConfigs;
import com.ordana.spelunkery.configs.CommonConfigs;
import com.ordana.spelunkery.events.PackProvider;
import com.ordana.spelunkery.reg.*;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Spelunkery {

    public static final String MOD_ID = "spelunkery";
    public static final Logger LOGGER = LogManager.getLogger();

    public static ResourceLocation res(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    public static void commonInit() {

        CommonConfigs.init();
        if(PlatformHelper.getEnv().isClient()){
            ClientConfigs.init();
        }

        PackProvider.INSTANCE.register();
        ModFeatures.init();
        ModGameEvents.init();
        ModBlocks.init();
        ModItems.init();
        ModEntities.init();
        ModRecipes.init();
    }

}