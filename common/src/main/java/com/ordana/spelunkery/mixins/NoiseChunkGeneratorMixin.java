/*
Method in this class adapted from @author Andrew6rant (Andrew Grant) under the MIT license

Full repo can be found here: https://github.com/Andrew6rant/MC-237017-Fix

        Permission is hereby granted, free of charge, to any person obtaining
        a copy of this software and associated documentation files (the
        'Software'), to deal in the Software without restriction, including
        without limitation the rights to use, copy, modify, merge, publish,
        distribute, sublicense, and/or sell copies of the Software, and to
        permit persons to whom the Software is furnished to do so, subject to
        the following conditions:

        The above copyright notice and this permission notice shall be
        included in all copies or substantial portions of the Software.

        THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
        EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
        MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
        IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
        CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
        TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
        SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package com.ordana.spelunkery.mixins;

import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(NoiseBasedChunkGenerator.class)
public abstract class NoiseChunkGeneratorMixin {
    // Original
//    @Inject(method = "createFluidPicker", at = @At("HEAD"), cancellable = true)
//    private static void createFluidPicker(settings, CallbackInfoReturnable<Aquifer.FluidPicker> cir) {
//        cir.setReturnValue((x, y, z) -> new Aquifer.FluidStatus(settings.seaLevel(), settings.defaultFluid()));
//    }

    // TODO 1.19.2 :: Unsure about reason - can this be done in the constructor? Or first-access? Accessor?
}