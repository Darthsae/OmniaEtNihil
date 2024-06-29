package com.sockmit2007.omniaetnihil.client.renderer.entity;

import com.sockmit2007.omniaetnihil.client.renderer.entity.model.ExampleEntityModel;
import com.sockmit2007.omniaetnihil.entity.ExampleEntity;

import mod.azure.azurelib.common.api.client.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ExampleEntityRenderer extends GeoEntityRenderer<ExampleEntity> {
    public ExampleEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new ExampleEntityModel());
    }
}