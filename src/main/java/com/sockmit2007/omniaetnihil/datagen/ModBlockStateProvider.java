package com.sockmit2007.omniaetnihil.datagen;

import com.sockmit2007.omniaetnihil.OmniaEtNihil;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, OmniaEtNihil.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

    }
}
