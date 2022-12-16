package com.ordana.underground_overhaul.fabric.mixins;

import com.ordana.underground_overhaul.reg.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(ExperienceOrb.class)
public class ExperienceOrbMixin {
    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ExperienceOrb;move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V", shift = At.Shift.BEFORE))
    private void siphonAttraction(CallbackInfo ci) {
        Optional<BlockPos> siphonPos = BlockPos.findClosestMatch(((ExperienceOrb) (Object) this).blockPosition(), 4, 2, (pos) -> ((ExperienceOrb) (Object) this).level.getBlockState(pos).is(ModBlocks.NEPHRITE_SIPHON.get()));
        if (siphonPos.isEmpty()) return;


        Vec3 vec3 = new Vec3((siphonPos.get().getX() + 0.5) - ((ExperienceOrb) (Object) this).getX(), (siphonPos.get().getY() + 0.5) - ((ExperienceOrb) (Object) this).getY(), (siphonPos.get().getZ() + 0.5) - ((ExperienceOrb) (Object) this).getZ());
        double d = vec3.lengthSqr();
        if (d < 64.0D) {
            double e = 1.0D - Math.sqrt(d) / 8.0D;
            ((ExperienceOrb) (Object) this).setDeltaMovement(((ExperienceOrb) (Object) this).getDeltaMovement().add(vec3.normalize().scale(e * e * 0.1D)));
        }
    }
}
