package com.ordana.spelunkery.reg;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.recipes.GrindstonePolishingRecipe;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.*;

public interface ModRecipeTypes<T extends Recipe<?>> {
    /*
    RecipeType<CraftingRecipe> GRINDSTONE_POLISHNG = registerType("grindstone_polishing");

    public static final Registry<RecipeType<GrindstonePolishingRecipe>> GRINDSTONE_POLISHING = GRINDSTONE_POLISHNG.register(GrindstonePolishingRecipe.NAME, () -> registerType(GrindstonePolishingRecipe.NAME));

    public static <T extends Recipe<?>> RecipeType<T> registerType(final String identifier) {
        return new RecipeType<>() {
            public String toString() {
                return Spelunkery.MOD_ID + ":" + identifier;
            }
        };
    }

     */
}

