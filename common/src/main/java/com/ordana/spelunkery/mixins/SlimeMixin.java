package com.ordana.spelunkery.mixins;

import com.ordana.spelunkery.configs.CommonConfigs;
import com.ordana.spelunkery.reg.ModTags;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slime.class)
public abstract class SlimeMixin extends Mob {

    @Shadow public abstract int getSize();
    @Shadow public abstract void setSize(int size, boolean resetHealth);

    public boolean canPickUpLoot() {
        return true;
    }

    protected SlimeMixin(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }


    @Inject(method = "checkSlimeSpawnRules", at = @At("HEAD"), cancellable = true)
    private static void caveSlimeSpawns(EntityType<Slime> slime, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random, CallbackInfoReturnable<Boolean> cir) {
        if (CommonConfigs.INCREASED_SLIME_SPAWN_RATE.get() && level.getBiome(pos).is(BiomeTags.IS_OVERWORLD)) {
            if (pos.getY() < 60 && random.nextInt(124) > (pos.getY() + 64) && (level.getLightEmission(pos) == 0 || level.getBlockState(pos).is(Blocks.DEEPSLATE))) {
                cir.setReturnValue(checkMobSpawnRules(slime, level, spawnType, pos, random));
            }
        }
    }


    @Inject(method = "dealDamage", at = @At("HEAD"), cancellable = true)
    public void damage(LivingEntity livingEntity, CallbackInfo ci) {
        if ((livingEntity.hasEffect(MobEffects.REGENERATION) && CommonConfigs.SLIMES_FRIENDLY_REGEN.get()) || CommonConfigs.SLIMES_ALWAYS_FRIENDLY.get()) ci.cancel();
    }


    protected void pickUpItem(ItemEntity itemEntity) {
        if (random.nextInt(CommonConfigs.SLIME_EAT_COOLDOWN.get()) == 1) {
            if (itemEntity.getItem().is(ModTags.SLIME_FOOD)) {
                ItemStack itemStack = itemEntity.getItem();
                itemStack.setCount(itemStack.getCount() - 1);
                itemEntity.setItem(itemStack);
                this.playSound(SoundEvents.SLIME_SQUISH, 1.0F, 1.0F);
                this.playSound(SoundEvents.FOX_EAT, 1.0F, 1.0F);

                int i = 1 + this.random.nextInt(this.getSize());
                for (int j = 0; j < i; ++j) {
                    if (random.nextInt(3) <= this.getSize()) this.spawnAtLocation(Items.SLIME_BALL);
                }

                if (random.nextInt(CommonConfigs.SLIME_GROWTH_CHANCE.get()) == 1 && this.getSize() <= CommonConfigs.SLIME_GROWTH_MAX.get() && CommonConfigs.SLIME_GROWTH.get()) this.setSize(this.getSize() + 1, false);
            }
        }
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(ModTags.SLIME_FOOD)) {
            if (player instanceof ServerPlayer serverPlayer) CriteriaTriggers.PLAYER_INTERACTED_WITH_ENTITY.trigger(serverPlayer, itemStack, this);
            player.playSound(SoundEvents.SLIME_SQUISH, 1.0F, 1.0F);
            player.playSound(SoundEvents.FOX_EAT, 1.0F, 1.0F);
            if (!player.getAbilities().instabuild) itemStack.shrink(1);

            int i = 1 + this.random.nextInt(this.getSize());
            for (int j = 0; j < i; ++j) {
                if (random.nextInt(3) <= this.getSize()) {
                    ItemEntity itemEntity = this.spawnAtLocation(Items.SLIME_BALL);
                    if (itemEntity != null)
                        itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().add((this.random.nextFloat() - this.random.nextFloat()) * 0.1F, this.random.nextFloat() * 0.05F, (this.random.nextFloat() - this.random.nextFloat()) * 0.1F));
                }
            }
            return InteractionResult.SUCCESS;
        } else {
            return super.mobInteract(player, hand);
        }
    }

}
