package com.ordana.spelunkery.mixins;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.configs.CommonConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.CaveFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MossBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MossBlock.class)
public class MossBlockMixin extends Block {

    public MossBlockMixin(Properties properties) {
        super(properties);
    }


    @Inject(method = "performBonemeal", at = @At("HEAD"), cancellable = true)
    private void sporophyteBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state, CallbackInfo ci) {
        this.growMushroom(level, pos, state, random);
    }

    public boolean growMushroom(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        if (((level.registryAccess().registry(Registries.CONFIGURED_FEATURE).get().getHolder(
                ResourceKey.create(Registries.CONFIGURED_FEATURE, Spelunkery.res("spore_patch_bonemeal"))).get())
                .value()).place(level, level.getChunkSource().getGenerator(), random, pos)) {
            return true;
        } else {
            level.setBlock(pos, state, 3);
            return false;
        }
    }
}
