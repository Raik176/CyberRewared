package org.rhm.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.rhm.block.entity.EngineeringTableBlockEntity;
import org.rhm.registries.BlockEntityRegistry;

public class EngineeringTableBlock extends Block implements BlockEntityProvider {
    public static final VoxelShape NORTH_SHAPE = VoxelShapes.union(
        VoxelShapes.cuboid(0.25, 0, 0.75, 0.75, 1, 1),
        VoxelShapes.cuboid(0.25, 0.5625, 0.25, 0.75, 1, 1)
    );

    public static final VoxelShape EAST_SHAPE = VoxelShapes.union(
        VoxelShapes.cuboid(0, 0, 0.25, 0.25, 1, 0.75),
        VoxelShapes.cuboid(0, 0.5625, 0.25, 0.75, 1, 0.75)
    );

    public static final VoxelShape SOUTH_SHAPE = VoxelShapes.union(
        VoxelShapes.cuboid(0.25, 0, 0, 0.75, 1, 0.25),
        VoxelShapes.cuboid(0.25, 0.5625, 0, 0.75, 1, 0.75)
    );

    public static final VoxelShape WEST_SHAPE = VoxelShapes.union(
        VoxelShapes.cuboid(0.75, 0, 0.25, 1, 1, 0.75),
        VoxelShapes.cuboid(0.25, 0.5625, 0.25, 1, 1, 0.75)
    );

    public EngineeringTableBlock() {
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

    protected EngineeringTableBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER) {
            return VoxelShapes.fullCube();
        } else {
            switch (state.get(Properties.HORIZONTAL_FACING)) {
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
                    return VoxelShapes.fullCube();
                }
            }
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.DOUBLE_BLOCK_HALF, Properties.HORIZONTAL_FACING);
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (!world.isClient && world.getBlockEntity(pos) instanceof EngineeringTableBlockEntity etbe) {
            if (etbe.babe != null && etbe.babe.getPos() == sourcePos) etbe.setBabe(null);
            if (etbe.cbbe != null && etbe.cbbe.getPos() == sourcePos) etbe.setCbbe(null);
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos blockPos = ctx.getBlockPos();
        World world = ctx.getWorld();
        if (blockPos.getY() < world.getTopY() - 1 && world.getBlockState(blockPos.up()).canReplace(ctx)) {
            return this.getDefaultState()
                .with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite())
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
        return createCodec(EngineeringTableBlock::new);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return state.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? BlockEntityRegistry.ENGINEERING_TABLE.instantiate(pos, state) : null;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            if (state.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) pos = pos.down();
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
            if (world.getBlockEntity(pos) instanceof EngineeringTableBlockEntity sbe) {
                ItemScatterer.spawn(world, pos, sbe);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }
}
