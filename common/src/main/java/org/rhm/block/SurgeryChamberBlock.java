package org.rhm.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.rhm.registries.BlockEntityRegistry;

public class SurgeryChamberBlock extends Block {
    public static final VoxelShape NORTH_TOP = VoxelShapes.union(
        VoxelShapes.cuboid(0.125, 0, 0, 0.875, 1, 0.125),
        VoxelShapes.cuboid(0, 0, 0, 0.125, 1, 1),
        VoxelShapes.cuboid(0.875, 0, 0, 1, 1, 1),
        VoxelShapes.cuboid(0, 0.875, 0, 1, 1, 1)
    );
    public static final VoxelShape NORTH_BOTTOM = VoxelShapes.union(
        VoxelShapes.cuboid(0.125, 0, 0, 0.875, 1, 0.125),
        VoxelShapes.cuboid(0, 0, 0, 0.125, 1, 1),
        VoxelShapes.cuboid(0.875, 0, 0, 1, 1, 1),
        VoxelShapes.cuboid(0, 0, 0, 1, 0.0625, 1)
    );
    public static final VoxelShape EAST_TOP = VoxelShapes.union(
        VoxelShapes.cuboid(0.875, 0, 0.125, 1, 1, 0.875),
        VoxelShapes.cuboid(0, 0, 0, 1, 1, 0.125),
        VoxelShapes.cuboid(0, 0, 0.875, 1, 1, 1),
        VoxelShapes.cuboid(0, 0.875, 0, 1, 1, 1)
    );
    public static final VoxelShape EAST_BOTTOM = VoxelShapes.union(
        VoxelShapes.cuboid(0.875, 0, 0.125, 1, 1, 0.875),
        VoxelShapes.cuboid(0, 0, 0, 1, 1, 0.125),
        VoxelShapes.cuboid(0, 0, 0.875, 1, 1, 1),
        VoxelShapes.cuboid(0, 0, 0, 1, 0.0625, 1)
    );
    public static final VoxelShape SOUTH_TOP = VoxelShapes.union(
        VoxelShapes.cuboid(0.125, 0, 0.875, 0.875, 1, 1),
        VoxelShapes.cuboid(0, 0, 0, 0.125, 1, 1),
        VoxelShapes.cuboid(0.875, 0, 0, 1, 1, 1),
        VoxelShapes.cuboid(0, 0.875, 0, 1, 1, 1)
    );
    public static final VoxelShape SOUTH_BOTTOM = VoxelShapes.union(
        VoxelShapes.cuboid(0.125, 0, 0.875, 0.875, 1, 1),
        VoxelShapes.cuboid(0, 0, 0, 0.125, 1, 1),
        VoxelShapes.cuboid(0.875, 0, 0, 1, 1, 1),
        VoxelShapes.cuboid(0, 0, 0, 1, 0.0625, 1)
    );
    public static final VoxelShape WEST_TOP = VoxelShapes.union(
        VoxelShapes.cuboid(0, 0, 0.125, 0.125, 1, 0.875),
        VoxelShapes.cuboid(0, 0, 0, 1, 1, 0.125),
        VoxelShapes.cuboid(0, 0, 0.875, 1, 1, 1),
        VoxelShapes.cuboid(0, 0.875, 0, 1, 1, 1)
    );
    public static final VoxelShape WEST_BOTTOM = VoxelShapes.union(
        VoxelShapes.cuboid(0, 0, 0.125, 0.125, 1, 0.875),
        VoxelShapes.cuboid(0, 0, 0, 1, 1, 0.125),
        VoxelShapes.cuboid(0, 0, 0.875, 1, 1, 1),
        VoxelShapes.cuboid(0, 0, 0, 1, 0.0625, 1)
    );

    public SurgeryChamberBlock() {
        super(Settings.create()
            .mapColor(DyeColor.GRAY)
            .hardness(5)
            .resistance(10)
            .sounds(BlockSoundGroup.METAL)
        );
        this.setDefaultState(this.stateManager.getDefaultState()
            .with(Properties.HORIZONTAL_FACING, Direction.NORTH).with(Properties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER)
        );
    }

    protected SurgeryChamberBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.DOUBLE_BLOCK_HALF, Properties.HORIZONTAL_FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos blockPos = ctx.getBlockPos();
        World world = ctx.getWorld();
        if (blockPos.getY() < world.getTopY() - 1 && world.getBlockState(blockPos.up()).canReplace(ctx)) {
            return this.getDefaultState()
                .with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing())
                .with(Properties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER);
        } else {
            return null;
        }
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        world.setBlockState(pos.up(), state.with(Properties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER), 3);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER) {
            switch (state.get(Properties.HORIZONTAL_FACING)) {
                case NORTH -> {
                    return NORTH_BOTTOM;
                }
                case EAST -> {
                    return EAST_BOTTOM;
                }
                case SOUTH -> {
                    return SOUTH_BOTTOM;
                }
                case WEST -> {
                    return WEST_BOTTOM;
                }
            }
        } else {
            switch (state.get(Properties.HORIZONTAL_FACING)) {
                case NORTH -> {
                    return NORTH_TOP;
                }
                case EAST -> {
                    return EAST_TOP;
                }
                case SOUTH -> {
                    return SOUTH_TOP;
                }
                case WEST -> {
                    return WEST_TOP;
                }
            }
        }
        return VoxelShapes.fullCube();
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && (player.isCreative() || !player.canHarvest(state))) {
            BlockPos blockPos = state.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? pos.up() : pos.down();
            BlockState blockState = world.getBlockState(blockPos);
            if (blockState.isOf(state.getBlock())) {
                world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 35);
                world.syncWorldEvent(player, 2001, blockPos, Block.getRawIdFromState(blockState));
            }
        }

        return super.onBreak(world, pos, state, player);
    }

    @Override
    protected MapCodec<? extends Block> getCodec() {
        return createCodec(SurgeryChamberBlock::new);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            /* TODO: add screen
            NamedScreenHandlerFactory factory = new SimpleNamedScreenHandlerFactory(
                (syncId, inventory, p) -> be.createMenu(syncId, inventory, p), getName()
            );
            player.openHandledScreen(factory);
             */
        }
        return ActionResult.SUCCESS;
    }
}
