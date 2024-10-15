package org.rhm.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.rhm.registries.BlockEntityRegistry;
import org.rhm.util.IEnergyStorage;
import org.rhm.util.TickableBlockEntity;

public class ChargerBlockEntity extends BlockEntity implements TickableBlockEntity, IEnergyStorage {
    /*
    public final EnergyStorage energyStorage = new EnergyStorage(1000, 50, 0) {
        @Override
        public void onFinal() {
            markDirty();
        }
    };
     */
    public int energy;

    public ChargerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.CHARGER, pos, state);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);
        energy = nbt.getInt("EnergyStored");
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.saveAdditional(nbt, registryLookup);
        nbt.putInt("EnergyStored", energy);
    }

    @Override
    public void tick() {

    }

    @Override
    public void onFinal() {
        setChanged();
    }

    @Override
    public int getCapacity() {
        return 1024;
    }

    @Override
    public int getMaxIn() {
        return 8;
    }

    @Override
    public int getMaxOut() {
        return 0;
    }

    @Override
    public int getEnergy() {
        return energy;
    }

    @Override
    public void setEnergy(int value) {
        energy = value;
    }

    @Override
    public void extract(int amount) {
        IEnergyStorage.super.extract(amount);
        setChanged();
    }
}
