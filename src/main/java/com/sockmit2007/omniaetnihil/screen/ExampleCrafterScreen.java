package com.sockmit2007.omniaetnihil.screen;

import com.sockmit2007.omniaetnihil.OmniaEtNihil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ExampleCrafterScreen extends AbstractContainerScreen<ExampleCrafterMenu> {
    private static final ResourceLocation EXAMPLE_CRAFTER_TEXTURE = ResourceLocation.fromNamespaceAndPath(OmniaEtNihil.MODID, "textures/gui/menu/example_crafter.png");
    // private static final ResourceLocation ITEM_SLOT_SPRITE =
    // ResourceLocation.fromNamespaceAndPath(OmniaEtNihil.MODID,
    // "textures/gui/menu/item_slot.png");

    public ExampleCrafterScreen(ExampleCrafterMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.imageWidth = 176;
        this.imageHeight = 214;
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
        guiGraphics.blit(EXAMPLE_CRAFTER_TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight);

        int offsetY = (int) (20.0 * (1 - this.getMenu().getBurnProgress()));
        int sizeY = 20 - offsetY;

        // OmniaEtNihil.LOGGER.info("burnProgress: " + this.getMenu().getBurnProgress() + "offsetY: " + offsetY + " sizeY: " + sizeY);

        guiGraphics.blit(EXAMPLE_CRAFTER_TEXTURE, i + 136, j + 56 + offsetY, 176, offsetY, 6, sizeY);

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
