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
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.rhm.gui.EngineeringTableScreenHandler;
import org.rhm.recipe.EngineeringCraftRecipe;
import org.rhm.recipe.EngineeringSmashRecipe;
import org.rhm.registries.BlockEntityRegistry;
import org.rhm.registries.RecipeRegistry;
import org.rhm.util.ImplementedInventory;

public class EngineeringTableBlockEntity extends BlockEntity implements MenuProvider, ImplementedInventory {
    public final RecipeManager.CachedCheck<SingleRecipeInput, ? extends EngineeringSmashRecipe> smashGetter;
    public final RecipeManager.CachedCheck<EngineeringCraftRecipe.EngineeringCraftInput, ? extends EngineeringCraftRecipe> craftGetter;
    private final NonNullList<ItemStack> items;
    public BlueprintArchiveBlockEntity babe;
    public ComponentBoxBlockEntity cbbe;
    public Runnable contentsChangedCallback = () -> {
    };

    public EngineeringTableBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.ENGINEERING_TABLE, pos, state);
        this.items = NonNullList.withSize(11, ItemStack.EMPTY);
        this.smashGetter = RecipeManager.createCheck(RecipeRegistry.SMASHING_TYPE);
        this.craftGetter = RecipeManager.createCheck(RecipeRegistry.CRAFTING_TYPE);
    }

    public void setCbbe(ComponentBoxBlockEntity cbbe) {
        this.cbbe = cbbe;
        setChanged();
    }

    public void setBabe(BlueprintArchiveBlockEntity babe) {
        this.babe = babe;
        setChanged();
    }

    @Override
    public void contentChanged() {
        contentsChangedCallback.run();
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);
        ContainerHelper.loadAllItems(nbt, items, registryLookup);
        if (level != null) {
            if (nbt.contains("BlueprintArchive")) {
                int[] rawPos = nbt.getIntArray("BlueprintArchive");
                if (level.getBlockEntity(new BlockPos(rawPos[0], rawPos[1], rawPos[2])) instanceof BlueprintArchiveBlockEntity babe) {
                    this.babe = babe;
                }
            }
            if (nbt.contains("ComponentBox")) {
                int[] rawPos = nbt.getIntArray("ComponentBox");
                if (level.getBlockEntity(new BlockPos(rawPos[0], rawPos[1], rawPos[2])) instanceof ComponentBoxBlockEntity cbbe) {
                    this.cbbe = cbbe;
                }
            }
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider registryLookup) {
        super.saveAdditional(nbt, registryLookup);
        ContainerHelper.saveAllItems(nbt, items, true, registryLookup);

        if (babe != null) {
            nbt.putIntArray("BlueprintArchive", new int[]{
                babe.getBlockPos().getX(),
                babe.getBlockPos().getY(),
                babe.getBlockPos().getZ()
            });
        } else {
            nbt.remove("BlueprintArchive");
        }
        if (cbbe != null) {
            nbt.putIntArray("ComponentBox", new int[]{
                cbbe.getBlockPos().getX(),
                cbbe.getBlockPos().getY(),
                cbbe.getBlockPos().getZ()
            });
        } else {
            nbt.remove("ComponentBox");
        }
    }

    @Override
    public void setLevel(@NotNull Level world) { // theres probably a better way of doing this.
        super.setLevel(world);
        /*
        if (!world.isClient && world.getBlockState(pos) != null && (babe == null && cbbe == null)) {
            BlockPos leftPos;
            BlockPos rightPos;

            BlockState block = world.getBlockState(pos);
            if (block == null) return;
            switch (block.get(Properties.HORIZONTAL_FACING)) {
                case NORTH -> {
                    leftPos = pos.west();
                    rightPos = pos.east();
                }
                case SOUTH -> {
                    leftPos = pos.east();
                    rightPos = pos.west();
                }
                case EAST -> {
                    leftPos = pos.north();
                    rightPos = pos.south();
                }
                case WEST -> {
                    leftPos = pos.south();
                    rightPos = pos.north();
                }
                default -> {
                    leftPos = null;
                    rightPos = null;
                }
            }

            if (leftPos != null) {
                BlockEntity left = world.getBlockEntity(leftPos);
                if (left instanceof BlueprintArchiveBlockEntity babe) {
                    setBabe(babe);
                } else if (left instanceof ComponentBoxBlockEntity cbbe) {
                    setCbbe(cbbe);
                }
            }
            if (rightPos != null) {
                BlockEntity right = world.getBlockEntity(rightPos);
                if (right instanceof BlueprintArchiveBlockEntity babe) {
                    setBabe(babe);
                } else if (right instanceof ComponentBoxBlockEntity cbbe) {
                    setCbbe(cbbe);
                }
            }
        }

         */
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable(getBlockState().getBlock().getDescriptionId());
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int syncId, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new EngineeringTableScreenHandler(syncId, playerInventory, this, this);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }
}
