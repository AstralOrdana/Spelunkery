package com.ordana.spelunkery.reg;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.recipes.GrindstonePolishingRecipe;
import com.ordana.spelunkery.recipes.GrindstonePolishingRecipeBuilder;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.ItemLike;

import java.util.function.Function;
import java.util.function.Supplier;

public class ModRecipes {
    /*

    public static void init() {
    }

    public static final Supplier<RecipeSerializer<GrindstonePolishingRecipe>> GRINDSTONE_POLISHING_SERIALIZER = reg(
            "grindstone_polishing", GrindstonePolishingRecipe::new);


    private static <T extends Recipe<?>> Supplier<RecipeSerializer<T>> reg(String name, Function<ResourceLocation, T> factory) {
        return RegHelper.registerRecipeSerializer(Spelunkery.res(name), () -> new SimpleRecipeSerializer<>(factory));
    }

     */
}
