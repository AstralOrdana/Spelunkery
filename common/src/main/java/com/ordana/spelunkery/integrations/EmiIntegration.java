package com.ordana.spelunkery.integrations;

import com.ordana.spelunkery.configs.CommonConfigs;
import com.ordana.spelunkery.reg.ModBlocks;
import com.ordana.spelunkery.reg.ModFluids;
import com.ordana.spelunkery.reg.ModItems;
import com.ordana.spelunkery.reg.ModTags;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;

@EmiEntrypoint
public class EmiIntegration implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {

        EmiIngredient grindstone = EmiStack.of(Blocks.GRINDSTONE);
        EmiIngredient diamondGrindstone = EmiStack.of(ModBlocks.DIAMOND_GRINDSTONE.get());
        EmiIngredient sluice = EmiIngredient.of(ModTags.SLUICES);
        EmiStack water = EmiStack.of(Fluids.WATER);
        water.setRemainder(water);
        EmiStack lava = EmiStack.of(Fluids.LAVA);
        lava.setRemainder(lava);
        EmiStack portalFluid = EmiStack.of(ModFluids.PORTAL_FLUID.get());
        portalFluid.setRemainder(portalFluid);
        EmiStack springWater = EmiStack.of(ModFluids.SPRING_WATER.get());
        springWater.setRemainder(springWater);

        var style = Style.EMPTY.applyFormats(ChatFormatting.GREEN);


        if (PlatHelper.isModLoaded("create")) {
            try {
                registry.addRecipe(EmiWorldInteractionRecipe.builder()
                    .id(new ResourceLocation("spelunkery", "/rose_quartz"))
                    .rightInput(grindstone, true)
                    .leftInput(EmiStack.of(BuiltInRegistries.ITEM.get(new ResourceLocation("create:rose_quartz"))))
                    .output(EmiStack.of(BuiltInRegistries.ITEM.get(new ResourceLocation("create:polished_rose_quartz"))))
                    .build());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
            .id(new ResourceLocation("spelunkery", "/rock_salt_boiling"))
            .leftInput(EmiStack.of(ModItems.SALT.get()))
            .rightInput(EmiStack.of(Blocks.WATER_CAULDRON), false)
            .output(EmiStack.of(ModItems.ROCK_SALT.get()))
            .output(EmiStack.of(Blocks.CAULDRON))
            .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
            .id(new ResourceLocation("spelunkery", "/anvil_repair"))
            .leftInput(EmiIngredient.of(Ingredient.of(ModTags.ANVIL_REPAIR_ITEM)))
            .rightInput(EmiStack.of(Blocks.DAMAGED_ANVIL), false)
            .output(EmiStack.of(Blocks.CHIPPED_ANVIL))
            .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
            .id(new ResourceLocation("spelunkery", "/anvil_repair2"))
            .leftInput(EmiIngredient.of(Ingredient.of(ModTags.ANVIL_REPAIR_ITEM)))
            .rightInput(EmiStack.of(Blocks.CHIPPED_ANVIL), false)
            .output(EmiStack.of(Blocks.ANVIL))
            .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
            .id(new ResourceLocation("spelunkery", "/diamond_grindstone_repair"))
            .leftInput(EmiStack.of(ModItems.ROUGH_DIAMOND_SHARD.get()))
            .rightInput(diamondGrindstone, false)
            .output(EmiStack.of(ModBlocks.DIAMOND_GRINDSTONE.get()))
            .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
            .id(new ResourceLocation("spelunkery", "/salt_grinding"))
            .leftInput(EmiStack.of(ModItems.ROCK_SALT.get()))
            .rightInput(grindstone, false)
            .output(EmiStack.of(ModItems.SALT.get()))
            .build());

        if (CommonConfigs.CRYING_OBSIDIAN_PORTAL_FLUID.get()) {
            registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(new ResourceLocation("spelunkery", "/portal_fluid"))
                .rightInput(EmiStack.of(Blocks.CRYING_OBSIDIAN), false)
                .leftInput(EmiStack.of(Items.GLASS_BOTTLE))
                .output(EmiStack.of(Blocks.OBSIDIAN))
                .output(EmiStack.of(ModItems.PORTAL_FLUID_BOTTLE.get()))
                .build());
        }

        if (CommonConfigs.RESPAWN_ANCHOR_PORTAL_FLUID.get()) {
            registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(new ResourceLocation("spelunkery", "/portal_fluid_anchor"))
                .rightInput(EmiStack.of(Blocks.RESPAWN_ANCHOR), true)
                .leftInput(EmiStack.of(Items.GLASS_BOTTLE))
                .output(EmiStack.of(ModItems.PORTAL_FLUID_BOTTLE.get()))
                .build());
        }

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
            .id(new ResourceLocation("spelunkery", "/cinnabar_shard"))
            .rightInput(grindstone, true)
            .leftInput(EmiStack.of(ModItems.ROUGH_CINNABAR_SHARD.get()))
            .output(EmiStack.of(ModItems.CINNABAR_SHARD.get()))
            .output(EmiStack.of(Items.REDSTONE))
            .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
            .id(new ResourceLocation("spelunkery", "/cinnabar"))
            .rightInput(grindstone, true)
            .leftInput(EmiStack.of(ModItems.ROUGH_CINNABAR.get()))
            .output(EmiStack.of(ModItems.CINNABAR.get()))
            .output(EmiStack.of(Items.REDSTONE))
            .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
            .id(new ResourceLocation("spelunkery", "/cinnabar_block"))
            .rightInput(grindstone, true)
            .leftInput(EmiStack.of(ModBlocks.ROUGH_CINNABAR_BLOCK.get()))
            .output(EmiStack.of(ModBlocks.CINNABAR_BLOCK.get()))
            .output(EmiStack.of(Items.REDSTONE))
            .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
            .id(new ResourceLocation("spelunkery", "/lazurite_shard"))
            .rightInput(grindstone, true)
            .leftInput(EmiStack.of(ModItems.ROUGH_LAZURITE_SHARD.get()))
            .output(EmiStack.of(ModItems.LAPIS_LAZULI_SHARD.get()))
            .output(EmiStack.of(ModItems.RAW_GOLD_NUGGET.get()))
            .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
            .id(new ResourceLocation("spelunkery", "/lazurite"))
            .rightInput(grindstone, true)
            .leftInput(EmiStack.of(ModItems.ROUGH_LAZURITE.get()))
            .output(EmiStack.of(Items.LAPIS_LAZULI))
            .output(EmiStack.of(ModItems.RAW_GOLD_NUGGET.get()))
            .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
            .id(new ResourceLocation("spelunkery", "/lazurite_block"))
            .rightInput(grindstone, true)
            .leftInput(EmiStack.of(ModBlocks.ROUGH_LAZURITE_BLOCK.get()))
            .output(EmiStack.of(Blocks.LAPIS_BLOCK))
            .output(EmiStack.of(ModItems.RAW_GOLD_NUGGET.get()))
            .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
            .id(new ResourceLocation("spelunkery", "/emerald_shard"))
            .rightInput(diamondGrindstone, true)
            .leftInput(EmiStack.of(ModItems.ROUGH_EMERALD_SHARD.get()))
            .output(EmiStack.of(ModItems.EMERALD_SHARD.get()))
            .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
            .id(new ResourceLocation("spelunkery", "/emerald"))
            .rightInput(diamondGrindstone, true)
            .leftInput(EmiStack.of(ModItems.ROUGH_EMERALD.get()))
            .output(EmiStack.of(Items.EMERALD))
            .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
            .id(new ResourceLocation("spelunkery", "/emerald_block"))
            .rightInput(diamondGrindstone, true)
            .leftInput(EmiStack.of(ModBlocks.ROUGH_EMERALD_BLOCK.get()))
            .output(EmiStack.of(Blocks.EMERALD_BLOCK))
            .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
            .id(new ResourceLocation("spelunkery", "/diamond_shard"))
            .rightInput(diamondGrindstone, true)
            .leftInput(EmiStack.of(ModItems.ROUGH_DIAMOND_SHARD.get()))
            .output(EmiStack.of(ModItems.DIAMOND_SHARD.get()))
            .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
            .id(new ResourceLocation("spelunkery", "/diamond"))
            .rightInput(diamondGrindstone, true)
            .leftInput(EmiStack.of(ModItems.ROUGH_DIAMOND.get()))
            .output(EmiStack.of(Items.DIAMOND))
            .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
            .id(new ResourceLocation("spelunkery", "/diamond_block"))
            .rightInput(diamondGrindstone, true)
            .leftInput(EmiStack.of(ModBlocks.ROUGH_DIAMOND_BLOCK.get()))
            .output(EmiStack.of(Blocks.DIAMOND_BLOCK))
            .build());


        //sluice recipes
        registry.addRecipe(EmiWorldInteractionRecipe.builder()
            .id(new ResourceLocation("spelunkery", "/spring_water_passive"))
            .rightInput(sluice, true)
            .leftInput(springWater)
            .output(EmiStack.of(ModItems.ROCK_SALT.get()).setChance(20f / 50f))
            .output(EmiStack.of(ModBlocks.SALTPETER.get()).setChance(15f / 50f))
            .output(EmiStack.of(ModBlocks.SULFUR.get()).setChance(5f / 50f))
            .output(EmiStack.of(ModItems.RAW_COPPER_NUGGET.get()).setChance(5f / 50f))
            .output(EmiStack.of(ModItems.RAW_IRON_NUGGET.get()).setChance(5f / 50f))
            .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
            .id(new ResourceLocation("spelunkery", "/water_passive"))
            .rightInput(sluice, true)
            .leftInput(water)
            .output(EmiStack.of(ModItems.STONE_PEBBLE.get()).setChance(20f / 60f))
            .output(EmiStack.of(Items.WHEAT_SEEDS).setChance(20f / 60f))
            .output(EmiStack.of(Items.SEAGRASS).setChance(15f / 60f))
            .output(EmiStack.of(Items.DIRT).setChance(5f / 60f))
            .output(EmiStack.of(Items.ICE).setChance(20f / 60f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.icy").setStyle(style)))
            .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
            .id(new ResourceLocation("spelunkery", "/water_sluice_clay"))
            .rightInput(sluice, true)
            .leftInput(water)
            .leftInput(EmiStack.of(Blocks.CLAY))
            .output(EmiStack.of(Items.SEAGRASS).setChance(10f / 80f))
            .output(EmiStack.of(ModBlocks.TANGLE_ROOTS.get()).setChance(10f / 80f))
            .output(EmiStack.of(ModItems.NEPHRITE_CHUNK.get()).setChance(5f / 80f))
            .output(EmiStack.of(ModItems.RAW_COPPER_NUGGET.get()).setChance(4f / 80f))
            .output(EmiStack.of(ModBlocks.SALTPETER.get()).setChance(10f / 80f))
            .output(EmiStack.of(Items.BOWL).setChance(10f / 80f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.ruins").setStyle(style)))
            .output(EmiStack.of(Items.BRICK).setChance(10f / 80f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.ruins").setStyle(style)))
            .output(EmiStack.of(Items.SPYGLASS).setChance(5f / 80f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.ruins").setStyle(style)))
            .output(EmiStack.of(Items.WRITABLE_BOOK).setChance(5f / 80f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.ruins").setStyle(style)))
            .output(EmiStack.of(Items.SHEARS).setChance(5f / 80f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.ruins").setStyle(style)))
            .output(EmiStack.of(Items.BUNDLE).setChance(5f / 80f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.ruins").setStyle(style)))
            .output(EmiStack.of(ModItems.ROUGH_DIAMOND_SHARD.get()).setChance(1f / 80f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.desert_villages").setStyle(style)))
            .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
            .id(new ResourceLocation("spelunkery", "/water_sluice_coarse_dirt"))
            .rightInput(sluice, true)
            .leftInput(water)
            .leftInput(EmiStack.of(Blocks.COARSE_DIRT))
            .output(EmiStack.of(Items.FLINT).setChance(0.357f))
            .output(EmiStack.of(ModItems.STONE_PEBBLE.get()).setChance(0.267f))
            .output(EmiStack.of(Items.MELON_SEEDS).setChance(0.178f))
            .output(EmiStack.of(Items.PUMPKIN_SEEDS).setChance(0.178f))
            .output(EmiStack.of(ModItems.DUST_BUNNY_SPAWN_EGG.get()).setChance(0.017f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.mineshafts").setStyle(style)))
            .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
            .id(new ResourceLocation("spelunkery", "/water_sluice_dirt"))
            .rightInput(sluice, true)
            .leftInput(water)
            .leftInput(EmiStack.of(Blocks.DIRT))
            .output(EmiStack.of(Items.STICK).setChance(0.166f))
            .output(EmiStack.of(Items.STRING).setChance(0.166f))
            .output(EmiStack.of(Items.FEATHER).setChance(0.166f))
            .output(EmiStack.of(Items.COBWEB).setChance(0.083f))
            .output(EmiStack.of(Items.GRASS).setChance(0.083f))
            .output(EmiStack.of(Items.WHEAT_SEEDS).setChance(0.083f))
            .output(EmiStack.of(Items.COCOA_BEANS).setChance(0.083f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.jungles").setStyle(style)))
            .output(EmiStack.of(ModItems.STONE_PEBBLE.get()).setChance(0.125f))
            .output(EmiStack.of(ModItems.DEEPSLATE_PEBBLE.get()).setChance(0.041f))
            .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
            .id(new ResourceLocation("spelunkery", "/water_sluice_gravel"))
            .rightInput(sluice, true)
            .leftInput(water)
            .leftInput(EmiStack.of(Blocks.GRAVEL))
            .output(EmiStack.of(Items.FLINT).setChance(0.25f))
            .output(EmiStack.of(ModItems.ROUGH_EMERALD_SHARD.get()).setChance(0.012f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.jagged_peaks").setStyle(style)))
            .output(EmiStack.of(Items.PRISMARINE_SHARD).setChance(0.125f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.oceans").setStyle(style)))
            .output(EmiStack.of(ModItems.NEPHRITE_CHUNK.get()).setChance(0.125f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.rivers").setStyle(style)))
            .output(EmiStack.of(Items.SAND).setChance(0.125f))
            .output(EmiStack.of(ModItems.STONE_PEBBLE.get()).setChance(0.25f))
            .output(EmiStack.of(ModItems.DEEPSLATE_PEBBLE.get()).setChance(0.062f))
            .output(EmiStack.of(ModItems.RAW_COPPER_NUGGET.get()).setChance(0.025f))
            .output(EmiStack.of(ModItems.RAW_IRON_NUGGET.get()).setChance(0.025f))
            .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
            .id(new ResourceLocation("spelunkery", "/water_sluice_mud"))
            .rightInput(sluice, true)
            .leftInput(water)
            .leftInput(EmiStack.of(Blocks.MUD))
            .output(EmiStack.of(Items.MANGROVE_ROOTS).setChance(0.5f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.mangrove_swamps").setStyle(style)))
            .output(EmiStack.of(Items.MANGROVE_PROPAGULE).setChance(0.5f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.mangrove_swamps").setStyle(style)))
            .output(EmiStack.of(Items.BONE).setChance(0.277f))
            .output(EmiStack.of(Items.SKELETON_SKULL).setChance(0.138f))
            .output(EmiStack.of(Items.LEATHER_BOOTS).setChance(0.277f))
            .output(EmiStack.of(Items.GLASS_BOTTLE).setChance(0.277f))
            .output(EmiStack.of(Items.CLAY).setChance(0.027f))
            .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
            .id(new ResourceLocation("spelunkery", "/water_sluice_red_sand"))
            .rightInput(sluice, true)
            .leftInput(water)
            .leftInput(EmiStack.of(Blocks.RED_SAND))
            .output(EmiStack.of(Items.DEAD_BUSH).setChance(0.222f))
            .output(EmiStack.of(Items.STICK).setChance(0.222f))
            .output(EmiStack.of(Items.BONE).setChance(0.222f))
            .output(EmiStack.of(Items.SKELETON_SKULL).setChance(0.055f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.badlands").setStyle(style)))
            .output(EmiStack.of(ModItems.RAW_GOLD_NUGGET.get()).setChance(0.111f))
            .output(EmiStack.of(ModItems.RAW_IRON_NUGGET.get()).setChance(0.111f))
            .output(EmiStack.of(ModItems.ROUGH_CINNABAR_SHARD.get()).setChance(0.055f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.badlands").setStyle(style)))
            .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
            .id(new ResourceLocation("spelunkery", "/water_sluice_rooted_dirt"))
            .rightInput(sluice, true)
            .leftInput(water)
            .leftInput(EmiStack.of(Blocks.ROOTED_DIRT))
            .output(EmiStack.of(Items.HANGING_ROOTS).setChance(0.303f))
            .output(EmiStack.of(ModBlocks.TANGLE_ROOTS.get()).setChance(0.303f))
            .output(EmiStack.of(Items.STRING).setChance(0.303f))
            .output(EmiStack.of(Items.POTATO).setChance(0.015f))
            .output(EmiStack.of(Items.CARROT).setChance(0.03f))
            .output(EmiStack.of(Items.POISONOUS_POTATO).setChance(0.015f))
            .output(EmiStack.of(Items.BEETROOT).setChance(0.03f))
            .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
            .id(new ResourceLocation("spelunkery", "/water_sluice_sand"))
            .rightInput(sluice, true)
            .leftInput(water)
            .leftInput(EmiStack.of(Blocks.SAND))
            .output(EmiStack.of(Items.KELP).setChance(20f / 158f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.oceans").setStyle(style)))
            .output(EmiStack.of(Items.SEAGRASS).setChance(15f / 158f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.oceans").setStyle(style)))

            .output(EmiStack.of(ModItems.RAW_GOLD_NUGGET.get()).setChance(5f / 158f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.badlands").setStyle(style)))
            .output(EmiStack.of(ModItems.ROUGH_LAZURITE_SHARD.get()).setChance(10f / 158f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.deserts").setStyle(style)))
            .output(EmiStack.of(Items.DEAD_BUSH).setChance(10f / 158f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.deserts").setStyle(style)))
            .output(EmiStack.of(ModItems.ROUGH_CINNABAR_SHARD.get()).setChance(1f / 158f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.deserts").setStyle(style)))

            .output(EmiStack.of(Items.NAUTILUS_SHELL).setChance(5f / 158f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.beaches").setStyle(style)))
            .output(EmiStack.of(Items.SCUTE).setChance(5f / 158f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.beaches").setStyle(style)))

            .output(EmiStack.of(Items.BEETROOT_SEEDS).setChance(20f / 158f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.beaches").setStyle(style)))
            .output(EmiStack.of(Items.SILVERFISH_SPAWN_EGG).setChance(5f / 158f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.beaches").setStyle(style)))
            .output(EmiStack.of(Items.STICK).setChance(10f / 158f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.beaches").setStyle(style)))
            .output(EmiStack.of(Items.GREEN_STAINED_GLASS_PANE).setChance(2f / 158f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.beaches").setStyle(style)))
            .output(EmiStack.of(Items.BLUE_STAINED_GLASS_PANE).setChance(2f / 158f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.beaches").setStyle(style)))
            .output(EmiStack.of(Items.BROWN_STAINED_GLASS_PANE).setChance(1f / 158f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.beaches").setStyle(style)))
            .output(EmiStack.of(Items.SEA_PICKLE).setChance(10f / 158f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.warm_oceans").setStyle(style)))
            .output(EmiStack.of(Items.SNIFFER_EGG).setChance(1f / 158f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.warm_oceans").setStyle(style)))
            .output(EmiStack.of(Items.FIRE_CORAL).setChance(2f / 158f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.warm_oceans").setStyle(style)))
            .output(EmiStack.of(Items.HORN_CORAL).setChance(2f / 158f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.warm_oceans").setStyle(style)))
            .output(EmiStack.of(Items.TUBE_CORAL).setChance(2f / 158f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.warm_oceans").setStyle(style)))
            .output(EmiStack.of(Items.BRAIN_CORAL).setChance(2f / 158f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.warm_oceans").setStyle(style)))
            .output(EmiStack.of(Items.BUBBLE_CORAL).setChance(2f / 158f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.warm_oceans").setStyle(style)))
            .output(EmiStack.of(Items.SAND).setChance(1f / 158f))
            .output(EmiStack.of(Items.QUARTZ).setChance(5f / 158f))
            .output(EmiStack.of(ModItems.STONE_PEBBLE.get()).setChance(20f / 158f))
            .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
            .id(new ResourceLocation("spelunkery", "/water_sluice_soul_sand"))
            .rightInput(sluice, true)
            .leftInput(water)
            .leftInput(EmiStack.of(Blocks.SOUL_SAND))
            .output(EmiStack.of(Items.BONE).setChance(20f / 85f))
            .output(EmiStack.of(ModItems.BASALT_PEBBLE.get()).setChance(10f / 85f))
            .output(EmiStack.of(Items.NETHER_WART).setChance(20f / 85f))
            .output(EmiStack.of(Items.QUARTZ).setChance(10f / 85f))
            .output(EmiStack.of(ModItems.RAW_GOLD_NUGGET.get()).setChance(10f / 85f))
            .output(EmiStack.of(ModBlocks.SULFUR.get()).setChance(15f / 85f))
            .build());



        registry.addRecipe(EmiWorldInteractionRecipe.builder()
            .id(new ResourceLocation("spelunkery", "/lava_passive"))
            .rightInput(sluice, true)
            .leftInput(lava)
            .output(EmiStack.of(ModItems.NETHERRACK_PEBBLE.get()).setChance(200f / 572f))
            .output(EmiStack.of(ModItems.BLACKSTONE_PEBBLE.get()).setChance(100f / 572f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.wastes_deltas").setStyle(style)))
            .output(EmiStack.of(Items.MAGMA_BLOCK).setChance(150f / 572f))
            .output(EmiStack.of(Items.MAGMA_CREAM).setChance(100f / 572f))
            .output(EmiStack.of(Items.MAGMA_CUBE_SPAWN_EGG).setChance(10f / 572f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.wastes_deltas").setStyle(style)))
            .output(EmiStack.of(Items.WITHER_SKELETON_SKULL).setChance(1f / 572f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.fortresses").setStyle(style)))
            .output(EmiStack.of(ModItems.RAW_GOLD_NUGGET.get()).setChance(10f / 572f))
            .output(EmiStack.of(Items.NETHERITE_SCRAP).setChance(1f / 572f), s -> s.appendTooltip(
                Component.translatable("tooltip.spelunkery.sluice.wastes_deltas").setStyle(style)))
            .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
            .id(new ResourceLocation("spelunkery", "/portal_fluid_passive"))
            .rightInput(sluice, true)
            .leftInput(portalFluid)
            .output(EmiStack.of(ModItems.END_STONE_PEBBLE.get()).setChance(0.571f))
            .output(EmiStack.of(Items.ENDER_PEARL).setChance(0.142f))
            .output(EmiStack.of(Items.POPPED_CHORUS_FRUIT).setChance(0.142f))
            .output(EmiStack.of(Items.ENDERMITE_SPAWN_EGG).setChance(0.142f))
            .build());
    }
}
