package com.sockmit2007.omniaetnihil.screen;

import com.sockmit2007.omniaetnihil.OmniaEtNihil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class PrecisionCraftingTableScreen extends AbstractContainerScreen<PrecisionCraftingTableMenu> {
    private static final ResourceLocation PRECISION_CRAFTING_TABLE_BACKGROUND_TEXTURE = ResourceLocation.fromNamespaceAndPath(OmniaEtNihil.MODID, "textures/gui/menu/precision_crafting_table.png");
    private static final ResourceLocation PRECISION_CRAFTING_TABLE_WIDGETS_TEXTURE = ResourceLocation.fromNamespaceAndPath(OmniaEtNihil.MODID, "textures/gui/menu/precision_crafting_table_images.png");
    // private static final ResourceLocation ITEM_SLOT_SPRITE =
    // ResourceLocation.fromNamespaceAndPath(OmniaEtNihil.MODID,
    // "textures/gui/menu/item_slot.png");

    public PrecisionCraftingTableScreen(PrecisionCraftingTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.imageWidth = 242;
        this.imageHeight = 256;
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void containerTick() {
        super.containerTick();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int i = this.leftPos;
        int j = this.topPos;
        guiGraphics.blit(PRECISION_CRAFTING_TABLE_BACKGROUND_TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight);

        int offsetYEnergy = (int) (20.0 * (1 - this.getMenu().getEnergyLevel()));
        int sizeYEnergy = 20 - offsetYEnergy;

        int offsetYFluid = (int) (20.0 * (1 - this.getMenu().getFluidLevel()));
        int sizeYFluid = 20 - offsetYFluid;

        // OmniaEtNihil.LOGGER.info("burnProgress: " + this.getMenu().getBurnProgress() + "offsetY: " + offsetY + " sizeY: " + sizeY);

        guiGraphics.blit(PRECISION_CRAFTING_TABLE_BACKGROUND_TEXTURE, i + 136, j + 34 + offsetYEnergy, 176, offsetYEnergy, 6, sizeYEnergy);
        guiGraphics.blit(PRECISION_CRAFTING_TABLE_BACKGROUND_TEXTURE, i + 136, j + 78 + offsetYFluid, 176, offsetYFluid, 6, sizeYFluid);

        /*
         * int slotX = i + 49; int slotY = j + 24;
         * 
         * guiGraphics.blitSprite(ITEM_SLOT_SPRITE, slotX, slotY, 20, 20);
         * 
         * guiGraphics.blitSprite(ITEM_SLOT_SPRITE, slotX, slotY + 32, 20, 20);
         * 
         * guiGraphics.blitSprite(ITEM_SLOT_SPRITE, slotX, slotY + 64, 20, 20);
         * 
         * guiGraphics.blitSprite(ITEM_SLOT_SPRITE, slotX + 32, slotY, 20, 20);
         * 
         * guiGraphics.blitSprite(ITEM_SLOT_SPRITE, slotX + 32, slotY + 32, 20, 20);
         * 
         * guiGraphics.blitSprite(ITEM_SLOT_SPRITE, slotX + 32, slotY + 64, 20, 20);
         * 
         * guiGraphics.blitSprite(ITEM_SLOT_SPRITE, slotX + 64, slotY, 20, 20);
         * 
         * guiGraphics.blitSprite(ITEM_SLOT_SPRITE, slotX + 64, slotY + 32, 20, 20);
         * 
         * guiGraphics.blitSprite(ITEM_SLOT_SPRITE, slotX + 64, slotY + 64, 20, 20);
         */
    }
}
