package org.rhm.gui;

import commonnetwork.api.Dispatcher;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import org.jetbrains.annotations.NotNull;
import org.rhm.api.IDeconstructable;
import org.rhm.api.IScannable;
import org.rhm.block.entity.EngineeringTableBlockEntity;
import org.rhm.block.entity.ScannerBlockEntity;
import org.rhm.network.EngineeringTableInfoPacket;
import org.rhm.network.EngineeringTableSmashPacket;
import org.rhm.recipe.EngineeringCraftRecipe;
import org.rhm.recipe.EngineeringSmashRecipe;
import org.rhm.registries.ScreenHandlerRegistry;
import org.rhm.util.CyberUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

// todo: implement, fix, etc
public class EngineeringTableScreenHandler extends CyberScreenHandler {
    private static final float SCAN_BASE_CHANCE = 15;
    private final Slot salvageSlot;
    private final Slot paperSlot;
    private final Slot[] outputSlots;
    private final Slot[] blueprintSlots;
    private final Slot blueprintSlot;
    private final Slot craftingOutputSlot;
    private final EngineeringTableBlockEntity blockEntity;
    private final boolean hasBabe;
    private final boolean hasCbbe;
    private boolean isUpdatingOutput = false; // Flag to prevent recursion
    private EngineeringCraftRecipe recipe = null;

    public EngineeringTableScreenHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(11), null);
    }

    public EngineeringTableScreenHandler(int syncId, Inventory playerInventory, Container inventory, EngineeringTableBlockEntity be) {
        super(ScreenHandlerRegistry.ENGINEERING_TABLE, syncId);
        this.inventory = inventory;
        this.playerInventory = playerInventory;
        this.blockEntity = be;

        playerInventory.startOpen(playerInventory.player);

        salvageSlot = new Slot(inventory, 0, 15, 20) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.getItem() instanceof IDeconstructable;
            }
        };
        paperSlot = new Slot(inventory, 1, 15, 53) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.getItem() == ScannerBlockEntity.PAPER_ITEM;
            }
        };
        outputSlots = new Slot[2 * 2];
        int slotIndex = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                outputSlots[slotIndex] = new Slot(inventory, 2 + slotIndex, 71 + j * 18, 17 + i * 18);
                this.addSlot(outputSlots[slotIndex]);
                slotIndex++;
            }
        }
        blueprintSlots = new Slot[2 * 2];
        slotIndex = 0;
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 2; j++) {
                blueprintSlots[slotIndex] = new Slot(inventory, 2 + outputSlots.length + slotIndex, 71 + j * 18, 53);
                this.addSlot(blueprintSlots[slotIndex]);
                slotIndex++;
            }
        }

        blueprintSlot = new Slot(inventory, 9, 115, 53);
        craftingOutputSlot = new Slot(inventory, 10, 145, 21) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(Player player, ItemStack stack) {
                super.onTake(player, stack);
                craftingSlotTake();
            }
        };

        this.addSlot(salvageSlot);
        this.addSlot(paperSlot);
        this.addSlot(blueprintSlot);
        this.addSlot(craftingOutputSlot);

        CyberUtil.addPlayerInventorySlots(playerInventory, this::addSlot);

        if (be != null) {
            be.contentsChangedCallback = () -> slotsChanged(inventory);
            slotsChanged(inventory);
        }

        if (!playerInventory.player.level().isClientSide && blockEntity != null) {
            hasBabe = blockEntity.babe != null;
            hasCbbe = blockEntity.cbbe != null;

            if (hasBabe) {
                // this.addSlot(new Slot(blockEntity.babe, 0, -10, -10));
            }
        } else {
            hasCbbe = hasBabe = false; // gotta satisfy the compiler
        }
    }

    @Override
    public void slotsChanged(@NotNull Container inventory) {
        float chance = 15;
        if (salvageSlot.getItem().getItem() instanceof IScannable scannable) {
            chance += scannable.scanSuccessChance();
        } else chance = 0;
        Dispatcher.sendToClient(new EngineeringTableInfoPacket(chance), (ServerPlayer) playerInventory.player);
        if (isUpdatingOutput) return;

        Optional<? extends RecipeHolder<? extends EngineeringCraftRecipe>> recipe = blockEntity.craftGetter.getRecipeFor(
            new EngineeringCraftRecipe.EngineeringCraftInput(
                Arrays.stream(outputSlots).map(Slot::getItem).toArray(ItemStack[]::new),
                blueprintSlot.getItem()
            ),
            Objects.requireNonNull(blockEntity.getLevel())
        );

        isUpdatingOutput = true;
        if (recipe.isPresent()) {
            this.recipe = recipe.get().value();
            craftingOutputSlot.set(recipe.get().value().output().copy());
        } else {
            craftingOutputSlot.set(ItemStack.EMPTY);
        }
        isUpdatingOutput = false;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int invSlot) {
        ItemStack fuckQuickMove = super.quickMoveStack(player, invSlot);
        if (!player.level().isClientSide) {
            if (invSlot == 9) craftingSlotTake();
            slotsChanged(inventory);
        }
        return fuckQuickMove;
    }

    private void craftingSlotTake() {
        if (recipe != null && !playerInventory.player.level().isClientSide) {
            for (ItemStack ingredient : recipe.ingredients()) {
                Iterator<ItemStack> iterator = Arrays.stream(outputSlots).map(Slot::getItem).iterator();
                while (iterator.hasNext()) {
                    ItemStack outputItem = iterator.next();

                    if (ItemStack.isSameItemSameComponents(ingredient, outputItem) && outputItem.getCount() >= ingredient.getCount()) {
                        outputItem.shrink(ingredient.getCount());
                        break;
                    }
                }
            }
            if (recipe.useBlueprint() &&
                ItemStack.isSameItemSameComponents(recipe.blueprint(),blueprintSlot.getItem())) blueprintSlot.getItem().shrink(1);
            recipe = null;
            slotsChanged(blockEntity);
        }
    }

    // TODO: cleanup
    public void smash(ServerPlayer entity, EngineeringTableSmashPacket payload) {
        if (salvageSlot.getItem().getItem() instanceof IScannable scannable) {
            if (scannable.scanCanOutputFromSmash() && playerInventory.player.level().getRandom().nextFloat() < (SCAN_BASE_CHANCE + scannable.scanSuccessChance())/100) {
                ItemStack result = scannable.getScanResult();
                for (Slot slot : blueprintSlots) {
                    if (slot.hasItem() && !ItemStack.isSameItemSameComponents(slot.getItem(), result)) return;
                    if (!slot.hasItem()) {
                        slot.set(result);
                    } else {
                        slot.getItem().grow(1);
                    }
                    break;
                }
                paperSlot.getItem().shrink(1);
            }
        }
        List<ItemStack> outputs = new ArrayList<>();
        if (salvageSlot.getItem().getItem() instanceof IDeconstructable deconstructable) {
            for (int i = 0; i < (payload.craftAll() ? salvageSlot.getItem().getCount() : 1); i++) {
                outputs.addAll(Arrays.asList(deconstructable.getDestructComponents()));
            }
        } else {
            Optional<? extends RecipeHolder<? extends EngineeringSmashRecipe>> recipe = blockEntity.smashGetter.getRecipeFor(
                new SingleRecipeInput(salvageSlot.getItem()),
                Objects.requireNonNull(blockEntity.getLevel())
            );
            if (recipe.isPresent()) {
                for (int i = 0; i < (payload.craftAll() ? salvageSlot.getItem().getCount() : 1); i++) {
                    outputs.addAll(recipe.get().value().craftRecipe(new SingleRecipeInput(salvageSlot.getItem())));
                }
            } else return;
        }
        salvageSlot.getItem().shrink(1);

        int numToRemove = switch (playerInventory.player.level().getDifficulty()) {
            case EASY, PEACEFUL -> 1;
            case HARD, NORMAL -> 2;
        };
        if (salvageSlot.getItem().isDamaged()) {
            float percent = (salvageSlot.getItem().getDamageValue() * 1F / salvageSlot.getItem().getMaxDamage());
            numToRemove += Math.max(0, (int) (outputs.size() * percent) - 1);
        }
        for (int i = 0; i < Math.min(numToRemove, outputs.size() - 1); i++) {
            outputs.remove(this.playerInventory.player.level().getRandom().nextInt(outputs.size()));
        }

        // This is horrible i know
        if (!outputs.isEmpty()) {
            for (ItemStack stack : outputs) {
                if (stack.isEmpty() || stack.getCount() < 1 || stack == ItemStack.EMPTY) continue;

                boolean placedInSlot = false;

                for (Slot outputSlot : outputSlots) {
                    ItemStack currentOutputSlotStack = outputSlot.getItem();

                    if (currentOutputSlotStack.isEmpty()) {
                        outputSlot.setByPlayer(stack.copy());
                        placedInSlot = true;
                        break;
                    } else if (ItemStack.isSameItemSameComponents(currentOutputSlotStack, stack)) {

                        int space = currentOutputSlotStack.getMaxStackSize() - currentOutputSlotStack.getCount();
                        int amountToAdd = Math.min(space, stack.getCount());

                        if (amountToAdd > 0) {
                            currentOutputSlotStack.grow(amountToAdd);
                            stack.shrink(amountToAdd);

                            if (stack.isEmpty()) {
                                placedInSlot = true;
                                break;
                            }
                        }
                    }
                    outputSlot.setChanged();
                }

                if (!placedInSlot && !stack.isEmpty()) {
                    if (!playerInventory.add(stack)) {
                        entity.drop(stack, false);
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
}
