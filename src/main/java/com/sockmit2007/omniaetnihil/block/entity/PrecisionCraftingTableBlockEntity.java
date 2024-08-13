package com.sockmit2007.omniaetnihil.block.entity;

import java.util.List;

import javax.annotation.Nullable;

import com.sockmit2007.omniaetnihil.OmniaEtNihil;
import com.sockmit2007.omniaetnihil.mana.IManaHandler;
import com.sockmit2007.omniaetnihil.quantum.IQuantumHandler;
import com.sockmit2007.omniaetnihil.fluid.FluidContainer;
import com.sockmit2007.omniaetnihil.recipe.PrecisionCraftingRecipe;
import com.sockmit2007.omniaetnihil.recipe.PrecisionCraftingRecipeInput;
import com.sockmit2007.omniaetnihil.screen.PrecisionCraftingTableMenu;
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
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;

public class PrecisionCraftingTableBlockEntity extends BaseContainerBlockEntity implements RecipeCraftingHolder, IManaHandler, IFluidHandler, IEnergyStorage, IQuantumHandler, IItemHandler {
    protected NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
    protected NonNullList<FluidContainer> fluids = NonNullList.withSize(6, new FluidContainer());
    int craftingProgress;
    int craftingTotalTime;
    int currentEnergy;
    public static final int MAX_ENERGY = 1000;
    int currentMana;
    public static final int MAX_MANA = 1000;
    int currentQuantum;
    public static final int MAX_QUANTUM = 1000;
    public static final int TIER = 1;
    protected final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int slot) {
            switch (slot) {
            case 0:
                return PrecisionCraftingTableBlockEntity.this.craftingProgress;
            case 1:
                return PrecisionCraftingTableBlockEntity.this.craftingTotalTime;
            case 2:
                return PrecisionCraftingTableBlockEntity.this.currentEnergy;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                return PrecisionCraftingTableBlockEntity.this.fluids.get(slot - 3).getFluid().getAmount();
            case 9:
                return PrecisionCraftingTableBlockEntity.this.currentMana;
            case 10:
                return PrecisionCraftingTableBlockEntity.this.currentQuantum;
            default:
                return 0;
            }
        }

        @Override
        public void set(int slot, int newValue) {
            switch (slot) {
            case 0:
                PrecisionCraftingTableBlockEntity.this.craftingProgress = newValue;
                break;
            case 1:
                PrecisionCraftingTableBlockEntity.this.craftingTotalTime = newValue;
                break;
            case 2:
                PrecisionCraftingTableBlockEntity.this.currentEnergy = newValue;
                break;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                PrecisionCraftingTableBlockEntity.this.fluids.get(slot - 3).getFluid().setAmount(newValue);
                break;
            case 9:
                PrecisionCraftingTableBlockEntity.this.currentMana = newValue;
                break;
            case 10:
                PrecisionCraftingTableBlockEntity.this.currentQuantum = newValue;
                break;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();
    private final RecipeManager.CachedCheck<PrecisionCraftingRecipeInput, PrecisionCraftingRecipe> quickCheck;

    public PrecisionCraftingTableBlockEntity() {
        this(OmniaEtNihil.PRECISION_CRAFTING_TABLE_BLOCK_ENTITY.get(), BlockPos.ZERO, OmniaEtNihil.PRECISION_CRAFTING_TABLE.get().defaultBlockState());
    }

    public PrecisionCraftingTableBlockEntity(BlockPos pPos, BlockState pBlockState) {
        this(OmniaEtNihil.PRECISION_CRAFTING_TABLE_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    public PrecisionCraftingTableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.quickCheck = RecipeManager.createCheck(OmniaEtNihil.PRECISION_CRAFTING_TYPE.get());
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("menu." + OmniaEtNihil.MODID + ".precision_crafting_table");
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return new PrecisionCraftingTableMenu(pContainerId, pInventory, this, this.dataAccess);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registry) {
        super.loadAdditional(tag, registry);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        this.craftingProgress = tag.getInt("CraftTime");
        this.craftingTotalTime = tag.getInt("CraftTimeTotal");
        this.currentEnergy = tag.getInt("Energy");
        this.currentMana = tag.getInt("Mana");
        this.currentQuantum = tag.getInt("Quantum");

        ContainerHelper.loadAllItems(tag, this.items, registry);

        NBTUtil.setFluidStackIfPresent(registry, tag, "Fluid0", value -> this.setFluid(value, 0));
        NBTUtil.setFluidStackIfPresent(registry, tag, "Fluid1", value -> this.setFluid(value, 1));
        NBTUtil.setFluidStackIfPresent(registry, tag, "Fluid2", value -> this.setFluid(value, 2));
        NBTUtil.setFluidStackIfPresent(registry, tag, "Fluid3", value -> this.setFluid(value, 3));
        NBTUtil.setFluidStackIfPresent(registry, tag, "Fluid4", value -> this.setFluid(value, 4));
        NBTUtil.setFluidStackIfPresent(registry, tag, "Fluid5", value -> this.setFluid(value, 5));
    }

    public void setFluid(FluidStack fluidStack, int tank) {
        this.fluids.get(tank).setFluid(fluidStack);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registry) {
        super.saveAdditional(tag, registry);
        tag.putInt("CookTime", this.craftingProgress);
        tag.putInt("CookTimeTotal", this.craftingTotalTime);
        tag.putInt("Energy", this.currentEnergy);
        tag.putInt("Mana", this.currentMana);
        tag.putInt("Quantum", this.currentQuantum);
        ContainerHelper.saveAllItems(tag, this.items, registry);

        tag.put("Fluid0", this.fluids.get(0).getFluid().saveOptional(registry));
        tag.put("Fluid1", this.fluids.get(1).getFluid().saveOptional(registry));
        tag.put("Fluid2", this.fluids.get(2).getFluid().saveOptional(registry));
        tag.put("Fluid3", this.fluids.get(3).getFluid().saveOptional(registry));
        tag.put("Fluid4", this.fluids.get(4).getFluid().saveOptional(registry));
        tag.put("Fluid5", this.fluids.get(5).getFluid().saveOptional(registry));
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    protected NonNullList<FluidStack> getFluidsForCrafting() {
        NonNullList<FluidStack> fluidStacks = NonNullList.withSize(6, FluidStack.EMPTY);

        for (int i = 0; i < 6; i++) {
            fluidStacks.set(i, this.fluids.get(i).getFluid());
        }

        return fluidStacks;
    }

    protected NonNullList<ItemStack> getItemsForCrafting() {
        // Return only the first 81 items as a NonNullList
        List<ItemStack> list = this.items.subList(0, 81);
        NonNullList<ItemStack> list2 = NonNullList.withSize(list.size(), list.get(0));

        for (int i = 0; i < list.size(); i++) {
            list2.set(i, list.get(i));
        }

        return list2;
    }

    protected NonNullList<ItemStack> getToolsForCrafting() {
        // Return only the first 6 items as a NonNullList
        List<ItemStack> list = this.items.subList(81, 87);
        NonNullList<ItemStack> list2 = NonNullList.withSize(list.size(), list.get(0));

        for (int i = 0; i < list.size(); i++) {
            list2.set(i, list.get(i));
        }

        return list2;
    }

    protected NonNullList<ItemStack> getMicroToolsForCrafting() {
        // Return only the first 4 items as a NonNullList
        List<ItemStack> list = this.items.subList(87, 91);
        NonNullList<ItemStack> list2 = NonNullList.withSize(list.size(), list.get(0));

        for (int i = 0; i < list.size(); i++) {
            list2.set(i, list.get(i));
        }

        return list2;
    }

    protected NonNullList<ItemStack> getMicroComponentsForCrafting() {
        // Return only the first 4 items as a NonNullList
        List<ItemStack> list = this.items.subList(91, 99);
        NonNullList<ItemStack> list2 = NonNullList.withSize(list.size(), list.get(0));

        for (int i = 0; i < list.size(); i++) {
            list2.set(i, list.get(i));
        }

        return list2;
    }

    protected NonNullList<ItemStack> getStabilizersForCrafting() {
        // Return only the first 4 items as a NonNullList
        List<ItemStack> list = this.items.subList(99, 103);
        NonNullList<ItemStack> list2 = NonNullList.withSize(list.size(), list.get(0));

        for (int i = 0; i < list.size(); i++) {
            list2.set(i, list.get(i));
        }

        return list2;
    }

    protected NonNullList<ItemStack> getModulatorsForCrafting() {
        // Return only the first 3 items as a NonNullList
        List<ItemStack> list = this.items.subList(103, 106);
        NonNullList<ItemStack> list2 = NonNullList.withSize(list.size(), list.get(0));

        for (int i = 0; i < list.size(); i++) {
            list2.set(i, list.get(i));
        }

        return list2;
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

    // #region Getters
    public @Nullable IFluidHandler getFluidHandler(@Nullable Direction side) {
        return this;
    }

    public @Nullable IEnergyStorage getEnergyStorage(@Nullable Direction side) {
        return this;
    }

    public @Nullable IItemHandler getInventory(@Nullable Direction side) {
        return this;
    }

    public @Nullable IManaHandler getManaHandler(@Nullable Direction side) {
        return this;
    }

    public @Nullable IQuantumHandler getQuantumHandler(@Nullable Direction side) {
        return this;
    }

    // #endregion

    // #region Crafting
    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, PrecisionCraftingTableBlockEntity blockEntity) {
        RecipeHolder<PrecisionCraftingRecipe> recipeHolder = blockEntity.quickCheck.getRecipeFor(new PrecisionCraftingRecipeInput(blockEntity.getItemsForCrafting(), blockEntity.getToolsForCrafting(), blockEntity.getFluidsForCrafting(), blockEntity.getMicroToolsForCrafting(), blockEntity.getMicroComponentsForCrafting(), blockEntity.getStabilizersForCrafting(), blockEntity.getModulatorsForCrafting(), blockEntity.getEnergyStored(), blockEntity.getManaStored(), blockEntity.getQuantumStored()), pLevel).orElse(null);
        int i = blockEntity.getMaxStackSize();

        if (blockEntity.canProcess(pLevel.registryAccess(), recipeHolder, blockEntity.items, blockEntity.fluids, i)) {
            if (blockEntity.craftingProgress == 0) {
                blockEntity.craftingTotalTime = getTotalProcessingTime(pLevel, blockEntity);
            }

            // OmniaEtNihil.LOGGER.info("Lets a go mario " + blockEntity.craftingProgress + "/" + blockEntity.craftingTotalTime);
            ++blockEntity.craftingProgress;
            if (blockEntity.craftingProgress == blockEntity.craftingTotalTime) {
                blockEntity.craftingProgress = 0;
                blockEntity.craftingTotalTime = getTotalProcessingTime(pLevel, blockEntity);
                if (blockEntity.process(pLevel.registryAccess(), recipeHolder, blockEntity.items, blockEntity.fluids, i)) {
                    blockEntity.setRecipeUsed(recipeHolder);
                }
            }
        } else {
            blockEntity.craftingProgress = 0;
        }
    }

    private boolean canProcess(RegistryAccess registryAccess, @Nullable RecipeHolder<PrecisionCraftingRecipe> recipeHolder, NonNullList<ItemStack> stacks, NonNullList<FluidContainer> fluids, int maxStackSize) {
        if (!(stacks.get(0).isEmpty() && fluids.isEmpty()) && recipeHolder != null) {
            PrecisionCraftingRecipeInput input = new PrecisionCraftingRecipeInput(getItemsForCrafting(), getToolsForCrafting(), getFluidsForCrafting(), getMicroToolsForCrafting(), getMicroComponentsForCrafting(), getStabilizersForCrafting(), getModulatorsForCrafting(), getEnergyStored(), getManaStored(), getQuantumStored());
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

    private boolean process(RegistryAccess registryAccess, @Nullable RecipeHolder<PrecisionCraftingRecipe> recipeHolder, NonNullList<ItemStack> stacks, NonNullList<FluidContainer> fluids, int maxStackSize) {
        if (recipeHolder != null && this.canProcess(registryAccess, recipeHolder, stacks, fluids, maxStackSize)) {
            PrecisionCraftingRecipeInput input = new PrecisionCraftingRecipeInput(getItemsForCrafting(), getToolsForCrafting(), getFluidsForCrafting(), getMicroToolsForCrafting(), getMicroComponentsForCrafting(), getStabilizersForCrafting(), getModulatorsForCrafting(), getEnergyStored(), getManaStored(), getQuantumStored());
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

            // fluids.shrink(recipeHolder.value().getFluidInput().getAmount());
            stacks.get(0).shrink(1);

            return true;
        } else {
            return false;
        }
    }

    private static int getTotalProcessingTime(Level level, PrecisionCraftingTableBlockEntity blockEntity) {
        return blockEntity.quickCheck.getRecipeFor(new PrecisionCraftingRecipeInput(blockEntity.getItemsForCrafting(), blockEntity.getToolsForCrafting(), blockEntity.getFluidsForCrafting(), blockEntity.getMicroToolsForCrafting(), blockEntity.getMicroComponentsForCrafting(), blockEntity.getStabilizersForCrafting(), blockEntity.getModulatorsForCrafting(), blockEntity.getEnergyStored(), blockEntity.getManaStored(), blockEntity.getQuantumStored()), level).map(recipeHolder -> recipeHolder.value().getTicks()).orElse(200);
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
    // #endregion

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
    public FluidStack getFluidInTank(int tank) {
        return fluids.get(tank).getFluid();
    }

    @Override
    public int getTankCapacity(int tank) {
        return fluids.get(tank).getCapacity();
    }

    @Override
    public int getTanks() {
        return 6;
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return fluids.get(tank).isFluidValid(stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fill'");
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'drain'");
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'drain'");
    }
    // #endregion

    // #region Mana
    @Override
    public int receiveMana(int toReceive, boolean simulate) {
        int receivable = Math.min(getMaxManaStored() - getManaStored(), toReceive);
        if (!simulate) {
            currentMana += receivable;
        }
        return receivable;
    }

    @Override
    public int extractMana(int toExtract, boolean simulate) {
        int extractable = Math.min(getManaStored(), toExtract);
        if (!simulate) {
            currentMana -= extractable;
        }
        return extractable;
    }

    @Override
    public int getManaStored() {
        return currentMana;
    }

    @Override
    public int getMaxManaStored() {
        return MAX_MANA;
    }

    @Override
    public boolean canExtractMana() {
        if (currentMana > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean canReceiveMana() {
        if (currentMana < MAX_MANA) {
            return true;
        }
        return false;
    }
    // #endregion

    // #region Quantum Methods
    @Override
    public int receiveQuantum(int toReceive, boolean simulate) {
        int receivable = Math.min(getMaxQuantumStored() - getQuantumStored(), toReceive);
        if (!simulate) {
            currentQuantum += receivable;
        }
        return receivable;
    }

    @Override
    public int extractQuantum(int toExtract, boolean simulate) {
        int extractable = Math.min(getQuantumStored(), toExtract);
        if (!simulate) {
            currentQuantum -= extractable;
        }
        return extractable;
    }

    @Override
    public int getQuantumStored() {
        return currentQuantum;
    }

    @Override
    public int getMaxQuantumStored() {
        return MAX_QUANTUM;
    }

    @Override
    public boolean canExtractQuantum() {
        if (currentQuantum > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean canReceiveQuantum() {
        if (currentQuantum < MAX_QUANTUM) {
            return true;
        }
        return false;
    }
    // #endregion
}
