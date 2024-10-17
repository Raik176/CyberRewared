package org.rhm.block.entity;

import net.minecraft.ChatFormatting;
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
import org.rhm.CyberRewaredMod;
import org.rhm.gui.SurgeryScreenHandler;
import org.rhm.registries.BlockEntityRegistry;
import org.rhm.registries.BlockRegistry;
import org.rhm.util.ImplementedInventory;

public class RobosurgeonBlockEntity extends BlockEntity implements MenuProvider, ImplementedInventory {
    public static final String NO_CHAMBER_ERROR = "block." + CyberRewaredMod.MOD_ID + ".surgery.error";

    private final NonNullList<ItemStack> items;

    public RobosurgeonBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.ROBOSURGEON, pos, state);
        this.items = NonNullList.withSize(3, ItemStack.EMPTY);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);
        this.items.clear();
        ContainerHelper.loadAllItems(nbt, items, registryLookup);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider registryLookup) {
        super.saveAdditional(nbt, registryLookup);
        ContainerHelper.saveAllItems(nbt, items, true, registryLookup);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable(getBlockState().getBlock().getDescriptionId());
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int syncId, @NotNull Inventory playerInventory, @NotNull Player player) {
        if (level == null || level.getBlockState(getBlockPos().below()).getBlock() != BlockRegistry.SURGERY_CHAMBER) {
            player.sendSystemMessage(Component.translatable(NO_CHAMBER_ERROR).withStyle(ChatFormatting.RED));
            return null;
        }
        return new SurgeryScreenHandler(syncId, playerInventory, this, this);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }
}
