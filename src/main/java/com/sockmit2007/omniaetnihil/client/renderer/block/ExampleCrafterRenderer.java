package com.sockmit2007.omniaetnihil.client.renderer.block;

import com.sockmit2007.omniaetnihil.block.entity.ExampleCrafterBlockEntity;
import com.sockmit2007.omniaetnihil.client.renderer.block.model.ExampleCrafterModel;

import mod.azure.azurelib.common.api.client.renderer.GeoBlockRenderer;

public class ExampleCrafterRenderer extends GeoBlockRenderer<ExampleCrafterBlockEntity> {
    public ExampleCrafterRenderer() {
        super(new ExampleCrafterModel());
    }
}