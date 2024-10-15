package org.rhm.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.rhm.block.entity.ScannerBlockEntity;
import org.rhm.registries.BlockEntityRegistry;
import org.rhm.util.TickableBlockEntity;

public class ScannerBlock extends Block implements EntityBlock {
    public static final VoxelShape SHAPE = Shapes.or(
        Shapes.box(0, 0, 0, 0.0625, 0.9375, 0.0625),
        Shapes.box(0.9375, 0, 0, 1, 0.9375, 0.0625),
        Shapes.box(0, 0, 0.9375, 0, 0.9375, 1),
        Shapes.box(0.9375, 0, 0.9375, 1, 0.9375, 1),
        Shapes.box(0, 0, 0, 1, 0.375, 1),
        Shapes.box(0, 0.875, 0, 1, 0.9375, 0.0625),
        Shapes.box(0, 0.875, 0, 0.0625, 0.9375, 1),
        Shapes.box(0.9375, 0.875, 0, 1, 0.9375, 1),
        Shapes.box(0, 0.875, 0.9375, 1, 0.9375, 1)
    );

    public ScannerBlock() {
        super(Properties.of()
            .mapColor(DyeColor.GRAY)
            .destroyTime(5)
            .explosionResistance(10)
            .sound(SoundType.METAL)
        );
    }

    protected ScannerBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return simpleCodec(ScannerBlock::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityRegistry.SCANNER.create(pos, state);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (!world.isClientSide) {
            player.openMenu(state.getMenuProvider(world, pos));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected @Nullable MenuProvider getMenuProvider(BlockState state, Level world, BlockPos pos) {
        return (MenuProvider) world.getBlockEntity(pos);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            if (world.getBlockEntity(pos) instanceof ScannerBlockEntity sbe) {
                Containers.dropContents(world, pos, sbe);
            }
            super.onRemove(state, world, pos, newState, moved);
        }
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return TickableBlockEntity.getTicker(world);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}
