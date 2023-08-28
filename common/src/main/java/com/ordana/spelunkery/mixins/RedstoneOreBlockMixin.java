package com.ordana.spelunkery.mixins;

import com.ordana.spelunkery.configs.CommonConfigs;
import com.ordana.spelunkery.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.RedStoneOreBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RedStoneOreBlock.class)
public class RedstoneOreBlockMixin extends Block {

    public RedstoneOreBlockMixin(Properties properties) {
        super(properties);
    }


    @Inject(method = "spawnAfterBreak", at = @At("HEAD"), cancellable = true)
    private void preventOreDrops(BlockState state, ServerLevel level, BlockPos pos, ItemStack stack, boolean dropExperience, CallbackInfo ci) {
        if (state.is(ModTags.XP_DROP_DISABLED) && CommonConfigs.ENABLE_ROUGH_GEMS.get()) ci.cancel();
    }
}
