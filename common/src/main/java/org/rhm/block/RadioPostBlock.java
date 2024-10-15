package org.rhm.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.rhm.block.entity.RadioPostBlockEntity;
import org.rhm.registries.BlockEntityRegistry;

import java.util.ArrayList;
import java.util.List;

public class RadioPostBlock extends FenceBlock implements EntityBlock {
    private RadioPostBlockEntity blockEntity;

    public RadioPostBlock() {
        super(Properties.of()
            .mapColor(DyeColor.GRAY)
            .destroyTime(5)
            .explosionResistance(10)
            .sound(SoundType.METAL)
        );
    }

    @Override
    public boolean connectsTo(BlockState state, boolean neighborIsFullSquare, Direction dir) {
        return state.getBlock() instanceof RadioPostBlock;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.setPlacedBy(world, pos, state, placer, itemStack);
        if (!(world instanceof ServerLevel sw)) return;
        BlockPos lowestCenter = pos.below(9);
        if (!(world.getBlockState(lowestCenter).getBlock() instanceof RadioPostBlock)) return;
        BlockPos corner = lowestCenter.west().north();
        if (!(world.getBlockState(corner).getBlock() instanceof RadioPostBlock)) return;
        List<BlockPos> blockPositions = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            blockPositions.add(pos.below(i));
        }
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 3; x++) {
                for (int z = 0; z < 3; z++) {
                    BlockPos pos1 = corner.offset(x, y, z);
                    if (!(world.getBlockState(pos1).getBlock() instanceof RadioPostBlock)) {
                        return;
                    }
                    blockPositions.add(pos1);
                }
            }
        }
        if (world.getBlockEntity(lowestCenter) instanceof RadioPostBlockEntity centerPost) {
            centerPost.setMultiBlocks(blockPositions);
        } else {
            // TODO: handle this
            // what??
            return;
        }
        for (BlockPos blockPosition : blockPositions) {
            sw.sendParticles(
                new DustParticleOptions(DustParticleOptions.REDSTONE_PARTICLE_COLOR, 0.85f),
                blockPosition.getX() + 0.5, blockPosition.getY(), blockPosition.getZ() + 0.5,
                10,
                0.25, 0.25, 0.25,
                3

            );
            if (world.getBlockEntity(blockPosition) instanceof RadioPostBlockEntity rpbe) {
                rpbe.setCenterBlock(lowestCenter);
            } else {
                // TODO: handle this
            }
        }
    }

    @Override
    protected void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (newState.getBlock() == Blocks.AIR) {
            if (world.getBlockEntity(pos) instanceof RadioPostBlockEntity rpbe && rpbe.getCenterBlock() != null) {
                if (world.getBlockEntity(rpbe.getCenterBlock()) instanceof RadioPostBlockEntity center) {
                    for (BlockPos multiBlock : center.getMultiBlocks()) {
                        if (world.getBlockEntity(multiBlock) instanceof RadioPostBlockEntity be) {
                            be.setCenterBlock(null);
                            be.setMultiBlocks(null);
                        }
                    }
                } else {
                    // uh??
                    // TODO: handle this
                }
            }
        }
        super.onRemove(state, world, pos, newState, moved);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityRegistry.RADIO_POST.create(pos, state);
    }
}
