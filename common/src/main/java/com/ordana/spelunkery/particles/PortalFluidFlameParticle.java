package com.ordana.spelunkery.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public class PortalFluidFlameParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    protected boolean isGlowing;
    private final Quaternionf rotation = new Quaternionf(0F, 0F, 0F, 0F);

    PortalFluidFlameParticle(ClientLevel clientLevel, double d, double e, double f, double g, double h, double i, SpriteSet spriteSet) {
        super(clientLevel, d, e, f, g, h, i);
        this.sprites = spriteSet;
        this.scale(2F);
        this.lifetime = (int)(this.random.nextFloat() + 6.0f);
        this.setSpriteFromAge(spriteSet);
    }

    public void setSpriteFromAge(SpriteSet sprite) {
        if (!this.removed) {
            this.setSprite(sprite.get(this.age, this.lifetime));
        }
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        Vec3 vec3 = renderInfo.getPosition();
        float f = (float) (Mth.lerp(partialTicks, this.xo, this.x) - vec3.x());
        float g = (float) (Mth.lerp(partialTicks, this.yo, this.y) - vec3.y());
        float h = (float) (Mth.lerp(partialTicks, this.zo, this.z) - vec3.z());
        this.rotation.set(0.0f, 0.0f, 0.0f, 1.0f);
        this.rotation.mul(Axis.YP.rotationDegrees(-renderInfo.getYRot()));
        //this.rotation.mul(Axis.XP.rotationDegrees(renderInfo.getXRot() * (Mth.lerp(partialTicks, this.prevXRotMultiplier, this.xRotMultiplier))));
        if (this.roll != 0.0f) {
            float i = Mth.lerp(partialTicks, this.oRoll, this.roll);
            this.rotation.mul(Axis.ZP.rotation(i));
        }
        Vector3f[] vector3fs = new Vector3f[]{new Vector3f(-1.0f, -1.0f, 0.0f), new Vector3f(-1.0f, 1.0f, 0.0f), new Vector3f(1.0f, 1.0f, 0.0f), new Vector3f(1.0f, -1.0f, 0.0f)};
        float j = this.getQuadSize(partialTicks);
        for (int k = 0; k < 4; ++k) {
            Vector3f vector3f2 = vector3fs[k];
            vector3f2.rotate(this.rotation);
            vector3f2.mul(j);
            vector3f2.add(f, g, h);
        }
        float l = this.getU0();
        float m = this.getU1();
        float n = this.getV0();
        float o = this.getV1();
        int p = this.getLightColor(partialTicks);
        buffer.vertex(vector3fs[0].x(), vector3fs[0].y(), vector3fs[0].z()).uv(m, o).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(p).endVertex();
        buffer.vertex(vector3fs[1].x(), vector3fs[1].y(), vector3fs[1].z()).uv(m, n).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(p).endVertex();
        buffer.vertex(vector3fs[2].x(), vector3fs[2].y(), vector3fs[2].z()).uv(l, n).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(p).endVertex();
        buffer.vertex(vector3fs[3].x(), vector3fs[3].y(), vector3fs[3].z()).uv(l, o).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(p).endVertex();
    }

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
