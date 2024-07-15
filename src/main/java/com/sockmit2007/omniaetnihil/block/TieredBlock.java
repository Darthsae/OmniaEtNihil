package com.sockmit2007.omniaetnihil.block;

import javax.annotation.Nullable;

import com.sockmit2007.omniaetnihil.block.state.properties.ModBlockStateProperties;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

public class TieredBlock extends Block {
    public static final IntegerProperty TIER = ModBlockStateProperties.TIER;

    public TieredBlock(Properties p_49795_) {
        super(p_49795_);
        registerDefaultState(stateDefinition.any().setValue(TIER, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(TIER);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return defaultBlockState().setValue(TIER, 0);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer,
            BlockHitResult pHitResult) {
        int newTier = pState.getValue(TIER) + 1;

        if (newTier > 3)
            return InteractionResult.FAIL;

        pLevel.setBlock(pPos, pState.setValue(TIER, newTier), UPDATE_ALL);

        return InteractionResult.SUCCESS;
    }
}
