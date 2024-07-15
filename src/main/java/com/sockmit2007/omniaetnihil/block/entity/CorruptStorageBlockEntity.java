package com.sockmit2007.omniaetnihil.block.entity;

import com.sockmit2007.omniaetnihil.OmniaEtNihil;
import com.sockmit2007.omniaetnihil.screen.CorruptStorageMenu;

import mod.azure.azurelib.common.api.common.animatable.GeoBlockEntity;
import mod.azure.azurelib.common.internal.common.util.AzureLibUtil;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager.ControllerRegistrar;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;

public class CorruptStorageBlockEntity extends BaseContainerBlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

    protected NonNullList<ItemStack> items = NonNullList.withSize(9, ItemStack.EMPTY);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controllerName", 0, event -> {
            return event.setAndContinue(RawAnimation.begin().thenLoop("close"));
        }));
    }

    public CorruptStorageBlockEntity() {
        this(OmniaEtNihil.CORRUPT_STORAGE_BLOCK_ENTITY.get(), BlockPos.ZERO,
                OmniaEtNihil.CORRUPT_STORAGE.get().defaultBlockState());
    }

    public CorruptStorageBlockEntity(BlockPos pPos, BlockState pBlockState) {
        this(OmniaEtNihil.CORRUPT_STORAGE_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    public CorruptStorageBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("menu." + OmniaEtNihil.MODID + ".corrupt_storage");
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return new CorruptStorageMenu(pContainerId, pInventory, this);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registry) {
        super.loadAdditional(tag, registry);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.items, registry);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registry) {
        super.saveAdditional(tag, registry);
        ContainerHelper.saveAllItems(tag, this.items, registry);
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> pItems) {
        this.items = pItems;
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        return this.items.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        return ContainerHelper.removeItem(this.items, index, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ContainerHelper.takeItem(this.items, index);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        this.items.set(index, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }
}
