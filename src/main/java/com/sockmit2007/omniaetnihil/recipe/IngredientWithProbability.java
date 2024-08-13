package com.sockmit2007.omniaetnihil.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.item.crafting.Ingredient;

public record IngredientWithProbability(Ingredient input, double probability) {
    public static final Codec<IngredientWithProbability> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter((input) -> {
            return input.input;
        }), Codec.DOUBLE.optionalFieldOf("probability", 1.0).forGetter((input) -> {
            return input.probability;
        })).apply(instance, IngredientWithProbability::new);
    });
}