package com.ordana.spelunkery.reg;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.configs.CommonConfigs;
import net.mehvahdjukaar.moonlight.api.misc.RegSupplier;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.Arrays;
import java.util.function.Supplier;

public class ModCreativeTabs {

    public static final RegSupplier<CreativeModeTab> MOD_TAB = !CommonConfigs.CREATIVE_TAB.get() ? null :
            RegHelper.registerCreativeModeTab(Spelunkery.res("spelunkery"),
                    (c) -> c.title(Component.translatable("itemGroup.spelunkery"))
                            .icon(() -> ModItems.ROCK_SALT.get().getDefaultInstance()));


    public static void init() {
        RegHelper.addItemsToTabsRegistration(ModCreativeTabs::registerItemsToTabs);
    }


    public static void registerItemsToTabs(RegHelper.ItemToTabEvent e) {

        if (CommonConfigs.ENABLE_MORES.get()) {
            if (PlatHelper.isModLoaded("create")) {
                after(e, Items.DEEPSLATE_DIAMOND_ORE, CreativeModeTabs.NATURAL_BLOCKS,
                        ModBlocks.ANDESITE_ZINC_ORE, ModBlocks.DIORITE_ZINC_ORE, ModBlocks.GRANITE_ZINC_ORE, ModBlocks.DIORITE_ZINC_ORE
                );

                after(e, Items.IRON_NUGGET, CreativeModeTabs.INGREDIENTS,
                        ModItems.COPPER_NUGGET
                );

                before(e, Items.COAL, CreativeModeTabs.INGREDIENTS,
                        ModItems.RAW_ZINC_NUGGET
                );
            }
            if (PlatHelper.isModLoaded("oreganized")) {
                after(e, Items.DEEPSLATE_DIAMOND_ORE, CreativeModeTabs.NATURAL_BLOCKS,
                        ModBlocks.ANDESITE_SILVER_ORE, ModBlocks.DIORITE_SILVER_ORE, ModBlocks.GRANITE_SILVER_ORE, ModBlocks.TUFF_SILVER_ORE
                );

                after(e, Items.DEEPSLATE_DIAMOND_ORE, CreativeModeTabs.NATURAL_BLOCKS,
                        ModBlocks.ANDESITE_LEAD_ORE, ModBlocks.DIORITE_LEAD_ORE, ModBlocks.GRANITE_LEAD_ORE, ModBlocks.TUFF_LEAD_ORE
                );

                before(e, Items.COAL, CreativeModeTabs.INGREDIENTS,
                        ModItems.RAW_LEAD_NUGGET, ModItems.RAW_SILVER_NUGGET
                );
            }
            if (PlatHelper.isModLoaded("etcetera")) {
                before(e, Items.COAL, CreativeModeTabs.INGREDIENTS,
                        ModItems.RAW_BISMUTH_NUGGET
                );
            }
            if (PlatHelper.isModLoaded("create_dd")) {
                before(e, Items.COAL, CreativeModeTabs.INGREDIENTS,
                        ModItems.RAW_TIN_NUGGET
                );
            }
            if (PlatHelper.isModLoaded("sullysmod")) {
                after(e, Items.DEEPSLATE_DIAMOND_ORE, CreativeModeTabs.NATURAL_BLOCKS,
                        ModBlocks.ANDESITE_JADE_ORE, ModBlocks.DIORITE_JADE_ORE, ModBlocks.GRANITE_JADE_ORE, ModBlocks.TUFF_JADE_ORE
                );

                before(e, Items.COAL, CreativeModeTabs.INGREDIENTS,
                        ModItems.ROUGH_JADE_SHARD, ModItems.JADE_SHARD
                );
            }

            after(e, Items.DEEPSLATE_COAL_ORE, CreativeModeTabs.NATURAL_BLOCKS,
                    ModBlocks.ANDESITE_COAL_ORE, ModBlocks.DIORITE_COAL_ORE, ModBlocks.GRANITE_COAL_ORE, ModBlocks.TUFF_COAL_ORE
            );

            after(e, Items.DEEPSLATE_IRON_ORE, CreativeModeTabs.NATURAL_BLOCKS,
                    ModBlocks.ANDESITE_IRON_ORE, ModBlocks.DIORITE_IRON_ORE, ModBlocks.GRANITE_IRON_ORE, ModBlocks.TUFF_IRON_ORE
            );
            after(e, Items.DEEPSLATE_COPPER_ORE, CreativeModeTabs.NATURAL_BLOCKS,
                    ModBlocks.ANDESITE_COPPER_ORE, ModBlocks.DIORITE_COPPER_ORE, ModBlocks.GRANITE_COPPER_ORE, ModBlocks.TUFF_COPPER_ORE
            );
            after(e, Items.DEEPSLATE_GOLD_ORE, CreativeModeTabs.NATURAL_BLOCKS,
                    ModBlocks.ANDESITE_GOLD_ORE, ModBlocks.DIORITE_GOLD_ORE, ModBlocks.GRANITE_GOLD_ORE, ModBlocks.TUFF_GOLD_ORE
            );
            after(e, Items.DEEPSLATE_REDSTONE_ORE, CreativeModeTabs.NATURAL_BLOCKS,
                    ModBlocks.ANDESITE_REDSTONE_ORE, ModBlocks.DIORITE_REDSTONE_ORE, ModBlocks.GRANITE_REDSTONE_ORE, ModBlocks.TUFF_REDSTONE_ORE,
                    ModBlocks.CALCITE_REDSTONE_ORE
            );
            after(e, Items.DEEPSLATE_LAPIS_ORE, CreativeModeTabs.NATURAL_BLOCKS,
                    ModBlocks.ANDESITE_LAPIS_ORE, ModBlocks.DIORITE_LAPIS_ORE, ModBlocks.GRANITE_LAPIS_ORE, ModBlocks.TUFF_LAPIS_ORE,
                    ModBlocks.SANDSTONE_LAPIS_ORE
            );
            after(e, Items.DEEPSLATE_EMERALD_ORE, CreativeModeTabs.NATURAL_BLOCKS,
                    ModBlocks.ANDESITE_EMERALD_ORE, ModBlocks.DIORITE_EMERALD_ORE, ModBlocks.GRANITE_EMERALD_ORE, ModBlocks.TUFF_EMERALD_ORE
            );
            after(e, Items.DEEPSLATE_DIAMOND_ORE, CreativeModeTabs.NATURAL_BLOCKS,
                    ModBlocks.ANDESITE_DIAMOND_ORE, ModBlocks.DIORITE_DIAMOND_ORE, ModBlocks.GRANITE_DIAMOND_ORE, ModBlocks.TUFF_DIAMOND_ORE,
                    ModBlocks.SMOOTH_BASALT_DIAMOND_ORE
            );
        }

        after(e, Items.RAW_GOLD_BLOCK, CreativeModeTabs.NATURAL_BLOCKS,
                ModBlocks.ROUGH_CINNABAR_BLOCK, ModBlocks.ROUGH_LAZURITE_BLOCK, ModBlocks.ROUGH_EMERALD_BLOCK,
                ModBlocks.ROUGH_DIAMOND_BLOCK, ModBlocks.ROUGH_QUARTZ_BLOCK, ModBlocks.RAW_MAGNETITE_BLOCK
        );

        after(e, Items.REDSTONE_BLOCK, CreativeModeTabs.BUILDING_BLOCKS,
                ModBlocks.CINNABAR_BLOCK
        );
        after(e, Items.REDSTONE_BLOCK, CreativeModeTabs.REDSTONE_BLOCKS,
                ModBlocks.CINNABAR_BLOCK
        );

        after(e, Items.GRINDSTONE, CreativeModeTabs.FUNCTIONAL_BLOCKS,
                ModBlocks.DIAMOND_GRINDSTONE
        );

        after(e, Items.REDSTONE_TORCH, CreativeModeTabs.FUNCTIONAL_BLOCKS,
                ModBlocks.SALT_LAMP
        );

        after(e, Items.RED_SANDSTONE, CreativeModeTabs.NATURAL_BLOCKS,
                ModBlocks.DUST_BLOCK, ModBlocks.DUST,
                ModBlocks.SALT_BLOCK, ModBlocks.SALT,
                ModBlocks.ROCK_SALT_BLOCK, ModBlocks.ROCK_SALT,
                ModBlocks.SULFUR_BLOCK, ModBlocks.SULFUR,
                ModBlocks.SALTPETER_BLOCK, ModBlocks.SALTPETER
        );

        after(e, Items.CUT_RED_SANDSTONE_SLAB, CreativeModeTabs.BUILDING_BLOCKS,
                ModBlocks.ROCK_SALT_BLOCK, ModBlocks.ROCK_SALT_STAIRS, ModBlocks.ROCK_SALT_SLAB, ModBlocks.ROCK_SALT_WALL,
                ModBlocks.POLISHED_ROCK_SALT, ModBlocks.POLISHED_ROCK_SALT_STAIRS, ModBlocks.POLISHED_ROCK_SALT_SLAB, ModBlocks.POLISHED_ROCK_SALT_WALL,
                ModBlocks.ROCK_SALT_BRICKS, ModBlocks.ROCK_SALT_BRICK_STAIRS, ModBlocks.ROCK_SALT_BRICK_SLAB, ModBlocks.ROCK_SALT_BRICK_WALL
        );

        after(e, Items.DARK_PRISMARINE_SLAB, CreativeModeTabs.BUILDING_BLOCKS,
                ModBlocks.NEPHRITE, ModBlocks.NEPHRITE_STAIRS, ModBlocks.NEPHRITE_SLAB, ModBlocks.NEPHRITE_WALL,
                ModBlocks.POLISHED_NEPHRITE, ModBlocks.POLISHED_NEPHRITE_STAIRS, ModBlocks.POLISHED_NEPHRITE_SLAB, ModBlocks.POLISHED_NEPHRITE_WALL,
                ModBlocks.POLISHED_NEPHRITE_BRICKS, ModBlocks.POLISHED_NEPHRITE_BRICK_STAIRS, ModBlocks.POLISHED_NEPHRITE_BRICK_SLAB, ModBlocks.POLISHED_NEPHRITE_BRICK_WALL
        );

        after(e, Items.VINE, CreativeModeTabs.NATURAL_BLOCKS,
                ModBlocks.TANGLE_ROOTS_BLOCK, ModBlocks.TANGLE_ROOTS
        );

        after(e, Items.CLAY_BALL, CreativeModeTabs.INGREDIENTS,
                ModBlocks.ROCK_SALT, ModBlocks.SALT,
                ModBlocks.SALTPETER,
                ModBlocks.SULFUR
        );

        after(e, Items.AMETHYST_SHARD, CreativeModeTabs.INGREDIENTS,
                ModItems.NEPHRITE_CHUNK
        );


        if (!PlatHelper.isModLoaded("create")) {
            after(e, Items.IRON_NUGGET, CreativeModeTabs.INGREDIENTS,
                    ModItems.COPPER_NUGGET
            );
        }

        after(e, Items.RAW_GOLD, CreativeModeTabs.INGREDIENTS,
                ModItems.RAW_MAGNETITE, ModItems.ROUGH_CINNABAR, ModItems.ROUGH_EMERALD, ModItems.ROUGH_LAZURITE, ModItems.ROUGH_DIAMOND, ModItems.CINNABAR
        );

        before(e, Items.COAL, CreativeModeTabs.INGREDIENTS,
                ModItems.STONE_PEBBLE, ModItems.DEEPSLATE_PEBBLE, ModItems.NETHERRACK_PEBBLE,
                ModItems.MAGMA_PEBBLE, ModItems.BLACKSTONE_PEBBLE, ModItems.BASALT_PEBBLE,
                ModItems.END_STONE_PEBBLE,
                ModItems.COAL_LUMP, ModItems.CHARCOAL_LUMP, ModItems.RAW_IRON_NUGGET, ModItems.RAW_COPPER_NUGGET, ModItems.RAW_GOLD_NUGGET, ModItems.RAW_MAGNETITE_NUGGET,
                ModItems.ROUGH_CINNABAR_SHARD, ModItems.ROUGH_EMERALD_SHARD, ModItems.ROUGH_LAZURITE_SHARD, ModItems.ROUGH_DIAMOND_SHARD, ModItems.ROUGH_QUARTZ_SHARD
        );

        after(e, Items.GOLD_NUGGET, CreativeModeTabs.INGREDIENTS,
                ModItems.CINNABAR_SHARD, ModItems.EMERALD_SHARD, ModItems.LAPIS_LAZULI_SHARD, ModItems.DIAMOND_SHARD
        );

        after(e, Items.LIGHTNING_ROD, CreativeModeTabs.REDSTONE_BLOCKS,
                ModBlocks.CARVED_NEPHRITE, ModBlocks.NEPHRITE_SIPHON, ModBlocks.NEPHRITE_FOUNTAIN, ModBlocks.NEPHRITE_DIODE
        );

        after(e, Items.TNT, CreativeModeTabs.REDSTONE_BLOCKS,
                ModBlocks.COMPRESSION_BLAST_MINER
        );

        after(e, Items.CAULDRON, CreativeModeTabs.REDSTONE_BLOCKS,
                ModBlocks.WOODEN_RAIL
        );

        after(e, Items.MAGMA_BLOCK, CreativeModeTabs.FUNCTIONAL_BLOCKS,
                ModBlocks.WOODEN_CHANNEL,
                ModBlocks.WOODEN_SLUICE,
                ModBlocks.STONE_CHANNEL,
                ModBlocks.STONE_SLUICE
        );

        after(e, Items.LADDER, CreativeModeTabs.FUNCTIONAL_BLOCKS,
                ModBlocks.ROPE_LADDER
        );

        after(e, Items.NETHERITE_HOE, CreativeModeTabs.TOOLS_AND_UTILITIES,
                ModItems.FLINT_HAMMER_AND_CHISEL, ModItems.OBSIDIAN_HAMMER_AND_CHISEL
        );

        after(e, Items.WATER_BUCKET, CreativeModeTabs.TOOLS_AND_UTILITIES,
                ModItems.SPRING_WATER_BUCKET
        );

        after(e, Items.MILK_BUCKET, CreativeModeTabs.TOOLS_AND_UTILITIES,
                ModItems.SALT_BUCKET
        );

        after(e, Items.RECOVERY_COMPASS, CreativeModeTabs.TOOLS_AND_UTILITIES,
                ModItems.MAGNETIC_COMPASS, ModItems.DEPTH_GAUGE
        );

        before(e, Items.COMPASS, CreativeModeTabs.TOOLS_AND_UTILITIES,
                ModItems.ITEM_MAGNET,
                ModItems.TUNING_FORK, ModItems.ECHO_FORK
        );

        before(e, Items.ELYTRA, CreativeModeTabs.TOOLS_AND_UTILITIES,
                ModItems.PARACHUTE
        );

        before(e, Items.RAIL, CreativeModeTabs.TOOLS_AND_UTILITIES,
                ModBlocks.WOODEN_RAIL
        );

        after(e, Items.FIRE_CHARGE, CreativeModeTabs.TOOLS_AND_UTILITIES,
                ModItems.MINEOMITE, ModItems.COMPRESSION_BLAST_MINER
        );

        after(e, Items.BRUSH, CreativeModeTabs.TOOLS_AND_UTILITIES,
                ModItems.DUST_BUN, ModItems.BUNNY_EARS, ModItems.GLOWSTICK
        );

        after(e, Items.END_ROD, CreativeModeTabs.FUNCTIONAL_BLOCKS,
                ModItems.GLOWSTICK,
                ModItems.WHITE_GLOWSTICK, ModItems.LIGHT_GRAY_GLOWSTICK, ModItems.GRAY_GLOWSTICK, ModItems.BLACK_GLOWSTICK,
                ModItems.BROWN_GLOWSTICK, ModItems.RED_GLOWSTICK, ModItems.ORANGE_GLOWSTICK, ModItems.YELLOW_GLOWSTICK,
                ModItems.LIME_GLOWSTICK, ModItems.GREEN_GLOWSTICK, ModItems.CYAN_GLOWSTICK, ModItems.LIGHT_BLUE_GLOWSTICK,
                ModItems.BLUE_GLOWSTICK, ModItems.PURPLE_GLOWSTICK, ModItems.MAGENTA_GLOWSTICK, ModItems.PINK_GLOWSTICK
        );

        after(e, Items.PINK_BANNER, CreativeModeTabs.COLORED_BLOCKS,
                ModItems.GLOWSTICK,
                ModItems.WHITE_GLOWSTICK, ModItems.LIGHT_GRAY_GLOWSTICK, ModItems.GRAY_GLOWSTICK, ModItems.BLACK_GLOWSTICK,
                ModItems.BROWN_GLOWSTICK, ModItems.RED_GLOWSTICK, ModItems.ORANGE_GLOWSTICK, ModItems.YELLOW_GLOWSTICK,
                ModItems.LIME_GLOWSTICK, ModItems.GREEN_GLOWSTICK, ModItems.CYAN_GLOWSTICK, ModItems.LIGHT_BLUE_GLOWSTICK,
                ModItems.BLUE_GLOWSTICK, ModItems.PURPLE_GLOWSTICK, ModItems.MAGENTA_GLOWSTICK, ModItems.PINK_GLOWSTICK
        );

        e.add(CreativeModeTabs.SPAWN_EGGS, ModItems.DUST_BUNNY_SPAWN_EGG.get());
    }

    private static void after(RegHelper.ItemToTabEvent event, Item target,
                              ResourceKey<CreativeModeTab> tab, Supplier<?>... items) {

        ItemLike[] entries = Arrays.stream(items).map((s -> (ItemLike) (s.get()))).toArray(ItemLike[]::new);
        if(CommonConfigs.CREATIVE_TAB.get()) {
            tab = MOD_TAB.getHolder().unwrapKey().get();
        }
        event.addAfter(tab, i -> i.is(target), entries);
    }

    private static void before(RegHelper.ItemToTabEvent event, Item target,
                               ResourceKey<CreativeModeTab> tab, Supplier<?>... items) {

        ItemLike[] entries = Arrays.stream(items).map(s -> (ItemLike) s.get()).toArray(ItemLike[]::new);
        if(CommonConfigs.CREATIVE_TAB.get()){
            tab = MOD_TAB.getHolder().unwrapKey().get();
        }
        event.addBefore(tab, i -> i.is(target), entries);
    }

}