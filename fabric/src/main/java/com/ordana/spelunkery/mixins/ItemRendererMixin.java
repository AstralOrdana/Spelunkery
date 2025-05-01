package com.ordana.spelunkery.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.reg.ModItems;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @ModifyVariable(method = "render", at = @At(value = "HEAD"), argsOnly = true)
    public BakedModel applyModels(BakedModel value, ItemStack stack, ItemTransforms.TransformType renderMode, boolean leftHanded, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        if (stack.is(ModItems.BUNNY_EARS.get()) && renderMode != ItemTransforms.TransformType.GUI) {
            return ((ItemRendererAccessor) this).spelunk$getItemModelShaper().getModelManager().getModel(new ModelResourceLocation(Spelunkery.MOD_ID, "bunny_ears_3d", "inventory"));
        }
        if (stack.is(ModItems.TRUE_CROWN.get()) && renderMode != ItemTransforms.TransformType.GUI) {
            return ((ItemRendererAccessor) this).spelunk$getItemModelShaper().getModelManager().getModel(new ModelResourceLocation(Spelunkery.MOD_ID, "true_crown_3d", "inventory"));
        }
        return value;
    }
}
