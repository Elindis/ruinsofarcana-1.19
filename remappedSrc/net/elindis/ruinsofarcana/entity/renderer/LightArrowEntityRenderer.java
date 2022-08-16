package net.elindis.ruinsofarcana.entity.renderer;

import net.elindis.ruinsofarcana.RuinsOfArcana;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.ArrowEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.Identifier;

@Environment(value= EnvType.CLIENT)
public class LightArrowEntityRenderer extends ArrowEntityRenderer {
    public static final Identifier TEXTURE = new Identifier(RuinsOfArcana.MOD_ID, "textures/entity/projectiles/light_arrow.png");

    public LightArrowEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(ArrowEntity arrowEntity) {
        return TEXTURE;
    }
}

