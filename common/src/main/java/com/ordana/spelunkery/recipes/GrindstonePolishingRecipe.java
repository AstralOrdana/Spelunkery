package com.ordana.spelunkery.recipes;

import com.google.gson.JsonObject;
import com.ordana.spelunkery.reg.ModRecipeTypes;
import com.ordana.spelunkery.reg.ModRecipes;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.awt.*;

public class GrindstonePolishingRecipe implements Recipe {

    public boolean matches(Container container, Level level) {
        return false;
    }

    public ItemStack assemble(Container container) {
        return null;
    }

    @Override
    public boolean matches(net.minecraft.world.Container container, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(net.minecraft.world.Container container) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getResultItem() {
        return null;
    }

    @Override
    public ResourceLocation getId() {
        return null;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return null;
    }
    /*
    public static final String NAME = "grindstone_polishing";

    private final ResourceLocation id;
    private final String recipeGroup;
    public final ItemStack ingredient;
    public final ItemStack result;
    private final int resultCount;
    private final int experience;

    public GrindstonePolishingRecipe(ResourceLocation id, String recipeGroup, ItemStack ingredient, ItemStack result, int resultCount, int experience) {
        this.id = id;
        this.recipeGroup = recipeGroup;
        this.ingredient = ingredient;
        this.result = result;
        this.resultCount = resultCount;
        this.experience = experience;
    }

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        return true;
    }

    @Override
    public ItemStack assemble(Container pContainer) {
        return this.result;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    public int getExperience() {
        return this.experience;
    }

    public int getResultCount() {
        return this.resultCount;
    }

    @Override
    public ItemStack getResultItem() {
        return this.result.copy();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public String getGroup() {
        return this.recipeGroup;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.GRINDSTONE_POLISHING.get();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializer.GRINDSTONE_POLISHING_SERIALIZER.get();
    }

    public static List<GrindstonePolishingRecipe> getRecipes(Level level) {
        return level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.GRINDSTONE_POLISHNG.get());
    }

    public static class Serializer implements RecipeSerializer<GrindstonePolishingRecipe> {

        @Override
        public GrindstonePolishingRecipe fromJson(ResourceLocation pRecipeId, JsonObject jsonObject) {
            String group = GsonHelper.getAsString(jsonObject, "group", "");

            if (!jsonObject.has("ingredient")) throw new com.google.gson.JsonSyntaxException("Missing ingredient, expected to find a string or object");
            ItemStack ingredient;
            if (jsonObject.get("ingredient").isJsonObject()) {
                ingredient = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "ingredient"));
            }
            else {
                String ingredientItem = GsonHelper.getAsString(jsonObject, "ingredient");
                ResourceLocation resourcelocation = new ResourceLocation(ingredientItem);
                ingredient = new ItemStack(Registry.ITEM.getOptional(resourcelocation).orElseThrow(() -> new IllegalStateException("Item: " + ingredientItem + " does not exist")));
            }
            if (!jsonObject.has("result")) throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
            ItemStack result;
            if (jsonObject.get("result").isJsonObject()) {
                result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
            }
            else {
                String resultItem = GsonHelper.getAsString(jsonObject, "result");
                ResourceLocation resourcelocation = new ResourceLocation(resultItem);
                result = new ItemStack(Registry.ITEM.getOptional(resourcelocation).orElseThrow(() -> new IllegalStateException("Item: " + resultItem + " does not exist")));
            }
            int resultCount = GsonHelper.getAsInt(jsonObject, "resultCount", 1);
            int experience = GsonHelper.getAsInt(jsonObject, "experience", 0);

            return new GrindstonePolishingRecipe(pRecipeId, group, ingredient, result, resultCount, experience);
        }

        @Nullable
        @Override
        public GrindstonePolishingRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            String group = pBuffer.readUtf();
            ItemStack ingredient = pBuffer.readItem();
            ItemStack result = pBuffer.readItem();
            int resultCount = pBuffer.readInt();
            int experience = pBuffer.readInt();

            return new GrindstonePolishingRecipe(pRecipeId, group, ingredient, result, resultCount, experience);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, GrindstonePolishingRecipe pRecipe) {
            pBuffer.writeUtf(pRecipe.recipeGroup);
            pBuffer.writeItem(pRecipe.ingredient);
            pBuffer.writeItem(pRecipe.result);
            pBuffer.writeInt(pRecipe.resultCount);
            pBuffer.writeInt(pRecipe.experience);
        }
    }

     */
}
