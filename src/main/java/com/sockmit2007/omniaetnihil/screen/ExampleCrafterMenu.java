package com.sockmit2007.omniaetnihil.screen;

import com.sockmit2007.omniaetnihil.OmniaEtNihil;

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

public class ExampleCrafterMenu extends AbstractContainerMenu {
    private final Container container;
    private final ContainerData data;
    protected final Level level;

    public ExampleCrafterMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(11), new SimpleContainerData(2));
    }

    public ExampleCrafterMenu(int containerId, Inventory playerInventory, Container container, ContainerData data) {
        super(OmniaEtNihil.EXAMPLE_CRAFTER_MENU.get(), containerId);
        checkContainerSize(container, 11);
        checkContainerDataCount(data, 2);
        this.container = container;
        this.data = data;
        this.level = playerInventory.player.level();

        // Altar
        this.addSlot(new Slot(container, 0, 19, 26));
        this.addSlot(new Slot(container, 1, 19, 58));
        this.addSlot(new Slot(container, 2, 19, 90));
        this.addSlot(new Slot(container, 3, 51, 26));
        this.addSlot(new Slot(container, 4, 51, 58));
        this.addSlot(new Slot(container, 5, 51, 90));
        this.addSlot(new Slot(container, 6, 83, 26));
        this.addSlot(new Slot(container, 7, 83, 58));
        this.addSlot(new Slot(container, 8, 83, 90));

        this.addSlot(new Slot(container, 9, 115, 58));
        this.addSlot(new Slot(container, 10, 147, 58));

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

    public boolean isCrafting() {
        return this.data.get(0) > 0;
    }
}
