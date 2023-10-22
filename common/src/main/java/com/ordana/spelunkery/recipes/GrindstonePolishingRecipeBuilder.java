package com.ordana.spelunkery.recipes;

/* Code below modified and adapted from Sully's Mod: https://github.com/Uraneptus/Sullys-Mod/
Specific section modified:https://github.com/Uraneptus/Sullys-Mod/blob/8860f86d897e23ed1997f8dd2022260c9db2e422/src/main/java/com/uraneptus/sullysmod/core/data/server/builder/GrindstonePolishingRecipeBuilder.java
Significant changes include: addition of byproducts, diamond grinding tier.
Used under GNU LESSER GENERAL PUBLIC LICENSE, full text can be found in root/LICENSE
 */


import com.google.gson.JsonObject;
import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.reg.ModRecipes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@SuppressWarnings({"unused", "deprecation"})
@MethodsReturnNonnullByDefault
public class GrindstonePolishingRecipeBuilder {

    private final Item ingredient;
    private final Item result;
    private final int resultCount;
    private final Item byproduct;
    private final int byproductMin;
    private final int byproductMax;
    private final int experience;
    private final boolean requiresDiamondGrindstone;
    private final RecipeSerializer<?> serializer;
    private String recipeGroup;

    private GrindstonePolishingRecipeBuilder(ItemLike ingredient, ItemLike result, int resultCount, ItemLike byproduct, int byproductMin, int byproductMax, int experience, boolean requiresDiamondGrindstone, RecipeSerializer<?> serializer) {
        this.ingredient = ingredient.asItem();
        this.result = result.asItem();
        this.resultCount = resultCount;
        this.byproduct = byproduct.asItem();
        this.byproductMin = byproductMin;
        this.byproductMax = byproductMax;
        this.experience = experience;
        this.requiresDiamondGrindstone = requiresDiamondGrindstone;
        this.serializer = serializer;
    }


    public static GrindstonePolishingRecipeBuilder grindstonePolishing(ItemLike ingredient, ItemLike result) {
        return grindstonePolishing(ingredient, result, 1, Blocks.AIR, 1, 1, 0, false);
    }

    public static GrindstonePolishingRecipeBuilder grindstonePolishing(ItemLike ingredient, ItemLike result, int resultCount, ItemLike byproduct, int byproductMin, int byproductMax, int experience, boolean requiresDiamondGrindstone) {
        return new GrindstonePolishingRecipeBuilder(ingredient, result, resultCount, byproduct, byproductMin, byproductMax, experience, requiresDiamondGrindstone, ModRecipes.GRINDSTONE_POLISHING_SERIALIZER.get());
    }

    //The save methods here could be improved!
    public void save(Consumer<FinishedRecipe> consumer) {
        ResourceLocation resourcelocation = BuiltInRegistries.ITEM.getKey(this.result);
        if (resourcelocation != null) {
            this.save(consumer, Spelunkery.MOD_ID + ":grindstone_polishing/" + resourcelocation.getPath() + "_from_grindstone_polishing");
        }
    }

    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, String save) {
        ResourceLocation defaultResourcelocation = BuiltInRegistries.ITEM.getKey(this.result);
        ResourceLocation resourcelocation = new ResourceLocation(save);
        if (resourcelocation.equals(defaultResourcelocation)) {
            throw new IllegalStateException("Polishing Recipe " + save + " should remove its 'save' argument as it is equal to default one");
        } else {
            this.save(pFinishedRecipeConsumer, resourcelocation);
        }
    }

    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        consumer.accept(new GrindstonePolishingRecipeBuilder.Result(id, this.serializer, this.recipeGroup == null ? "" : this.recipeGroup, this.ingredient, this.result, this.resultCount, this.byproduct, this.byproductMin, this.byproductMax, this.experience, this.requiresDiamondGrindstone));
    }

    public static class Result implements FinishedRecipe {

        private final ResourceLocation id;
        private final RecipeSerializer<?> serializer;
        private final String group;
        private final Item ingredient;
        private final Item result;
        private final int resultCount;
        private final Item byproduct;
        private final int byproductMin;
        private final int byproductMax;
        private final int experience;
        private final boolean requiresDiamondGrindstone;

        public Result(ResourceLocation pId, RecipeSerializer<?> serializer, String pGroup, Item pIngredient, Item pResult, int resultCount, Item pByproduct, int byproductMin, int byproductMax, int experience, boolean requiresDiamondGrindstone) {
            this.id = pId;
            this.serializer = serializer;
            this.group = pGroup;
            this.ingredient = pIngredient;
            this.result = pResult;
            this.resultCount = resultCount;
            this.byproduct = pByproduct;
            this.byproductMin = byproductMin;
            this.byproductMax = byproductMax;
            this.experience = experience;
            this.requiresDiamondGrindstone = requiresDiamondGrindstone;
        }

        @Override
        public void serializeRecipeData(JsonObject pJson) {
            if (!this.group.isEmpty()) {
                pJson.addProperty("group", this.group);
            }

            pJson.addProperty("ingredient", BuiltInRegistries.ITEM.getKey(this.ingredient).toString());
            pJson.addProperty("result", BuiltInRegistries.ITEM.getKey(this.result).toString());
            pJson.addProperty("resultCount", this.resultCount);
            pJson.addProperty("byproduct", BuiltInRegistries.ITEM.getKey(this.byproduct).toString());
            pJson.addProperty("byproductMin", this.byproductMin);
            pJson.addProperty("byproductMax", this.byproductMax);
            pJson.addProperty("experience", this.experience);
            pJson.addProperty("requiresDiamondGrindstone", this.requiresDiamondGrindstone);
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return this.serializer;
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }

}