package org.rhm.model.entity;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.ZombieEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import org.rhm.entity.CyberzombieEntity;

public class CyberzombieEntityModel extends ZombieEntityModel<CyberzombieEntity> {
    public CyberzombieEntityModel(ModelPart modelPart) {
        super(modelPart);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        super.render(matrices, vertices, light, overlay, color);
    }
}
