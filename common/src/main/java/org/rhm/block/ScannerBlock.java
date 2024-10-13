package org.rhm.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.rhm.block.entity.ScannerBlockEntity;
import org.rhm.registries.BlockEntityRegistry;
import org.rhm.util.TickableBlockEntity;

public class ScannerBlock extends Block implements BlockEntityProvider {
    public static final VoxelShape SHAPE = VoxelShapes.union(
        VoxelShapes.cuboid(0, 0, 0, 0.0625, 0.9375, 0.0625),
        VoxelShapes.cuboid(0.9375, 0, 0, 1, 0.9375, 0.0625),
        VoxelShapes.cuboid(0, 0, 0.9375, 0, 0.9375, 1),
        VoxelShapes.cuboid(0.9375, 0, 0.9375, 1, 0.9375, 1),
        VoxelShapes.cuboid(0, 0, 0, 1, 0.375, 1),
        VoxelShapes.cuboid(0, 0.875, 0, 1, 0.9375, 0.0625),
        VoxelShapes.cuboid(0, 0.875, 0, 0.0625, 0.9375, 1),
        VoxelShapes.cuboid(0.9375, 0.875, 0, 1, 0.9375, 1),
        VoxelShapes.cuboid(0, 0.875, 0.9375, 1, 0.9375, 1)
    );

    public ScannerBlock() {
        super(Settings.create()
            .mapColor(DyeColor.GRAY)
            .hardness(5)
            .resistance(10)
            .sounds(BlockSoundGroup.METAL)
        );
    }

    protected ScannerBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends Block> getCodec() {
        return createCodec(ScannerBlock::new);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityRegistry.SCANNER.instantiate(pos, state);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
        }
        return ActionResult.SUCCESS;
    }

    @Override
    protected @Nullable NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return (NamedScreenHandlerFactory) world.getBlockEntity(pos);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            if (world.getBlockEntity(pos) instanceof ScannerBlockEntity sbe) {
                ItemScatterer.spawn(world, pos, sbe);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return TickableBlockEntity.getTicker(world);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
}
