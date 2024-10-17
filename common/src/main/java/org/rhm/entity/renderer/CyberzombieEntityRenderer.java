package org.rhm.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.rhm.CyberRewaredMod;
import org.rhm.entity.CyberzombieEntity;
import org.rhm.model.entity.CyberzombieEntityModel;

public class CyberzombieEntityRenderer extends AbstractZombieRenderer<CyberzombieEntity, CyberzombieEntityModel> {
    public static ResourceLocation TEXTURE_NORMAL = ResourceLocation.fromNamespaceAndPath(CyberRewaredMod.MOD_ID, "textures/entity/cyberzombie.png");
    public static ResourceLocation TEXTURE_BRUTE = ResourceLocation.fromNamespaceAndPath(CyberRewaredMod.MOD_ID, "textures/entity/cyberzombie_brute.png");

    public static ResourceLocation TEXTURE_NORMAL_HL = ResourceLocation.fromNamespaceAndPath(CyberRewaredMod.MOD_ID, "textures/entity/cyberzombie_hl.png");
    public static ResourceLocation TEXTURE_BRUTE_HL = ResourceLocation.fromNamespaceAndPath(CyberRewaredMod.MOD_ID, "textures/entity/cyberzombie_brute_hl.png");


    public CyberzombieEntityRenderer(EntityRendererProvider.Context ctx) {
        super(
            ctx,
            new CyberzombieEntityModel(ctx.bakeLayer(ModelLayers.ZOMBIE)),
            new CyberzombieEntityModel(ctx.bakeLayer(ModelLayers.ZOMBIE_INNER_ARMOR)),
            new CyberzombieEntityModel(ctx.bakeLayer(ModelLayers.ZOMBIE_OUTER_ARMOR))
        );
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(CyberzombieEntity entity) {
        return entity.isBrute() ? TEXTURE_BRUTE : TEXTURE_NORMAL;
    }

    public RenderType getHighlightTexture(CyberzombieEntity entity) {
        return RenderType.eyes(entity.isBrute() ? TEXTURE_BRUTE_HL : TEXTURE_NORMAL_HL);
    }
}
