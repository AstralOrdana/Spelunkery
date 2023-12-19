package com.ordana.spelunkery.recipes;

/* Code below modified and adapted from Sully's Mod: https://github.com/Uraneptus/Sullys-Mod/
Specific section modified:https://github.com/Uraneptus/Sullys-Mod/blob/1.19.x/src/main/java/com/uraneptus/sullysmod/common/recipes/GrindstonePolishingRecipe.java
Significant changes include: addition of byproducts, diamond grinding tier.
Used under GNU LESSER GENERAL PUBLIC LICENSE, full text can be found in root/LICENSE
 */

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.reg.ModRecipes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
@MethodsReturnNonnullByDefault
public class GrindstonePolishingRecipe implements Recipe<Container> {
    public static final String NAME = "grindstone_polishing";

    protected ResourceLocation id;
    protected NonNullList<Ingredient> ingredients;
    protected NonNullList<ProcessingOutput> results;
    protected int experience;
    protected boolean diamondGrindstone;

    public GrindstonePolishingRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, NonNullList<ProcessingOutput> results, int experience, boolean diamondGrindstone) {
        this.id = id;
        this.ingredients = ingredients;
        this.results = results;
        this.experience = experience;
        this.diamondGrindstone = diamondGrindstone;
    }


/*
    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        return true;
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return this.result;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    public int getResultCount() {
        return this.resultCount;
    }


    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
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



 */

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.GRINDSTONE_POLISHING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.GRINDSTONE_POLISHING.get();
    }

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        return true;
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return getResultItem(registryAccess);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return getRollableResults().isEmpty() ? ItemStack.EMPTY
            : getRollableResults().get(0)
            .getStack();
    }

    public int getExperience() {
        return this.experience;
    }
    public boolean needsDiamond() {
        return this.diamondGrindstone;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    // Processing recipes do not show up in the recipe book
    @Override
    public String getGroup() {
        return "grindstone_polishing";
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    public static boolean canPolish(Level world) {
        return !getRecipes(world).isEmpty();
    }

    public static List<GrindstonePolishingRecipe> getRecipes(Level level) {
        return level.getRecipeManager().getAllRecipesFor(ModRecipes.GRINDSTONE_POLISHING.get());
    }

    protected int getMaxInputCount() {
        return 1;
    }

    protected int getMaxOutputCount() {
        return 4;
    }

    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<ProcessingOutput> getRollableResults() {
        return results;
    }

    public List<ItemStack> getRollableResultsAsItemStacks() {
        return getRollableResults().stream()
            .map(ProcessingOutput::getStack)
            .collect(Collectors.toList());
    }
    public List<ItemStack> rollResults() {
        return rollResults(this.getRollableResults());
    }

    public List<ItemStack> rollResults(List<ProcessingOutput> rollableResults) {
        List<ItemStack> results = new ArrayList<>();
        for (ProcessingOutput output : rollableResults) {
            ItemStack stack = output.rollOutput();
            if (!stack.isEmpty())
                results.add(stack);
        }
        return results;
    }

    private void validate(ResourceLocation recipeTypeId) {
        String messageHeader = "Your custom " + recipeTypeId + " recipe (" + id.toString() + ")";
        Logger logger = Spelunkery.LOGGER;
        int ingredientCount = ingredients.size();
        int outputCount = results.size();

        if (ingredientCount > getMaxInputCount())
            logger.warn(messageHeader + " has more item inputs (" + ingredientCount + ") than supported ("
                + getMaxInputCount() + ").");

        if (outputCount > getMaxOutputCount())
            logger.warn(messageHeader + " has more item outputs (" + outputCount + ") than supported ("
                + getMaxOutputCount() + ").");

    }




    public static class Serializer implements RecipeSerializer<GrindstonePolishingRecipe> {

        protected void writeToJson(JsonObject json, GrindstonePolishingRecipe recipe) {
            JsonArray jsonIngredients = new JsonArray();
            JsonArray jsonOutputs = new JsonArray();

            recipe.ingredients.forEach(i -> jsonIngredients.add(i.toJson()));

            recipe.results.forEach(o -> jsonOutputs.add(o.serialize()));

            json.add("ingredients", jsonIngredients);
            json.add("results", jsonOutputs);

            //recipe.writeAdditional(json);
        }

        protected void writeToBuffer(FriendlyByteBuf buffer, GrindstonePolishingRecipe recipe) {

            //recipe.writeAdditional(buffer);
        }


        public final void write(JsonObject json, GrindstonePolishingRecipe recipe) {
            writeToJson(json, recipe);
        }

        @Override
        public final GrindstonePolishingRecipe fromJson(ResourceLocation id, JsonObject json) {

            NonNullList<Ingredient> ingredients = NonNullList.create();
            NonNullList<ProcessingOutput> results = NonNullList.create();

            for (JsonElement je : GsonHelper.getAsJsonArray(json, "ingredients")) {
                ingredients.add(Ingredient.fromJson(je));
            }

            for (JsonElement je : GsonHelper.getAsJsonArray(json, "results")) {
                results.add(ProcessingOutput.deserialize(je));
            }

            int experience = GsonHelper.getAsInt(json, "experience", 0);
            boolean requiresDiamondGrindstone = GsonHelper.getAsBoolean(json, "requiresDiamondGrindstone", false);

            return new GrindstonePolishingRecipe(id, ingredients, results, experience, requiresDiamondGrindstone);
        }

        @Override
        public final GrindstonePolishingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            NonNullList<Ingredient> ingredients = NonNullList.create();
            NonNullList<ProcessingOutput> results = NonNullList.create();

            int size = buffer.readVarInt();
            for (int i = 0; i < size; i++)
                ingredients.add(Ingredient.fromNetwork(buffer));

            size = buffer.readVarInt();
            for (int i = 0; i < size; i++)
                results.add(ProcessingOutput.read(buffer));

            int experience = buffer.readInt();
            boolean requiresDiamondGrindstone = buffer.readBoolean();

            return new GrindstonePolishingRecipe(id, ingredients, results, experience, requiresDiamondGrindstone);
        }

        @Override
        public final void toNetwork(FriendlyByteBuf buffer, GrindstonePolishingRecipe recipe) {

            NonNullList<Ingredient> ingredients = recipe.ingredients;
            NonNullList<ProcessingOutput> outputs = recipe.results;

            buffer.writeVarInt(ingredients.size());
            ingredients.forEach(i -> i.toNetwork(buffer));

            buffer.writeVarInt(outputs.size());
            outputs.forEach(o -> o.write(buffer));

            buffer.writeInt(recipe.experience);
            buffer.writeBoolean(recipe.diamondGrindstone);
        }








        /*
        @Override
        public GrindstonePolishingRecipe fromJson(ResourceLocation pRecipeId, JsonObject jsonObject) {

            if (!jsonObject.has("ingredient")) throw new com.google.gson.JsonSyntaxException("Missing ingredient, expected to find a string or object");
            ItemStack ingredient;
            if (jsonObject.get("ingredient").isJsonObject()) {
                ingredient = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "ingredient"));
            }
            else {
                String ingredientItem = GsonHelper.getAsString(jsonObject, "ingredient");
                ResourceLocation resourcelocation = new ResourceLocation(ingredientItem);
                ingredient = new ItemStack(BuiltInRegistries.ITEM.getOptional(resourcelocation).orElseThrow(() -> new IllegalStateException("Item: " + ingredientItem + " does not exist")));
            }
            if (!jsonObject.has("result")) throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
            ItemStack result;
            if (jsonObject.get("result").isJsonObject()) {
                result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
            }
            else {
                String resultItem = GsonHelper.getAsString(jsonObject, "result");
                ResourceLocation resourcelocation = new ResourceLocation(resultItem);
                result = new ItemStack(BuiltInRegistries.ITEM.getOptional(resourcelocation).orElseThrow(() -> new IllegalStateException("Item: " + resultItem + " does not exist")));
            }
            int resultCount = GsonHelper.getAsInt(jsonObject, "resultCount", 1);

            ItemStack byproduct;
            if (jsonObject.get("byproduct").isJsonObject()) {
                byproduct = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "byproduct"));
            }
            else {
                String byproductItem = GsonHelper.getAsString(jsonObject, "byproduct");
                ResourceLocation resourcelocation = new ResourceLocation(byproductItem);
                byproduct = new ItemStack(BuiltInRegistries.ITEM.getOptional(resourcelocation).orElseThrow(() -> new IllegalStateException("Item: " + byproductItem + " does not exist")));
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

         */
    }
}