package com.ordana.spelunkery.reg;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class ModBlockProperties {

    public static final BooleanProperty ILLUMINATED = BooleanProperty.create("illuminated");
    public static final BooleanProperty TOP = BooleanProperty.create("top");
    public static final BooleanProperty END = BooleanProperty.create("end");
    public static final BooleanProperty FLOOR = BooleanProperty.create("floor");
    public static final IntegerProperty CAPS = IntegerProperty.create("caps", 1, 8);

}
