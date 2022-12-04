package com.ordana.underhaul.mixins;

//import net.minecraft.world.level.levelgen.OreVeinifier;


/*
@Mixin(OreVeinifier.class)
public abstract class MixinOreVeinifier {

    @Shadow @Final private BlockState normalBlock;

    @Inject(method = "oreVeinify", at = @At("RETURN"), cancellable = true)
    private void addVeinBuffer(RandomSource randomSource, int i, int j, int k, double d, double e, double f, CallbackInfoReturnable<BlockState> cir){
        double randomDither = randomSource.nextDouble() * 0.2D;
        if(this.isInVein(e, f, randomDither) && cir.getReturnValue() == this.normalBlock && getVeinType(d, j) != 0){
            cir.setReturnValue(Blocks.DIAMOND_BLOCK.defaultBlockState());
        }
    }

    private boolean isInVein(double d, double e, double randomDither) {
        double f = Math.abs(1.0D * d) - 0.2599999821186066D - randomDither;
        double g = Math.abs(1.0D * e) - 0.2599999821186066D - randomDither;
        return Math.max(f, g) < 0.0D;
    }

    private int getVeinType(double d, int i) {
        int veinType = d > 0.0D ? 1 : -1;
        int j = (veinType == 1 ? 50 : -8) - i;
        int k = i - (veinType == 1 ? 0 : -60);
        if (k >= 0 && j >= 0) {
            int l = Math.min(j, k);
            double e = Mth.clampedMap((double)l, 0.0D, 20.0D, -0.2D, 0.0D);
            return Math.abs(d) + e < 0.4000000059604645D ? 0 : veinType;
        } else {
            return 0;
        }
    }
}

 */
