package org.rhm.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
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
    public long energy;

    public ChargerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.CHARGER, pos, state);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        energy = nbt.getLong("EnergyStored");
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.putLong("EnergyStored", energy);
    }

    @Override
    public void tick() {

    }

    @Override
    public void onFinal() {
        markDirty();
    }

    @Override
    public long getCapacity() {
        return 1024;
    }

    @Override
    public long getMaxIn() {
        return 8;
    }

    @Override
    public long getMaxOut() {
        return 0;
    }

    @Override
    public long getEnergy() {
        return energy;
    }

    @Override
    public void setEnergy(long value) {
        energy = value;
    }

    @Override
    public void extract(long amount) {
        IEnergyStorage.super.extract(amount);
        markDirty();
    }
}
