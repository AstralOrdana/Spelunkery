package com.ordana.spelunkery.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.SpelunkeryClient;
import com.ordana.spelunkery.entities.EggPlither;
import net.minecraft.client.model.WitherBossModel;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class EggPlitherRenderer extends MobRenderer<EggPlither, WitherBossModel<EggPlither>> {
    private static final ResourceLocation PLITHER_LOCATION = Spelunkery.res("textures/entity/plither/egg_plither.png");

    public EggPlitherRenderer(EntityRendererProvider.Context context) {
        super(context, new WitherBossModel<>(context.bakeLayer(SpelunkeryClient.EGG_PLITHER)), 0.9F);
    }

    @Override
    public ResourceLocation getTextureLocation(EggPlither plither) {
        return PLITHER_LOCATION;
    }

    @Override
    protected void scale(EggPlither plither, PoseStack poseStack, float f) {
        poseStack.scale(1.2F, 1.2F, 1.2F);
        super.scale(plither, poseStack, f);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild("shoulders", CubeListBuilder.create().texOffs(0, 16).addBox(-10.0F, 3.9F, -0.5F, 20.0F, 3.0F, 3.0F), PartPose.ZERO);
        float f = 0.20420352F;
        partDefinition.addOrReplaceChild("ribcage", CubeListBuilder.create().texOffs(0, 22).addBox(0.0F, 0.0F, 0.0F, 3.0F, 10.0F, 3.0F).texOffs(24, 22).addBox(-4.0F, 1.5F, 0.5F, 11.0F, 2.0F, 2.0F).texOffs(24, 22).addBox(-4.0F, 4.0F, 0.5F, 11.0F, 2.0F, 2.0F).texOffs(24, 22).addBox(-4.0F, 6.5F, 0.5F, 11.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(-2.0F, 6.9F, -0.5F, 0.20420352F, 0.0F, 0.0F));
        partDefinition.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(12, 22).addBox(0.0F, 0.0F, 0.0F, 3.0F, 6.0F, 3.0F), PartPose.offsetAndRotation(-2.0F, 6.9F + Mth.cos(0.20420352F) * 10.0F, -0.5F + Mth.sin(0.20420352F) * 10.0F, 0.83252203F, 0.0F, 0.0F));
        partDefinition.addOrReplaceChild("center_head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.ZERO);
        CubeListBuilder cubeListBuilder = CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -4.0F, -4.0F, 6.0F, 6.0F, 6.0F);
        partDefinition.addOrReplaceChild("right_head", cubeListBuilder, PartPose.offset(-8.0F, 4.0F, 0.0F));
        partDefinition.addOrReplaceChild("left_head", cubeListBuilder, PartPose.offset(10.0F, 4.0F, 0.0F));
        return LayerDefinition.create(meshDefinition, 64, 64);
    }
}

