package com.ordana.spelunkery.forge;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/** License: LGPL-2.1-only | Original: <a href="https://github.com/neoforged/NeoForge/pull/65">NeoForge</a> */
public class ModBiomeModifier {
    private static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, "spelunkery");

    /**
     * Stock biome modifier for adding carvers to biomes.
     */
    public static final RegistryObject<Codec<AddCarversBiomeModifier>> ADD_CARVERS_BIOME_MODIFIER_TYPE = BIOME_MODIFIER_SERIALIZERS.register("add_carvers", () ->
            RecordCodecBuilder.create(builder -> builder.group(
                    Biome.LIST_CODEC.fieldOf("biomes").forGetter(AddCarversBiomeModifier::biomes),
                    ConfiguredWorldCarver.LIST_CODEC.fieldOf("carvers").forGetter(AddCarversBiomeModifier::carvers),
                    GenerationStep.Carving.CODEC.fieldOf("step").forGetter(AddCarversBiomeModifier::step)
            ).apply(builder, AddCarversBiomeModifier::new))
    );

    public record AddCarversBiomeModifier(HolderSet<Biome> biomes, HolderSet<ConfiguredWorldCarver<?>> carvers, GenerationStep.Carving step) implements BiomeModifier
    {
        @Override
        public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder)
        {
            if (phase == Phase.ADD && this.biomes.contains(biome))
            {
                BiomeGenerationSettingsBuilder generationSettings = builder.getGenerationSettings();
                this.carvers.forEach(holder -> generationSettings.addCarver(this.step, holder));
            }
        }

        @Override
        public Codec<? extends BiomeModifier> codec()
        {
            return ADD_CARVERS_BIOME_MODIFIER_TYPE.get();
        }
    }

    public static void register(final IEventBus eventBus) {
        BIOME_MODIFIER_SERIALIZERS.register(eventBus);
    }
}
