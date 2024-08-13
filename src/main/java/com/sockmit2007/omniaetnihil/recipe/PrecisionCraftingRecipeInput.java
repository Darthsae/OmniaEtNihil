package com.sockmit2007.omniaetnihil.recipe;

import java.util.List;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;

public record PrecisionCraftingRecipeInput(List<ItemStack> itemInput, List<ItemStack> toolInput, List<FluidStack> fluidInput, List<ItemStack> microToolInput, List<ItemStack> microComponentInput, List<ItemStack> stabilizerInput, List<ItemStack> modulationChipInput, int energy, int mana, int quantum) implements RecipeInput {
    @Override
    public ItemStack getItem(int index) {
        return this.itemInput.get(index);
    }

    @Override
    public int size() {
        return this.itemInput.size();
    }

    public ItemStack getTool(int index) {
        return this.toolInput.get(index);
    }

    public int getToolSize() {
        return this.toolInput.size();
    }

    public FluidStack getFluid(int index) {
        return this.fluidInput.get(index);
    }

    public int getFluidSize() {
        return this.fluidInput.size();
    }

    public ItemStack getMicroTool(int index) {
        return this.microToolInput.get(index);
    }

    public int getMicroToolSize() {
        return this.microToolInput.size();
    }

    public ItemStack getMicroComponent(int index) {
        return this.microComponentInput.get(index);
    }

    public int getMicroComponentSize() {
        return this.microComponentInput.size();
    }

    public ItemStack getStabilizer(int index) {
        return this.stabilizerInput.get(index);
    }

    public int getStabilizerSize() {
        return this.stabilizerInput.size();
    }

    public ItemStack getModulationChip(int index) {
        return this.modulationChipInput.get(index);
    }

    public int getModulationChipSize() {
        return this.modulationChipInput.size();
    }

    public int getEnergy() {
        return energy;
    }

    public int getMana() {
        return mana;
    }

    public int getQuantum() {
        return quantum;
    }
}