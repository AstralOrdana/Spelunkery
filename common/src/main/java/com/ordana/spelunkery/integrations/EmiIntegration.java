package com.ordana.spelunkery.integrations;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.configs.CommonConfigs;
import com.ordana.spelunkery.reg.ModBlocks;
import com.ordana.spelunkery.reg.ModItems;
import com.ordana.spelunkery.reg.ModTags;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

@EmiEntrypoint
public class EmiIntegration implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {

        EmiIngredient bottle = EmiStack.of(Items.GLASS_BOTTLE);

        if (PlatformHelper.isModLoaded("create")) {
            try {
                registry.addRecipe(EmiWorldInteractionRecipe.builder()
                        .id(new ResourceLocation("spelunkery", "/rose_quartz"))
                        .rightInput(EmiStack.of(Blocks.GRINDSTONE), true)
                        .leftInput(EmiStack.of(Registry.ITEM.get(new ResourceLocation("create:rose_quartz"))))
                        .output(EmiStack.of(Registry.ITEM.get(new ResourceLocation("create:polished_rose_quartz"))))
                        .build());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(new ResourceLocation("spelunkery", "/rock_salt_boiling"))
                .leftInput(EmiStack.of(ModItems.ROUGH_CINNABAR_SHARD.get()))
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
                .rightInput(EmiStack.of(ModBlocks.DIAMOND_GRINDSTONE.get()), false)
                .output(EmiStack.of(ModBlocks.DIAMOND_GRINDSTONE.get()))
                .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(new ResourceLocation("spelunkery", "/salt_grinding"))
                .leftInput(EmiStack.of(ModItems.ROCK_SALT.get()))
                .rightInput(EmiStack.of(Blocks.GRINDSTONE), false)
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
                .rightInput(EmiStack.of(Blocks.GRINDSTONE), true)
                .leftInput(EmiStack.of(ModItems.ROUGH_CINNABAR_SHARD.get()))
                .output(EmiStack.of(ModItems.CINNABAR_SHARD.get()))
                .output(EmiStack.of(Items.REDSTONE))
                .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(new ResourceLocation("spelunkery", "/cinnabar"))
                .rightInput(EmiStack.of(Blocks.GRINDSTONE), true)
                .leftInput(EmiStack.of(ModItems.ROUGH_CINNABAR.get()))
                .output(EmiStack.of(ModItems.CINNABAR.get()))
                .output(EmiStack.of(Items.REDSTONE))
                .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(new ResourceLocation("spelunkery", "/cinnabar_block"))
                .rightInput(EmiStack.of(Blocks.GRINDSTONE), true)
                .leftInput(EmiStack.of(ModBlocks.ROUGH_CINNABAR_BLOCK.get()))
                .output(EmiStack.of(ModBlocks.CINNABAR_BLOCK.get()))
                .output(EmiStack.of(Items.REDSTONE))
                .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(new ResourceLocation("spelunkery", "/lazurite_shard"))
                .rightInput(EmiStack.of(Blocks.GRINDSTONE), true)
                .leftInput(EmiStack.of(ModItems.ROUGH_LAZURITE_SHARD.get()))
                .output(EmiStack.of(ModItems.LAPIS_LAZULI_SHARD.get()))
                .output(EmiStack.of(ModItems.RAW_GOLD_NUGGET.get()))
                .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(new ResourceLocation("spelunkery", "/lazurite"))
                .rightInput(EmiStack.of(Blocks.GRINDSTONE), true)
                .leftInput(EmiStack.of(ModItems.ROUGH_LAZURITE.get()))
                .output(EmiStack.of(Items.LAPIS_LAZULI))
                .output(EmiStack.of(ModItems.RAW_GOLD_NUGGET.get()))
                .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(new ResourceLocation("spelunkery", "/lazurite_block"))
                .rightInput(EmiStack.of(Blocks.GRINDSTONE), true)
                .leftInput(EmiStack.of(ModBlocks.ROUGH_LAZURITE_BLOCK.get()))
                .output(EmiStack.of(Blocks.LAPIS_BLOCK))
                .output(EmiStack.of(ModItems.RAW_GOLD_NUGGET.get()))
                .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(new ResourceLocation("spelunkery", "/emerald_shard"))
                .rightInput(EmiStack.of(ModBlocks.DIAMOND_GRINDSTONE.get()), true)
                .leftInput(EmiStack.of(ModItems.ROUGH_EMERALD_SHARD.get()))
                .output(EmiStack.of(ModItems.EMERALD_SHARD.get()))
                .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(new ResourceLocation("spelunkery", "/emerald"))
                .rightInput(EmiStack.of(ModBlocks.DIAMOND_GRINDSTONE.get()), true)
                .leftInput(EmiStack.of(ModItems.ROUGH_EMERALD.get()))
                .output(EmiStack.of(Items.EMERALD))
                .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(new ResourceLocation("spelunkery", "/emerald_block"))
                .rightInput(EmiStack.of(ModBlocks.DIAMOND_GRINDSTONE.get()), true)
                .leftInput(EmiStack.of(ModBlocks.ROUGH_EMERALD_BLOCK.get()))
                .output(EmiStack.of(Blocks.EMERALD_BLOCK))
                .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(new ResourceLocation("spelunkery", "/diamond_shard"))
                .rightInput(EmiStack.of(ModBlocks.DIAMOND_GRINDSTONE.get()), true)
                .leftInput(EmiStack.of(ModItems.ROUGH_DIAMOND_SHARD.get()))
                .output(EmiStack.of(ModItems.DIAMOND_SHARD.get()))
                .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(new ResourceLocation("spelunkery", "/diamond"))
                .rightInput(EmiStack.of(ModBlocks.DIAMOND_GRINDSTONE.get()), true)
                .leftInput(EmiStack.of(ModItems.ROUGH_DIAMOND.get()))
                .output(EmiStack.of(Items.DIAMOND))
                .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(new ResourceLocation("spelunkery", "/diamond_block"))
                .rightInput(EmiStack.of(ModBlocks.DIAMOND_GRINDSTONE.get()), true)
                .leftInput(EmiStack.of(ModBlocks.ROUGH_DIAMOND_BLOCK.get()))
                .output(EmiStack.of(Blocks.DIAMOND_BLOCK))
                .build());
    }
}
