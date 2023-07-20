package com.ordana.spelunkery.reg;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.recipes.GrindstonePolishingRecipe;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Supplier;

public class ModRecipes {
    public static void init() {
    }

    public static final Supplier<RecipeType<GrindstonePolishingRecipe>> GRINDSTONE_POLISHING =
            RegHelper.register(new ResourceLocation(Spelunkery.MOD_ID + ":grindstone_polishing"),()-> {
        var r =  new RecipeType<GrindstonePolishingRecipe>() {
            @Override
            public String toString() {
                return Spelunkery.MOD_ID + ":grindstone_polishing";
            }
        };
        return r;
    }, Registries.RECIPE_TYPE);

    public static final Supplier<RecipeSerializer<GrindstonePolishingRecipe>> GRINDSTONE_POLISHING_SERIALIZER =
            RegHelper.registerRecipeSerializer(new ResourceLocation(Spelunkery.MOD_ID + ":grindstone_polishing"),
                    GrindstonePolishingRecipe.Serializer::new);
}
