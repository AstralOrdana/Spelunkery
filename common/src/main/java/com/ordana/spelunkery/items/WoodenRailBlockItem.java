package com.ordana.spelunkery.items;

import com.ordana.spelunkery.blocks.WoodenRailBlock;
import com.ordana.spelunkery.configs.ClientConfigs;
import com.ordana.spelunkery.utils.TranslationUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
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
import net.minecraft.world.level.block.state.properties.RailShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WoodenRailBlockItem extends BlockItem {
    public WoodenRailBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @javax.annotation.Nullable Level level, List<Component> tooltip, TooltipFlag context) {
        if (ClientConfigs.ENABLE_TOOLTIPS.get()) {
            if (!Screen.hasShiftDown()) {
                tooltip.add(TranslationUtils.CROUCH.component());
            }
            if (Screen.hasShiftDown()) {
                tooltip.add(Component.translatable("tooltip.spelunkery.wooden_rail_1").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
                tooltip.add(Component.translatable("tooltip.spelunkery.wooden_rail_2").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
            }
        }
    }

    @Override
    @Nullable
    public BlockPlaceContext updatePlacementContext(BlockPlaceContext context) {
        BlockPos blockPos = context.getClickedPos().below();
        Level level = context.getLevel();

        BlockState railBlockState = level.getBlockState(blockPos);
        BlockState blockState = level.getBlockState(blockPos);
        Block block = this.getBlock();
        if (!blockState.is(block)) {
            return context;
        } else {
            Direction direction = context.getHorizontalDirection();

            int i = 0;
            BlockPos.MutableBlockPos mutableBlockPos = blockPos.mutable().move(direction);

            while(i < 7) {
                if (!level.isClientSide && !level.isInWorldBounds(mutableBlockPos)) {
                    Player player = context.getPlayer();
                    int j = level.getMaxBuildHeight();
                    if (player instanceof ServerPlayer && mutableBlockPos.getY() >= j) {
                        ((ServerPlayer)player).sendSystemMessage(Component.translatable("build.tooHigh", j - 1).withStyle(ChatFormatting.RED), true);
                    }
                    break;
                }

                blockState = level.getBlockState(mutableBlockPos);
                if (!blockState.is(this.getBlock())) {
                    if (blockState.canBeReplaced(context)) {
                        if (((direction == Direction.EAST || direction == Direction.WEST) && (railBlockState.getValue(WoodenRailBlock.SHAPE) == RailShape.EAST_WEST))
                        || ((direction == Direction.NORTH || direction == Direction.SOUTH) && (railBlockState.getValue(WoodenRailBlock.SHAPE) == RailShape.NORTH_SOUTH))) {
                            return BlockPlaceContext.at(context, mutableBlockPos, direction);
                        }
                    }
                    break;
                }

                mutableBlockPos.move(direction);
                if (!direction.getAxis().isHorizontal()) {
                    ++i;
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
