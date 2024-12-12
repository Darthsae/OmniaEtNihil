package com.sockmit2007.omniaetnihil.block;

import javax.annotation.Nullable;

import com.mojang.serialization.MapCodec;
import com.sockmit2007.omniaetnihil.OmniaEtNihil;
import com.sockmit2007.omniaetnihil.block.entity.CrudeConeCrusherBlockEntity;
import com.sockmit2007.omniaetnihil.block.state.properties.ModBlockStateProperties;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;

public class CrudeConeCrusher extends BaseEntityBlock {
    public static final MapCodec<CrudeConeCrusher> CODEC = simpleCodec(CrudeConeCrusher::new);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty ACTIVE = ModBlockStateProperties.ACTIVE;

    public CrudeConeCrusher(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(ACTIVE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING).add(ACTIVE);
    }

    @Override
    public MapCodec<CrudeConeCrusher> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos arg0, BlockState arg1) {
        return OmniaEtNihil.CRUDE_CONE_CRUSHER_BLOCK_ENTITY.get().create(arg0, arg1);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        } else {
            this.openContainer(level, pos, player);
            return InteractionResult.CONSUME;
        }
    }

    protected void openContainer(Level level, BlockPos pos, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CrudeConeCrusherBlockEntity crudeConeCrusherBlockEntity) {
            player.openMenu(crudeConeCrusherBlockEntity);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite()).setValue(ACTIVE, false);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof CrudeConeCrusherBlockEntity crudeConeCrusherBlockEntity) {
                if (level instanceof ServerLevel) {
                    Containers.dropContents(level, pos, crudeConeCrusherBlockEntity);
                }
                super.onRemove(state, level, pos, newState, isMoving);
                level.updateNeighbourForOutputSignal(pos, this);
            } else {
                super.onRemove(state, level, pos, newState, isMoving);
            }
        }
    }

    @Override
    protected RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createCrudeConeCrusherTicker(level, type, OmniaEtNihil.CRUDE_CONE_CRUSHER_BLOCK_ENTITY.get());
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createCrudeConeCrusherTicker(Level pLevel, BlockEntityType<T> pServerType, BlockEntityType<? extends CrudeConeCrusherBlockEntity> pClientType) {

        return pLevel.isClientSide ? null : createTickerHelper(pServerType, pClientType, CrudeConeCrusherBlockEntity::serverTick);
    }
}
