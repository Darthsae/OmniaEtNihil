package com.sockmit2007.omniaetnihil.capabilities;

import org.jetbrains.annotations.Nullable;

import com.sockmit2007.omniaetnihil.mana.IManaHandler;
import com.sockmit2007.omniaetnihil.quantum.IQuantumHandler;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;

public class OmniaEtNihilCapabilities {
    public static final class QuantumHandler {
        public static final BlockCapability<IQuantumHandler, @Nullable Direction> BLOCK = BlockCapability.createSided(create("quantum"), IQuantumHandler.class);
        public static final EntityCapability<IQuantumHandler, @Nullable Direction> ENTITY = EntityCapability.createSided(create("quantum"), IQuantumHandler.class);
        public static final ItemCapability<IQuantumHandler, @Nullable Void> ITEM = ItemCapability.createVoid(create("quantum"), IQuantumHandler.class);

        private QuantumHandler() {
        }
    }

    public static final class ManaHandler {
        public static final BlockCapability<IManaHandler, @Nullable Direction> BLOCK = BlockCapability.createSided(create("mana"), IManaHandler.class);
        public static final EntityCapability<IManaHandler, @Nullable Direction> ENTITY = EntityCapability.createSided(create("mana"), IManaHandler.class);
        public static final ItemCapability<IManaHandler, @Nullable Void> ITEM = ItemCapability.createVoid(create("mana"), IManaHandler.class);

        private ManaHandler() {
        }
    }

    private static ResourceLocation create(String path) {
        return ResourceLocation.fromNamespaceAndPath("neoforge", path);
    }

    private OmniaEtNihilCapabilities() {
    }
}
