package com.sockmit2007.omniaetnihil.block;

import com.mojang.serialization.MapCodec;
import com.sockmit2007.omniaetnihil.OmniaEtNihil;
import com.sockmit2007.omniaetnihil.block.entity.CorruptStorageBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class CorruptStorage extends BaseEntityBlock {
    public static final MapCodec<CorruptStorage> CODEC = simpleCodec(CorruptStorage::new);

    public CorruptStorage(Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<CorruptStorage> codec() {
        return CODEC;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos arg0, BlockState arg1) {
        return OmniaEtNihil.CORRUPT_STORAGE_BLOCK_ENTITY.get().create(arg0, arg1);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
            BlockHitResult hit) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        } else {
            this.openContainer(level, pos, player);
            if (level.getBlockEntity(pos) instanceof CorruptStorageBlockEntity corruptStorageBlockEntity) {
                corruptStorageBlockEntity.applyEffects();
                this.triggerAnim("baseAnim", "anim");
            }
            return InteractionResult.CONSUME;
        }
    }

    protected void openContainer(Level level, BlockPos pos, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CorruptStorageBlockEntity corruptStorageBlockEntity) {
            player.openMenu(corruptStorageBlockEntity);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof CorruptStorageBlockEntity corruptStorageBlockEntity) {
                if (level instanceof ServerLevel) {
                    Containers.dropContents(level, pos, corruptStorageBlockEntity);
                }
                super.onRemove(state, level, pos, newState, isMoving);
                level.updateNeighbourForOutputSignal(pos, this);
            } else {
                super.onRemove(state, level, pos, newState, isMoving);
            }
        }
    }
}
