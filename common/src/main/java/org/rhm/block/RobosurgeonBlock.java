package org.rhm.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RobosurgeonBlock extends Block {
    public RobosurgeonBlock() {
        super(Settings.create()
            .mapColor(DyeColor.GRAY)
            .hardness(5)
            .resistance(10)
            .sounds(BlockSoundGroup.METAL)
        );
    }

    protected RobosurgeonBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends Block> getCodec() {
        return createCodec(RobosurgeonBlock::new);
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
