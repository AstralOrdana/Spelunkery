package com.ordana.spelunkery.items;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import com.mojang.math.Vector3f;
import com.mojang.serialization.DataResult;
import com.ordana.spelunkery.configs.ClientConfigs;
import com.ordana.spelunkery.configs.CommonConfigs;
import com.ordana.spelunkery.reg.ModGameEvents;
import com.ordana.spelunkery.utils.TranslationUtils;
import dev.architectury.injectables.annotations.PlatformOnly;
import net.mehvahdjukaar.moonlight.api.item.IFirstPersonAnimationProvider;
import net.mehvahdjukaar.moonlight.api.item.IThirdPersonAnimationProvider;
import net.mehvahdjukaar.moonlight.api.misc.DualWeildState;
import net.mehvahdjukaar.moonlight.api.util.math.MthUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AmethystTuningForkItem extends Item implements IFirstPersonAnimationProvider, IThirdPersonAnimationProvider {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String TAG_LODESTONE_POS = "LodestonePos";
    public static final String TAG_LODESTONE_DIMENSION = "LodestoneDimension";
    public static final String TAG_LODESTONE_TRACKED = "LodestoneTracked";
    public static boolean tolling;


    public AmethystTuningForkItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag context) {
        if (ClientConfigs.ENABLE_TOOLTIPS.get()) {
            if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), Minecraft.getInstance().options.keyShift.key.getValue())) {
                tooltip.add(Component.translatable("tooltip.spelunkery.tuning_fork_1", getTollRange()).setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
                tooltip.add(Component.translatable("tooltip.spelunkery.tuning_fork_2").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
            } else {
                tooltip.add(TranslationUtils.CROUCH.component());
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        CompoundTag compoundTag = stack.getOrCreateTag();
        boolean hasTag = compoundTag.contains("amethystPos");
        if (level.dimension() != Level.OVERWORLD) return new InteractionResultHolder<>(InteractionResult.PASS, stack);

        if (player.isSecondaryUseActive()) {
            if (hasTag) {
                compoundTag.remove("amethystPos");
                compoundTag.remove("amethystDimension");
            }
            tolling = false;
        } else if (!hasTag) {
            level.gameEvent(player, ModGameEvents.FORK_TONE_EVENT.get(), player.blockPosition());
            player.getCooldowns().addCooldown(this, 20);
        }
        if (player instanceof ServerPlayer serverPlayer) CriteriaTriggers.USING_ITEM.trigger(serverPlayer, stack);
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }




    /*
    public void tollFork(Player player, ItemStack stack, Level level) {
        if(level instanceof ServerLevel serverLevel && stack.getItem() instanceof AmethystTuningForkItem) {
            level.playSound(null, player.blockPosition(), SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 1.0f, 1.0f);

            int playerX = player.getBlockX();
            int playerY = player.getBlockY();
            int playerZ = player.getBlockZ();

            for (int x = -16; x < 16; x++) {
                for (int y = level.getMinBuildHeight(); y < 16; y++) {
                    for (int z = -16; z < 16; z++) {
                        var pos = new BlockPos(x, y, z);
                        var offsetPos = pos.offset(playerX, playerY, playerZ);
                        if (level.getBlockState(offsetPos).is(Blocks.BUDDING_AMETHYST)) {
                            level.playSound(null, player.blockPosition(), SoundEvents.ARROW_HIT_PLAYER, SoundSource.BLOCKS, 1.0f, 1.0f);
                            level.playSound(null, offsetPos, SoundEvents.ARROW_HIT_PLAYER, SoundSource.BLOCKS, 1.0f, 1.0f);
                            for(int i = 0; i < 10; ++i) {
                                double d0 = serverLevel.random.nextGaussian() * 0.02D;
                                double d1 = serverLevel.random.nextGaussian() * 0.02D;
                                double d2 = serverLevel.random.nextGaussian() * 0.02D;
                                serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, (offsetPos.getX() + 0.5) - d0 * 10.0D, (offsetPos.getY() + 1.5) - d1 * 10.0D, (offsetPos.getZ() + 0.5) - d2 * 10.0D, 1, 0, 0, 0, d2);
                            }
                        }
                    }
                }
            }

        }
    }
     */


    public int getTollRange() {
        return CommonConfigs.TUNING_FORK_RANGE.get();
    }

    public void setPlayerX(ItemStack stack, int amount) {
        stack.getOrCreateTag().putInt("ex", amount);
    }

    public int getPlayerX(ItemStack stack) {
        return stack.getOrCreateTag().getInt("ex");
    }

    public void setPlayerZ(ItemStack stack, int amount) {
        stack.getOrCreateTag().putInt("zed", amount);
    }

    public int getPlayerZ(ItemStack stack) {
        return stack.getOrCreateTag().getInt("zed");
    }

    //Override
    @PlatformOnly(PlatformOnly.FORGE)
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    //Override
    @PlatformOnly(PlatformOnly.FABRIC)
    public boolean allowNbtUpdateAnimation(Player player, InteractionHand hand, ItemStack originalStack, ItemStack updatedStack) {
        return false;
    }

    private static Optional<ResourceKey<Level>> getDimension(CompoundTag compoundTag) {
        return Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, compoundTag.get("amethystDimension")).result();
    }

    private int tickCounter = 0;
    public int setTickCounter(int tick) {
        return tickCounter = tick;
    }


    /*
    @Override
    public void inventoryTick(@NotNull ItemStack stack, Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide()) {
            if (entity instanceof ServerPlayer player){
                this.setPlayerX(stack, player.getBlockX());
                this.setPlayerZ(stack, player.getBlockZ());

            }

            CompoundTag compoundTag = stack.getOrCreateTag();
            tickCounter++;
            if (tickCounter == 100) {
                if (!compoundTag.contains("amethystPos")) level.gameEvent(entity, ModGameEvents.FORK_TONE_EVENT.get(), entity.blockPosition());
                setTickCounter(0);
            }

            Optional<ResourceKey<Level>> optional = getDimension(compoundTag);
            if (optional.isPresent() && optional.get() == level.dimension() && compoundTag.contains("amethystPos")) {
                BlockPos blockPos = NbtUtils.readBlockPos(compoundTag.getCompound("amethystPos"));
                if (!level.isInWorldBounds(blockPos) || !level.getBlockState(blockPos).is(Blocks.BUDDING_AMETHYST)) {
                    compoundTag.remove("amethystPos");
                    level.gameEvent(entity, ModGameEvents.FORK_TONE_EVENT.get(), entity.blockPosition());
                }
            }
        }
    }

     */

    public static void addAmethystTags(ResourceKey<Level> lodestoneDimension, BlockPos pos, CompoundTag compoundTag) {
        if (!compoundTag.contains("amethystPos")) compoundTag.put("amethystPos", NbtUtils.writeBlockPos(pos));
        tolling = true;
        DataResult<Tag> var10000 = Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, lodestoneDimension);
        Logger var10001 = LOGGER;
        Objects.requireNonNull(var10001);
        var10000.resultOrPartial(var10001::error).ifPresent(tag -> compoundTag.put("amethystDimension", tag));
    }

    public static boolean isAmethystNearby(ItemStack stack) {
        CompoundTag compoundTag = stack.getTag();
        return compoundTag != null && compoundTag.contains("amethystPos");
    }


    public static double distanceToAmethyst(Vec3i playerPos, BlockPos amethystPos) {
        return amethystPos.distSqr(playerPos);
    }

    @Nullable
    public static GlobalPos getAmethystPos(CompoundTag compoundTag) {
        boolean bl = compoundTag.contains("amethystPos");
        boolean bl2 = compoundTag.contains("amethystDimension");
        if (bl && bl2) {
            Optional<ResourceKey<Level>> optional = getDimension(compoundTag);
            if (optional.isPresent()) {
                BlockPos blockPos = NbtUtils.readBlockPos(compoundTag.getCompound("amethystPos"));
                return GlobalPos.of(optional.get(), blockPos);
            }
        }

        return null;
    }




    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        //need to use NONE for custom one
        return UseAnim.NONE;
    }

    @Override
    public <T extends LivingEntity> boolean poseLeftArm(ItemStack itemStack, HumanoidModel<T> model, T entity, HumanoidArm humanoidArm, DualWeildState dualWeildState) {
        if (entity.getUseItemRemainingTicks() > 0 &&
                entity.getUseItem().getItem() == this &&
                entity.getTicksUsingItem() < 50) {
            //twoHanded.setTwoHanded(true);
            model.leftArm.yRot = MthUtils.wrapRad(0.1F + model.head.yRot);
            model.leftArm.xRot = MthUtils.wrapRad((-(float) Math.PI / 2F) + model.head.xRot);
            return true;
        }
        return false;
    }

    @Override
    public <T extends LivingEntity> boolean poseRightArm(ItemStack itemStack, HumanoidModel<T> model, T entity, HumanoidArm humanoidArm, DualWeildState dualWeildState) {
        if (entity.getUseItemRemainingTicks() > 0 &&
                entity.getUseItem().getItem() == this &&
                entity.getTicksUsingItem() < 50) {
            //twoHanded.setTwoHanded(true);
            model.rightArm.yRot = MthUtils.wrapRad(-0.1F + model.head.yRot);
            model.rightArm.xRot = MthUtils.wrapRad((-(float) Math.PI / 2F) + model.head.xRot);
            return true;
        }
        return false;
    }

    @Override
    public void animateItemFirstPerson(LivingEntity entity, ItemStack stack, InteractionHand hand, PoseStack poseStack, float partialTicks, float pitch, float attackAnim, float handHeight) {
        //is using item
        if (tolling && entity.getUsedItemHand() == hand && entity.level instanceof ClientLevel level) {

            //budding amethyst block pos
            BlockPos blockPos = NbtUtils.readBlockPos(stack.getOrCreateTag().getCompound("amethystPos"));

            //time
            var time = level.getGameTime();
            //the wobble
            var value = (float)Math.cos(time + partialTicks) * 100;
            //rotation value
            var modifier = getModifierAngle(entity, time, blockPos) * 360;
            //distance calculation
            var distance = StrictMath.cbrt(distanceToAmethyst(entity.blockPosition(), blockPos));

            //position the fork
            poseStack.mulPose(Vector3f.XP.rotationDegrees(-80.0F));
            poseStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(0.0F));

            //wobble + rotation
            poseStack.mulPose(Vector3f.XP.rotationDegrees((float)(value / distance) + modifier));

        }


        /*

        if (tolling && entity.getUsedItemHand() == hand && entity.level() instanceof ClientLevel level) {

            float f = (float)(entity.getUseItemRemainingTicks() % 10);
            float h = 1.0F - (f - partialTicks + 1.0F) / 10.0F;
            //float n =  +  * ;

            var compoundTag = stack.getOrCreateTag();
            BlockPos blockPos = NbtUtils.readBlockPos(compoundTag.getCompound("amethystPos"));
            //float vibrationSpan = 4.0f;
            var time = level.getGameTime();
            //var value = (Mth.sin((time + (partialTicks * 100))));
            var value = (float)Math.cos(time + partialTicks) * 10;
            var modifier = getModifierAngle(entity, time, blockPos);

            //poseStack.translate(-0.25D, 0.22D, 0.35D);
            poseStack.mulPose(Axis.XP.rotationDegrees(-80.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(0.0F));
            //(float)((value / Math.sqrt(distanceToAmethyst(entity.blockPosition(), blockPos) / 100))) + modifier * 10
            poseStack.mulPose(Axis.XP.rotationDegrees((float)((value / StrictMath.cbrt(distanceToAmethyst(entity.blockPosition(), blockPos)) * 10)) + modifier * 360));

            //poseStack.mulPose(Axis.XP.rotationDegrees(Mth.cos((partialTicks / (float)distanceToAmethyst(entity.blockPosition(), blockPos)) * vibrationSpan * (float)Math.PI) * 75.0F - 90.0F));

            // / (float)distanceToAmethyst(entity.blockPosition(), blockPos)
            //poseStack.mulPose(Axis.YP.rotationDegrees(Mth.sin(entity.level.getGameTime() + partialTicks)));


            float timeLeft = (float) 20 - ((float) 1600 - partialTicks + 1.0F);
            float f12 = 1f;

            float f15 = Mth.sin((timeLeft - 0.1F) *  1.3F);
            float f18 = f12 - 0.1F;
            float f20 = f15 * f18;
            poseStack.translate(0, f20 * 0.007F, 0);

            poseStack.translate(0, 0, f12 * 0.04F);
            poseStack.scale(1.0F, 1.0F, 1.0F + f12 * 0.2F);


            //poseStack.rotateAround(Axis.YP.rotationDegrees(Mth.sin(entity.level.getGameTime() + partialTicks)), 0, 0, 0);



            //matrixStack.mulPose(Vector3f.YN.rotationDegrees((float)k * 45.0F));

        }
        */
    }

    private final CompassWobble wobble = new CompassWobble();

    public float getModifierAngle(Entity entity, long l, BlockPos blockPos) {
        double d = this.getAngleFromEntityToPos(entity, blockPos);
        double e = this.getWrappedVisualRotationY(entity);
        double f;

        if (entity instanceof Player) {
            Player player = (Player)entity;
            if (this.wobble.shouldUpdate(l)) {
                this.wobble.update(l, 0.5D - (e - 0.25D));
            }

            f = d + this.wobble.rotation;
            return Mth.positiveModulo((float)f, 1.0F);
        }

        f = 0.5D - (e - 0.25D - d);
        return Mth.positiveModulo((float)f, 1.0F);
    }


    private float getAngleFromEntityToPos(Entity entity, BlockPos blockPos) {
        Vec3 vec3 = Vec3.atCenterOf(blockPos);
        return (float)Math.atan2(vec3.z() - entity.getZ(), vec3.x() - entity.getX()) / (float)6.2831854820251465D;
    }

    private double getWrappedVisualRotationY(Entity entity) {
        return Mth.positiveModulo(entity.getVisualRotationYInDegrees() / 360.0F, 1.0D);
    }

    static class CompassWobble {
        double rotation;
        private double deltaRotation;
        private long lastUpdateTick;

        CompassWobble() {
        }

        boolean shouldUpdate(long l) {
            return this.lastUpdateTick != l;
        }

        void update(long l, double d) {
            this.lastUpdateTick = l;
            double e = d - this.rotation;
            e = Mth.positiveModulo(e + 0.5D, 1.0D) - 0.5D;
            this.deltaRotation += e * 0.1D;
            this.deltaRotation *= 0.8D;
            this.rotation = Mth.positiveModulo(this.rotation + this.deltaRotation, 1.0D);
        }
    }
}
