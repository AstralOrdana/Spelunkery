package com.ordana.spelunkery.configs;

import com.ordana.spelunkery.Spelunkery;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigBuilder;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigSpec;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigType;

import java.util.function.Supplier;

public class ClientConfigs {

    public static ConfigSpec CLIENT_SPEC;

    public static Supplier<Boolean> ENABLE_TOOLTIPS;

    public static void init() {
        ConfigBuilder builder = ConfigBuilder.create(Spelunkery.res("client"), ConfigType.CLIENT);

        builder.push("general");
        ENABLE_TOOLTIPS = builder.comment("Enable Tooltips").define("enable_tooltips", true);

        builder.pop();

        CLIENT_SPEC = builder.buildAndRegister();

        //load early
        CLIENT_SPEC.loadFromFile();
    }

    static {
        Spelunkery.commonInit();
    }

}