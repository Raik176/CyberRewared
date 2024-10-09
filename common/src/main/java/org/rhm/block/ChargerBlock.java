package org.rhm.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.rhm.block.entity.ChargerBlockEntity;
import org.rhm.registries.BlockEntityRegistry;
import org.rhm.util.TickableBlockEntity;

public class ChargerBlock extends Block implements BlockEntityProvider {
    private ChargerBlockEntity be;

    public ChargerBlock() {
        super(Settings.create()
            .mapColor(DyeColor.GRAY)
            .hardness(5)
            .resistance(10)
            .sounds(BlockSoundGroup.METAL)
        );
    }

    protected ChargerBlock(Settings settings) {
        super(settings);
    }

    public ChargerBlockEntity getBlockEntity() {
        return be;
    }

    @Override
    protected MapCodec<? extends Block> getCodec() {
        return createCodec(ChargerBlock::new);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        be = BlockEntityRegistry.CHARGER.get().instantiate(pos, state);
        return be;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return TickableBlockEntity.getTicker(world);
    }
}
