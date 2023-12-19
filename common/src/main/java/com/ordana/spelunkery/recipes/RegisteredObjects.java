package com.ordana.spelunkery.recipes;

import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

public final class RegisteredObjects {
  // registry argument for easier porting to 1.19
  @NotNull
  public static <V> ResourceLocation getKeyOrThrow(Registry<V> registry, V value) {
    ResourceLocation key = registry.getKey(value);
    if (key == null) {
      throw new IllegalArgumentException("Could not get key for value " + value + "!");
    }
    return key;
  }

  @NotNull
  public static ResourceLocation getKeyOrThrow(Block value) {
    return getKeyOrThrow(BuiltInRegistries.BLOCK, value);
  }

  @NotNull
  public static ResourceLocation getKeyOrThrow(Item value) {
    return getKeyOrThrow(BuiltInRegistries.ITEM, value);
  }

  @NotNull
  public static ResourceLocation getKeyOrThrow(Fluid value) {
    return getKeyOrThrow(BuiltInRegistries.FLUID, value);
  }

  @NotNull
  public static ResourceLocation getKeyOrThrow(EntityType<?> value) {
    return getKeyOrThrow(BuiltInRegistries.ENTITY_TYPE, value);
  }

  @NotNull
  public static ResourceLocation getKeyOrThrow(BlockEntityType<?> value) {
    return getKeyOrThrow(BuiltInRegistries.BLOCK_ENTITY_TYPE, value);
  }

  @NotNull
  public static ResourceLocation getKeyOrThrow(Potion value) {
    return getKeyOrThrow(BuiltInRegistries.POTION, value);
  }

  @NotNull
  public static ResourceLocation getKeyOrThrow(ParticleType<?> value) {
    return getKeyOrThrow(BuiltInRegistries.PARTICLE_TYPE, value);
  }

  @NotNull
  public static ResourceLocation getKeyOrThrow(RecipeSerializer<?> value) {
    return getKeyOrThrow(BuiltInRegistries.RECIPE_SERIALIZER, value);
  }
}