package com.sockmit2007.omniaetnihil.datagen;

import com.sockmit2007.omniaetnihil.OmniaEtNihil;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, OmniaEtNihil.MODID, helper);
    }

    public String itemName(Item item) {
        ResourceLocation location = BuiltInRegistries.ITEM.getKey(item);
        if (location != null) {
            return location.getPath();
        } else {
            throw new IllegalStateException("Unknown item: " + item.toString());
        }
    }

    public void item(Item item, String location) {
        this.withExistingParent(this.itemName(item), mcLoc("item/generated")).texture("layer0", modLoc("item/" + location + this.itemName(item)));
    }

    @Override
    protected void registerModels() {
        this.item(OmniaEtNihil.ABYSSAL_FLESH.get(), "drops/");
        this.item(OmniaEtNihil.ABYSSAL_BONE.get(), "drops/");
        this.item(OmniaEtNihil.AQUATIC_FLESH.get(), "drops/");
        this.item(OmniaEtNihil.AQUATIC_BONE.get(), "drops/");
        this.item(OmniaEtNihil.ARCANE_FLESH.get(), "drops/");
        this.item(OmniaEtNihil.ARCANE_BONE.get(), "drops/");
        this.item(OmniaEtNihil.DIVINE_FLESH.get(), "drops/");
        this.item(OmniaEtNihil.DIVINE_BONE.get(), "drops/");
        this.item(OmniaEtNihil.ETHEREAL_FLESH.get(), "drops/");
        this.item(OmniaEtNihil.ETHEREAL_BONE.get(), "drops/");
        this.item(OmniaEtNihil.INFERNAL_FLESH.get(), "drops/");
        this.item(OmniaEtNihil.INFERNAL_BONE.get(), "drops/");
        this.item(OmniaEtNihil.PETRIFIED_FLESH.get(), "drops/");
        this.item(OmniaEtNihil.PETRIFIED_BONE.get(), "drops/");
        this.item(OmniaEtNihil.VERDANT_FLESH.get(), "drops/");
        this.item(OmniaEtNihil.VERDANT_BONE.get(), "drops/");
        this.item(OmniaEtNihil.RADIANT_FLESH.get(), "drops/");
        this.item(OmniaEtNihil.RADIANT_BONE.get(), "drops/");
    }
}