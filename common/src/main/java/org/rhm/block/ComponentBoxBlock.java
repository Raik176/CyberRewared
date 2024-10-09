package org.rhm.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
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
import org.rhm.block.entity.ComponentBoxBlockEntity;
import org.rhm.registries.BlockEntityRegistry;

// TODO: add opening it from the inventory, like in the original
public class ComponentBoxBlock extends HorizontalFacingBlock implements BlockEntityProvider {
    public static final VoxelShape EAST_SHAPE = VoxelShapes.cuboid(0.25, 0, 0.0625, 0.75, 0.625, 0.9375);
    public static final VoxelShape NORTH_SHAPE = VoxelShapes.cuboid(0.0625, 0, 0.25, 0.9375, 0.625, 0.75);
    private ComponentBoxBlockEntity be;

    public ComponentBoxBlock() {
        super(Settings.create()
            .mapColor(DyeColor.GRAY)
            .hardness(5)
            .resistance(10)
            .sounds(BlockSoundGroup.METAL)
        );
    }

    protected ComponentBoxBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing());
    }

    public ComponentBoxBlockEntity getBlockEntity() {
        return be;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        be = BlockEntityRegistry.COMPONENT_BOX.get().instantiate(pos, state);
        return be;
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return createCodec(ComponentBoxBlock::new);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            NamedScreenHandlerFactory factory = new SimpleNamedScreenHandlerFactory(
                (syncId, inventory, p) -> be.createMenu(syncId, inventory, p), getName()
            );
            player.openHandledScreen(factory);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            if (world.getBlockEntity(pos) instanceof ComponentBoxBlockEntity cbbe) {
                ItemScatterer.spawn(world, pos, cbbe);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        return switch (dir) {
            case NORTH, SOUTH -> NORTH_SHAPE;
            case EAST, WEST -> EAST_SHAPE;
            default -> VoxelShapes.fullCube();
        };
    }

}
