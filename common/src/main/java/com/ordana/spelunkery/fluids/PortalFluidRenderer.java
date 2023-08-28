package com.ordana.spelunkery.fluids;

import net.mehvahdjukaar.moonlight.api.client.ModFluidRenderProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class PortalFluidRenderer extends ModFluidRenderProperties {
    private final ResourceLocation overlay;
    private final ResourceLocation renderOverlay;
    private final Vec3 fogColor;

    public PortalFluidRenderer(ResourceLocation still, ResourceLocation flowing, int tint, ResourceLocation overlay, ResourceLocation renderOverlay, Vec3 fogColor) {
        super(still, flowing, tint);
        this.overlay = overlay;
        this.renderOverlay = renderOverlay;
        this.fogColor = fogColor;
    }

    @Nullable
    @Override
    public ResourceLocation getOverlayTexture() {
        return this.overlay;
    }

    public ResourceLocation getOverlayTexture(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
        return this.getOverlayTexture();
    }

    public ResourceLocation getRenderOverlayTexture() {
        return this.renderOverlay;
    }

    public ResourceLocation getRenderOverlayTexture(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
        return this.getRenderOverlayTexture();
    }

    public Vec3 modifyFogColor() {
        return this.fogColor;
    }

    public Vec3 modifyFogColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
        return this.modifyFogColor();
    }

}