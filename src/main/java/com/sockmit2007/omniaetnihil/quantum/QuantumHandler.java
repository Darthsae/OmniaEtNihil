package com.sockmit2007.omniaetnihil.quantum;

public class QuantumHandler implements IQuantumHandler {
    public int maxQuantumStored;
    public int quantumStored;

    @Override
    public int receiveQuantum(int toReceive, boolean simulate) {
        int receivable = Math.min(getMaxQuantumStored() - getQuantumStored(), toReceive);
        if (!simulate) {
            quantumStored += receivable;
        }
        return receivable;
    }

    @Override
    public int extractQuantum(int toExtract, boolean simulate) {
        int extractable = Math.min(getQuantumStored(), toExtract);
        if (!simulate) {
            quantumStored -= extractable;
        }
        return extractable;
    }

    @Override
    public int getQuantumStored() {
        return quantumStored;
    }

    @Override
    public int getMaxQuantumStored() {
        return maxQuantumStored;
    }

    @Override
    public boolean canExtractQuantum() {
        if (quantumStored > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean canReceiveQuantum() {
        if (quantumStored < maxQuantumStored) {
            return true;
        }
        return false;
    }
}
