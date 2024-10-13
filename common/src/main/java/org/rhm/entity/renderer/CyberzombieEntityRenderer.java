package org.rhm.entity.renderer;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ZombieBaseEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;
import org.rhm.CyberRewaredMod;
import org.rhm.entity.CyberzombieEntity;
import org.rhm.model.entity.CyberzombieEntityModel;

public class CyberzombieEntityRenderer extends ZombieBaseEntityRenderer<CyberzombieEntity, CyberzombieEntityModel> {
    public static Identifier TEXTURE_NORMAL = Identifier.of(CyberRewaredMod.MOD_ID, "textures/entity/cyberzombie.png");
    public static Identifier TEXTURE_BRUTE = Identifier.of(CyberRewaredMod.MOD_ID, "textures/entity/cyberzombie_brute.png");

    public static Identifier TEXTURE_NORMAL_HL = Identifier.of(CyberRewaredMod.MOD_ID, "textures/entity/cyberzombie_hl.png");
    public static Identifier TEXTURE_BRUTE_HL = Identifier.of(CyberRewaredMod.MOD_ID, "textures/entity/cyberzombie_brute_hl.png");


    public CyberzombieEntityRenderer(EntityRendererFactory.Context ctx) {
        super(
            ctx,
            new CyberzombieEntityModel(ctx.getPart(EntityModelLayers.ZOMBIE)),
            new CyberzombieEntityModel(ctx.getPart(EntityModelLayers.ZOMBIE_INNER_ARMOR)),
            new CyberzombieEntityModel(ctx.getPart(EntityModelLayers.ZOMBIE_OUTER_ARMOR))
        );
    }

    @Override
    public Identifier getTexture(CyberzombieEntity entity) {
        return entity.isBrute() ? TEXTURE_BRUTE : TEXTURE_NORMAL;
    }

    public RenderLayer getHighlightTexture(CyberzombieEntity entity) {
        return RenderLayer.getEyes(entity.isBrute() ? TEXTURE_BRUTE_HL : TEXTURE_NORMAL_HL);
    }
}
