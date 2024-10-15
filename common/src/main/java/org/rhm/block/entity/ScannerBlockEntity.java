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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.rhm.api.IScannable;
import org.rhm.gui.ScannerScreenHandler;
import org.rhm.registries.BlockEntityRegistry;
import org.rhm.util.ImplementedInventory;
import org.rhm.util.TickableBlockEntity;

public class ScannerBlockEntity extends BlockEntity implements MenuProvider, TickableBlockEntity, ImplementedInventory {
    public static final Item PAPER_ITEM = Items.PAPER;
    public static final long TICKS_PER_OPERATION = 24000; // 20 minutes
    private final NonNullList<ItemStack> items;
    public Runnable updateCallback = () -> {
    };
    private long scanTimeCurrent = 0;
    private long ticks = 0;
    private ItemStack currentResult = ItemStack.EMPTY;
    private float currentChance = 0;

    public ScannerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.SCANNER, pos, state);
        this.items = NonNullList.withSize(3, ItemStack.EMPTY);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);
        this.ticks = nbt.getLong("Ticks");
        this.items.clear();
        ContainerHelper.loadAllItems(nbt, items, registryLookup);
        setRecipe();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider registryLookup) {
        super.saveAdditional(nbt, registryLookup);
        nbt.putLong("Ticks", ticks);
        ContainerHelper.saveAllItems(nbt, items, true, registryLookup);
    }


    public long getTicks() {
        return ticks;
    }

    public long getScanTimeCurrent() {
        return scanTimeCurrent;
    }

    public float getCurrentChance() {
        return currentChance;
    }

    @Override
    public void tick() {
        assert level != null;
        if (level.isClientSide) return;
        ItemStack scanItem = items.get(1);
        if (!scanItem.isEmpty() && scanTimeCurrent != 0) {
            if (ticks++ % 40 == 0) { // save ticks every 10 seconds
                setChanged();
            }
            if (ticks >= scanTimeCurrent) {
                setChanged();
                if (level.getRandom().nextFloat() > currentChance / 100) {
                    ticks = 0;
                    return;
                }
                scanItem.shrink(1);
                items.get(0).shrink(1);
                if (items.get(2).isEmpty()) {
                    items.set(2, currentResult);
                } else {
                    items.get(2).grow(currentResult.getCount());
                }
                reset();
            }

        }
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable(getBlockState().getBlock().getDescriptionId());
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int syncId, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new ScannerScreenHandler(syncId, playerInventory, this, this);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    public void contentChanged() {
        if (!(items.get(0).isEmpty() || items.get(1).isEmpty())) {
            setRecipe();
        } else {
            reset();
        }
        setChanged();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        updateCallback.run();
    }

    private void setRecipe() {
        if (items.get(1).getItem() instanceof IScannable scannable) {
            if (!items.get(2).isEmpty() && (!ItemStack.isSameItemSameComponents(items.get(2), scannable.getScanResult()))
                || (scannable.scanNeedsPaper() && items.getFirst().isEmpty())) {
                return;
            }
            scanTimeCurrent = scannable.getScanTime() != -1 ? scannable.getScanTime() : TICKS_PER_OPERATION;
            currentResult = scannable.getScanResult();
            currentChance = scannable.scanSuccessChance();
        }
    }

    private void reset() {
        ticks = 0;
        scanTimeCurrent = 0;
        currentResult = ItemStack.EMPTY;
        currentChance = 0;
        setChanged();
    }
}
