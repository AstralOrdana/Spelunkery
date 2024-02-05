package com.ordana.spelunkery.recipes;

/* Code below modified and adapted from Sully's Mod: https://github.com/Uraneptus/Sullys-Mod/
Specific section modified:https://github.com/Uraneptus/Sullys-Mod/blob/1.19.x/src/main/java/com/uraneptus/sullysmod/common/recipes/GrindstonePolishingRecipe.java
Significant changes include: addition of byproducts, diamond grinding tier.
Used under GNU LESSER GENERAL PUBLIC LICENSE, full text can be found in root/LICENSE
 */

import com.google.gson.JsonObject;
import com.ordana.spelunkery.reg.ModRecipes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("deprecation")
@MethodsReturnNonnullByDefault
public class GrindstonePolishingRecipe implements Recipe<Container> {
    public static final String NAME = "grindstone_polishing";

    private final ResourceLocation id;
    private final String recipeGroup;
    public final ItemStack ingredient;
    public final ItemStack result;
    private final int resultCount;
    public final ItemStack byproduct;
    private final int byproductMin;
    private final int byproductMax;
    private final int experience;
    private final boolean requiresDiamondGrindstone;

    public GrindstonePolishingRecipe(ResourceLocation id, String recipeGroup, ItemStack ingredient, ItemStack result, int resultCount, ItemStack byproduct, int byproductMin, int byproductMax, int experience, boolean requiresDiamondGrindstone) {
        this.id = id;
        this.recipeGroup = recipeGroup;
        this.ingredient = ingredient;
        this.result = result;
        this.resultCount = resultCount;
        this.byproduct = byproduct;
        this.byproductMin = byproductMin;
        this.byproductMax = byproductMax;
        this.experience = experience;
        this.requiresDiamondGrindstone = requiresDiamondGrindstone;
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

    public boolean isRequiresDiamondGrindstone() {
        return this.requiresDiamondGrindstone;
    }

    @Override
    public ItemStack getResultItem() {
        return this.result.copy();
    }

    public int getByproductMin() {
        return this.byproductMin;
    }

    public int getByproductMax() {
        return this.byproductMax;
    }

    public ItemStack getByproduct() {
        return this.byproduct.copy();
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
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.GRINDSTONE_POLISHING_SERIALIZER.get();
    }


    @Override
    public RecipeType<?> getType() {
        return ModRecipes.GRINDSTONE_POLISHING.get();
    }

    public static List<GrindstonePolishingRecipe> getRecipes(Level level) {
        return level.getRecipeManager().getAllRecipesFor(ModRecipes.GRINDSTONE_POLISHING.get());
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

            ItemStack byproduct;
            if (jsonObject.get("byproduct").isJsonObject()) {
                byproduct = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "byproduct"));
            }
            else {
                String byproductItem = GsonHelper.getAsString(jsonObject, "byproduct");
                ResourceLocation resourcelocation = new ResourceLocation(byproductItem);
                byproduct = new ItemStack(Registry.ITEM.getOptional(resourcelocation).orElseThrow(() -> new IllegalStateException("Item: " + byproductItem + " does not exist")));
            }
            int byproductMin = GsonHelper.getAsInt(jsonObject, "byproductMin", 1);
            int byproductMax = GsonHelper.getAsInt(jsonObject, "byproductMax", 1);
            int experience = GsonHelper.getAsInt(jsonObject, "experience", 0);
            boolean requiresDiamondGrindstone = GsonHelper.getAsBoolean(jsonObject, "requiresDiamondGrindstone", false);

            return new GrindstonePolishingRecipe(pRecipeId, group, ingredient, result, resultCount, byproduct, byproductMin, byproductMax, experience, requiresDiamondGrindstone);
        }

        @Nullable
        @Override
        public GrindstonePolishingRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            String group = pBuffer.readUtf();
            ItemStack ingredient = pBuffer.readItem();
            ItemStack result = pBuffer.readItem();
            int resultCount = pBuffer.readInt();
            ItemStack byproduct = pBuffer.readItem();
            int byproductMin = pBuffer.readInt();
            int byproductMax = pBuffer.readInt();
            int experience = pBuffer.readInt();
            boolean requiresDiamondGrindstone = pBuffer.readBoolean();

            return new GrindstonePolishingRecipe(pRecipeId, group, ingredient, result, resultCount, byproduct, byproductMin, byproductMax, experience, requiresDiamondGrindstone);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, GrindstonePolishingRecipe pRecipe) {
            pBuffer.writeUtf(pRecipe.recipeGroup);
            pBuffer.writeItem(pRecipe.ingredient);
            pBuffer.writeItem(pRecipe.result);
            pBuffer.writeInt(pRecipe.resultCount);
            pBuffer.writeItem(pRecipe.byproduct);
            pBuffer.writeInt(pRecipe.byproductMin);
            pBuffer.writeInt(pRecipe.byproductMax);
            pBuffer.writeInt(pRecipe.experience);
            pBuffer.writeBoolean(pRecipe.requiresDiamondGrindstone);
        }
    }
}