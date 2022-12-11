package com.ordana.underground_overhaul.configs;

import com.ordana.underground_overhaul.UndergroundOverhaul;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigBuilder;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigSpec;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigType;

import java.util.function.Supplier;

public class CommonConfigs {


    public static ConfigSpec SERVER_SPEC;

    public static Supplier<Boolean> CREATIVE_TAB;
    public static Supplier<Boolean> CROSS_SECTION;
    public static Supplier<Integer> INTEGER_CONFIG;
    public static Supplier<Double> DOUBLE_CONFIG;


    public static void init() {
        ConfigBuilder builder = ConfigBuilder.create(UndergroundOverhaul.res("common"), ConfigType.COMMON);

        builder.setSynced();

        builder.push("general");
        CREATIVE_TAB = builder.define("creative_tab", false);
        CROSS_SECTION = builder.define("cross_section", false);
        builder.pop();

        builder.push("examples");
        INTEGER_CONFIG = builder.define("integer_config", 4, 1, 8);
        DOUBLE_CONFIG = builder.define("double_config", 0.06, 0, 1);
        builder.pop();

        //fabric specific
        PlatformHelper.getPlatform().ifFabric(() -> {


        });


        SERVER_SPEC = builder.buildAndRegister();
        SERVER_SPEC.loadFromFile();
    }
}
