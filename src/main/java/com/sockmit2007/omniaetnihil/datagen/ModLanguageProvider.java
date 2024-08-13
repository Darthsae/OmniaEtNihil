package com.sockmit2007.omniaetnihil.datagen;

import com.sockmit2007.omniaetnihil.OmniaEtNihil;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(PackOutput output, String id) {
        super(output, id, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.addItem(OmniaEtNihil.ABYSSAL_FLESH, "Abyssal Flesh");
        this.addItem(OmniaEtNihil.ABYSSAL_BONE, "Abyssal Bone");
        this.addItem(OmniaEtNihil.AQUATIC_FLESH, "Aquatic Flesh");
        this.addItem(OmniaEtNihil.AQUATIC_BONE, "Aquatic Bone");
        this.addItem(OmniaEtNihil.ARCANE_FLESH, "Arcane Flesh");
        this.addItem(OmniaEtNihil.ARCANE_BONE, "Arcane Bone");
        this.addItem(OmniaEtNihil.DIVINE_FLESH, "Divine Flesh");
        this.addItem(OmniaEtNihil.DIVINE_BONE, "Divine Bone");
        this.addItem(OmniaEtNihil.ETHEREAL_FLESH, "Ethereal Flesh");
        this.addItem(OmniaEtNihil.ETHEREAL_BONE, "Ethereal Bone");
        this.addItem(OmniaEtNihil.INFERNAL_FLESH, "Infernal Flesh");
        this.addItem(OmniaEtNihil.INFERNAL_BONE, "Infernal Bone");
        this.addItem(OmniaEtNihil.PETRIFIED_FLESH, "Petrified Flesh");
        this.addItem(OmniaEtNihil.PETRIFIED_BONE, "Petrified Bone");
        this.addItem(OmniaEtNihil.VERDANT_FLESH, "Verdant Flesh");
        this.addItem(OmniaEtNihil.VERDANT_BONE, "Verdant Bone");
        this.addItem(OmniaEtNihil.RADIANT_FLESH, "Yellow Flesh");
        this.addItem(OmniaEtNihil.RADIANT_BONE, "Yellow Bone");
    }
}
