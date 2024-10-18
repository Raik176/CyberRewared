package org.rhm.block.entity;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
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

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider registryLookup) {
        super.saveAdditional(nbt, registryLookup);
        nbt.remove("centerBlock");
        nbt.remove("multiBlocks");
        if (centerBlock != null)
            nbt.put("centerBlock", BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, centerBlock).getOrThrow());
        if (multiBlocks != null)
            nbt.put("multiBlocks", Codec.list(BlockPos.CODEC).encodeStart(NbtOps.INSTANCE, multiBlocks).getOrThrow());
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);

        if (nbt.contains("centerBlock"))
            centerBlock = BlockPos.CODEC.decode(NbtOps.INSTANCE, nbt.get("centerBlock")).getOrThrow().getFirst();

        multiBlocks = new ArrayList<>();
        if (nbt.contains("multiBlocks"))
            multiBlocks = Codec.list(BlockPos.CODEC).decode(NbtOps.INSTANCE, nbt.get("multiBlocks")).getOrThrow().getFirst();
    }
}
