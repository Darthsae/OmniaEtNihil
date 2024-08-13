package com.sockmit2007.omniaetnihil.mana;

public class ManaHandler implements IManaHandler {
    public int maxManaStored;
    public int manaStored;

    @Override
    public int receiveMana(int toReceive, boolean simulate) {
        int receivable = Math.min(getMaxManaStored() - getManaStored(), toReceive);
        if (!simulate) {
            manaStored += receivable;
        }
        return receivable;
    }

    @Override
    public int extractMana(int toExtract, boolean simulate) {
        int extractable = Math.min(getManaStored(), toExtract);
        if (!simulate) {
            manaStored -= extractable;
        }
        return extractable;
    }

    @Override
    public int getManaStored() {
        return manaStored;
    }

    @Override
    public int getMaxManaStored() {
        return maxManaStored;
    }

    @Override
    public boolean canExtractMana() {
        if (manaStored > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean canReceiveMana() {
        if (manaStored < maxManaStored) {
            return true;
        }
        return false;
    }
}
