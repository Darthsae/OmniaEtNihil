package com.sockmit2007.omniaetnihil.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sockmit2007.omniaetnihil.OmniaEtNihil;
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
import net.neoforged.neoforge.fluids.FluidStack;

public class PreprocessingRecipe implements Recipe<PreprocessingRecipeInput> {
    private final ItemStack output;
    private final ItemStack byproduct;
    private final int tier;
    private final IngredientWithCount itemInput;
    private final FluidStack fluidInput;
    private final int ticks;

    public PreprocessingRecipe(ItemStack output, ItemStack byproduct, int tier, IngredientWithCount input, FluidStack fluidInput, int ticks) {
        this.output = output;
        this.byproduct = byproduct;
        this.tier = tier;
        this.itemInput = input;
        this.fluidInput = fluidInput;
        this.ticks = ticks;
    }

    public ItemStack getOutput() {
        return output;
    }

    public ItemStack getByproduct() {
        return byproduct;
    }

    public int getTier() {
        return tier;
    }

    public FluidStack getFluidInput() {
        return fluidInput;
    }

    public int getTicks() {
        return ticks;
    }

    @Override
    public boolean matches(PreprocessingRecipeInput container, Level level) {
        if (level.isClientSide)
            return false;

        if (itemInput != null && fluidInput != null) {
            return itemInput.input().test(container.getItem(0)) && itemInput.count() <= container.getItem(0).getCount() && (FluidStack.isSameFluid(container.fluidInput(), fluidInput) && container.fluidInput().getAmount() >= fluidInput.getAmount()) && container.getTier() >= tier;
        }

        return false;
    }

    @Override
    public ItemStack assemble(PreprocessingRecipeInput container, HolderLookup.Provider registries) {
        return output;
    }

    public ItemStack assembleByproduct(PreprocessingRecipeInput container, HolderLookup.Provider registries) {
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
        return new ItemStack(OmniaEtNihil.CRUDE_PREPROCESSOR_ITEM.get());
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return OmniaEtNihil.PREPROCESSING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return OmniaEtNihil.PREPROCESSING_TYPE.get();
    }

    public static final class Serializer implements RecipeSerializer<PreprocessingRecipe> {
        public Serializer() {
        }

        private final MapCodec<PreprocessingRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
            return instance.group(CodecFix.ITEM_STACK_CODEC.fieldOf("output").forGetter((recipe) -> {
                return recipe.output;
            }), CodecFix.ITEM_STACK_CODEC.fieldOf("byproduct").forGetter((recipe) -> {
                return recipe.byproduct;
            }), ExtraCodecs.POSITIVE_INT.fieldOf("tier").forGetter((recipe) -> {
                return recipe.tier;
            }), IngredientWithCount.CODEC.fieldOf("input").forGetter((recipe) -> {
                return recipe.itemInput;
            }), FluidStack.CODEC.fieldOf("fluidInput").forGetter((recipe) -> {
                return recipe.fluidInput;
            }), ExtraCodecs.POSITIVE_INT.fieldOf("ticks").forGetter((recipe) -> {
                return recipe.ticks;
            })).apply(instance, PreprocessingRecipe::new);
        });

        private final StreamCodec<RegistryFriendlyByteBuf, PreprocessingRecipe> STREAM_CODEC = StreamCodec.of(Serializer::write, Serializer::read);

        @Override
        public MapCodec<PreprocessingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, PreprocessingRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static PreprocessingRecipe read(RegistryFriendlyByteBuf buffer) {
            IngredientWithCount input = new IngredientWithCount(Ingredient.CONTENTS_STREAM_CODEC.decode(buffer), buffer.readInt());
            FluidStack fluid = FluidStack.OPTIONAL_STREAM_CODEC.decode(buffer);
            ItemStack output = ItemStack.OPTIONAL_STREAM_CODEC.decode(buffer);
            ItemStack byproduct = ItemStack.OPTIONAL_STREAM_CODEC.decode(buffer);

            int tier = buffer.readInt();

            int time = buffer.readInt();

            return new PreprocessingRecipe(output, byproduct, tier, input, fluid, time);
        }

        private static void write(RegistryFriendlyByteBuf buffer, PreprocessingRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.itemInput.input());
            buffer.writeInt(recipe.itemInput.count());

            FluidStack.OPTIONAL_STREAM_CODEC.encode(buffer, recipe.fluidInput);

            ItemStack.OPTIONAL_STREAM_CODEC.encode(buffer, recipe.output);
            ItemStack.OPTIONAL_STREAM_CODEC.encode(buffer, recipe.byproduct);

            buffer.writeInt(recipe.tier);

            buffer.writeInt(recipe.ticks);
        }
    }
}