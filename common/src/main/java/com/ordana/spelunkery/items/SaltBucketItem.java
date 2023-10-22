package com.ordana.spelunkery.items;

import com.ordana.spelunkery.configs.ClientConfigs;
import com.ordana.spelunkery.reg.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

public class SaltBucketItem extends Item {
    public SaltBucketItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag context) {
        if (ClientConfigs.ENABLE_TOOLTIPS.get()) {
            tooltip.add(Component.translatable("tooltip.spelunkery.salt_bucket_1", getSaltToPlace(stack)).setStyle(Style.EMPTY.applyFormats(ChatFormatting.WHITE)));
            tooltip.add(Component.translatable("tooltip.spelunkery.salt_bucket_2").setStyle(Style.EMPTY.applyFormats(ChatFormatting.GRAY, ChatFormatting.ITALIC)));
        }
    }

    public void setAmount(ItemStack stack, int amount) {
        stack.getOrCreateTag().putInt("salt", amount);
    }

    public int getAmount(ItemStack stack) {
        return stack.getOrCreateTag().getInt("salt");
    }

    public int getSaltToPlace(ItemStack stack) {
        return switch (getAmount(stack)) {
            default -> 8;
            case 1 -> 7;
            case 2 -> 6;
            case 3 -> 5;
            case 4 -> 4;
            case 5 -> 3;
            case 6 -> 2;
            case 7 -> 1;
        };
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        InteractionResult interactionResult = this.place(new BlockPlaceContext(context));
        if (!interactionResult.consumesAction() && this.isEdible()) {
            InteractionResult interactionResult2 = this.use(context.getLevel(), context.getPlayer(), context.getHand()).getResult();
            return interactionResult2 == InteractionResult.CONSUME ? InteractionResult.CONSUME_PARTIAL : interactionResult2;
        } else {
            return interactionResult;
        }
    }

    public InteractionResult place(BlockPlaceContext context) {
        if (!context.canPlace()) {
            return InteractionResult.FAIL;
        } else {
            BlockPlaceContext blockPlaceContext = this.updatePlacementContext(context);
            if (blockPlaceContext == null) {
                return InteractionResult.FAIL;
            } else {
                BlockState blockState = this.getPlacementState(blockPlaceContext);
                if (blockState == null) {
                    return InteractionResult.FAIL;
                } else if (!this.placeBlock(blockPlaceContext, blockState)) {
                    return InteractionResult.FAIL;
                } else {
                    BlockPos blockPos = blockPlaceContext.getClickedPos();
                    Level level = blockPlaceContext.getLevel();
                    Player player = blockPlaceContext.getPlayer();
                    ItemStack itemStack = blockPlaceContext.getItemInHand();
                    BlockState blockState2 = level.getBlockState(blockPos);
                    if (blockState2.is(blockState.getBlock())) {
                        blockState2 = this.updateBlockStateFromTag(blockPos, level, itemStack, blockState2);
                        this.updateCustomBlockEntityTag(blockPos, level, player, itemStack, blockState2);
                        blockState2.getBlock().setPlacedBy(level, blockPos, blockState2, player, itemStack);
                        if (player instanceof ServerPlayer) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, blockPos, itemStack);
                        }
                    }

                    SoundType soundType = blockState2.getSoundType();
                    level.playSound(player, blockPos, this.getPlaceSound(blockState2), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
                    level.gameEvent(GameEvent.BLOCK_PLACE, blockPos, GameEvent.Context.of(player, blockState2));
                    if (player == null || !player.getAbilities().instabuild) {
                        if (getAmount(itemStack) < 7) this.setAmount(itemStack, getAmount(itemStack) + 1);
                        else player.setItemInHand(context.getHand(), Items.BUCKET.getDefaultInstance());
                    }

                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }
    }


    protected SoundEvent getPlaceSound(BlockState state) {
        return state.getSoundType().getPlaceSound();
    }

    @Nullable
    public BlockPlaceContext updatePlacementContext(BlockPlaceContext context) {
        return context;
    }

    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, @Nullable Player player, ItemStack stack, BlockState state) {
        return updateCustomBlockEntityTag(level, player, pos, stack);
    }

    @Nullable
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState blockState = this.getBlock().getStateForPlacement(context);
        return blockState != null && this.canPlace(context, blockState) ? blockState : null;
    }

    private BlockState updateBlockStateFromTag(BlockPos pos, Level level, ItemStack stack, BlockState state) {
        BlockState blockState = state;
        CompoundTag compoundTag = stack.getTag();
        if (compoundTag != null) {
            CompoundTag compoundTag2 = compoundTag.getCompound("BlockStateTag");
            StateDefinition<Block, BlockState> stateDefinition = state.getBlock().getStateDefinition();
            Iterator var9 = compoundTag2.getAllKeys().iterator();

            while(var9.hasNext()) {
                String string = (String)var9.next();
                Property<?> property = stateDefinition.getProperty(string);
                if (property != null) {
                    String string2 = compoundTag2.get(string).getAsString();
                    blockState = updateState(blockState, property, string2);
                }
            }
        }

        if (blockState != state) {
            level.setBlock(pos, blockState, 2);
        }

        return blockState;
    }

    private static <T extends Comparable<T>> BlockState updateState(BlockState state, Property<T> property, String valueIdentifier) {
        return property.getValue(valueIdentifier).map((comparable) -> {
            return state.setValue(property, comparable);
        }).orElse(state);
    }

    protected boolean canPlace(BlockPlaceContext context, BlockState state) {
        Player player = context.getPlayer();
        CollisionContext collisionContext = player == null ? CollisionContext.empty() : CollisionContext.of(player);
        return (!this.mustSurvive() || state.canSurvive(context.getLevel(), context.getClickedPos())) && context.getLevel().isUnobstructed(state, context.getClickedPos(), collisionContext);
    }

    protected boolean mustSurvive() {
        return true;
    }

    protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
        return context.getLevel().setBlock(context.getClickedPos(), state, 11);
    }

    public static boolean updateCustomBlockEntityTag(Level level, @Nullable Player player, BlockPos pos, ItemStack stack) {
        MinecraftServer minecraftServer = level.getServer();
        if (minecraftServer == null) {
            return false;
        } else {
            CompoundTag compoundTag = getBlockEntityData(stack);
            if (compoundTag != null) {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity != null) {
                    if (!level.isClientSide && blockEntity.onlyOpCanSetNbt() && (player == null || !player.canUseGameMasterBlocks())) {
                        return false;
                    }

                    CompoundTag compoundTag2 = blockEntity.saveWithoutMetadata();
                    CompoundTag compoundTag3 = compoundTag2.copy();
                    compoundTag2.merge(compoundTag);
                    if (!compoundTag2.equals(compoundTag3)) {
                        blockEntity.load(compoundTag2);
                        blockEntity.setChanged();
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public Block getBlock() {
        return ModBlocks.SALT.get();
    }

    @Nullable
    public static CompoundTag getBlockEntityData(ItemStack stack) {
        return stack.getTagElement("BlockEntityTag");
    }

}
