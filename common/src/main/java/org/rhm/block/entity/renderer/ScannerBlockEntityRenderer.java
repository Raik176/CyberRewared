package org.rhm.block.entity.renderer;


import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import org.rhm.block.entity.ScannerBlockEntity;

public class ScannerBlockEntityRenderer implements BlockEntityRenderer<ScannerBlockEntity> {
    public ScannerBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(ScannerBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (!entity.isEmpty()) {
            matrices.push();
            matrices.scale(0.5f,0.38f,0.5f);
            matrices.translate(1,1,1);

            matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(90));
            MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getStack(1), ModelTransformationMode.FIXED,light,overlay,matrices,vertexConsumers, entity.getWorld(), 0);

            matrices.pop();
        }
    }
}
