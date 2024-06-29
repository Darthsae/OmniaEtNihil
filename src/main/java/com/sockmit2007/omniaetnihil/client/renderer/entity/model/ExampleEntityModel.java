package com.sockmit2007.omniaetnihil.client.renderer.entity.model;

import com.sockmit2007.omniaetnihil.OmniaEtNihil;
import com.sockmit2007.omniaetnihil.entity.ExampleEntity;

import mod.azure.azurelib.common.api.client.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public class ExampleEntityModel extends GeoModel<ExampleEntity> {
    // Models must be stored in assets/<modid>/geo with subfolders supported inside
    // the geo folder
    private static final ResourceLocation model = ResourceLocation.fromNamespaceAndPath(OmniaEtNihil.MODID,
            "geo/example_entity.geo.json");
    // Textures must be stored in assets/<modid>/geo with subfolders supported
    // inside the textures folder
    private static final ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(OmniaEtNihil.MODID,
            "textures/entity/example_entity.png");
    // Animations must be stored in assets/<modid>/animations with subfolders
    // supported inside the animations folder
    private static final ResourceLocation animation = ResourceLocation.fromNamespaceAndPath(OmniaEtNihil.MODID,
            "animations/example_entity.animation.json");

    @Override
    public ResourceLocation getModelResource(ExampleEntity object) {
        return ExampleEntityModel.model;
    }

    @Override
    public ResourceLocation getTextureResource(ExampleEntity object) {
        return ExampleEntityModel.texture;
    }

    @Override
    public ResourceLocation getAnimationResource(ExampleEntity object) {
        return ExampleEntityModel.animation;
    }
}