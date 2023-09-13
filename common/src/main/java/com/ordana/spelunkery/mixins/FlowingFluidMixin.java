package com.ordana.spelunkery.mixins;

import com.mojang.datafixers.util.Pair;
import com.ordana.spelunkery.blocks.WoodenChannelBlock;
import it.unimi.dsi.fastutil.shorts.Short2BooleanMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.Map;

@Mixin(FlowingFluid.class)
abstract class FlowingFluidMixin {

    @Unique
    private static Direction dir;


    @ModifyVariable(method = "getSpread", at = @At("STORE"), ordinal = 0)
    private boolean modifyFlag(boolean flag, LevelReader level, BlockPos pos, BlockState state) {

        var channelState = level.getBlockState(pos.below());
        if (channelState.getBlock() instanceof WoodenChannelBlock && dir != Direction.UP && dir != Direction.DOWN) {
            if (channelState.getValue(WoodenChannelBlock.PROPERTY_BY_DIRECTION.get(dir))) return false;
        }
        return flag;
    }

    @Inject(method = "getSpread", at = @At(value = "INVOKE",
            target = "Lit/unimi/dsi/fastutil/shorts/Short2BooleanMap;computeIfAbsent(SLit/unimi/dsi/fastutil/shorts/Short2BooleanFunction;)Z"),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void getDirection(LevelReader level, BlockPos pos, BlockState state, CallbackInfoReturnable<Map<Direction, FluidState>> cir, int i, Map map, Short2ObjectMap short2ObjectMap, Short2BooleanMap short2BooleanMap, Iterator var8, Direction direction, BlockPos blockPos, short s, Pair pair, BlockState blockState, FluidState fluidState, FluidState fluidState2, BlockPos blockPos2) {
        dir = direction;
    }

    @Redirect(method = "getSpread",
            at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
    private <K, V> V addChannelException(Map<K, V> instance, K direction, V fluidState, LevelReader level, BlockPos pos, BlockState blockState) {

        var channelState = level.getBlockState(pos.below());
        if (channelState.getBlock() instanceof WoodenChannelBlock && direction != Direction.DOWN && direction != Direction.UP) {
            if (channelState.getValue(WoodenChannelBlock.PROPERTY_BY_DIRECTION.get(direction))) return null;
        }
        return instance.put(direction, fluidState);

    }
}

