package com.ordana.spelunkery.reg;

import com.ordana.spelunkery.Spelunkery;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ModSoundEvents {
    public static void init() {
    }

    public static Supplier<SoundEvent> PORTAL_FLUID_AMBIENT = registerSoundEvent("portal_fluid_ambient");
    public static Supplier<SoundEvent> PORTAL_FLUID_ENTER = registerSoundEvent("portal_fluid_submerge");
    public static Supplier<SoundEvent> PORTAL_FLUID_TELEPORT = registerSoundEvent("portal_fluid_teleport");
    public static Supplier<SoundEvent> PORTAL_FLUID_BUCKET_EMPTY = registerSoundEvent("portal_fluid_bucket_empty");
    public static Supplier<SoundEvent> PORTAL_FLUID_BUCKET_FILL = registerSoundEvent("portal_fluid_bucket_fill");
    public static Supplier<SoundEvent> PORTAL_FLUID_BOTTLE_EMPTY = registerSoundEvent("portal_fluid_bottle_empty");
    public static Supplier<SoundEvent> PORTAL_FLUID_BOTTLE_FILL = registerSoundEvent("portal_fluid_bottle_fill");

    @NotNull
    private static Supplier<SoundEvent> registerSoundEvent(@NotNull String name) {
        return RegHelper.registerSound(Spelunkery.res(name));
    }
}
