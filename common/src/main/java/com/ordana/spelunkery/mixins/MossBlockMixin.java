package com.ordana.spelunkery.mixins;

import com.ordana.spelunkery.reg.ModFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MossBlock;
import net.minecraft.world.level.block.state.BlockState;
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
        ModFeatures.SPORE_MOSS_PATCH_BONEMEAL.get().place(level, level.getChunkSource().getGenerator(), random, pos.above());
        ci.cancel();
    }
}
