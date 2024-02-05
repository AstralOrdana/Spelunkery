package com.ordana.spelunkery.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class SulfurParticle extends TextureSheetParticle {
    private final float rotSpeed;
    private final SpriteSet sprites;

    SulfurParticle(ClientLevel clientLevel, double d, double e, double f, double color, double orientation, double unused, SpriteSet spriteSet) {
        super(clientLevel, d, e, f, color, orientation, unused);
        this.sprites = spriteSet;
        this.friction = 0.7F;
        this.gravity = 0.5F;
        var ordinal = Direction.from3DDataValue((int)orientation).step();
        var ordinalX = ordinal.x();
        var ordinalY = ordinal.y();
        var ordinalZ = ordinal.z();

        var g = (ordinalX == 0 ? Mth.nextDouble(level.random, -0.6D, 0.6D) : (ordinalX < 0 ? ordinalX - Mth.nextDouble(level.random, 0.1D, 1.5D) : ordinalX + Mth.nextDouble(level.random, 0.1D, 1.5D)));
        var h = (ordinalY == 0 ? Mth.nextDouble(level.random, -0.6D, 0.6D) : (ordinalY < 0 ? ordinalY - Mth.nextDouble(level.random, 0.1D, 1.5D) : ordinalY + Mth.nextDouble(level.random, 0.1D, 1.5D)));
        var i = (ordinalZ == 0 ? Mth.nextDouble(level.random, -0.6D, 0.6D) : (ordinalZ < 0 ? ordinalZ - Mth.nextDouble(level.random, 0.1D, 1.5D) : ordinalZ + Mth.nextDouble(level.random, 0.1D, 1.5D)));

        if (color != 0) {
            float rCol = (float) ((int) color >> 16 & 255) / 255.0F;
            float gCol = (float) ((int) color >> 8 & 255) / 255.0F;
            float bCol = (float) ((int) color & 255) / 255.0F;

            setColor(rCol, gCol, bCol);
            this.rCol = rCol;
            this.gCol = gCol;
            this.bCol = bCol;
        }

        this.xd *= 0.1D;
        this.yd *= 0.1D;
        this.zd *= 0.1D;
        this.xd += g * 0.4D;
        this.yd += h * 0.4D;
        this.zd += i * 0.4D;
        this.quadSize *= 0.75F;
        this.lifetime = Math.max((int)(6.0D / (Math.random() * 0.8D + 0.6D)), 1);
        this.hasPhysics = false;
        this.rotSpeed = ((float)Math.random() - 0.5F) * 0.1F;
        this.roll = (float)Math.random() * 6.2831855F;
        this.hasPhysics = true;
        this.tick();
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.sprites);
            this.oRoll = this.roll;
            this.roll += 3.1415927F * this.rotSpeed * 2.0F;
            if (this.onGround) {
                this.oRoll = this.roll = 0.0F;
            }

            this.move(this.xd, this.yd, this.zd);
            this.yd -= 0.003D;
            this.yd = Math.max(this.yd, -0.14D);
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet spriteSet) {
            this.sprite = spriteSet;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double color, double orientation, double unused) {
            var sulfurParticle = new SulfurParticle(level, x, y, z, color, orientation, unused, this.sprite);
            sulfurParticle.pickSprite(this.sprite);
            return sulfurParticle;
        }
    }
}
