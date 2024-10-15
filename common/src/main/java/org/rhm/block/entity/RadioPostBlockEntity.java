package org.rhm.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
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
        setChanged();
    }

    public BlockPos getCenterBlock() {
        return centerBlock;
    }

    public void setCenterBlock(BlockPos centerBlock) {
        this.centerBlock = centerBlock;
        setChanged();
    }

    // this is so horrible
    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.saveAdditional(nbt, registryLookup);
        nbt.remove("centerBlock");
        nbt.remove("multiBlocks");
        if (centerBlock != null) {
            CompoundTag centerNbt = new CompoundTag();
            centerNbt.putInt("x", centerBlock.getX());
            centerNbt.putInt("y", centerBlock.getY());
            centerNbt.putInt("z", centerBlock.getZ());
            nbt.put("centerBlock", centerNbt);
        }
        ListTag multiBlocksList = new ListTag();
        if (multiBlocks != null) {
            for (BlockPos pos : multiBlocks) {
                CompoundTag posNbt = new CompoundTag();
                posNbt.putInt("x", pos.getX());
                posNbt.putInt("y", pos.getY());
                posNbt.putInt("z", pos.getZ());
                multiBlocksList.add(posNbt);
            }
        }
        nbt.put("multiBlocks", multiBlocksList);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);

        if (nbt.contains("centerBlock")) {
            CompoundTag centerNbt = nbt.getCompound("centerBlock");
            centerBlock = new BlockPos(centerNbt.getInt("x"), centerNbt.getInt("y"), centerNbt.getInt("z"));
        }

        multiBlocks = new ArrayList<>();
        ListTag multiBlocksList = nbt.getList("multiBlocks", Tag.TAG_COMPOUND);

        for (int i = 0; i < multiBlocksList.size(); i++) {
            CompoundTag posNbt = multiBlocksList.getCompound(i);
            BlockPos pos = new BlockPos(posNbt.getInt("x"), posNbt.getInt("y"), posNbt.getInt("z"));
            multiBlocks.add(pos);
        }
    }
}
