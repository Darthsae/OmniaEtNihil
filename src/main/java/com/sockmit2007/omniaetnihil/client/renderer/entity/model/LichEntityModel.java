package com.sockmit2007.omniaetnihil.client.renderer.entity.model;

import com.sockmit2007.omniaetnihil.OmniaEtNihil;
import com.sockmit2007.omniaetnihil.entity.LichEntity;

import mod.azure.azurelib.common.api.client.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public class LichEntityModel extends GeoModel<LichEntity> {
    // Models must be stored in assets/<modid>/geo with subfolders supported inside
    // the geo folder
    private static final ResourceLocation model = ResourceLocation.fromNamespaceAndPath(OmniaEtNihil.MODID,
            "geo/lich.geo.json");
    // Textures must be stored in assets/<modid>/geo with subfolders supported
    // inside the textures folder
    private static final ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(OmniaEtNihil.MODID,
            "textures/entity/lich.png");
    // Animations must be stored in assets/<modid>/animations with subfolders
    // supported inside the animations folder
    private static final ResourceLocation animation = ResourceLocation.fromNamespaceAndPath(OmniaEtNihil.MODID,
            "animations/lich.animation.json");

    @Override
    public ResourceLocation getModelResource(LichEntity object) {
        return LichEntityModel.model;
    }

    @Override
    public ResourceLocation getTextureResource(LichEntity object) {
        return LichEntityModel.texture;
    }

    @Override
    public ResourceLocation getAnimationResource(LichEntity object) {
        return LichEntityModel.animation;
    }
}