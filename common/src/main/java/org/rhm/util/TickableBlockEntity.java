package org.rhm.util;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.world.World;

public interface TickableBlockEntity {
    static <T extends BlockEntity> BlockEntityTicker<T> getTicker(World w) {
        return w.isClient ? null : (world, pos, state, blockEntity) -> {
            if (blockEntity instanceof TickableBlockEntity tbe) {
                tbe.tick();
            }
        };
    }

    void tick();
}
