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
    public static Supplier<Boolean> INCREASED_SLIME_SPAWN_RATE;
    public static Supplier<Boolean> ENABLE_ROUGH_GEMS;
    public static Supplier<Boolean> ENABLE_RAW_NUGGETS;

    public static Supplier<Boolean> RESPAWN_ANCHOR_PORTAL_FLUID;
    public static Supplier<Boolean> CRYING_OBSIDIAN_PORTAL_FLUID;
    public static Supplier<Boolean> FlINT_AND_STEEL_PORTAL_LIGHTING;
    public static Supplier<Boolean> PORTAL_DESTRUCTION_CRYING_OBSIDIAN;
    public static Supplier<Boolean> PIGLINS_GIVE_CRYING_OBSIDIAN;
    public static Supplier<Boolean> PORTAL_CREATION_SOUND;
    public static Supplier<Boolean> PORTAL_DESTRUCTION_SOUND;

    public static Supplier<Boolean> STONE_STRIPE_FEATURES;
    public static Supplier<Boolean> ENABLE_SPOROPHYTES;
    public static Supplier<Boolean> DARK_FOREST_PORTABELLAS;

    public static Supplier<Integer> MAGNET_RANGE;
    public static Supplier<Integer> MAGNETITE_RANGE;


    public static Supplier<Double> DOUBLE_CONFIG;


    public static void init() {
        ConfigBuilder builder = ConfigBuilder.create(Spelunkery.res("common"), ConfigType.COMMON);

        builder.setSynced();

        builder.push("misc");
        CREATIVE_TAB = builder.define("creative_tab", false);
        CROSS_SECTION = builder.define("cross_section_world_mode", false);
        INCREASED_SLIME_SPAWN_RATE = builder.define("increased_slime_spawn_rate", true);
        ENABLE_ROUGH_GEMS = builder.define("enable_rough_gems", true);
        ENABLE_RAW_NUGGETS = builder.define("enable_raw_nuggets", true);
        builder.pop();

        builder.push("nether_portals");
        FlINT_AND_STEEL_PORTAL_LIGHTING = builder.define("flint_and_steel_portal_lighting", true);
        CRYING_OBSIDIAN_PORTAL_FLUID = builder.define("crying_obsidian_dimensional_tears", true);
        RESPAWN_ANCHOR_PORTAL_FLUID = builder.define("respawn_anchor_dimensional_tears", true);
        PORTAL_DESTRUCTION_CRYING_OBSIDIAN = builder.define("portal_destruction_crying_obsidian", true);
        PIGLINS_GIVE_CRYING_OBSIDIAN = builder.define("piglins_give_crying_obsidian", true);
        PORTAL_CREATION_SOUND = builder.define("portal_creation_sound", true);
        PORTAL_DESTRUCTION_SOUND = builder.define("portal_destruction_sound", true);
        builder.pop();

        builder.push("worldgen");
        STONE_STRIPE_FEATURES = builder.define("stone_stripe_features", true);
        ENABLE_SPOROPHYTES = builder.define("enable_sporophytes", true);
        DARK_FOREST_PORTABELLAS = builder.define("dark_forest_portabellas", true);
        builder.pop();

        builder.push("magnets");
        MAGNET_RANGE = builder.define("magnet_range", 8, 1, 32);
        MAGNETITE_RANGE = builder.define("magnetite_range", 128, 1, 512);
        builder.pop();

        //fabric specific
        PlatformHelper.getPlatform().ifFabric(() -> {

        });


        SERVER_SPEC = builder.buildAndRegister();
        SERVER_SPEC.loadFromFile();
    }

    static {
        Spelunkery.commonInit();
    }
}
