package org.rhm.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.rhm.gui.EngineeringTableScreenHandler;
import org.rhm.recipe.EngineeringCraftRecipe;
import org.rhm.recipe.EngineeringSmashRecipe;
import org.rhm.registries.BlockEntityRegistry;
import org.rhm.registries.RecipeRegistry;
import org.rhm.util.ImplementedInventory;

import java.util.ArrayList;
import java.util.List;

public class EngineeringTableBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> items;
    public final RecipeManager.MatchGetter<SingleStackRecipeInput, ? extends EngineeringSmashRecipe> smashGetter;
    public final RecipeManager.MatchGetter<EngineeringCraftRecipe.EngineeringCraftInput, ? extends EngineeringCraftRecipe> craftGetter;
    public BlueprintArchiveBlockEntity babe;
    public ComponentBoxBlockEntity cbbe;
    public List<EngineeringTableScreenHandler> serverSideScreenHandlers;

    public EngineeringTableBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.ENGINEERING_TABLE, pos, state);
        this.items = DefaultedList.ofSize(11, ItemStack.EMPTY);
        this.smashGetter = RecipeManager.createCachedMatchGetter(RecipeRegistry.SMASHING_TYPE);
        this.craftGetter = RecipeManager.createCachedMatchGetter(RecipeRegistry.CRAFTING_TYPE);
        this.serverSideScreenHandlers = new ArrayList<>();
    }

    public void setCbbe(ComponentBoxBlockEntity cbbe) {
        this.cbbe = cbbe;
        markDirty();
    }

    public void setBabe(BlueprintArchiveBlockEntity babe) {
        this.babe = babe;
        markDirty();
    }

    @Override
    public void contentChanged() {
        System.out.println("daws");
        for (EngineeringTableScreenHandler serverSideScreenHandler : serverSideScreenHandlers) {
            serverSideScreenHandler.onContentChanged(this);
        }
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        Inventories.readNbt(nbt, items, registryLookup);
        if (world != null) {
            if (nbt.contains("BlueprintArchive")) {
                int[] rawPos = nbt.getIntArray("BlueprintArchive");
                if (world.getBlockEntity(new BlockPos(rawPos[0],rawPos[1],rawPos[2])) instanceof BlueprintArchiveBlockEntity babe) {
                    this.babe = babe;
                }
            }
            if (nbt.contains("ComponentBox")) {
                int[] rawPos = nbt.getIntArray("ComponentBox");
                if (world.getBlockEntity(new BlockPos(rawPos[0],rawPos[1],rawPos[2])) instanceof ComponentBoxBlockEntity cbbe) {
                    this.cbbe = cbbe;
                }
            }
        }
    }
    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, items, true, registryLookup);

        if (babe != null) {
            nbt.putIntArray("BlueprintArchive", new int[] {
                babe.getPos().getX(),
                babe.getPos().getY(),
                babe.getPos().getZ()
            });
        } else nbt.remove("BlueprintArchive");
        if (cbbe != null) {
            nbt.putIntArray("ComponentBox", new int[] {
                cbbe.getPos().getX(),
                cbbe.getPos().getY(),
                cbbe.getPos().getZ()
            });
        } else nbt.remove("ComponentBox");
    }

    @Override
    public void setWorld(World world) { //theres probably a better way of doing this.
        super.setWorld(world);
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
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        EngineeringTableScreenHandler handler = new EngineeringTableScreenHandler(syncId, playerInventory, this, this);
        serverSideScreenHandlers.add(handler);
        return handler;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }
}
