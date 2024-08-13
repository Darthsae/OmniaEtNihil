package com.sockmit2007.omniaetnihil.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;

public record PreprocessingRecipeInput(ItemStack input, FluidStack fluidInput, int tier) implements RecipeInput {
    @Override
    public ItemStack getItem(int index) {
        return this.input;
    }

    public FluidStack getFluid(int index) {
        return this.fluidInput;
    }

    public int getTier() {
        return tier;
    }

    @Override
    public int size() {
        return 1;
    }
}