package com.sockmit2007.omniaetnihil.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record JawCrushingRecipeInput(ItemStack input, int tier) implements RecipeInput {
    @Override
    public ItemStack getItem(int index) {
        return this.input;
    }

    public int getTier() {
        return tier;
    }

    @Override
    public int size() {
        return 1;
    }
}