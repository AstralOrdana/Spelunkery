package com.ordana.spelunkery.configs;

import com.ordana.spelunkery.Spelunkery;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigBuilder;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigSpec;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigType;

import java.util.function.Supplier;

public class CommonConfigs {


    public static final ConfigSpec SERVER_SPEC;

    public static final Supplier<Boolean> CREATIVE_TAB;
    public static final Supplier<Boolean> CROSS_SECTION;
    public static final Supplier<Integer> DIAMOND_GRINDSTONE_DEPLETE_CHANCE;
    public static final Supplier<Boolean> ENABLE_ROUGH_GEMS;
    //public static final Supplier<Boolean> ENABLE_GEM_SHARDS;
    public static final Supplier<Boolean> ENABLE_RAW_NUGGETS;

    public static final Supplier<Boolean> INCREASED_SLIME_SPAWN_RATE;
    public static final Supplier<Boolean> SLIME_CAULDRONS;
    public static final Supplier<Boolean> SLIMES_FRIENDLY_REGEN;
    public static final Supplier<Boolean> SLIMES_ALWAYS_FRIENDLY;
    public static final Supplier<Integer> SLIME_EAT_COOLDOWN;
    public static final Supplier<Boolean> SLIME_GROWTH;
    public static final Supplier<Integer> SLIME_GROWTH_CHANCE;
    public static final Supplier<Integer> SLIME_GROWTH_MAX;

    public static final Supplier<Integer> HAMMER_CHISEL_CHARGE_TIME;
    public static final Supplier<Integer> ECHO_FORK_RANGE;
    public static final Supplier<Integer> ECHO_DURRATION;
    public static final Supplier<Integer> ECHO_COOLDOWN;
    public static final Supplier<Boolean> SCULK_SHEARING;
    public static final Supplier<Integer> MAGNET_RANGE;
    public static final Supplier<Integer> MAGNETITE_RANGE;

    public static final Supplier<Boolean> RESPAWN_ANCHOR_PORTAL_FLUID;
    public static final Supplier<Boolean> CRYING_OBSIDIAN_PORTAL_FLUID;
    public static final Supplier<Boolean> FlINT_AND_STEEL_PORTAL_LIGHTING;
    public static final Supplier<Boolean> PORTAL_DESTRUCTION_CRYING_OBSIDIAN;
    public static final Supplier<Boolean> PIGLINS_GIVE_CRYING_OBSIDIAN;
    public static final Supplier<Boolean> PORTAL_CREATION_SOUND;
    public static final Supplier<Boolean> PORTAL_DESTRUCTION_SOUND;

    public static final Supplier<Boolean> STONE_STRIPE_FEATURES;
    public static final Supplier<Boolean> ENABLE_SPOROPHYTES;
    public static final Supplier<Boolean> DARK_FOREST_PORTABELLAS;
    public static final Supplier<Boolean> ENABLE_MORES;



    static {
        ConfigBuilder builder = ConfigBuilder.create(Spelunkery.res("common"), ConfigType.COMMON);

        builder.setSynced();

        builder.push("misc");
        CREATIVE_TAB = builder.define("creative_tab", false);
        CROSS_SECTION = builder.define("cross_section_world_mode", false);
        DIAMOND_GRINDSTONE_DEPLETE_CHANCE = builder.define("diamond_grindstone_deplete_chance", 20, 1, 128);
        ENABLE_ROUGH_GEMS = builder.define("enable_rough_gems", true);
        //ENABLE_GEM_SHARDS = builder.define("enable_gem_shards", true);
        ENABLE_RAW_NUGGETS = builder.define("enable_raw_nuggets", true);
        builder.pop();

        builder.push("slimes");
        INCREASED_SLIME_SPAWN_RATE = builder.define("increased_slime_spawn_rate", true);
        SLIME_CAULDRONS = builder.define("slime_cauldrons", true);
        SLIMES_FRIENDLY_REGEN = builder.define("slimes_friendly_if_regen", true);
        SLIMES_ALWAYS_FRIENDLY = builder.define("slimes_always_friendly", false);
        SLIME_EAT_COOLDOWN = builder.define("slime_eat_cooldown", 150, 1, 2400);
        SLIME_GROWTH = builder.define("enable_slime_growth", true);
        SLIME_GROWTH_CHANCE = builder.define("slime_growth_chance", 10, 1, 127);
        SLIME_GROWTH_MAX = builder.define("slime_growth_max", 5, 1, 127);
        builder.pop();

        builder.push("utilities");
        HAMMER_CHISEL_CHARGE_TIME = builder.define("hammer_chisel_charge_time", 20, 1, 128);
        SCULK_SHEARING = builder.define("sculk_drops_with_shears", true);
        ECHO_FORK_RANGE = builder.define("echo_fork_range", 16, 1, 128);
        ECHO_COOLDOWN = builder.define("echo_cooldown", 600, 1, 72000);
        ECHO_DURRATION = builder.define("echo_glow_durration", 1200, 1, 72000);
        MAGNET_RANGE = builder.define("magnet_range", 8, 1, 32);
        MAGNETITE_RANGE = builder.define("magnetite_range", 128, 1, 512);
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
        ENABLE_MORES = builder.define("enable_mores", true);
        builder.pop();

        //fabric specific
        PlatformHelper.getPlatform().ifFabric(() -> {

        });


        SERVER_SPEC = builder.buildAndRegister();
        SERVER_SPEC.loadFromFile();
    }

    public static void bump() {
        // Literally just a way to ensure for the class to be loaded
    }
}
