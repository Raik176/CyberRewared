package org.rhm.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RadioKitBlock extends HorizontalDirectionalBlock {
    public static final VoxelShape SHAPE_NORTH = Shapes.or(
        Shapes.box(0.0625, 0, 0.125, 0.8125, 0.25, 0.625),
        Shapes.box(0.75, 0, 0.8125, 0.875, 0.0625, 0.9375),
        Shapes.box(0.78125, 0.0625, 0.84375, 0.84375, 0.875, 0.90625)
    );
    public static final VoxelShape SHAPE_EAST = Shapes.or(
        Shapes.box(0.375, 0, 0.0625, 0.875, 0.25, 0.8125),
        Shapes.box(0.0625, 0, 0.75, 0.1875, 0.0625, 0.875),
        Shapes.box(0.09375, 0.0625, 0.78125, 0.15625, 0.875, 0.84375)
    );
    public static final VoxelShape SHAPE_SOUTH = Shapes.or(
        Shapes.box(0.1875, 0, 0.375, 0.9375, 0.25, 0.875),
        Shapes.box(0.125, 0, 0.0625, 0.25, 0.0625, 0.1875),
        Shapes.box(0.15625, 0.0625, 0.09375, 0.21875, 0.875, 0.15625)
    );
    public static final VoxelShape SHAPE_WEST = Shapes.or(
        Shapes.box(0.125, 0, 0.1875, 0.625, 0.25, 0.9375),
        Shapes.box(0.8125, 0, 0.125, 0.9375, 0.0625, 0.25),
        Shapes.box(0.84375, 0.0625, 0.15625, 0.90625, 0.875, 0.21875)
    );

    public RadioKitBlock() {
        super(Properties.of()
            .mapColor(DyeColor.GRAY)
            .destroyTime(5)
            .explosionResistance(10)
            .sound(SoundType.METAL)
            .noOcclusion()
        );
    }

    protected RadioKitBlock(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
            case NORTH -> SHAPE_NORTH;
            case EAST -> SHAPE_EAST;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            default -> Shapes.block();
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return super.getStateForPlacement(ctx).setValue(BlockStateProperties.HORIZONTAL_FACING, ctx.getHorizontalDirection());
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(RadioKitBlock::new);
    }
}
