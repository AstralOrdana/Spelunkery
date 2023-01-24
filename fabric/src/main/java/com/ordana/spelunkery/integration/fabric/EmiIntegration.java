package com.ordana.spelunkery.integration.fabric;

import com.ordana.spelunkery.reg.ModBlocks;
import com.ordana.spelunkery.reg.ModItems;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class EmiIntegration implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {

        EmiIngredient bottle = EmiStack.of(Items.GLASS_BOTTLE);

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(new ResourceLocation("spelunkery", "/portal_fluid"))
                .rightInput(EmiStack.of(Blocks.CRYING_OBSIDIAN), false)
                .leftInput(EmiStack.of(Items.GLASS_BOTTLE))
                .output(EmiStack.of(Blocks.OBSIDIAN))
                .output(EmiStack.of(ModItems.PORTAL_FLUID_BOTTLE.get()))
                .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(new ResourceLocation("spelunkery", "/portal_fluid_anchor"))
                .rightInput(EmiStack.of(Blocks.RESPAWN_ANCHOR), true)
                .leftInput(EmiStack.of(Items.GLASS_BOTTLE))
                .output(EmiStack.of(ModItems.PORTAL_FLUID_BOTTLE.get()))
                .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(new ResourceLocation("spelunkery", "/cinnabar_shard"))
                .rightInput(EmiStack.of(Blocks.GRINDSTONE), true)
                .leftInput(EmiStack.of(ModItems.ROUGH_CINNABAR_SHARD.get()))
                .output(EmiStack.of(ModItems.CINNABAR_SHARD.get()))
                .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(new ResourceLocation("spelunkery", "/cinnabar"))
                .rightInput(EmiStack.of(Blocks.GRINDSTONE), true)
                .leftInput(EmiStack.of(ModItems.ROUGH_CINNABAR.get()))
                .output(EmiStack.of(ModItems.CINNABAR.get()))
                .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(new ResourceLocation("spelunkery", "/cinnabar_block"))
                .rightInput(EmiStack.of(Blocks.GRINDSTONE), true)
                .leftInput(EmiStack.of(ModBlocks.ROUGH_CINNABAR_BLOCK.get()))
                .output(EmiStack.of(ModBlocks.CINNABAR_BLOCK.get()))
                .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(new ResourceLocation("spelunkery", "/lazurite_shard"))
                .rightInput(EmiStack.of(Blocks.GRINDSTONE), true)
                .leftInput(EmiStack.of(ModItems.ROUGH_LAZURITE_SHARD.get()))
                .output(EmiStack.of(ModItems.LAPIS_LAZULI_SHARD.get()))
                .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(new ResourceLocation("spelunkery", "/lazurite"))
                .rightInput(EmiStack.of(Blocks.GRINDSTONE), true)
                .leftInput(EmiStack.of(ModItems.ROUGH_LAZURITE.get()))
                .output(EmiStack.of(Items.LAPIS_LAZULI))
                .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(new ResourceLocation("spelunkery", "/lazurite_block"))
                .rightInput(EmiStack.of(Blocks.GRINDSTONE), true)
                .leftInput(EmiStack.of(ModBlocks.ROUGH_LAZURITE_BLOCK.get()))
                .output(EmiStack.of(Blocks.LAPIS_BLOCK))
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
