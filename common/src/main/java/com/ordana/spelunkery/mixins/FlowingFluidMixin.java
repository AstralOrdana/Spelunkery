package com.ordana.spelunkery.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.datafixers.util.Pair;
import com.ordana.spelunkery.blocks.WoodenChannelBlock;
import com.ordana.spelunkery.reg.ModBlocks;
import it.unimi.dsi.fastutil.shorts.Short2BooleanMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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
    private boolean injected(boolean flag, Level level, BlockPos pos, BlockState state) {

        var channelState = level.getBlockState(pos.below());
        if (channelState.is(ModBlocks.OAK_CHANNEL.get()) && dir != Direction.UP) {
            if (channelState.getValue(WoodenChannelBlock.HOLE) && dir == Direction.DOWN) return true;
            if (channelState.getValue(WoodenChannelBlock.PROPERTY_BY_DIRECTION.get(dir))) return false;
        }
        return flag;
    }

    @Inject(method = "getSpread", at = @At(value = "INVOKE",
            target = "Lit/unimi/dsi/fastutil/shorts/Short2BooleanMap;computeIfAbsent(SLit/unimi/dsi/fastutil/shorts/Short2BooleanFunction;)Z"),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void addChannel(Level level, BlockPos pos, BlockState state, CallbackInfoReturnable<Map<Direction, FluidState>> cir, int i, Map map, Short2ObjectMap short2ObjectMap, Short2BooleanMap short2BooleanMap, Iterator var8, Direction direction, BlockPos blockPos, short s, Pair pair, BlockState blockState, FluidState fluidState, FluidState fluidState2, BlockPos blockPos2) {
        dir = direction;
    }

    @Redirect(method = "getSpread",
            at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
    private <K, V> V chnnelException(Map<K, V> instance, K direction, V fluidState, Level level, BlockPos pos, BlockState blockState) {

        var channelState = level.getBlockState(pos.below());
        if (channelState.is(ModBlocks.OAK_CHANNEL.get()) && direction != Direction.DOWN && direction != Direction.UP) {
            if (channelState.getValue(WoodenChannelBlock.PROPERTY_BY_DIRECTION.get(direction))) return null;
        }
        return instance.put(direction, fluidState);

    }


    /*

    @ModifyExpressionValue(method = "getSpread", at = @At(value = "INVOKE",
            target = "Ljava/util/Iterator;next()Ljava/lang/Object;"))
    private boolean addChannelCheck(boolean original) {

    }



    @Inject(method = "isWaterHole", at = @At("HEAD"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    private void addChannel(BlockGetter level, Fluid fluid, BlockPos pos, BlockState state, BlockPos spreadPos, BlockState spreadState, CallbackInfoReturnable<Boolean> cir) {
        float deltaX = spreadPos.getX() - pos.getX();
        float deltaZ = spreadPos.getZ() - pos.getZ();
        if (deltaX != 0 && deltaZ != 0) {

            var channelState = level.getBlockState(pos.below());
            var direction = getDirection(pos, spreadPos);
            if (channelState.is(ModBlocks.OAK_CHANNEL.get()) && direction != Direction.DOWN && direction != Direction.UP) {
                if (channelState.getValue(WoodenChannelBlock.PROPERTY_BY_DIRECTION.get(direction))) cir.setReturnValue(false);
            }
        }
    }


    public Direction getDirection(BlockPos origin, BlockPos destination) {
        float deltaX = destination.getX() - origin.getX();
        float deltaZ = destination.getZ() - origin.getZ();

        if (deltaZ > 0) return Direction.SOUTH;
        if (deltaZ < 0) return Direction.NORTH;
        if (deltaX > 0) return Direction.EAST;
        if (deltaX < 0) return Direction.WEST;

        else return null;
    }


    @Inject(method = "getSpread", at = @At(value = "INVOKE",
            target = "Lit/unimi/dsi/fastutil/shorts/Short2BooleanMap;computeIfAbsent(SLit/unimi/dsi/fastutil/shorts/Short2BooleanFunction;)Z",
            shift = At.Shift.BEFORE),
            locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    private void addChannel(Level level, BlockPos pos, BlockState state, CallbackInfoReturnable<Map<Direction, FluidState>> cir, int i, Map map, Short2ObjectMap short2ObjectMap, Short2BooleanMap short2BooleanMap, Iterator var8, Direction direction, BlockPos blockPos, short s, Pair pair, BlockState blockState, FluidState fluidState, FluidState fluidState2, BlockPos blockPos2) {

        var channelState = level.getBlockState(pos.below());
        if (channelState.is(ModBlocks.OAK_CHANNEL.get()) && direction != Direction.DOWN && direction != Direction.UP) {
            if (channelState.getValue(WoodenChannelBlock.PROPERTY_BY_DIRECTION.get(direction))) {
                map.put(direction, fluidState2);
                cir.setReturnValue(map);
            }
        }
    }

     */

    @ModifyExpressionValue(method = "tick",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/material/FluidState;isSource()Z"))
    private boolean channelSpread(boolean original, Level level, BlockPos pos, FluidState state) {
        return original || level.getBlockState(pos).getBlock() instanceof WoodenChannelBlock;
    }

}

