package com.ordana.spelunkery.reg;

import com.mojang.serialization.Codec;
import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.items.HandheldCompactorItem;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;

import java.util.function.Supplier;

public class ModComponents {
    public static void init() {
    }

    public static <T> Supplier<DataComponentType<T>> regComponent(String name, Supplier<DataComponentType<T>> itemSup) {
        return RegHelper.registerDataComponent(Spelunkery.res(name), itemSup);
    }

    public static final Supplier<DataComponentType<GlobalPos>> AMETHYST_POS = regComponent("amethyst_pos", ()->
            DataComponentType.<GlobalPos>builder().persistent(GlobalPos.CODEC).networkSynchronized(GlobalPos.STREAM_CODEC).build());

    public static final Supplier<DataComponentType<GlobalPos>> MAGNETITE_POS = regComponent("magnetite_pos", ()->
            DataComponentType.<GlobalPos>builder().persistent(GlobalPos.CODEC).networkSynchronized(GlobalPos.STREAM_CODEC).build());

    public static final Supplier<DataComponentType<BlockPos>> PLAYER_POS = regComponent("player_pos", ()->
            DataComponentType.<BlockPos>builder().persistent(BlockPos.CODEC).networkSynchronized(BlockPos.STREAM_CODEC).build());

    public static final Supplier<DataComponentType<Boolean>> USED = regComponent("used", ()->
            DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());

    public static final Supplier<DataComponentType<Boolean>> ACTIVE = regComponent("active", ()->
            DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());

    public static final Supplier<DataComponentType<Integer>> XP = regComponent("xp", ()->
            DataComponentType.<Integer>builder().persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.INT).build());

    public static final Supplier<DataComponentType<Integer>> SALT = regComponent("salt", ()->
            DataComponentType.<Integer>builder().persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.INT).build());

    public static final Supplier<DataComponentType<HandheldCompactorItem.CompressionMode>> MODE = regComponent("mode", ()->
            DataComponentType.<HandheldCompactorItem.CompressionMode>builder().persistent(HandheldCompactorItem.CompressionMode.CODEC).networkSynchronized(HandheldCompactorItem.CompressionMode.STREAM_CODEC).build());

    public static final Supplier<DataComponentType<Integer>> DEPTH = regComponent("depth", ()->
            DataComponentType.<Integer>builder().persistent(ExtraCodecs.intRange(Integer.MIN_VALUE, Integer.MAX_VALUE)).networkSynchronized(ByteBufCodecs.INT).build());

    public static final Supplier<DataComponentType<Integer>> DEPLETION = regComponent("depletion", ()->
            DataComponentType.<Integer>builder().persistent(ExtraCodecs.intRange(Integer.MIN_VALUE, Integer.MAX_VALUE)).networkSynchronized(ByteBufCodecs.INT).build());

}