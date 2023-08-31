package com.ordana.spelunkery.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

@Environment(EnvType.CLIENT)
public class PortalFluidFlameParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    protected boolean isGlowing;
    private final Quaternionf rotation = new Quaternionf(0F, 0F, 0F, 0F);

    PortalFluidFlameParticle(ClientLevel clientLevel, double d, double e, double f, double g, double h, double i, SpriteSet spriteSet) {
        super(clientLevel, d, e, f, g, h, i);
        this.sprites = spriteSet;
        this.scale(2F);
        this.setSpriteFromAge(spriteSet);
    }

    /*
    @Override
    public void render(@NotNull VertexConsumer buffer, @NotNull Camera renderInfo, float partialTicks) {
        this.rotation.set(0.0f, 0.0f, 0.0f, 1.0f);
        this.rotation.mul(Axis.YP.rotationDegrees(-renderInfo.getYRot()));
    }

     */

    public int getLightColor(float partialTick) {
        return this.isGlowing ? 240 : super.getLightColor(partialTick);
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public void tick() {
        this.setSpriteFromAge(this.sprites);
        if (this.age++ >= this.lifetime) {
            this.remove();
        }
    }

    @Environment(EnvType.CLIENT)
    public static class EmissiveProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public EmissiveProvider(SpriteSet spriteSet) {
            this.sprite = spriteSet;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            PortalFluidFlameParticle flameParticle = new PortalFluidFlameParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprite);
            flameParticle.setAlpha(1.0F);
            flameParticle.isGlowing = true;
            return flameParticle;
        }
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet spriteSet) {
            this.sprite = spriteSet;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            PortalFluidFlameParticle flameParticle = new PortalFluidFlameParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprite);
            return flameParticle;
        }
    }
}
