package org.rhm.block;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.rhm.block.entity.RadioPostBlockEntity;
import org.rhm.registries.BlockEntityRegistry;

import java.util.ArrayList;
import java.util.List;

public class RadioPostBlock extends FenceBlock implements BlockEntityProvider {
    private RadioPostBlockEntity blockEntity;
    public RadioPostBlock() {
        super(Settings.create()
            .mapColor(DyeColor.GRAY)
            .hardness(5)
            .resistance(10)
            .sounds(BlockSoundGroup.METAL)
        );
    }

    @Override
    public boolean canConnect(BlockState state, boolean neighborIsFullSquare, Direction dir) {
        return state.getBlock() instanceof RadioPostBlock;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (!(world instanceof ServerWorld sw)) return;
        BlockPos lowestCenter = pos.down(9);
        if (!(world.getBlockState(lowestCenter).getBlock() instanceof RadioPostBlock)) return;
        BlockPos corner = lowestCenter.west().north();
        if (!(world.getBlockState(corner).getBlock() instanceof RadioPostBlock)) return;
        List<BlockPos> blockPositions = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            blockPositions.add(pos.down(i));
        }
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 3; x++) {
                for (int z = 0; z < 3; z++) {
                    BlockPos pos1 = corner.add(x,y,z);
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
            //TODO: handle this
            //what??
            return;
        }
        for (BlockPos blockPosition : blockPositions) {
            sw.spawnParticles(
                new DustParticleEffect(DustParticleEffect.RED, 0.85f),
                blockPosition.getX() + 0.5, blockPosition.getY(), blockPosition.getZ() + 0.5,
                10,
                0.25, 0.25, 0.25,
                3

            );
            if (world.getBlockEntity(blockPosition) instanceof RadioPostBlockEntity rpbe) {
                rpbe.setCenterBlock(lowestCenter);
            } else {
                //TODO: handle this
            }
        }
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
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
                    //uh??
                    //TODO: handle this
                }
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityRegistry.RADIO_POST.instantiate(pos,state);
    }
}
