package com.sockmit2007.omniaetnihil.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.crafting.Ingredient;

public record IngredientWithProbabilityWithCount(Ingredient input, int count, double probability) {
    public static final Codec<IngredientWithProbabilityWithCount> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter((input) -> {
            return input.input;
        }), ExtraCodecs.POSITIVE_INT.optionalFieldOf("count", 1).forGetter((input) -> {
            return input.count;
        }), Codec.DOUBLE.optionalFieldOf("probability", 1.0).forGetter((input) -> {
            return input.probability;
        })).apply(instance, IngredientWithProbabilityWithCount::new);
    });
}