package org.rhm.util;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;

public interface TickableBlockEntity {
    static <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level w) {
        return w.isClientSide ? null : (world, pos, state, blockEntity) -> {
            if (blockEntity instanceof TickableBlockEntity tbe) {
                tbe.tick();
            }
        };
    }

    void tick();
}
