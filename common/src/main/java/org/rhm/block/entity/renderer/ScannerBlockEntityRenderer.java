package org.rhm.block.entity.renderer;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import org.rhm.block.entity.ScannerBlockEntity;

public class ScannerBlockEntityRenderer implements BlockEntityRenderer<ScannerBlockEntity> {
    public ScannerBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(ScannerBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        if (!entity.isEmpty()) {
            matrices.pushPose();
            matrices.scale(0.5f, 0.38f, 0.5f);
            matrices.translate(1, 1, 1);

            matrices.mulPose(Axis.XN.rotationDegrees(90));
            Minecraft.getInstance().getItemRenderer().renderStatic(entity.getItem(1), ItemDisplayContext.FIXED, light, overlay, matrices, vertexConsumers, entity.getLevel(), 0);

            matrices.popPose();
        }
    }
}
