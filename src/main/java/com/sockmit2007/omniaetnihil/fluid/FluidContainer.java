package com.sockmit2007.omniaetnihil.fluid;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.IFluidTank;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;

public class FluidContainer implements IFluidTank {
    FluidStack currentFluid = FluidStack.EMPTY;
    public static final int MAX_FLUID = 1000;

    public FluidContainer() {
    }

    public FluidContainer(FluidStack fluid) {
        currentFluid = fluid;
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        int drainable = Math.min(currentFluid.getAmount(), maxDrain);
        if (!action.simulate()) {
            currentFluid.shrink(drainable);
        }
        FluidStack copy = currentFluid.copy();
        copy.setAmount(drainable);
        return copy;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return drain(resource.getAmount(), action);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        int fillable = Math.min(MAX_FLUID - currentFluid.getAmount(), resource.getAmount());
        if (!action.simulate()) {
            currentFluid = resource.copy();
            currentFluid.setAmount(fillable);
        }
        return fillable;
    }

    @Override
    public int getCapacity() {
        return MAX_FLUID;
    }

    @Override
    public FluidStack getFluid() {
        return currentFluid;
    }

    public void setFluid(FluidStack fluidStack) {
        this.currentFluid = fluidStack;
    }

    @Override
    public int getFluidAmount() {
        return currentFluid.getAmount();
    }

    @Override
    public boolean isFluidValid(FluidStack stack) {
        return true;
    }
}
