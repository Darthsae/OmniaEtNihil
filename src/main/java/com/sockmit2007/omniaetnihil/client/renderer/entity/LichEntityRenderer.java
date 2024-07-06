package com.sockmit2007.omniaetnihil.client.renderer.entity;

import com.sockmit2007.omniaetnihil.client.renderer.entity.model.LichEntityModel;
import com.sockmit2007.omniaetnihil.entity.LichEntity;

import mod.azure.azurelib.common.api.client.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class LichEntityRenderer extends GeoEntityRenderer<LichEntity> {
    public LichEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new LichEntityModel());
    }
}