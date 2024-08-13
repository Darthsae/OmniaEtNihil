package com.sockmit2007.omniaetnihil.screen;

import com.sockmit2007.omniaetnihil.OmniaEtNihil;
import com.sockmit2007.omniaetnihil.block.entity.CrudePreprocessorBlockEntity;

import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PrecisionCraftingTableMenu extends AbstractContainerMenu {
    private final Container container;
    private final ContainerData data;
    protected final Level level;

    public PrecisionCraftingTableMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(105), new SimpleContainerData(11));
    }

    public PrecisionCraftingTableMenu(int containerId, Inventory playerInventory, Container container, ContainerData data) {
        super(OmniaEtNihil.CRUDE_PREPROCESSOR_MENU.get(), containerId);
        checkContainerSize(container, 3);
        checkContainerDataCount(data, 11);
        this.container = container;
        this.data = data;
        this.level = playerInventory.player.level();

        // Crafting Grid
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(container, j + i * 9, 7 + j * 18, 7 + i * 18));
            }
        }

        // Tools
        for (int i = 0; i < 6; ++i) {
            this.addSlot(new Slot(container, 81 + i, 173, 7 + i * 18));
        }

        // Precision Tools
        for (int i = 0; i < 4; ++i) {
            this.addSlot(new Slot(container, 87 + i, 195, 7 + i * 18));
        }

        // Micro Components
        for (int i = 0; i < 8; ++i) {
            this.addSlot(new Slot(container, 91 + i, 217, 7 + i * 18));
        }

        // Stabilizers
        for (int i = 0; i < 4; ++i) {
            this.addSlot(new Slot(container, 99 + i, 195, 83 + i * 18));
        }

        // Modulators
        for (int i = 0; i < 3; ++i) {
            this.addSlot(new Slot(container, 103 + i, 213, 159 + i * 18));
        }

        // Inventory
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 132 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 190));
        }

        addDataSlots(data);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    public float getBurnProgress() {
        int i = this.data.get(0);
        int j = this.data.get(1);
        return j != 0 && i != 0 ? Mth.clamp((float) i / (float) j, 0.0F, 1.0F) : 0.0F;
    }

    public float getFluidLevel() {
        int i = this.data.get(3);
        int j = CrudePreprocessorBlockEntity.MAX_FLUID;
        return j != 0 && i != 0 ? Mth.clamp((float) i / (float) j, 0.0F, 1.0F) : 0.0F;
    }

    public float getEnergyLevel() {
        int i = this.data.get(2);
        int j = CrudePreprocessorBlockEntity.MAX_ENERGY;
        return j != 0 && i != 0 ? Mth.clamp((float) i / (float) j, 0.0F, 1.0F) : 0.0F;
    }

    public boolean isCrafting() {
        return this.data.get(0) > 0;
    }
}
