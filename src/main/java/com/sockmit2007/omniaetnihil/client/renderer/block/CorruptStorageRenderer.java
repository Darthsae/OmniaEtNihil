package com.sockmit2007.omniaetnihil.client.renderer.block;

import com.sockmit2007.omniaetnihil.block.entity.CorruptStorageBlockEntity;
import com.sockmit2007.omniaetnihil.client.renderer.block.model.CorruptStorageModel;

import mod.azure.azurelib.common.api.client.renderer.GeoBlockRenderer;

public class CorruptStorageRenderer extends GeoBlockRenderer<CorruptStorageBlockEntity> {
    public CorruptStorageRenderer() {
        super(new CorruptStorageModel());
    }
}