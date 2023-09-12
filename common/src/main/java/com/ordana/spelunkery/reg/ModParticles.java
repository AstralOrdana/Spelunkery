package com.ordana.spelunkery.reg;

import com.ordana.spelunkery.Spelunkery;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.particles.SimpleParticleType;

import java.util.function.Supplier;

public class ModParticles {

    public static void init() {
    }

    public static Supplier<SimpleParticleType> registerParticle(String name) {
        return RegHelper.registerParticle(Spelunkery.res(name));
    }

//    public static final Supplier<SimpleParticleType> SULFUR = registerParticle("sulfur");
//    public static final Supplier<SimpleParticleType> SULFUR_DUSTING = registerParticle("sulfur_dusting");
    public static final Supplier<SimpleParticleType> PORTAL_FLAME = registerParticle("portal_fluid_flame");

}
