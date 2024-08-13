package com.sockmit2007.omniaetnihil.block;

import javax.annotation.Nullable;

import com.mojang.serialization.MapCodec;
import com.sockmit2007.omniaetnihil.OmniaEtNihil;
import com.sockmit2007.omniaetnihil.block.entity.ExampleCrafterBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ExampleCrafter extends BaseEntityBlock {
    public static final MapCodec<ExampleCrafter> CODEC = simpleCodec(ExampleCrafter::new);

    public ExampleCrafter(Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<ExampleCrafter> codec() {
        return CODEC;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos arg0, BlockState arg1) {
        return OmniaEtNihil.EXAMPLE_CRAFTER_BLOCK_ENTITY.get().create(arg0, arg1);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
            BlockHitResult hit) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        } else {
            this.openContainer(level, pos, player);
            return InteractionResult.CONSUME;
        }
    }

    protected void openContainer(Level level, BlockPos pos, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ExampleCrafterBlockEntity exampleCrafterBlockEntity) {
            player.openMenu(exampleCrafterBlockEntity);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ExampleCrafterBlockEntity exampleCrafterBlockEntity) {
                if (level instanceof ServerLevel) {
                    Containers.dropContents(level, pos, exampleCrafterBlockEntity);
                }
                super.onRemove(state, level, pos, newState, isMoving);
                level.updateNeighbourForOutputSignal(pos, this);
            } else {
                super.onRemove(state, level, pos, newState, isMoving);
            }
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
            BlockEntityType<T> type) {
        return createExampleCrafterTicker(level, type, OmniaEtNihil.EXAMPLE_CRAFTER_BLOCK_ENTITY.get());
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createExampleCrafterTicker(
            Level pLevel, BlockEntityType<T> pServerType,
            BlockEntityType<? extends ExampleCrafterBlockEntity> pClientType) {

        return pLevel.isClientSide ? null
                : createTickerHelper(pServerType, pClientType,
                        ExampleCrafterBlockEntity::serverTick);
    }
}
