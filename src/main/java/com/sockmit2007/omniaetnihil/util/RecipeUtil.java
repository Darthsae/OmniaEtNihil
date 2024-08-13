package com.sockmit2007.omniaetnihil.util;

import com.sockmit2007.omniaetnihil.recipe.IngredientWithCount;
import com.sockmit2007.omniaetnihil.recipe.IngredientWithProbability;
import com.sockmit2007.omniaetnihil.recipe.IngredientWithProbabilityWithCount;

import net.minecraft.world.item.crafting.Ingredient;

public class RecipeUtil {
    public static Ingredient getWithProbability(IngredientWithProbability ingredientWithProbability) {
        if (ingredientWithProbability.probability() > Math.random()) {
            return ingredientWithProbability.input();
        } else {
            return Ingredient.EMPTY;
        }
    }

    public static IngredientWithCount getCountWithProbability(IngredientWithProbabilityWithCount ingredientWithProbabilityWithCount) {
        if (ingredientWithProbabilityWithCount.probability() > Math.random()) {
            return new IngredientWithCount(ingredientWithProbabilityWithCount.input(), ingredientWithProbabilityWithCount.count());
        } else {
            return IngredientWithCount.EMPTY;
        }
    }
}
