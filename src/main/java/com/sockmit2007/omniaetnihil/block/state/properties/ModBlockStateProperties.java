package com.sockmit2007.omniaetnihil.block.state.properties;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class ModBlockStateProperties {

    public static final BooleanProperty DECAYED = BooleanProperty.create("decayed");
    public static final IntegerProperty TIER = IntegerProperty.create("tier", 0, 3);
}
