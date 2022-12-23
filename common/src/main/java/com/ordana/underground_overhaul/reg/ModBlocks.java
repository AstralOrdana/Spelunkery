package com.ordana.underground_overhaul.reg;

import com.ordana.underground_overhaul.UndergroundOverhaul;
import com.ordana.underground_overhaul.blocks.GlowstickBlock;
import com.ordana.underground_overhaul.blocks.RopeLadderBlock;
import com.ordana.underground_overhaul.blocks.nephrite.CarvedNephriteBlock;
import com.ordana.underground_overhaul.blocks.nephrite.NephriteDiodeBlock;
import com.ordana.underground_overhaul.blocks.nephrite.NephriteSiphonBlock;
import com.ordana.underground_overhaul.blocks.nephrite.NephriteSpoutBlock;
import com.ordana.underground_overhaul.blocks.rock_salt.*;
import net.mehvahdjukaar.moonlight.api.block.ModStairBlock;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import java.util.Enumeration;
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

    private static boolean ifNotEmpty(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return state.getValue(CarvedNephriteBlock.CHARGE) != CarvedNephriteBlock.ChargeState.EMPTY;
    }

        private static ToIntFunction<BlockState> createLightLevelFromIlluminatedBlockState(int litLevel) {
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
    public static final Supplier<Block> ROUGH_LAZURITE_BLOCK = regWithItem("rough_lazurite_block", () ->
            new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.LAPIS)
                    .requiresCorrectToolForDrops().strength(5f, 6f)));
    public static final Supplier<Block> ROUGH_EMERALD_BLOCK = regWithItem("rough_emerald_block", () ->
            new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.EMERALD)
                    .requiresCorrectToolForDrops().strength(5f, 6f)));
    public static final Supplier<Block> ROUGH_DIAMOND_BLOCK = regWithItem("rough_diamond_block", () ->
            new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.DIAMOND)
                    .requiresCorrectToolForDrops().strength(5f, 6f)));

    //rock salt
    public static final Supplier<Block> ROCK_SALT = regWithItem("rock_salt", () ->
            new RockSaltCrystalBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_PINK)
                    .requiresCorrectToolForDrops().strength(3f, 2f).sound(SoundType.CALCITE).lightLevel(createLightLevelFromIlluminatedBlockState(1)).emissiveRendering(ModBlocks::ifIlluminated).noOcclusion()));
    public static final Supplier<Block> SALT_LAMP = regWithItem("salt_lamp", () ->
            new SaltLampBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_PINK)
                    .strength(0.5f, 2f).sound(SoundType.CALCITE).lightLevel(createLightLevelFromIlluminatedBlockState(7)).emissiveRendering(ModBlocks::ifIlluminated).noOcclusion()));
    public static final Supplier<Block> SALT = regWithItem("salt", () ->
            new SaltBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_PINK).instabreak()));

    public static final Supplier<Block> ROCK_SALT_BLOCK = regWithItem("rock_salt_block", () ->
            new RockSaltBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_PINK)
                    .requiresCorrectToolForDrops().strength(3f, 2f).sound(SoundType.CALCITE).lightLevel(createLightLevelFromIlluminatedBlockState(1)).emissiveRendering(ModBlocks::ifIlluminated)));
    public static final Supplier<Block> ROCK_SALT_SLAB = regWithItem("rock_salt_slab", () ->
            new RockSaltSlab(BlockBehaviour.Properties.copy(ROCK_SALT_BLOCK.get())));
    public static final Supplier<Block> ROCK_SALT_STAIRS = regWithItem("rock_salt_stairs", () ->
            new RockSaltStairs(ROCK_SALT_BLOCK.get().defaultBlockState(), BlockBehaviour.Properties.copy(ROCK_SALT_BLOCK.get())));
    public static final Supplier<Block> ROCK_SALT_WALL = regWithItem("rock_salt_wall", () ->
            new RockSaltWall(BlockBehaviour.Properties.copy(ROCK_SALT_BLOCK.get())));

    public static final Supplier<Block> POLISHED_ROCK_SALT = regWithItem("polished_rock_salt", () ->
            new RockSaltBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_PINK)
                    .requiresCorrectToolForDrops().strength(3f, 2f).sound(SoundType.CALCITE).lightLevel(createLightLevelFromIlluminatedBlockState(1)).emissiveRendering(ModBlocks::ifIlluminated)));
    public static final Supplier<Block> POLISHED_ROCK_SALT_SLAB = regWithItem("polished_rock_salt_slab", () ->
            new RockSaltSlab(BlockBehaviour.Properties.copy(POLISHED_ROCK_SALT.get())));
    public static final Supplier<Block> POLISHED_ROCK_SALT_STAIRS = regWithItem("polished_rock_salt_stairs", () ->
            new RockSaltStairs(POLISHED_ROCK_SALT.get().defaultBlockState(), BlockBehaviour.Properties.copy(POLISHED_ROCK_SALT.get())));
    public static final Supplier<Block> POLISHED_ROCK_SALT_WALL = regWithItem("polished_rock_salt_wall", () ->
            new RockSaltWall(BlockBehaviour.Properties.copy(POLISHED_ROCK_SALT.get())));

    public static final Supplier<Block> ROCK_SALT_BRICKS = regWithItem("rock_salt_bricks", () ->
            new RockSaltBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_PINK)
                    .requiresCorrectToolForDrops().strength(3f, 2f).sound(SoundType.CALCITE).lightLevel(createLightLevelFromIlluminatedBlockState(1)).emissiveRendering(ModBlocks::ifIlluminated)));
    public static final Supplier<Block> ROCK_SALT_BRICK_SLAB = regWithItem("rock_salt_brick_slab", () ->
            new RockSaltSlab(BlockBehaviour.Properties.copy(ROCK_SALT_BRICKS.get())));
    public static final Supplier<Block> ROCK_SALT_BRICK_STAIRS = regWithItem("rock_salt_brick_stairs", () ->
            new RockSaltStairs(ROCK_SALT_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(ROCK_SALT_BRICKS.get())));
    public static final Supplier<Block> ROCK_SALT_BRICK_WALL = regWithItem("rock_salt_brick_wall", () ->
            new RockSaltWall(BlockBehaviour.Properties.copy(ROCK_SALT_BRICKS.get())));



    //nephrite
    public static final Supplier<Block> RAW_NEPHRITE = regWithItem("raw_nephrite", () ->
            new HugeMushroomBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.EMERALD).requiresCorrectToolForDrops().strength(3f, 2f)));
    public static final Supplier<Block> NEPHRITE = regWithItem("nephrite", () ->
            new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.EMERALD).requiresCorrectToolForDrops().strength(3f, 2f)));
    public static final Supplier<Block> NEPHRITE_SLAB = regWithItem("nephrite_slab", () ->
            new SlabBlock(BlockBehaviour.Properties.copy(NEPHRITE.get())));
    public static final Supplier<Block> NEPHRITE_STAIRS = regWithItem("nephrite_stairs", () ->
            new ModStairBlock(NEPHRITE, BlockBehaviour.Properties.copy(NEPHRITE.get())));
    public static final Supplier<Block> NEPHRITE_WALL = regWithItem("nephrite_wall", () ->
            new WallBlock(BlockBehaviour.Properties.copy(NEPHRITE.get())));
    public static final Supplier<Block> POLISHED_NEPHRITE = regWithItem("polished_nephrite", () ->
            new Block(BlockBehaviour.Properties.copy(NEPHRITE.get())));


    public static final Supplier<Block> CARVED_NEPHRITE = regBlock("carved_nephrite", () ->
            new CarvedNephriteBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.EMERALD).requiresCorrectToolForDrops().strength(3f, 2f).emissiveRendering(ModBlocks::ifNotEmpty)));
    public static final Supplier<Block> NEPHRITE_SIPHON = regBlock("nephrite_siphon", () ->
            new NephriteSiphonBlock(BlockBehaviour.Properties.copy(NEPHRITE.get())));
    public static final Supplier<Block> NEPHRITE_SPOUT = regBlock("nephrite_spout", () ->
            new NephriteSpoutBlock(BlockBehaviour.Properties.copy(NEPHRITE.get())));
    public static final Supplier<Block> NEPHRITE_DIODE = regBlock("nephrite_diode", () ->
            new NephriteDiodeBlock(BlockBehaviour.Properties.copy(NEPHRITE.get())));


    //mining gear
    public static final Supplier<Block> GLOWSTICK = regBlock("glowstick", () ->
            new GlowstickBlock(BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noCollission().noOcclusion().emissiveRendering(ModBlocks::always).lightLevel((blockStatex) -> 14).sound(SoundType.CANDLE)));
    public static final Supplier<Block> ROPE_LADDER = regBlock("rope_ladder", () ->
            new RopeLadderBlock(BlockBehaviour.Properties.copy(Blocks.LADDER).strength(1f).sound(SoundType.WOOD)));

    //glowsticks
    public static final Supplier<Block> RED_GLOWSTICK = regBlock("red_glowstick", () ->
            new GlowstickBlock(BlockBehaviour.Properties.copy(GLOWSTICK.get())));
    public static final Supplier<Block> ORANGE_GLOWSTICK = regBlock("orange_glowstick", () ->
            new GlowstickBlock(BlockBehaviour.Properties.copy(GLOWSTICK.get())));
    public static final Supplier<Block> YELLOW_GLOWSTICK = regBlock("yellow_glowstick", () ->
            new GlowstickBlock(BlockBehaviour.Properties.copy(GLOWSTICK.get())));
    public static final Supplier<Block> LIME_GLOWSTICK = regBlock("lime_glowstick", () ->
            new GlowstickBlock(BlockBehaviour.Properties.copy(GLOWSTICK.get())));
    public static final Supplier<Block> GREEN_GLOWSTICK = regBlock("green_glowstick", () ->
            new GlowstickBlock(BlockBehaviour.Properties.copy(GLOWSTICK.get())));
    public static final Supplier<Block> CYAN_GLOWSTICK = regBlock("cyan_glowstick", () ->
            new GlowstickBlock(BlockBehaviour.Properties.copy(GLOWSTICK.get())));
    public static final Supplier<Block> LIGHT_BLUE_GLOWSTICK = regBlock("light_blue_glowstick", () ->
            new GlowstickBlock(BlockBehaviour.Properties.copy(GLOWSTICK.get())));
    public static final Supplier<Block> BLUE_GLOWSTICK = regBlock("blue_glowstick", () ->
            new GlowstickBlock(BlockBehaviour.Properties.copy(GLOWSTICK.get())));
    public static final Supplier<Block> PURPLE_GLOWSTICK = regBlock("purple_glowstick", () ->
            new GlowstickBlock(BlockBehaviour.Properties.copy(GLOWSTICK.get())));
    public static final Supplier<Block> MAGENTA_GLOWSTICK = regBlock("magenta_glowstick", () ->
            new GlowstickBlock(BlockBehaviour.Properties.copy(GLOWSTICK.get())));
    public static final Supplier<Block> PINK_GLOWSTICK = regBlock("pink_glowstick", () ->
            new GlowstickBlock(BlockBehaviour.Properties.copy(GLOWSTICK.get())));
    public static final Supplier<Block> BROWN_GLOWSTICK = regBlock("brown_glowstick", () ->
            new GlowstickBlock(BlockBehaviour.Properties.copy(GLOWSTICK.get())));
    public static final Supplier<Block> BLACK_GLOWSTICK = regBlock("black_glowstick", () ->
            new GlowstickBlock(BlockBehaviour.Properties.copy(GLOWSTICK.get())));
    public static final Supplier<Block> WHITE_GLOWSTICK = regBlock("white_glowstick", () ->
            new GlowstickBlock(BlockBehaviour.Properties.copy(GLOWSTICK.get())));
    public static final Supplier<Block> GRAY_GLOWSTICK = regBlock("gray_glowstick", () ->
            new GlowstickBlock(BlockBehaviour.Properties.copy(GLOWSTICK.get())));
    public static final Supplier<Block> LIGHT_GRAY_GLOWSTICK = regBlock("light_gray_glowstick", () ->
            new GlowstickBlock(BlockBehaviour.Properties.copy(GLOWSTICK.get())));


}
