package com.sockmit2007.omniaetnihil.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class HurtBlock extends Block {
    @Override
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
        pEntity.push(0, 1, 0);
        super.stepOn(pLevel, pPos, pState, pEntity);
    }

    public HurtBlock(Properties p_49795_) {
        super(p_49795_);
    }

}
