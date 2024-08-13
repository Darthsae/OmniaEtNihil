package com.sockmit2007.omniaetnihil.quantum;

public interface IQuantumHandler {
    /**
     * Adds quantum to the storage. Returns the amount of quantum that was accepted.
     *
     * @param toReceive The amount of quantum being received.
     * @param simulate  If true, the insertion will only be simulated, meaning {@link #getQuantumStored()} will not change.
     * @return Amount of quantum that was (or would have been, if simulated) accepted by the storage.
     */
    int receiveQuantum(int toReceive, boolean simulate);

    /**
     * Removes quantum from the storage. Returns the amount of quantum that was removed.
     *
     * @param toExtract The amount of quantum being extracted.
     * @param simulate  If true, the extraction will only be simulated, meaning {@link #getQuantumStored()} will not change.
     * @return Amount of quantum that was (or would have been, if simulated) extracted from the storage.
     */
    int extractQuantum(int toExtract, boolean simulate);

    /**
     * Returns the amount of quantum currently stored.
     */
    int getQuantumStored();

    /**
     * Returns the maximum amount of quantum that can be stored.
     */
    int getMaxQuantumStored();

    /**
     * Returns if this storage can have quantum extracted. If this is false, then any calls to extractQuantum will return 0.
     */
    boolean canExtractQuantum();

    /**
     * Used to determine if this storage can receive quantum. If this is false, then any calls to receiveQuantum will return 0.
     */
    boolean canReceiveQuantum();
}
