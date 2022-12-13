package com.ordana.underground_overhaul.reg;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.Locale;

public class ModBlockProperties {

    public static final BooleanProperty ILLUMINATED = BooleanProperty.create("illuminated");
    public static final BooleanProperty TEST = BooleanProperty.create("test");
    public static final IntegerProperty CHARGED = IntegerProperty.create("charged", 0, 100);



}
