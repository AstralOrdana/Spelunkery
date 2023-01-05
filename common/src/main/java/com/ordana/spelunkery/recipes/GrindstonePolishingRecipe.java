package com.ordana.spelunkery.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ordana.spelunkery.Spelunkery;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class GrindstonePolishingRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;

    public GrindstonePolishingRecipe(ResourceLocation id, ItemStack output,
                                    NonNullList<Ingredient> recipeItems) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
    }


    @Override
    public boolean matches(SimpleContainer container, Level level) {
        if(level.isClientSide()) return false;


        return recipeItems.get(0).test(container.getItem(1));
    }

    @Override
    public ItemStack assemble(SimpleContainer container) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<GrindstonePolishingRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "grindstone_polishing";
    }

    public static class Serializer implements RecipeSerializer<GrindstonePolishingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(Spelunkery.MOD_ID, "grindstone_polishing");

        @Override
        public GrindstonePolishingRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new GrindstonePolishingRecipe(pRecipeId, output, inputs);
        }

        @Override
        public @Nullable
        GrindstonePolishingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(buf));
            }

            ItemStack output = buf.readItem();
            return new GrindstonePolishingRecipe(id, output, inputs);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, GrindstonePolishingRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());
            //buf.writeFluidStack(recipe.fluidStack);

            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buf);
            }
            buf.writeItem(recipe.getResultItem());
        }
    }
}
