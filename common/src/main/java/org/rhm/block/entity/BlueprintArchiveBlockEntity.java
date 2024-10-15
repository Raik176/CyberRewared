package org.rhm.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.rhm.gui.BlueprintArchiveScreenHandler;
import org.rhm.registries.BlockEntityRegistry;
import org.rhm.util.ImplementedInventory;

public class BlueprintArchiveBlockEntity extends BlockEntity implements MenuProvider, ImplementedInventory {
    private final NonNullList<ItemStack> items = NonNullList.withSize(BlueprintArchiveScreenHandler.SLOT_COUNT, ItemStack.EMPTY);

    public BlueprintArchiveBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.BLUEPRINT_ARCHIVE, pos, state);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);
        ContainerHelper.loadAllItems(nbt, items, registryLookup);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.saveAdditional(nbt, registryLookup);
        ContainerHelper.saveAllItems(nbt, items, registryLookup);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable(getBlockState().getBlock().getDescriptionId());
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new BlueprintArchiveScreenHandler(syncId, playerInventory, this);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    public void contentChanged() {
        setChanged();
    }
}
