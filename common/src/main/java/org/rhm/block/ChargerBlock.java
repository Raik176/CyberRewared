package org.rhm.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.rhm.registries.BlockEntityRegistry;
import org.rhm.util.TickableBlockEntity;

public class ChargerBlock extends Block implements EntityBlock {

    public ChargerBlock() {
        super(Properties.of()
            .mapColor(DyeColor.GRAY)
            .destroyTime(5)
            .explosionResistance(10)
            .sound(SoundType.METAL)
        );
    }

    protected ChargerBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return simpleCodec(ChargerBlock::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityRegistry.CHARGER.create(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return TickableBlockEntity.getTicker(world);
    }
}
