package org.rhm.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class RadioKitBlock extends HorizontalFacingBlock {
    public static final VoxelShape SHAPE_NORTH = VoxelShapes.union(
        VoxelShapes.cuboid(0.0625, 0, 0.125, 0.8125, 0.25, 0.625),
        VoxelShapes.cuboid(0.75, 0, 0.8125, 0.875, 0.0625, 0.9375),
        VoxelShapes.cuboid(0.78125, 0.0625, 0.84375, 0.84375, 0.875, 0.90625)
    );
    public static final VoxelShape SHAPE_EAST = VoxelShapes.union(
        VoxelShapes.cuboid(0.375, 0, 0.0625, 0.875, 0.25, 0.8125),
        VoxelShapes.cuboid(0.0625, 0, 0.75, 0.1875, 0.0625, 0.875),
        VoxelShapes.cuboid(0.09375, 0.0625, 0.78125, 0.15625, 0.875, 0.84375)
    );
    public static final VoxelShape SHAPE_SOUTH = VoxelShapes.union(
        VoxelShapes.cuboid(0.1875, 0, 0.375, 0.9375, 0.25, 0.875),
        VoxelShapes.cuboid(0.125, 0, 0.0625, 0.25, 0.0625, 0.1875),
        VoxelShapes.cuboid(0.15625, 0.0625, 0.09375, 0.21875, 0.875, 0.15625)
    );
    public static final VoxelShape SHAPE_WEST = VoxelShapes.union(
        VoxelShapes.cuboid(0.125, 0, 0.1875, 0.625, 0.25, 0.9375),
        VoxelShapes.cuboid(0.8125, 0, 0.125, 0.9375, 0.0625, 0.25),
        VoxelShapes.cuboid(0.84375, 0.0625, 0.15625, 0.90625, 0.875, 0.21875)
    );

    public RadioKitBlock() {
        super(Settings.create()
            .mapColor(DyeColor.GRAY)
            .hardness(5)
            .resistance(10)
            .sounds(BlockSoundGroup.METAL)
            .nonOpaque()
        );
    }

    protected RadioKitBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(Properties.HORIZONTAL_FACING)) {
            case NORTH -> SHAPE_NORTH;
            case EAST -> SHAPE_EAST;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            default -> VoxelShapes.fullCube();
        };
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing());
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return createCodec(RadioKitBlock::new);
    }
}
