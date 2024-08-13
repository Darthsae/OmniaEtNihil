package com.sockmit2007.omniaetnihil.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sockmit2007.omniaetnihil.OmniaEtNihil;
import com.sockmit2007.omniaetnihil.codec.ArrayCodec;
import com.sockmit2007.omniaetnihil.codec.CodecFix;

import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class ExampleCraftingRecipe implements Recipe<ExampleCraftingRecipeInput> {
    private final ItemStack output;
    private final ItemStack byproduct;
    private final IngredientWithCount[] inputs;
    private final int ticks;

    public ExampleCraftingRecipe(ItemStack output, ItemStack byproduct, IngredientWithCount[] inputs, int ticks) {
        this.output = output;
        this.byproduct = byproduct;
        this.inputs = inputs;
        this.ticks = ticks;
    }

    public ItemStack getOutput() {
        return output;
    }

    public ItemStack getByproduct() {
        return byproduct;
    }

    public IngredientWithCount[] getInputs() {
        return inputs;
    }

    public int getTicks() {
        return ticks;
    }

    @Override
    public boolean matches(ExampleCraftingRecipeInput container, Level level) {
        if (level.isClientSide)
            return false;

        boolean[] usedIndices = new boolean[9];
        for (int i = 0; i < 9; i++)
            usedIndices[i] = container.getItem(i).isEmpty();

        int len = Math.min(inputs.length, 9);
        for (int i = 0; i < len; i++) {
            IngredientWithCount input = inputs[i];

            int indexMinCount = -1;
            int minCount = Integer.MAX_VALUE;

            for (int j = 0; j < 9; j++) {
                if (usedIndices[j])
                    continue;

                ItemStack item = container.getItem(j);

                if ((indexMinCount == -1 || item.getCount() < minCount) && input.input().test(item)
                        && item.getCount() >= input.count()) {
                    indexMinCount = j;
                    minCount = item.getCount();
                }
            }

            if (indexMinCount == -1)
                return false; // Ingredient did not match any item

            usedIndices[indexMinCount] = true;
        }

        for (boolean usedIndex : usedIndices)
            if (!usedIndex) // Unused items present
                return false;

        return true;
    }

    @Override
    public ItemStack assemble(ExampleCraftingRecipeInput container, HolderLookup.Provider registries) {
        return output;
    }

    public ItemStack assembleByproduct(ExampleCraftingRecipeInput container, HolderLookup.Provider registries) {
        return byproduct;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return output.copy();
    }

    public ItemStack getResultByProduct(HolderLookup.Provider registries) {
        return byproduct.copy();
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(OmniaEtNihil.EXAMPLE_CRAFTER_ITEM.get());
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return OmniaEtNihil.EXAMPLE_CRAFTING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return OmniaEtNihil.EXAMPLE_CRAFTING_TYPE.get();
    }

    public static final class Serializer implements RecipeSerializer<ExampleCraftingRecipe> {
        public Serializer() {
        }

        private final MapCodec<ExampleCraftingRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
            return instance.group(CodecFix.ITEM_STACK_CODEC.fieldOf("output").forGetter((recipe) -> {
                return recipe.output;
            }), CodecFix.ITEM_STACK_CODEC.fieldOf("byproduct").forGetter((recipe) -> {
                return recipe.byproduct;
            }), new ArrayCodec<>(IngredientWithCount.CODEC, IngredientWithCount[]::new).fieldOf("inputs")
                    .forGetter((recipe) -> {
                        return recipe.inputs;
                    }), ExtraCodecs.POSITIVE_INT.fieldOf("ticks").forGetter((recipe) -> {
                        return recipe.ticks;
                    })).apply(instance, ExampleCraftingRecipe::new);
        });

        private final StreamCodec<RegistryFriendlyByteBuf, ExampleCraftingRecipe> STREAM_CODEC = StreamCodec
                .of(Serializer::write, Serializer::read);

        @Override
        public MapCodec<ExampleCraftingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ExampleCraftingRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static ExampleCraftingRecipe read(RegistryFriendlyByteBuf buffer) {
            int len = buffer.readInt();
            IngredientWithCount[] inputs = new IngredientWithCount[len];
            for (int i = 0; i < len; i++) {
                Ingredient input = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
                int count = buffer.readInt();

                inputs[i] = new IngredientWithCount(input, count);
            }

            ItemStack output = ItemStack.OPTIONAL_STREAM_CODEC.decode(buffer);
            ItemStack byproduct = ItemStack.OPTIONAL_STREAM_CODEC.decode(buffer);

            int time = buffer.readInt();

            return new ExampleCraftingRecipe(output, byproduct, inputs, time);
        }

        private static void write(RegistryFriendlyByteBuf buffer, ExampleCraftingRecipe recipe) {
            buffer.writeInt(recipe.inputs.length);
            for (int i = 0; i < recipe.inputs.length; i++) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.inputs[i].input());
                buffer.writeInt(recipe.inputs[i].count());
            }

            ItemStack.OPTIONAL_STREAM_CODEC.encode(buffer, recipe.output);
            ItemStack.OPTIONAL_STREAM_CODEC.encode(buffer, recipe.byproduct);

            buffer.writeInt(recipe.ticks);
        }
    }
}