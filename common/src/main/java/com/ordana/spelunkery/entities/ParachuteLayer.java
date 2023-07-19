package com.ordana.spelunkery.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.ordana.spelunkery.items.ParachuteItem;
import com.ordana.spelunkery.reg.ModItems;
import com.ordana.spelunkery.utils.IParachuteEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;


public class ParachuteLayer<T extends LivingEntity & IParachuteEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {
    private final ItemRenderer itemRenderer;
    private final ParachuteMode parachuteMode;

    public ParachuteLayer(RenderLayerParent<T, M> parent) {
        super(parent);
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
        this.parachuteMode = ParachuteMode.VISIBLE;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {

        if (!(livingEntity instanceof IParachuteEntity))
            return; //failsafe for mods that change the renderer for some reason
        ParachuteMode mode = parachuteMode;
        ItemStack myParachute = livingEntity.getParachute();
        ItemStack parachute = new ItemStack(ModItems.PARACHUTE.get());
        ParachuteItem.set3DModel(parachute, true);
        if (mode == ParachuteMode.HIDDEN ||
                !ParachuteItem.getActive(myParachute)) return;


        if (!parachute.isEmpty()) {

            poseStack.pushPose();

            poseStack.translate(0, -2, 0);
            poseStack.mulPose(Vector3f.XN.rotationDegrees(180));
            //this.getParentModel().body.translateAndRotate(poseStack);


            /*
            {
                double offset = 0;

                {
                    o += 3 / 16f + offset;
                    poseStack.translate(0, 0.1, o);
                    if (flipped) poseStack.scale(-1, 1, -1);

                }
            }

             */

            itemRenderer.renderStatic(livingEntity, parachute, ItemTransforms.TransformType.HEAD, false,
                    poseStack, buffer, livingEntity.level, packedLight, OverlayTexture.NO_OVERLAY, 0);

            poseStack.popPose();
        }


        /*
        if (!(livingEntity instanceof IParachuteEntity))
            return; //failsafe for mods that change the renderer for some reason
        ParachuteMode mode = parachuteMode;
        ItemStack parachute;
        parachute = livingEntity.getParachute();

        if (mode == ParachuteMode.HIDDEN || !ParachuteItem.getActive(parachute) || livingEntity.getMainHandItem() == parachute || livingEntity.getOffhandItem() == parachute) return;

        if (!parachute.isEmpty()) {
            itemRenderer.renderStatic(livingEntity, parachute, ItemTransforms.TransformType.HEAD, false,
                    poseStack, buffer, livingEntity.level, packedLight, OverlayTexture.NO_OVERLAY, 0);

            poseStack.popPose();
        }
         */
    }

    public enum ParachuteMode {
        HIDDEN,
        VISIBLE
    }

}
