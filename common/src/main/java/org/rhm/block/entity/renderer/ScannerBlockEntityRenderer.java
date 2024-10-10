package org.rhm.block.entity.renderer;


import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import org.rhm.block.entity.ScannerBlockEntity;

public class ScannerBlockEntityRenderer implements BlockEntityRenderer<ScannerBlockEntity> {
    public ScannerBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(ScannerBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();

        System.out.println(entity.getItems().getFirst());

        matrices.pop();
    }
}
