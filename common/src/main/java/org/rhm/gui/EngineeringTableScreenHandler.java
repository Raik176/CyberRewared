package org.rhm.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import org.rhm.api.IBlueprint;
import org.rhm.api.IDeconstructable;
import org.rhm.block.entity.EngineeringTableBlockEntity;
import org.rhm.block.entity.ScannerBlockEntity;
import org.rhm.network.EngineeringTableSmashPacket;
import org.rhm.recipe.EngineeringSmashRecipe;
import org.rhm.registries.ScreenHandlerRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

// todo: implement, fix, etc
public class EngineeringTableScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final Slot salvageSlot;
    private final Slot paperSlot;
    private final Slot[] outputSlots;
    private final Slot blueprintSlot;
    private final Slot craftingOutputSlot;
    private final PlayerInventory playerInventory;
    private final EngineeringTableBlockEntity blockEntity;
    private final boolean hasBabe;
    private final boolean hasCbbe;

    public EngineeringTableScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(11), null);
    }

    public EngineeringTableScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, EngineeringTableBlockEntity be) {
        super(ScreenHandlerRegistry.ENGINEERING_TABLE, syncId);
        this.inventory = inventory;
        this.playerInventory = playerInventory;
        this.blockEntity = be;

        playerInventory.onOpen(playerInventory.player);

        salvageSlot = new Slot(inventory, 0, 15, 20) {
            @Override
            public boolean canInsert(ItemStack stack) {
                // return stack.getItem() instanceof IDeconstructable;
                return true;
            }
        };
        paperSlot = new Slot(inventory, 1, 15, 53) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.getItem() == ScannerBlockEntity.PAPER_ITEM;
            }
        };
        outputSlots = new Slot[3 * 2];
        int slotIndex = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                outputSlots[slotIndex] = new Slot(inventory, 2 + slotIndex, 71 + j * 18, 17 + i * 18);
                this.addSlot(outputSlots[slotIndex]);
                slotIndex++;
            }
        }

        blueprintSlot = new Slot(inventory, 9, 115, 53) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.getItem() instanceof IBlueprint;
            }
        };
        craftingOutputSlot = new Slot(inventory, 10, 145, 21) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }
        };

        this.addSlot(salvageSlot);
        this.addSlot(paperSlot);
        this.addSlot(blueprintSlot);
        this.addSlot(craftingOutputSlot);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
        if (!playerInventory.player.getWorld().isClient) {
            hasBabe = blockEntity.babe != null;
            hasCbbe = blockEntity.cbbe != null;

            if (hasBabe) {
                //this.addSlot(new Slot(blockEntity.babe, 0, -10, -10));
            }
        } else {
            hasCbbe = hasBabe = false; // gotta satisfy the compiler
        }
    }

    @Override
    public void syncState() {
        super.syncState();
        if (!playerInventory.player.getWorld().isClient) { // idk if this should sync every time but it can't hurt
            boolean hasBabe = blockEntity.babe != null;
            boolean hasCbbe = blockEntity.cbbe != null;
            if (hasBabe != this.hasBabe || hasCbbe != this.hasCbbe) {
                ((ServerPlayerEntity)playerInventory.player).closeHandledScreen();
            }
            /*
            PacketRegistry.sendPacketS2C.accept(
                (ServerPlayerEntity) playerInventory.player,
                new EngineeringTableGuiAddonPacket(
                    hasBabe,
                    blockEntity.babe != null ? blockEntity.babe.getPos() : BlockPos.ORIGIN,
                    hasCbbe,
                    blockEntity.cbbe != null ? blockEntity.cbbe.getPos() : BlockPos.ORIGIN
                )
            );*/
        }
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        System.out.println("!!");
    }

    //TODO: cleanup
    public void smash(ServerPlayerEntity entity, EngineeringTableSmashPacket payload) {
        List<ItemStack> outputs = new ArrayList<>();
        if (salvageSlot.getStack().getItem() instanceof IDeconstructable deconstructable) {
            for (int i = 0; i < (payload.craftAll() ? salvageSlot.getStack().getCount() : 1); i++) {
                salvageSlot.getStack().decrement(1);
                outputs.addAll(Arrays.asList(deconstructable.getDestructComponents()));
            }
        } else {
            Optional<? extends RecipeEntry<? extends EngineeringSmashRecipe>> recipe = blockEntity.smashGetter.getFirstMatch(
                new SingleStackRecipeInput(salvageSlot.getStack()),
                blockEntity.getWorld()
            );
            if (recipe.isPresent()) {
                for (int i = 0; i < (payload.craftAll() ? salvageSlot.getStack().getCount() : 1); i++) {
                    outputs.addAll(recipe.get().value().craftRecipe(new SingleStackRecipeInput(salvageSlot.getStack())));
                }
            }
        }
        salvageSlot.markDirty();
        //This is horrible i know
        if (!outputs.isEmpty()) {
            int outputIndex = 0;
            for (ItemStack outputStack : outputs) {
                boolean placedInSlot = false;

                for (Slot outputSlot : outputSlots) {
                    ItemStack currentOutputSlotStack = outputSlot.getStack();

                    if (currentOutputSlotStack.isEmpty()) {
                        outputSlot.setStack(outputStack.copy());
                        placedInSlot = true;
                        break;
                    }
                    else if (ItemStack.areItemsAndComponentsEqual(currentOutputSlotStack, outputStack)) {

                        int space = currentOutputSlotStack.getMaxCount() - currentOutputSlotStack.getCount();
                        int amountToAdd = Math.min(space, outputStack.getCount());

                        if (amountToAdd > 0) {
                            currentOutputSlotStack.increment(amountToAdd);
                            outputStack.decrement(amountToAdd);

                            if (outputStack.isEmpty()) {
                                placedInSlot = true;
                                break;
                            }
                        }
                    }
                    outputSlot.markDirty();
                }

                if (!placedInSlot && !outputStack.isEmpty()) {
                    if (!playerInventory.insertStack(outputStack)) {
                        entity.dropItem(outputStack, false);
                    }
                }
            }
        }
    }

    public Slot getSalvageSlot() {
        return salvageSlot;
    }

    public Slot getPaperSlot() {
        return paperSlot;
    }

    public Slot getBlueprintSlot() {
        return blueprintSlot;
    }


    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;

        Slot slot = this.slots.get(invSlot);
        if (slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }


    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }
}
