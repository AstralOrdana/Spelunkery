package com.ordana.spelunkery.configs;

import com.ordana.spelunkery.Spelunkery;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigBuilder;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigSpec;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigType;

import java.util.function.Supplier;

public class ClientConfigs {

    public static ConfigSpec CLIENT_SPEC;

    public static Supplier<Boolean> ENABLE_TOOLTIPS;
    public static Supplier<Double> PORTAL_FLUID_SEED;


    public static void init() {
        ConfigBuilder builder = ConfigBuilder.create(Spelunkery.res("client"), ConfigType.CLIENT);

        builder.push("general");
        ENABLE_TOOLTIPS = builder.comment("Enable Tooltips").define("enable_tooltips", true);
        PORTAL_FLUID_SEED = builder.define("portal_fluid_seed", 1D, 0.01D, 1D);

        builder.pop();

        CLIENT_SPEC = builder.buildAndRegister();

        //load early
        CLIENT_SPEC.loadFromFile();
    }

}