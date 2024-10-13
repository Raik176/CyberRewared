package org.rhm.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import org.rhm.registries.BlockEntityRegistry;

import java.util.ArrayList;
import java.util.List;

public class RadioPostBlockEntity extends BlockEntity {
    // This is probably very unoptimal but eh
    private BlockPos centerBlock;
    private List<BlockPos> multiBlocks;

    public RadioPostBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.RADIO_POST, pos, state);
    }

    public List<BlockPos> getMultiBlocks() {
        return multiBlocks;
    }

    public void setMultiBlocks(List<BlockPos> multiBlocks) {
        this.multiBlocks = multiBlocks;
        markDirty();
    }

    public BlockPos getCenterBlock() {
        return centerBlock;
    }

    public void setCenterBlock(BlockPos centerBlock) {
        this.centerBlock = centerBlock;
        markDirty();
    }

    // this is so horrible
    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.remove("centerBlock");
        nbt.remove("multiBlocks");
        if (centerBlock != null) {
            NbtCompound centerNbt = new NbtCompound();
            centerNbt.putInt("x", centerBlock.getX());
            centerNbt.putInt("y", centerBlock.getY());
            centerNbt.putInt("z", centerBlock.getZ());
            nbt.put("centerBlock", centerNbt);
        }
        NbtList multiBlocksList = new NbtList();
        if (multiBlocks != null) for (BlockPos pos : multiBlocks) {
                NbtCompound posNbt = new NbtCompound();
                posNbt.putInt("x", pos.getX());
                posNbt.putInt("y", pos.getY());
                posNbt.putInt("z", pos.getZ());
                multiBlocksList.add(posNbt);
        }
        nbt.put("multiBlocks", multiBlocksList);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);

        if (nbt.contains("centerBlock")) {
            NbtCompound centerNbt = nbt.getCompound("centerBlock");
            centerBlock = new BlockPos(centerNbt.getInt("x"), centerNbt.getInt("y"), centerNbt.getInt("z"));
        }

        multiBlocks = new ArrayList<>();
        NbtList multiBlocksList = nbt.getList("multiBlocks", NbtElement.COMPOUND_TYPE);

        for (int i = 0; i < multiBlocksList.size(); i++) {
            NbtCompound posNbt = multiBlocksList.getCompound(i);
            BlockPos pos = new BlockPos(posNbt.getInt("x"), posNbt.getInt("y"), posNbt.getInt("z"));
            multiBlocks.add(pos);
        }
    }
}
