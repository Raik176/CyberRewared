package org.rhm.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.rhm.api.IScannable;
import org.rhm.gui.ScannerScreenHandler;
import org.rhm.registries.BlockEntityRegistry;
import org.rhm.util.ImplementedInventory;
import org.rhm.util.TickableBlockEntity;

public class ScannerBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, TickableBlockEntity, ImplementedInventory {
    public static final Item PAPER_ITEM = Items.PAPER;
    public static final int TICKS_PER_OPERATION = 24000; // 20 minutes
    private final DefaultedList<ItemStack> items;
    private int scanTimeCurrent = TICKS_PER_OPERATION;
    private int ticks = 0;

    public ScannerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.SCANNER, pos, state);
        this.items = DefaultedList.ofSize(3, ItemStack.EMPTY);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        this.ticks = nbt.getInt("Ticks");
        this.items.clear();
        Inventories.readNbt(nbt, items, registryLookup);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.putInt("Ticks", ticks);
        Inventories.writeNbt(nbt, items, true, registryLookup);
    }

    public float getScanProgress() {
        return Math.min((float) ticks / scanTimeCurrent, 1f);
    }

    @Override
    public void tick() {
        assert world != null;
        if (world.isClient) return;
        ItemStack scanItem = items.get(1);
        if (scanItem.isEmpty()) {
            ticks = 0;
            scanTimeCurrent = TICKS_PER_OPERATION;
            markDirty();
        } else if (scanItem.getItem() instanceof IScannable scannable) {
            if (!items.get(2).isEmpty() && (!ItemStack.areItemsAndComponentsEqual(items.get(2), scannable.getResult()))
                || (scannable.needsPaper() && items.getFirst().isEmpty())) return;
            if (scannable.getScanTime() != -1) {
                scanTimeCurrent = scannable.getScanTime();
                markDirty();
            }
            if (ticks++ % (scanTimeCurrent / 10) == 0) {
                markDirty();
            }
            if (ticks >= scanTimeCurrent) {
                ticks = 0;
                scanItem.decrement(1);
                items.get(0).decrement(1);
                if (items.get(2).isEmpty()) {
                    items.set(2, scannable.getResult());
                } else {
                    items.get(2).increment(scannable.getResult().getCount());
                }
                markDirty();
            }

        }
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new ScannerScreenHandler(syncId, playerInventory, this, this);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    @Override
    public void contentChanged() {
        markDirty();
    }
}
