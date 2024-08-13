package com.sockmit2007.omniaetnihil.block.entity;

import javax.annotation.Nullable;

import com.sockmit2007.omniaetnihil.OmniaEtNihil;
import com.sockmit2007.omniaetnihil.recipe.ExampleCraftingRecipe;
import com.sockmit2007.omniaetnihil.recipe.ExampleCraftingRecipeInput;
import com.sockmit2007.omniaetnihil.screen.ExampleCrafterMenu;

import java.util.List;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import mod.azure.azurelib.common.api.common.animatable.GeoBlockEntity;
import mod.azure.azurelib.common.internal.common.util.AzureLibUtil;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager.ControllerRegistrar;
import mod.azure.azurelib.core.object.PlayState;
import net.minecraft.core.BlockPos;
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
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;

public class ExampleCrafterBlockEntity extends BaseContainerBlockEntity implements GeoBlockEntity, RecipeCraftingHolder {
    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

    protected NonNullList<ItemStack> items = NonNullList.withSize(11, ItemStack.EMPTY);
    int craftingProgress;
    int craftingTotalTime;
    protected final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int slot) {
            switch (slot) {
            case 0:
                return ExampleCrafterBlockEntity.this.craftingProgress;
            case 1:
                return ExampleCrafterBlockEntity.this.craftingTotalTime;
            default:
                return 0;
            }
        }

        @Override
        public void set(int slot, int newValue) {
            switch (slot) {
            case 0:
                ExampleCrafterBlockEntity.this.craftingProgress = newValue;
                break;
            case 1:
                ExampleCrafterBlockEntity.this.craftingTotalTime = newValue;
                break;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();
    private final RecipeManager.CachedCheck<ExampleCraftingRecipeInput, ExampleCraftingRecipe> quickCheck;

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "animationController", event -> PlayState.CONTINUE).triggerableAnim("idle", RawAnimation.begin().thenPlayAndHold("idle")).triggerableAnim("crafting", RawAnimation.begin().thenLoop("crafting")));
    }

    public ExampleCrafterBlockEntity() {
        this(OmniaEtNihil.EXAMPLE_CRAFTER_BLOCK_ENTITY.get(), BlockPos.ZERO, OmniaEtNihil.EXAMPLE_CRAFTER.get().defaultBlockState());
    }

    public ExampleCrafterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        this(OmniaEtNihil.EXAMPLE_CRAFTER_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    public ExampleCrafterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.quickCheck = RecipeManager.createCheck(OmniaEtNihil.EXAMPLE_CRAFTING_TYPE.get());
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("menu." + OmniaEtNihil.MODID + ".example_crafter");
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return new ExampleCrafterMenu(pContainerId, pInventory, this, this.dataAccess);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registry) {
        super.loadAdditional(tag, registry);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        this.craftingProgress = tag.getInt("CraftTime");
        this.craftingTotalTime = tag.getInt("CraftTimeTotal");
        ContainerHelper.loadAllItems(tag, this.items, registry);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registry) {
        super.saveAdditional(tag, registry);
        tag.putInt("CookTime", this.craftingProgress);
        tag.putInt("CookTimeTotal", this.craftingTotalTime);
        ContainerHelper.saveAllItems(tag, this.items, registry);
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    protected NonNullList<ItemStack> getItemsForCrafting() {
        // Return only the first 9 items as a NonNullList
        List<ItemStack> list = this.items.subList(0, 9);
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

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, ExampleCrafterBlockEntity blockEntity) {
        RecipeHolder<ExampleCraftingRecipe> recipeHolder = blockEntity.quickCheck.getRecipeFor(new ExampleCraftingRecipeInput(List.copyOf(blockEntity.getItemsForCrafting())), pLevel).orElse(null);
        int i = blockEntity.getMaxStackSize();

        if (blockEntity.canProcess(pLevel.registryAccess(), recipeHolder, blockEntity.items, i)) {
            if (blockEntity.craftingProgress == 0) {
                blockEntity.triggerAnim("animationController", "crafting");
                blockEntity.craftingTotalTime = getTotalProcessingTime(pLevel, blockEntity);
            }

            // OmniaEtNihil.LOGGER.info("Lets a go mario " + blockEntity.craftingProgress + "/" + blockEntity.craftingTotalTime);
            ++blockEntity.craftingProgress;
            if (blockEntity.craftingProgress == blockEntity.craftingTotalTime) {
                blockEntity.craftingProgress = 0;
                blockEntity.craftingTotalTime = getTotalProcessingTime(pLevel, blockEntity);
                if (blockEntity.process(pLevel.registryAccess(), recipeHolder, blockEntity.items, i)) {
                    blockEntity.setRecipeUsed(recipeHolder);
                }
            }
        } else {
            if (blockEntity.getLevel().getGameTime() % 80L == 0L)
                blockEntity.triggerAnim("animationController", "idle");

            blockEntity.craftingProgress = 0;
        }
    }

    private boolean canProcess(RegistryAccess registryAccess, @Nullable RecipeHolder<ExampleCraftingRecipe> recipeHolder, NonNullList<ItemStack> stacks, int maxStackSize) {
        if (!(stacks.get(0).isEmpty() && stacks.get(1).isEmpty() && stacks.get(2).isEmpty() && stacks.get(3).isEmpty() && stacks.get(4).isEmpty() && stacks.get(5).isEmpty() && stacks.get(6).isEmpty() && stacks.get(7).isEmpty() && stacks.get(8).isEmpty()) && recipeHolder != null) {
            ItemStack result = recipeHolder.value().assemble(new ExampleCraftingRecipeInput(List.copyOf(this.getItemsForCrafting())), registryAccess);
            ItemStack byProduct = recipeHolder.value().assembleByproduct(new ExampleCraftingRecipeInput(List.copyOf(this.getItemsForCrafting())), registryAccess);

            if (result.isEmpty() && byProduct.isEmpty()) {
                return false;
            } else {
                ItemStack inResultSlot = stacks.get(9);
                ItemStack inByProductSlot = stacks.get(10);
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

    private boolean process(RegistryAccess registryAccess, @Nullable RecipeHolder<ExampleCraftingRecipe> recipeHolder, NonNullList<ItemStack> stacks, int maxStackSize) {
        if (recipeHolder != null && this.canProcess(registryAccess, recipeHolder, stacks, maxStackSize)) {
            ItemStack result = recipeHolder.value().assemble(new ExampleCraftingRecipeInput(List.copyOf(this.getItemsForCrafting())), registryAccess);
            ItemStack output = stacks.get(9);
            if (output.isEmpty()) {
                stacks.set(9, result.copy());
            } else if (output.is(result.getItem())) {
                output.grow(result.getCount());
            }

            ItemStack byProduct = recipeHolder.value().assembleByproduct(new ExampleCraftingRecipeInput(List.copyOf(this.getItemsForCrafting())), registryAccess);
            ItemStack byProductOutput = stacks.get(10);
            if (byProductOutput.isEmpty()) {
                stacks.set(10, byProduct.copy());
            } else if (byProductOutput.is(byProduct.getItem())) {
                byProductOutput.grow(byProduct.getCount());
            }

            boolean[] used = new boolean[9];
            for (int i = 0; i < recipeHolder.value().getInputs().length; i++) {
                for (int j = 0; j < 9; j++) {
                    if (used[j])
                        continue;

                    if (recipeHolder.value().getInputs()[i].input().test(stacks.get(j))) {
                        ItemStack inpu = stacks.get(j);
                        inpu.shrink(recipeHolder.value().getInputs()[i].count());
                        used[j] = true;
                        break;
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    private static int getTotalProcessingTime(Level level, ExampleCrafterBlockEntity blockEntity) {
        return blockEntity.quickCheck.getRecipeFor(new ExampleCraftingRecipeInput(List.copyOf(blockEntity.getItemsForCrafting())), level).map(recipeHolder -> recipeHolder.value().getTicks()).orElse(200);
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
}
