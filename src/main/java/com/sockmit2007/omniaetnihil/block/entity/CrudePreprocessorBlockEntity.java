package com.sockmit2007.omniaetnihil.block.entity;

import javax.annotation.Nullable;

import com.sockmit2007.omniaetnihil.OmniaEtNihil;
import com.sockmit2007.omniaetnihil.recipe.PreprocessingRecipe;
import com.sockmit2007.omniaetnihil.recipe.PreprocessingRecipeInput;
import com.sockmit2007.omniaetnihil.screen.CrudePreprocessorMenu;
import com.sockmit2007.omniaetnihil.util.NBTUtil;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.IFluidTank;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;

public class CrudePreprocessorBlockEntity extends BaseContainerBlockEntity implements RecipeCraftingHolder, IFluidTank, IFluidHandler, IEnergyStorage, IItemHandler {
    protected NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
    int craftingProgress;
    int craftingTotalTime;
    int currentEnergy;
    public static final int MAX_ENERGY = 1000;
    FluidStack currentFluid = FluidStack.EMPTY;
    public static final int MAX_FLUID = 1000;
    public static final int TIER = 1;
    protected final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int slot) {
            switch (slot) {
            case 0:
                return CrudePreprocessorBlockEntity.this.craftingProgress;
            case 1:
                return CrudePreprocessorBlockEntity.this.craftingTotalTime;
            case 2:
                return CrudePreprocessorBlockEntity.this.currentEnergy;
            case 3:
                return CrudePreprocessorBlockEntity.this.currentFluid.getAmount();
            default:
                return 0;
            }
        }

        @Override
        public void set(int slot, int newValue) {
            switch (slot) {
            case 0:
                CrudePreprocessorBlockEntity.this.craftingProgress = newValue;
                break;
            case 1:
                CrudePreprocessorBlockEntity.this.craftingTotalTime = newValue;
                break;
            case 2:
                CrudePreprocessorBlockEntity.this.currentEnergy = newValue;
                break;
            case 3:
                CrudePreprocessorBlockEntity.this.currentFluid.setAmount(newValue);
                break;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();
    private final RecipeManager.CachedCheck<PreprocessingRecipeInput, PreprocessingRecipe> quickCheck;

    public CrudePreprocessorBlockEntity() {
        this(OmniaEtNihil.CRUDE_PREPROCESSOR_BLOCK_ENTITY.get(), BlockPos.ZERO, OmniaEtNihil.CRUDE_PREPROCESSOR.get().defaultBlockState());
    }

    public CrudePreprocessorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        this(OmniaEtNihil.CRUDE_PREPROCESSOR_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    public CrudePreprocessorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.quickCheck = RecipeManager.createCheck(OmniaEtNihil.PREPROCESSING_TYPE.get());
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("menu." + OmniaEtNihil.MODID + ".crude_preprocessor");
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return new CrudePreprocessorMenu(pContainerId, pInventory, this, this.dataAccess);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registry) {
        super.loadAdditional(tag, registry);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        this.craftingProgress = tag.getInt("CraftTime");
        this.craftingTotalTime = tag.getInt("CraftTimeTotal");
        this.currentEnergy = tag.getInt("Energy");

        ContainerHelper.loadAllItems(tag, this.items, registry);

        NBTUtil.setFluidStackIfPresent(registry, tag, "Fluid", value -> this.setFluid(value));
    }

    public void setFluid(FluidStack fluidStack) {
        this.currentFluid = fluidStack;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registry) {
        super.saveAdditional(tag, registry);
        tag.putInt("CookTime", this.craftingProgress);
        tag.putInt("CookTimeTotal", this.craftingTotalTime);
        tag.putInt("Energy", this.currentEnergy);
        ContainerHelper.saveAllItems(tag, this.items, registry);

        tag.put("Fluid", this.currentFluid.saveOptional(registry));

    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    protected FluidStack getFluidForCrafting() {
        return this.currentFluid;
    }

    protected ItemStack getItemForCrafting() {
        return this.items.get(0);
    }

    @Override
    protected void setItems(NonNullList<ItemStack> pItems) {
        this.items = pItems;
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    public boolean isCrafting() {
        return this.craftingProgress > 0;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        return this.items.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        return ContainerHelper.removeItem(this.items, index, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ContainerHelper.takeItem(this.items, index);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        this.items.set(index, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    public @Nullable IFluidHandler getFluidHandler(@Nullable Direction side) {
        return this;
    }

    public @Nullable IEnergyStorage getEnergyStorage(@Nullable Direction side) {
        return this;
    }

    public @Nullable IItemHandler getInventory(@Nullable Direction side) {
        return this;
    }

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, CrudePreprocessorBlockEntity blockEntity) {
        RecipeHolder<PreprocessingRecipe> recipeHolder = blockEntity.quickCheck.getRecipeFor(new PreprocessingRecipeInput(blockEntity.getItemForCrafting(), blockEntity.getFluidForCrafting(), TIER), pLevel).orElse(null);
        int i = blockEntity.getMaxStackSize();

        if (blockEntity.canProcess(pLevel.registryAccess(), recipeHolder, blockEntity.items, blockEntity.currentFluid, i)) {
            if (blockEntity.craftingProgress == 0) {
                blockEntity.craftingTotalTime = getTotalProcessingTime(pLevel, blockEntity);
            }

            // OmniaEtNihil.LOGGER.info("Lets a go mario " + blockEntity.craftingProgress + "/" + blockEntity.craftingTotalTime);
            ++blockEntity.craftingProgress;
            if (blockEntity.craftingProgress == blockEntity.craftingTotalTime) {
                blockEntity.craftingProgress = 0;
                blockEntity.craftingTotalTime = getTotalProcessingTime(pLevel, blockEntity);
                if (blockEntity.process(pLevel.registryAccess(), recipeHolder, blockEntity.items, blockEntity.currentFluid, i)) {
                    blockEntity.setRecipeUsed(recipeHolder);
                }
            }
        } else {
            blockEntity.craftingProgress = 0;
        }
    }

    private boolean canProcess(RegistryAccess registryAccess, @Nullable RecipeHolder<PreprocessingRecipe> recipeHolder, NonNullList<ItemStack> stacks, FluidStack fluidStack, int maxStackSize) {
        if (!(stacks.get(0).isEmpty() && fluidStack.isEmpty()) && recipeHolder != null) {
            PreprocessingRecipeInput input = new PreprocessingRecipeInput(stacks.get(0), fluidStack, TIER);
            ItemStack result = recipeHolder.value().assemble(input, registryAccess);
            ItemStack byProduct = recipeHolder.value().assembleByproduct(input, registryAccess);

            if (result.isEmpty() && byProduct.isEmpty()) {
                return false;
            } else {
                ItemStack inResultSlot = stacks.get(1);
                ItemStack inByProductSlot = stacks.get(2);
                boolean resultSloter = false;

                if (inByProductSlot.isEmpty()) {
                    resultSloter = true;
                } else if (!ItemStack.isSameItem(inByProductSlot, byProduct)) {
                    resultSloter = false;
                } else if (inByProductSlot.getCount() + byProduct.getCount() <= maxStackSize && inByProductSlot.getCount() + byProduct.getCount() <= inByProductSlot.getMaxStackSize()) {
                    resultSloter = true;
                } else {
                    resultSloter = inByProductSlot.getCount() + byProduct.getCount() <= byProduct.getMaxStackSize();
                }

                if (!resultSloter) {
                    return false;
                }

                if (inResultSlot.isEmpty()) {
                    return true;
                } else if (!ItemStack.isSameItem(inResultSlot, result)) {
                    return false;
                } else if (inResultSlot.getCount() + result.getCount() <= maxStackSize && inResultSlot.getCount() + result.getCount() <= inResultSlot.getMaxStackSize()) {
                    return true;
                } else {
                    return inResultSlot.getCount() + result.getCount() <= result.getMaxStackSize();
                }
            }
        } else {
            return false;
        }
    }

    private boolean process(RegistryAccess registryAccess, @Nullable RecipeHolder<PreprocessingRecipe> recipeHolder, NonNullList<ItemStack> stacks, FluidStack fluidStack, int maxStackSize) {
        if (recipeHolder != null && this.canProcess(registryAccess, recipeHolder, stacks, fluidStack, maxStackSize)) {
            PreprocessingRecipeInput input = new PreprocessingRecipeInput(stacks.get(0), fluidStack, TIER);
            ItemStack result = recipeHolder.value().assemble(input, registryAccess);
            ItemStack output = stacks.get(1);
            if (output.isEmpty()) {
                stacks.set(1, result.copy());
            } else if (output.is(result.getItem())) {
                output.grow(result.getCount());
            }

            ItemStack byProduct = recipeHolder.value().assembleByproduct(input, registryAccess);
            ItemStack byProductOutput = stacks.get(2);
            if (byProductOutput.isEmpty()) {
                stacks.set(2, byProduct.copy());
            } else if (byProductOutput.is(byProduct.getItem())) {
                byProductOutput.grow(byProduct.getCount());
            }

            fluidStack.shrink(recipeHolder.value().getFluidInput().getAmount());
            stacks.get(0).shrink(1);

            return true;
        } else {
            return false;
        }
    }

    private static int getTotalProcessingTime(Level level, CrudePreprocessorBlockEntity blockEntity) {
        return blockEntity.quickCheck.getRecipeFor(new PreprocessingRecipeInput(blockEntity.getItemForCrafting(), blockEntity.getFluidForCrafting(), TIER), level).map(recipeHolder -> recipeHolder.value().getTicks()).orElse(200);
    }

    @Override
    public void setRecipeUsed(@Nullable RecipeHolder<?> recipeHolder) {
        if (recipeHolder != null) {
            ResourceLocation location = recipeHolder.id();
            this.recipesUsed.addTo(location, 1);
        }
    }

    @Nullable @Override
    public RecipeHolder<?> getRecipeUsed() {
        return null;
    }

    // #region Energy Methods
    @Override
    public boolean canExtract() {
        return currentEnergy != 0;
    }

    @Override
    public boolean canReceive() {
        return currentEnergy < MAX_ENERGY;
    }

    @Override
    public int extractEnergy(int toExtract, boolean simulate) {
        int extractable = currentEnergy - Math.max(currentEnergy - toExtract, 0);
        if (!simulate)
            currentEnergy -= extractable;
        return extractable;
    }

    @Override
    public int getEnergyStored() {
        return currentEnergy;
    }

    @Override
    public int getMaxEnergyStored() {
        return MAX_ENERGY;
    }

    @Override
    public int receiveEnergy(int toReceive, boolean simulate) {
        int receivable = Math.min(MAX_ENERGY - currentEnergy, toReceive);
        if (!simulate)
            currentEnergy += receivable;
        return receivable;
    }
    // #endregion

    // #region Item Methods
    @Override
    public int getSlots() {
        return 3;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (isItemValid(slot, stack)) {
            int canInsert = Math.min(stack.getCount(), getSlotLimit(slot) - getStackInSlot(slot).getCount());

            if (!simulate) {
                if (items.get(slot).isEmpty()) {
                    items.set(slot, new ItemStack(stack.getItem(), canInsert));
                } else {
                    items.get(slot).grow(canInsert);
                }
            }

            if (canInsert >= stack.getCount()) {
                return ItemStack.EMPTY;
            } else {
                ItemStack returner = stack.copy();
                returner.shrink(canInsert);
                return returner;
            }
        }

        return stack;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack export = items.get(slot);

        if (export.isEmpty() || slot == 0) {
            return ItemStack.EMPTY;
        }

        int canExtractify = Math.min(export.getCount(), amount);

        if (!simulate) {
            export.shrink(canExtractify);
        }

        return new ItemStack(export.getItem(), canExtractify);
    }

    @Override
    public int getSlotLimit(int slot) {
        return items.get(slot).getMaxStackSize();
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return !(slot != 0 || !(items.get(slot).isEmpty() || ItemStack.isSameItem(items.get(slot), stack)));
    }
    // #endregion

    // #region Fluid Methods
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        int drainable = Math.min(currentFluid.getAmount(), maxDrain);
        if (!action.simulate()) {
            currentFluid.shrink(drainable);
        }
        FluidStack copy = currentFluid.copy();
        copy.setAmount(drainable);
        return copy;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return drain(resource.getAmount(), action);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        int fillable = Math.min(MAX_FLUID - currentFluid.getAmount(), resource.getAmount());
        if (!action.simulate()) {
            currentFluid = resource.copy();
            currentFluid.setAmount(fillable);
        }
        return fillable;
    }

    @Override
    public int getCapacity() {
        return MAX_FLUID;
    }

    @Override
    public FluidStack getFluid() {
        return currentFluid;
    }

    @Override
    public int getFluidAmount() {
        return currentFluid.getAmount();
    }

    @Override
    public boolean isFluidValid(FluidStack stack) {
        return true;
    }
    // #endregion

    @Override
    public FluidStack getFluidInTank(int tank) {
        return currentFluid;
    }

    @Override
    public int getTankCapacity(int tank) {
        return MAX_FLUID;
    }

    @Override
    public int getTanks() {
        return currentFluid.getAmount();
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return isFluidValid(stack);
    }
}
