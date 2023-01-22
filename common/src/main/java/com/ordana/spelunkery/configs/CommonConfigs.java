package com.ordana.spelunkery.configs;

import com.ordana.spelunkery.Spelunkery;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigBuilder;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigSpec;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigType;

import java.util.function.Supplier;

public class CommonConfigs {


    public static ConfigSpec SERVER_SPEC;

    public static Supplier<Boolean> CREATIVE_TAB;
    public static Supplier<Boolean> CROSS_SECTION;
    public static Supplier<Boolean> STONE_STRIPE_FEATURES;
    public static Supplier<Integer> MAGNET_RANGE;
    public static Supplier<Integer> MAGNETITE_RANGE;
    public static Supplier<Boolean> RESPAWN_ANCHOR_PORTAL_FLUID;


    public static Supplier<Double> DOUBLE_CONFIG;


    public static void init() {
        ConfigBuilder builder = ConfigBuilder.create(Spelunkery.res("common"), ConfigType.COMMON);

        builder.setSynced();

        builder.push("general");
        CREATIVE_TAB = builder.define("creative_tab", false);
        CROSS_SECTION = builder.define("cross_section_world_mode", false);
        STONE_STRIPE_FEATURES = builder.define("stone_stripe_features", true);
        MAGNET_RANGE = builder.define("magnet_range", 8, 1, 32);
        MAGNETITE_RANGE = builder.define("magnetite_range", 128, 1, 512);
        RESPAWN_ANCHOR_PORTAL_FLUID = builder.define("respawn_anchor_portal_fluid", false);
        builder.pop();

        builder.push("examples");
        DOUBLE_CONFIG = builder.define("double_config", 0.06, 0, 1);
        builder.pop();

        //fabric specific
        PlatformHelper.getPlatform().ifFabric(() -> {

        });


        SERVER_SPEC = builder.buildAndRegister();
        SERVER_SPEC.loadFromFile();
    }
}
