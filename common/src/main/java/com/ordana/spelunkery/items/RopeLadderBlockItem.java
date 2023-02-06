package com.ordana.spelunkery.items;

import com.ordana.spelunkery.blocks.RopeLadderBlock;
import com.ordana.spelunkery.configs.ClientConfigs;
import com.ordana.spelunkery.utils.TranslationUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RopeLadderBlockItem extends BlockItem {
    public RopeLadderBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @javax.annotation.Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag context) {
        if (ClientConfigs.ENABLE_TOOLTIPS.get()) {
            if (Screen.hasShiftDown()) {
                tooltip.add(Component.translatable("tooltip.spelunkery.rope_ladder_1").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
                tooltip.add(Component.translatable("tooltip.spelunkery.rope_ladder_2").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
                tooltip.add(Component.translatable("tooltip.spelunkery.rope_ladder_3").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
            } else {
                tooltip.add(TranslationUtils.CROUCH.component());
            }
        }
    }

    @Override
    @Nullable
    public BlockPlaceContext updatePlacementContext(@NotNull BlockPlaceContext context) {
        BlockPos blockPos = context.getClickedPos();
        Level level = context.getLevel();
        BlockState blockState = level.getBlockState(blockPos);
        Block block = this.getBlock();
        if (!blockState.is(block)) {
            return context;
        } else {
            Direction direction;
            direction = context.getClickedFace() == Direction.DOWN ? context.getHorizontalDirection() : Direction.DOWN;

            int i = 0;
            BlockPos.MutableBlockPos mutableBlockPos = blockPos.mutable().move(direction);

            while(i < 7) {
                if (!level.isClientSide && !level.isInWorldBounds(mutableBlockPos)) {
                    Player player = context.getPlayer();
                    int j = level.getMinBuildHeight();
                    if (player instanceof ServerPlayer && mutableBlockPos.getY() <= j) {
                        ((ServerPlayer)player).sendSystemMessage(Component.translatable("build.tooLow", j + 1).withStyle(ChatFormatting.RED), true);
                    }
                    break;
                }

                BlockState belowState = level.getBlockState(mutableBlockPos);
                if (!belowState.is(this.getBlock())) {
                    if (belowState.canBeReplaced(context)) {
                        return BlockPlaceContext.at(context, mutableBlockPos, blockState.getValue(RopeLadderBlock.FACING));
                    }
                    break;
                }

                mutableBlockPos.move(direction);
                if (direction.getAxis().isHorizontal()) {
                    --i;
                }
            }

            return null;
        }
    }

    @Override
    protected boolean mustSurvive() {
        return false;
    }
}
