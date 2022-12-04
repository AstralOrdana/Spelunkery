package com.ordana.underground_overhaul;

import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UndergroundOverhaul {

    public static final String MOD_ID = "underground_overhaul";
    public static final Logger LOGGER = LogManager.getLogger();

    private static final String NAME = "underground_overhaul";

    public static ResourceLocation res(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    public static void commonInit() {

    }

}