package com.ordana.spelunkery.reg;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class ModBlockProperties {

    public static final BooleanProperty ILLUMINATED = BooleanProperty.create("illuminated");
    public static final BooleanProperty TOP = BooleanProperty.create("top");
    public static final BooleanProperty END = BooleanProperty.create("end");
    public static final BooleanProperty FLOOR = BooleanProperty.create("floor");
    public static final BooleanProperty PRIMED = BooleanProperty.create("primed");
    public static final BooleanProperty SUPPORTED  = BooleanProperty.create("supported");
    public static final IntegerProperty CAPS = IntegerProperty.create("caps", 1, 8);
    public static final IntegerProperty STICKS = IntegerProperty.create("sticks", 1, 9);
    public static final IntegerProperty DEPLETION = IntegerProperty.create("depletion", 0, 3);
    public static final IntegerProperty LIGHT = IntegerProperty.create("light", 0, 16);
    public static final BooleanProperty OPEN = BooleanProperty.create("open");

}
