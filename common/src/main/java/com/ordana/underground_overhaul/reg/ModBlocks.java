package com.ordana.underground_overhaul.reg;

import com.ordana.underground_overhaul.UndergroundOverhaul;
import com.ordana.underground_overhaul.blocks.rock_salt.RockSaltBlock;
import com.ordana.underground_overhaul.blocks.rock_salt.RockSaltSlab;
import com.ordana.underground_overhaul.blocks.rock_salt.RockSaltStairs;
import com.ordana.underground_overhaul.blocks.rock_salt.RockSaltWall;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.mehvahdjukaar.moonlight.api.set.BlockSetAPI;
import net.mehvahdjukaar.moonlight.api.set.leaves.LeavesType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import static com.ordana.underground_overhaul.reg.ModCreativeTab.getTab;

public class ModBlocks {

    public static void init() {
    }

    private static boolean always(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return true;
    }

    private static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return false;
    }

    private static boolean ifIlluminated(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return state.getValue(ModBlockProperties.ILLUMINATED);
    }

    private static ToIntFunction<BlockState> createLightLevelFromSmolderingBlockState(int litLevel) {
        return (state) -> (Boolean)state.getValue(ModBlockProperties.ILLUMINATED) ? litLevel : 0;
    }

    public static <T extends Block> Supplier<T> regBlock(String name, Supplier<T> block) {
        return RegHelper.registerBlock(UndergroundOverhaul.res(name), block);
    }

    public static <T extends Block> Supplier<T> regWithItem(String name, Supplier<T> block) {
        return regWithItem(name, block, getTab(CreativeModeTab.TAB_BUILDING_BLOCKS));
    }

    public static <T extends Block> Supplier<T> regWithItem(String name, Supplier<T> blockFactory, CreativeModeTab tab) {
        Supplier<T> block = regBlock(name, blockFactory);
        regBlockItem(name, block, new Item.Properties().tab(tab));
        return block;
    }

    public static <T extends Block> Supplier<T> regWithItem(String name, Supplier<T> block, String requiredMod) {
        CreativeModeTab tab = isCompatBlockEanbled(requiredMod) ? getTab(CreativeModeTab.TAB_BUILDING_BLOCKS) : null;
        return regWithItem(name, block, tab);
    }

    private static boolean isCompatBlockEanbled(String requiredMod) {
        if(Objects.equals(requiredMod, "quark")) {
            if (PlatformHelper.getPlatform().isFabric()) {
                return requiredMod.equals("amogus");
            } else {
                return false;
                //return IntegrationHandler.quark && QuarkPlugin.isVerticalSlabsOn();
            }
        }
        return PlatformHelper.isModLoaded(requiredMod);
    }


    public static Supplier<BlockItem> regBlockItem(String name, Supplier<? extends Block> blockSup, Item.Properties properties) {
        return RegHelper.registerItem(UndergroundOverhaul.res(name), () -> new BlockItem(blockSup.get(), properties));
    }


    //rough gem blocks
    public static final Supplier<Block> ROUGH_CINNABR_BLOCK = regWithItem("rough_cinnabar_block", () ->
            new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_RED)
                    .requiresCorrectToolForDrops().strength(5f, 6f)));
    public static final Supplier<Block> ROUGH_LAPIS_BLOCK = regWithItem("rough_lapis_block", () ->
            new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.LAPIS)
                    .requiresCorrectToolForDrops().strength(5f, 6f)));
    public static final Supplier<Block> ROUGH_EMERALD_BLOCK = regWithItem("rough_emerald_block", () ->
            new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.EMERALD)
                    .requiresCorrectToolForDrops().strength(5f, 6f)));
    public static final Supplier<Block> ROUGH_DIAMOND_BLOCK = regWithItem("rough_diamond_block", () ->
            new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.DIAMOND)
                    .requiresCorrectToolForDrops().strength(5f, 6f)));

    //rock salt
    public static final Supplier<Block> ROCK_SALT_BLOCK = regWithItem("rock_salt_block", () ->
            new RockSaltBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_PINK)
                    .requiresCorrectToolForDrops().strength(3f, 2f).sound(SoundType.CALCITE).lightLevel(createLightLevelFromSmolderingBlockState(1)).emissiveRendering(ModBlocks::ifIlluminated)));
    public static final Supplier<Block> ROCK_SALT_SLAB = regWithItem("rock_salt_slab", () ->
            new RockSaltSlab(BlockBehaviour.Properties.copy(ROCK_SALT_BLOCK.get())));
    public static final Supplier<Block> ROCK_SALT_STAIRS = regWithItem("rock_salt_stairs", () ->
            new RockSaltStairs(ROCK_SALT_BLOCK.get().defaultBlockState(), BlockBehaviour.Properties.copy(ROCK_SALT_BLOCK.get())));
    /*
    public static final Supplier<Block> ROCK_SALT_WALL = regWithItem("rock_salt_wall", () ->
            new RockSaltWall(BlockBehaviour.Properties.copy(ROCK_SALT_BLOCK.get())));
     */

    public static final Supplier<Block> ROCK_SALT_BRICKS = regWithItem("rock_salt_bricks", () ->
            new RockSaltBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_PINK)
                    .requiresCorrectToolForDrops().strength(3f, 2f).sound(SoundType.CALCITE).lightLevel(createLightLevelFromSmolderingBlockState(1)).emissiveRendering(ModBlocks::ifIlluminated)));
    public static final Supplier<Block> ROCK_SALT_BRICK_SLAB = regWithItem("rock_salt_brick_slab", () ->
            new RockSaltSlab(BlockBehaviour.Properties.copy(ROCK_SALT_BRICKS.get()).lightLevel(createLightLevelFromSmolderingBlockState(1)).emissiveRendering(ModBlocks::ifIlluminated)));
    public static final Supplier<Block> ROCK_SALT_BRICK_STAIRS = regWithItem("rock_salt_brick_stairs", () ->
            new RockSaltStairs(ROCK_SALT_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(ROCK_SALT_BRICKS.get()).lightLevel(createLightLevelFromSmolderingBlockState(1)).emissiveRendering(ModBlocks::ifIlluminated)));
    /*
    public static final Supplier<Block> ROCK_SALT_BRICK_WALL = regWithItem("rock_salt_brick_wall", () ->
            new RockSaltWall(BlockBehaviour.Properties.copy(ROCK_SALT_BRICKS.get()).lightLevel(createLightLevelFromSmolderingBlockState(1)).emissiveRendering(ModBlocks::ifIlluminated)));
     */


}
