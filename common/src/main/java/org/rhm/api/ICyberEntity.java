package org.rhm.api;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

/**
 * Interface representing an entity that can have cybernetic enhancements.
 */
public interface ICyberEntity {

    /**
     * Reads cyber data from the given {@link CompoundTag}. This method can be overridden to provide specific
     * implementation for reading cyber data related to the entity. However, the supermethod should still be called.
     *
     * @param tag
     *     the {@link CompoundTag} containing the cyber data
     */
    void readCyberData(CompoundTag tag);

    /**
     * Writes cyber data to the given {@link CompoundTag}. This method can be overridden to provide specific
     * implementation for writing cyber data related to the entity. However, the supermethod should still be called.
     *
     * @param tag
     *     the {@link CompoundTag} to write the cyber data to
     */
    void writeCyberData(CompoundTag tag);

    /**
     * Retrieves a list of installed cyberware from the given {@link CompoundTag} for the specified slot.
     *
     * @param tag
     *     the {@link CompoundTag} containing the cyberware data
     * @param slot
     *     the slot from which to retrieve the installed cyberware
     * @return a NonNullList of ItemStacks representing the installed cyberware; empty if none found
     */
    default NonNullList<ItemStack> getInstalledCyberware(CompoundTag tag, ICyberware.Slot slot) {
        NonNullList<ItemStack> list = NonNullList.create();
        if (tag.contains(slot.name()) && tag.get(slot.name()) instanceof ListTag lt) {
            for (int i = 0; i < lt.size(); i++) {
                DataResult<Pair<ItemStack, Tag>> result = ItemStack.CODEC.decode(NbtOps.INSTANCE, lt.getCompound(i));
                list.add(result.isSuccess() ? result.getOrThrow().getFirst() : ItemStack.EMPTY);
            }
        }
        return list;
    }
}
