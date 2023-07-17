package com.ordana.spelunkery.mixins;

import com.ordana.spelunkery.configs.CommonConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NetherPortalBlock.class)
public class NetherPortalBlockMixin {

    @Inject(method = "updateShape", at = @At("HEAD"))
    public void bringToTears(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos, CallbackInfoReturnable<BlockState> cir) {
        if (CommonConfigs.PORTAL_DESTRUCTION_CRYING_OBSIDIAN.get() && !level.isClientSide()) {
            Direction.Axis axis2 = state.getValue(NetherPortalBlock.AXIS);
            RandomSource random = level.getRandom();
            for (Direction cryDir : Direction.values()) {
                BlockState cryState = level.getBlockState(currentPos.relative(cryDir));
                if (random.nextInt(3) == 1 && cryState.is(Blocks.OBSIDIAN) && !(new PortalShape(level, currentPos, axis2)).isComplete()) {
                    level.setBlock(currentPos.relative(cryDir), Blocks.CRYING_OBSIDIAN.defaultBlockState(), 3);
                }
            }
        }
    }
}
