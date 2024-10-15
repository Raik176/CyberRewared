package org.rhm.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.rhm.block.entity.EngineeringTableBlockEntity;
import org.rhm.registries.BlockEntityRegistry;

public class EngineeringTableBlock extends Block implements EntityBlock {
    public static final VoxelShape NORTH_SHAPE = Shapes.or(
        Shapes.box(0.25, 0, 0.75, 0.75, 1, 1),
        Shapes.box(0.25, 0.5625, 0.25, 0.75, 1, 1)
    );

    public static final VoxelShape EAST_SHAPE = Shapes.or(
        Shapes.box(0, 0, 0.25, 0.25, 1, 0.75),
        Shapes.box(0, 0.5625, 0.25, 0.75, 1, 0.75)
    );

    public static final VoxelShape SOUTH_SHAPE = Shapes.or(
        Shapes.box(0.25, 0, 0, 0.75, 1, 0.25),
        Shapes.box(0.25, 0.5625, 0, 0.75, 1, 0.75)
    );

    public static final VoxelShape WEST_SHAPE = Shapes.or(
        Shapes.box(0.75, 0, 0.25, 1, 1, 0.75),
        Shapes.box(0.25, 0.5625, 0.25, 1, 1, 0.75)
    );

    public EngineeringTableBlock() {
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

    protected EngineeringTableBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if (state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER) {
            return Shapes.block();
        } else {
            switch (state.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
                case NORTH -> {
                    return NORTH_SHAPE;
                }
                case EAST -> {
                    return EAST_SHAPE;
                }
                case SOUTH -> {
                    return SOUTH_SHAPE;
                }
                case WEST -> {
                    return WEST_SHAPE;
                }
                default -> {
                    return Shapes.block();
                }
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.DOUBLE_BLOCK_HALF, BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    protected void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (!world.isClientSide && world.getBlockEntity(pos) instanceof EngineeringTableBlockEntity etbe) {
            if (etbe.babe != null && etbe.babe.getBlockPos() == sourcePos) etbe.setBabe(null);
            if (etbe.cbbe != null && etbe.cbbe.getBlockPos() == sourcePos) etbe.setCbbe(null);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos blockPos = ctx.getClickedPos();
        Level world = ctx.getLevel();
        if (blockPos.getY() < world.getMaxBuildHeight() - 1 && world.getBlockState(blockPos.above()).canBeReplaced(ctx)) {
            return this.defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, ctx.getHorizontalDirection().getOpposite())
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
        return simpleCodec(EngineeringTableBlock::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? BlockEntityRegistry.ENGINEERING_TABLE.create(pos, state) : null;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (!world.isClientSide) {
            if (state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) pos = pos.below();
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
            if (world.getBlockEntity(pos) instanceof EngineeringTableBlockEntity sbe) {
                Containers.dropContents(world, pos, sbe);
            }
            super.onRemove(state, world, pos, newState, moved);
        }
    }
}
