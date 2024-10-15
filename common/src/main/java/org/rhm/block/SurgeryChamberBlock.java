package org.rhm.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SurgeryChamberBlock extends Block {
    public static final VoxelShape NORTH_TOP = Shapes.or(
        Shapes.box(0.125, 0, 0, 0.875, 1, 0.125),
        Shapes.box(0, 0, 0, 0.125, 1, 1),
        Shapes.box(0.875, 0, 0, 1, 1, 1),
        Shapes.box(0, 0.9375, 0, 1, 1, 1)
    );
    public static final VoxelShape NORTH_BOTTOM = Shapes.or(
        Shapes.box(0.125, 0, 0, 0.875, 1, 0.125),
        Shapes.box(0, 0, 0, 0.125, 1, 1),
        Shapes.box(0.875, 0, 0, 1, 1, 1),
        Shapes.box(0, 0, 0, 1, 0.125, 1)
    );
    public static final VoxelShape EAST_TOP = Shapes.or(
        Shapes.box(0.875, 0, 0.125, 1, 1, 0.875),
        Shapes.box(0, 0, 0, 1, 1, 0.125),
        Shapes.box(0, 0, 0.875, 1, 1, 1),
        Shapes.box(0, 0.9375, 0, 1, 1, 1)
    );
    public static final VoxelShape EAST_BOTTOM = Shapes.or(
        Shapes.box(0.875, 0, 0.125, 1, 1, 0.875),
        Shapes.box(0, 0, 0, 1, 1, 0.125),
        Shapes.box(0, 0, 0.875, 1, 1, 1),
        Shapes.box(0, 0, 0, 1, 0.125, 1)
    );
    public static final VoxelShape SOUTH_TOP = Shapes.or(
        Shapes.box(0.125, 0, 0.875, 0.875, 1, 1),
        Shapes.box(0, 0, 0, 0.125, 1, 1),
        Shapes.box(0.875, 0, 0, 1, 1, 1),
        Shapes.box(0, 0.9375, 0, 1, 1, 1)
    );
    public static final VoxelShape SOUTH_BOTTOM = Shapes.or(
        Shapes.box(0.125, 0, 0.875, 0.875, 1, 1),
        Shapes.box(0, 0, 0, 0.125, 1, 1),
        Shapes.box(0.875, 0, 0, 1, 1, 1),
        Shapes.box(0, 0, 0, 1, 0.125, 1)
    );
    public static final VoxelShape WEST_TOP = Shapes.or(
        Shapes.box(0, 0, 0.125, 0.125, 1, 0.875),
        Shapes.box(0, 0, 0, 1, 1, 0.125),
        Shapes.box(0, 0, 0.875, 1, 1, 1),
        Shapes.box(0, 0.9375, 0, 1, 1, 1)
    );
    public static final VoxelShape WEST_BOTTOM = Shapes.or(
        Shapes.box(0, 0, 0.125, 0.125, 1, 0.875),
        Shapes.box(0, 0, 0, 1, 1, 0.125),
        Shapes.box(0, 0, 0.875, 1, 1, 1),
        Shapes.box(0, 0, 0, 1, 0.125, 1)
    );

    public SurgeryChamberBlock() {
        super(Properties.of()
            .mapColor(DyeColor.GRAY)
            .destroyTime(5)
            .explosionResistance(10)
            .sound(SoundType.METAL)
        );
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER)
        );
    }

    protected SurgeryChamberBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.DOUBLE_BLOCK_HALF, BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos blockPos = ctx.getClickedPos();
        Level world = ctx.getLevel();
        if (blockPos.getY() < world.getMaxBuildHeight() - 1 && world.getBlockState(blockPos.above()).canBeReplaced(ctx)) {
            return this.defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, ctx.getHorizontalDirection())
                .setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER);
        } else {
            return null;
        }
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        world.setBlock(pos.above(), state.setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER), 3);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if (state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER) {
            switch (state.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
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
            switch (state.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
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
        return Shapes.block();
    }

    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if (!world.isClientSide && (player.isCreative() || !player.hasCorrectToolForDrops(state))) {
            BlockPos blockPos = state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? pos.above() : pos.below();
            BlockState blockState = world.getBlockState(blockPos);
            if (blockState.is(state.getBlock())) {
                world.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 35);
                world.levelEvent(player, 2001, blockPos, Block.getId(blockState));
            }
        }

        return super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return simpleCodec(SurgeryChamberBlock::new);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (!world.isClientSide) {
            /* TODO: add screen
            NamedScreenHandlerFactory factory = new SimpleNamedScreenHandlerFactory(
                (syncId, inventory, p) -> be.createMenu(syncId, inventory, p), getName()
            );
            player.openHandledScreen(factory);
             */
        }
        return InteractionResult.SUCCESS;
    }
}
