package com.ordana.spelunkery.reg;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.recipes.GrindstonePolishingRecipe;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public class ModRecipes {
    public static void init() {
        Registry.register(Registry.RECIPE_SERIALIZER, new ResourceLocation(Spelunkery.MOD_ID, "grindstone_polishing"),
                GrindstonePolishingRecipe.Serializer.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(Spelunkery.MOD_ID, "grindstone_polishing"),
                GrindstonePolishingRecipe.Type.INSTANCE);
    }
}
