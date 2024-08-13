package com.sockmit2007.omniaetnihil.recipe;

import java.util.List;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record ExampleCraftingRecipeInput(List<ItemStack> input) implements RecipeInput {
    @Override
    public ItemStack getItem(int index) {
        return this.input.get(index);
    }

    @Override
    public int size() {
        return this.input.size();
    }
}