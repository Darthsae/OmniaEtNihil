package com.sockmit2007.omniaetnihil.mana;

public interface IManaHandler {
    /**
     * Adds mana to the storage. Returns the amount of mana that was accepted.
     *
     * @param toReceive The amount of mana being received.
     * @param simulate  If true, the insertion will only be simulated, meaning {@link #getManaStored()} will not change.
     * @return Amount of mana that was (or would have been, if simulated) accepted by the storage.
     */
    int receiveMana(int toReceive, boolean simulate);

    /**
     * Removes mana from the storage. Returns the amount of mana that was removed.
     *
     * @param toExtract The amount of mana being extracted.
     * @param simulate  If true, the extraction will only be simulated, meaning {@link #getManaStored()} will not change.
     * @return Amount of mana that was (or would have been, if simulated) extracted from the storage.
     */
    int extractMana(int toExtract, boolean simulate);

    /**
     * Returns the amount of mana currently stored.
     */
    int getManaStored();

    /**
     * Returns the maximum amount of mana that can be stored.
     */
    int getMaxManaStored();

    /**
     * Returns if this storage can have mana extracted. If this is false, then any calls to extractMana will return 0.
     */
    boolean canExtractMana();

    /**
     * Used to determine if this storage can receive mana. If this is false, then any calls to receiveMana will return 0.
     */
    boolean canReceiveMana();
}