package com.ordana.spelunkery.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.ordana.spelunkery.SpelunkeryClient;
import com.ordana.spelunkery.items.ParachuteItem;
import com.ordana.spelunkery.reg.ModItems;
import com.ordana.spelunkery.utils.IParachuteEntity;
import net.mehvahdjukaar.moonlight.api.client.util.RotHlpr;
import net.mehvahdjukaar.moonlight.api.platform.ClientPlatformHelper;
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
        if (mode == ParachuteMode.HIDDEN ||
                !ParachuteItem.getActive(myParachute)) return;

        //dont change nbt here in render code
        if (!myParachute.isEmpty()) {

            poseStack.pushPose();

            poseStack.mulPose(RotHlpr.X180);

            var model = ClientPlatformHelper.getModel(Minecraft.getInstance().getModelManager(), SpelunkeryClient.PARACHUTE_3D_MODEL);
            float ticks = livingEntity.getParachuteTicks() + partialTick;
            int timeToOpen = 15;


            float x = Math.min(1, ticks / timeToOpen);
            float scaleH = x;
            float scaleW = x * x;
            poseStack.translate(0, 0.5 + scaleH / 2, 0);
            poseStack.scale(scaleW, scaleH, scaleW);
            poseStack.translate(0, 1, 0);

            itemRenderer.render(ModItems.PARACHUTE.get().getDefaultInstance(), ItemTransforms.TransformType.HEAD,
                    false, poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, model);

            poseStack.popPose();
        }
    }

    public enum ParachuteMode {
        HIDDEN,
        VISIBLE
    }

}
