package com.ordana.spelunkery.reg;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.blocks.*;
import com.ordana.spelunkery.blocks.fungi.ConkMushroomBlock;
import com.ordana.spelunkery.blocks.fungi.MillyBubcapMushroomBlock;
import com.ordana.spelunkery.blocks.fungi.ModMushroomBlock;
import com.ordana.spelunkery.blocks.fungi.FloorAndSidesMushroomBlock;
import com.ordana.spelunkery.blocks.nephrite.*;
import com.ordana.spelunkery.blocks.rock_salt.*;
import net.mehvahdjukaar.moonlight.api.block.ModStairBlock;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.UniformInt;
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

import java.util.Objects;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import static com.ordana.spelunkery.reg.ModCreativeTab.getTab;

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

    private static ToIntFunction<BlockState> litBlockEmission(int lightValue) {
        return (blockState) -> (Boolean)blockState.getValue(BlockStateProperties.LIT) ? lightValue : 0;
    }

    public static <T extends Block> Supplier<T> regBlock(String name, Supplier<T> block) {
        return RegHelper.registerBlock(Spelunkery.res(name), block);
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
        return RegHelper.registerItem(Spelunkery.res(name), () -> new BlockItem(blockSup.get(), properties));
    }


    //rough gem blocks
    public static final Supplier<Block> CALCITE_REDSTONE_ORE = regWithItem("calcite_redstone_ore", () ->
            new RedStoneOreBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_ORE)
                    .requiresCorrectToolForDrops().strength(3f, 3f).sound(SoundType.CALCITE).lightLevel(litBlockEmission(9))));
    public static final Supplier<Block> ROUGH_CINNABR_BLOCK = regWithItem("rough_cinnabar_block", () ->
            new RoughCinnabarBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_RED)
                    .requiresCorrectToolForDrops().strength(5f, 6f).sound(SoundType.CALCITE).lightLevel(litBlockEmission(9))));

    public static final Supplier<Block> SANDSTONE_LAPIS_ORE = regWithItem("sandstone_lapis_ore", () ->
            new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.LAPIS_ORE)
                    .requiresCorrectToolForDrops().strength(2.5f, 3f), UniformInt.of(2, 5)));
    public static final Supplier<Block> ROUGH_LAZURITE_BLOCK = regWithItem("rough_lazurite_block", () ->
            new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.LAPIS)
                    .requiresCorrectToolForDrops().strength(5f, 6f).sound(SoundType.CALCITE)));

    public static final Supplier<Block> ANDESITE_EMERALD_ORE = regWithItem("andesite_emerald_ore", () ->
            new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.EMERALD_ORE)
                    .requiresCorrectToolForDrops().strength(3f, 3f), UniformInt.of(3, 7)));
    public static final Supplier<Block> ROUGH_EMERALD_BLOCK = regWithItem("rough_emerald_block", () ->
            new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.EMERALD)
                    .requiresCorrectToolForDrops().strength(5f, 6f).sound(SoundType.CALCITE)));

    public static final Supplier<Block> SMOOTH_BASALT_DIAMOND_ORE = regWithItem("smooth_basalt_diamond_ore", () ->
            new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_ORE)
                    .requiresCorrectToolForDrops().strength(3f, 3f).sound(SoundType.BASALT), UniformInt.of(3, 7)));
    public static final Supplier<Block> ROUGH_DIAMOND_BLOCK = regWithItem("rough_diamond_block", () ->
            new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.DIAMOND)
                    .requiresCorrectToolForDrops().strength(5f, 6f).sound(SoundType.CALCITE)));

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
            new RawNephriteBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.EMERALD).requiresCorrectToolForDrops().strength(3f, 2f)));
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
    public static final Supplier<Block> POLISHED_NEPHRITE_SLAB = regWithItem("polished_nephrite_slab", () ->
            new SlabBlock(BlockBehaviour.Properties.copy(NEPHRITE.get())));
    public static final Supplier<Block> POLISHED_NEPHRITE_STAIRS = regWithItem("polished_nephrite_stairs", () ->
            new ModStairBlock(NEPHRITE, BlockBehaviour.Properties.copy(NEPHRITE.get())));
    public static final Supplier<Block> POLISHED_NEPHRITE_WALL = regWithItem("polished_nephrite_wall", () ->
            new WallBlock(BlockBehaviour.Properties.copy(NEPHRITE.get())));
   
    public static final Supplier<Block> POLISHED_NEPHRITE_BRICKS = regWithItem("polished_nephrite_bricks", () ->
            new Block(BlockBehaviour.Properties.copy(NEPHRITE.get())));              
    public static final Supplier<Block> POLISHED_NEPHRITE_BRICK_SLAB = regWithItem("polished_nephrite_brick_slab", () ->
            new SlabBlock(BlockBehaviour.Properties.copy(NEPHRITE.get())));
    public static final Supplier<Block> POLISHED_NEPHRITE_BRICK_STAIRS = regWithItem("polished_nephrite_brick_stairs", () ->
            new ModStairBlock(NEPHRITE, BlockBehaviour.Properties.copy(NEPHRITE.get())));
    public static final Supplier<Block> POLISHED_NEPHRITE__BRICK_WALL = regWithItem("polished_nephrite_brick_wall", () ->
            new WallBlock(BlockBehaviour.Properties.copy(NEPHRITE.get())));

    public static final Supplier<Block> CARVED_NEPHRITE = regBlock("carved_nephrite", () ->
            new CarvedNephriteBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.EMERALD).requiresCorrectToolForDrops().strength(3f, 2f).emissiveRendering(ModBlocks::ifNotEmpty)));
    public static final Supplier<Block> NEPHRITE_SIPHON = regBlock("nephrite_siphon", () ->
            new NephriteSiphonBlock(BlockBehaviour.Properties.copy(NEPHRITE.get())));
    public static final Supplier<Block> NEPHRITE_SPOUT = regBlock("nephrite_spout", () ->
            new NephriteSpoutBlock(BlockBehaviour.Properties.copy(NEPHRITE.get())));
    public static final Supplier<Block> NEPHRITE_DIODE = regBlock("nephrite_diode", () ->
            new NephriteDiodeBlock(BlockBehaviour.Properties.copy(NEPHRITE.get())));


    //plants
    public static final Supplier<Block> TANGLE_ROOTS = regWithItem("tangle_roots", () ->
            new TangleRootsBlock(BlockBehaviour.Properties.of(Material.PLANT, MaterialColor.NETHER).randomTicks().noCollission().instabreak().sound(SoundType.WEEPING_VINES)), getTab(CreativeModeTab.TAB_DECORATIONS));
    public static final Supplier<Block> TANGLE_ROOTS_PLANT = regBlock("tangle_roots_plant", () ->
            new TangleRootsPlantBlock(BlockBehaviour.Properties.of(Material.PLANT, MaterialColor.NETHER).randomTicks().noCollission().instabreak().sound(SoundType.WEEPING_VINES)));
    public static final Supplier<Block> SPOROPHYTE = regWithItem("sporophyte", () ->
            new SporophyteBlock(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().instabreak().sound(SoundType.MOSS).offsetType(BlockBehaviour.OffsetType.XZ)), getTab(CreativeModeTab.TAB_DECORATIONS));
    public static final Supplier<Block> TALL_SPOROPHYTE = regWithItem("tall_sporophyte", () ->
            new DoublePlantBlock(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().instabreak().sound(SoundType.MOSS).offsetType(BlockBehaviour.OffsetType.XZ)), getTab(CreativeModeTab.TAB_DECORATIONS));



    //fungi
    public static final Supplier<Block> CONK_FUNGUS = regWithItem("conk_fungus", () ->
            new ConkMushroomBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.FUNGUS)), getTab(CreativeModeTab.TAB_DECORATIONS));
    public static final Supplier<Block> PORTABELLA = regBlock("portabella", () ->
            new ModMushroomBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.FUNGUS).offsetType(BlockBehaviour.OffsetType.XZ)));
    public static final Supplier<Block> CRIMINI = regBlock("crimini", () ->
            new ModMushroomBlock(BlockBehaviour.Properties.copy(PORTABELLA.get())));
    public static final Supplier<Block> BUTTON_MUSHROOM = regBlock("button_mushroom", () ->
            new ModMushroomBlock(BlockBehaviour.Properties.copy(PORTABELLA.get())));
    public static final Supplier<Block> INKCAP_MUSHROOM = regWithItem("inkcap_mushroom", () ->
            new ModMushroomBlock(BlockBehaviour.Properties.copy(PORTABELLA.get())), getTab(CreativeModeTab.TAB_DECORATIONS));
    public static final Supplier<Block> WHITE_INKCAP_MUSHROOM = regWithItem("white_inkcap_mushroom", () ->
            new ModMushroomBlock(BlockBehaviour.Properties.copy(PORTABELLA.get())), getTab(CreativeModeTab.TAB_DECORATIONS));
    public static final Supplier<Block> PHOSPHOR_FUNGUS = regWithItem("phosphor_fungus", () ->
            new FloorAndSidesMushroomBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.FUNGUS).emissiveRendering(ModBlocks::always).lightLevel((blockStatex) -> 1)), getTab(CreativeModeTab.TAB_DECORATIONS));
    public static final Supplier<Block> MILLY_BUBCAP = regWithItem("milly_bubcap", () ->
            new MillyBubcapMushroomBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.FUNGUS).offsetType(BlockBehaviour.OffsetType.XZ)), getTab(CreativeModeTab.TAB_DECORATIONS));


    //mining gear
    public static final Supplier<Block> GLOWSTICK = regBlock("glowstick", () ->
            new GlowstickBlock(BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noCollission().noOcclusion().emissiveRendering(ModBlocks::always).lightLevel((blockStatex) -> 14).sound(SoundType.CANDLE)));
    public static final Supplier<Block> ROPE_LADDER = regBlock("rope_ladder", () ->
            new RopeLadderBlock(BlockBehaviour.Properties.copy(Blocks.LADDER).strength(1f).sound(SoundType.WOOD)));
    public static final Supplier<Block> WOODEN_RAIL = regBlock("wooden_rail", () ->
            new WoodenRailBlock(BlockBehaviour.Properties.of(Material.DECORATION).noOcclusion().strength(0.7F).sound(SoundType.WOOD).instabreak()));

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
