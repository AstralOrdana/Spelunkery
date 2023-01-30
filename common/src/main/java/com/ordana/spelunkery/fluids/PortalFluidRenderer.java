package com.ordana.spelunkery.fluids;

import com.mojang.math.Vector3f;
import net.mehvahdjukaar.moonlight.api.client.ModFluidRenderProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

public class PortalFluidRenderer extends ModFluidRenderProperties {
    private final ResourceLocation overlay;
    private final ResourceLocation renderOverlay;
    private final Vector3f fogColor;

    public PortalFluidRenderer(ResourceLocation still, ResourceLocation flowing, int tint, ResourceLocation overlay, ResourceLocation renderOverlay, Vector3f fogColor) {
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

    public Vector3f modifyFogColor() {
        return this.fogColor;
    }

    public Vector3f modifyFogColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
        return this.modifyFogColor();
    }

}
