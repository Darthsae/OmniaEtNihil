package com.sockmit2007.omniaetnihil.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.crafting.Ingredient;

public record ToolIngredient(Ingredient input, int tier) {
    public static final ToolIngredient EMPTY = new ToolIngredient(Ingredient.EMPTY, 0);

    public static final Codec<ToolIngredient> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter((input) -> {
            return input.input;
        }), ExtraCodecs.POSITIVE_INT.optionalFieldOf("tier", 1).forGetter((input) -> {
            return input.tier;
        })).apply(instance, ToolIngredient::new);
    });
}