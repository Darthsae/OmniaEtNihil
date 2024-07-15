package com.sockmit2007.omniaetnihil.block;

import javax.annotation.Nullable;

import com.sockmit2007.omniaetnihil.OmniaEtNihil;
import com.sockmit2007.omniaetnihil.block.state.properties.ModBlockStateProperties;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class SpreadBlock extends Block {
    public static final BooleanProperty DECAYED = ModBlockStateProperties.DECAYED;

    public SpreadBlock(Properties p_49795_) {
        super(p_49795_);
        registerDefaultState(stateDefinition.any().setValue(DECAYED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(DECAYED);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return defaultBlockState().setValue(DECAYED, false);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState pState) {
        return !pState.getValue(DECAYED);
    }

    @Override
    protected void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pRandom.nextFloat() > 0.9f) {
            BlockPos newPos = pPos.offset(pRandom.nextIntBetweenInclusive(-1, 1),
                    pRandom.nextIntBetweenInclusive(-1, 1), pRandom.nextIntBetweenInclusive(-1, 1));
            if (!pLevel.getBlockState(newPos).is(OmniaEtNihil.unspreadableBlocksTag)) {
                pLevel.setBlock(newPos, pState, UPDATE_ALL);
                if (pRandom.nextBoolean())
                    pLevel.setBlock(pPos, pState.setValue(DECAYED, true), UPDATE_ALL);
            }
        }
    }
}
