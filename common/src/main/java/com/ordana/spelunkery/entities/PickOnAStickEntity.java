package com.ordana.spelunkery.entities;

import com.ordana.spelunkery.reg.ModEntities;
import com.ordana.spelunkery.utils.IPickHelper;
import net.mehvahdjukaar.moonlight.api.entity.ImprovedProjectileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PickOnAStickEntity extends ImprovedProjectileEntity {
    private BlockPos pos;

    public PickOnAStickEntity(EntityType<PickOnAStickEntity> type, Level level) {
        super(type, level);
        this.maxStuckTime = Integer.MAX_VALUE;
    }

    public PickOnAStickEntity(Level level, LivingEntity thrower) {
        super(ModEntities.PICK.get(), thrower, level);
        this.maxStuckTime = Integer.MAX_VALUE;
    }

    public PickOnAStickEntity(Level worldIn, double x, double y, double z) {
        super(ModEntities.PICK.get(), x, y, z, worldIn);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getOwner() instanceof IPickHelper mi && mi.spelunkery$getEntity() != this) this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        var location = result.getBlockPos();
        this.pos = new BlockPos(location);
        this.setNoGravity(true);
    }

    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("pos", NbtUtils.writeBlockPos(pos));
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        Optional<BlockPos> pos = NbtUtils.readBlockPos(tag, ("pos"));
        this.setHitBlock(pos.orElse(null));
    }

    public void setHitBlock(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    protected Item getDefaultItem() {
        return Items.IRON_PICKAXE;
    }

    public BlockPos getPos() {
        return pos;
    }

    public void setPos(BlockPos pos) {
        this.pos = pos;
    }
}