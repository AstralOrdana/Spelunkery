package com.ordana.underground_overhaul.reg;

import com.ordana.underground_overhaul.UndergroundOverhaul;
import com.ordana.underground_overhaul.recipes.GrindstonePolishingRecipe;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.intellij.lang.annotations.Identifier;

import java.util.function.Supplier;

public class ModRecipes {
    public static void init() {
        Registry.register(Registry.RECIPE_SERIALIZER, new ResourceLocation(UndergroundOverhaul.MOD_ID, "grindstone_polishing"),
                GrindstonePolishingRecipe.Serializer.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(UndergroundOverhaul.MOD_ID, "grindstone_polishing"),
                GrindstonePolishingRecipe.Type.INSTANCE);
    }
}
