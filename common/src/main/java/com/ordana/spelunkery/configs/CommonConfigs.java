package com.ordana.spelunkery.configs;

import com.ordana.spelunkery.Spelunkery;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigBuilder;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigSpec;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigType;

import java.util.function.Supplier;

public class CommonConfigs {


    public static ConfigSpec SERVER_SPEC;

    public static Supplier<Boolean> CREATIVE_TAB;
    //public static Supplier<Boolean> CROSS_SECTION;
    public static Supplier<Boolean> STONECUTTER_DAMAGE;
    public static Supplier<Boolean> DISABLE_DIAMOND_GRINDSTONE_DEPLETION;
    public static Supplier<Integer> DIAMOND_GRINDSTONE_DEPLETE_CHANCE;
    public static Supplier<Integer> PARACHUTE_DELAY;
    public static Supplier<Boolean> ENABLE_ROUGH_GEMS;
    public static Supplier<Boolean> ENABLE_RAW_NUGGETS;
    public static Supplier<Boolean> ORE_STONE_DROPS;
    //public static Supplier<Boolean> ENABLE_GEM_SHARDS;

    public static Supplier<Boolean> INCREASED_SLIME_SPAWN_RATE;
    public static Supplier<Boolean> SLIME_CAULDRONS;
    public static Supplier<Boolean> SLIMES_FRIENDLY_REGEN;
    public static Supplier<Boolean> SLIMES_ALWAYS_FRIENDLY;
    public static Supplier<Integer> SLIME_EAT_COOLDOWN;
    public static Supplier<Boolean> SLIME_GROWTH;
    public static Supplier<Integer> SLIME_GROWTH_CHANCE;
    public static Supplier<Integer> SLIME_GROWTH_MAX;

    public static Supplier<Integer> HAMMER_CHISEL_CHARGE_TIME;
    public static Supplier<Integer> ECHO_FORK_RANGE;
    public static Supplier<Integer> ECHO_DURRATION;
    public static Supplier<Integer> ECHO_COOLDOWN;
    public static Supplier<Boolean> SCULK_SHEARING;
    public static Supplier<Integer> MAGNET_RANGE;
    public static Supplier<Integer> MAGNETITE_RANGE;

    public static Supplier<Boolean> PORTAL_FLUID_DRINKING;
    public static Supplier<Boolean> RESPAWN_ANCHOR_PORTAL_FLUID;
    public static Supplier<Boolean> CRYING_OBSIDIAN_PORTAL_FLUID;
    public static Supplier<Boolean> FlINT_AND_STEEL_PORTAL_LIGHTING;
    public static Supplier<Boolean> PORTAL_DESTRUCTION_CRYING_OBSIDIAN;
    public static Supplier<Boolean> PIGLINS_GIVE_CRYING_OBSIDIAN;
    public static Supplier<Boolean> PORTAL_CREATION_SOUND;
    public static Supplier<Boolean> PORTAL_DESTRUCTION_SOUND;

    public static Supplier<Boolean> STONE_STRIPE_FEATURES;
    public static Supplier<Boolean> ENABLE_SPOROPHYTES;
    public static Supplier<Boolean> BETTER_SCULK_PATCHES;
    public static Supplier<Boolean> DARK_FOREST_PORTABELLAS;
    public static Supplier<Boolean> ENABLE_MORES;
    public static Supplier<Boolean> PORTAL_FLUID_OCEAN;


    public static void init() {
        // bump class load init
    }

    static {
        ConfigBuilder builder = ConfigBuilder.create(Spelunkery.res("common"), ConfigType.COMMON);

        builder.setSynced();

        builder.push("misc");
        CREATIVE_TAB = builder.define("creative_tab", false);
        STONECUTTER_DAMAGE = builder.define("stonecutter_damage", true);
        DISABLE_DIAMOND_GRINDSTONE_DEPLETION = builder.define("disable_diamond_grindstone_depletion", false);
        DIAMOND_GRINDSTONE_DEPLETE_CHANCE = builder.define("diamond_grindstone_deplete_chance", 20, 0, 128);
        PARACHUTE_DELAY = builder.define("parachute_delay", 10, 0, 128);
        ENABLE_ROUGH_GEMS = builder.define("enable_rough_gems", true);
        ENABLE_RAW_NUGGETS = builder.define("enable_raw_nuggets", true);
        PlatHelper.getPlatform().ifFabric(() -> {
            ORE_STONE_DROPS = builder.define("ore_stone_drops", true);
        });
        //ENABLE_GEM_SHARDS = builder.define("enable_gem_shards", true);
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
        MAGNETITE_RANGE = builder.define("magnetite_range", 64, 1, 512);
        builder.pop();

        builder.push("nether_portals");
        PORTAL_FLUID_DRINKING = builder.define("portal_fluid_drinking", true);
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
        BETTER_SCULK_PATCHES = builder.define("better_sculk_patches", false);
        DARK_FOREST_PORTABELLAS = builder.define("dark_forest_portabellas", true);
        ENABLE_MORES = builder.define("enable_mores", true);
        PORTAL_FLUID_OCEAN = builder.define("portal_fluid_ocean", false);
        builder.pop();

        SERVER_SPEC = builder.buildAndRegister();
        SERVER_SPEC.loadFromFile();
    }
}
