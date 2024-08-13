package com.sockmit2007.omniaetnihil.client.renderer.block.model;

import com.sockmit2007.omniaetnihil.OmniaEtNihil;
import com.sockmit2007.omniaetnihil.block.entity.ExampleCrafterBlockEntity;

import mod.azure.azurelib.common.api.client.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public class ExampleCrafterModel extends GeoModel<ExampleCrafterBlockEntity> {
    // Models must be stored in assets/<modid>/geo with subfolders supported inside
    // the geo folder
    private static final ResourceLocation model = ResourceLocation.fromNamespaceAndPath(OmniaEtNihil.MODID,
            "geo/example_crafter.geo.json");
    // Textures must be stored in assets/<modid>/geo with subfolders supported
    // inside the textures folder
    private static final ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(OmniaEtNihil.MODID,
            "textures/block/example_crafter.png");
    // Animations must be stored in assets/<modid>/animations with subfolders
    // supported inside the animations folder
    private static final ResourceLocation animation = ResourceLocation.fromNamespaceAndPath(OmniaEtNihil.MODID,
            "animations/example_crafter.animation.json");

    @Override
    public ResourceLocation getModelResource(ExampleCrafterBlockEntity object) {
        return ExampleCrafterModel.model;
    }

    @Override
    public ResourceLocation getTextureResource(ExampleCrafterBlockEntity object) {
        return ExampleCrafterModel.texture;
    }

    @Override
    public ResourceLocation getAnimationResource(ExampleCrafterBlockEntity object) {
        return ExampleCrafterModel.animation;
    }

    /*
     * @Override
     * public RenderType getRenderType(CorruptStorageBlockEntity animatable,
     * ResourceLocation texture) {
     * return RenderType.entityTranslucent(getTextureResource(animatable));
     * }
     */
}