package org.rhm.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelPart;
import org.rhm.entity.CyberzombieEntity;

public class CyberzombieEntityModel extends ZombieModel<CyberzombieEntity> {
    public CyberzombieEntityModel(ModelPart modelPart) {
        super(modelPart);
    }

    @Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        super.renderToBuffer(matrices, vertices, light, overlay, color);
    }
}
