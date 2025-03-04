package com.ordana.spelunkery.mixins;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.configs.CommonConfigs;
import com.ordana.spelunkery.reg.ModTags;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

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

            var magma = (Entity)this instanceof MagmaCube;
            var itemStack = itemEntity.getItem();
            boolean foodCheck = (magma && itemStack.is(ModTags.MAGMA_CUBE_FOOD)) || ((!magma) && itemStack.is(ModTags.SLIME_FOOD));

            if (foodCheck) {
                itemStack.setCount(itemStack.getCount() - 1);
                itemEntity.setItem(itemStack);
                this.playSound(SoundEvents.SLIME_SQUISH, 1.0F, 1.0F);
                this.playSound(SoundEvents.FOX_EAT, 1.0F, 1.0F);

                int i = 1 + this.random.nextInt(this.getSize());
                for (int j = 0; j < i; ++j) {
                    if (random.nextInt(3) <= this.getSize()) this.spawnAtLocation(magma ? Items.MAGMA_CREAM : Items.SLIME_BALL);
                }

                if (random.nextInt(CommonConfigs.SLIME_GROWTH_CHANCE.get()) == 1 && this.getSize() <= CommonConfigs.SLIME_GROWTH_MAX.get() && CommonConfigs.SLIME_GROWTH.get()) this.setSize(this.getSize() + 1, false);
            }
        }
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        var magma = (Entity)this instanceof MagmaCube;
        boolean foodCheck = (magma && itemStack.is(ModTags.MAGMA_CUBE_FOOD)) || ((!magma) && itemStack.is(ModTags.SLIME_FOOD));

        if (foodCheck) {

            if (player instanceof ServerPlayer serverPlayer) CriteriaTriggers.PLAYER_INTERACTED_WITH_ENTITY.trigger(serverPlayer, itemStack, this);
            player.playSound(SoundEvents.SLIME_SQUISH, 1.0F, 1.0F);
            player.playSound(SoundEvents.FOX_EAT, 1.0F, 1.0F);

            if (!player.getAbilities().instabuild) itemStack.shrink(1);

            if (player instanceof ServerPlayer) {
                int i = 1 + this.random.nextInt(this.getSize());
                for (int j = 0; j < i; ++j) {
                    if (random.nextInt(3) <= this.getSize()) {
                        LootTable lootTable = Objects.requireNonNull(level.getServer()).getLootTables().get(Spelunkery.res(magma ? "gameplay/magma_cube_feeding" : "gameplay/slime_feeding"));

                        LootContext.Builder builder = new LootContext.Builder((ServerLevel) level)
                                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(Vec3i.ZERO))
                                .withOptionalParameter(LootContextParams.THIS_ENTITY, player);

                        var l = lootTable.getRandomItems(builder.create(LootContextParamSets.GIFT));

                        ItemEntity itemEntity = this.spawnAtLocation(l.iterator().next());
                        if (itemEntity != null)
                            itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().add((this.random.nextFloat() - this.random.nextFloat()) * 0.1F, this.random.nextFloat() * 0.05F, (this.random.nextFloat() - this.random.nextFloat()) * 0.1F));
                    }
                }
            }
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

}
