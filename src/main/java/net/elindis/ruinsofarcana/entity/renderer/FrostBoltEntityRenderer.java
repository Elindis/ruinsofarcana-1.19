package net.elindis.ruinsofarcana.entity.renderer;

import net.elindis.ruinsofarcana.RuinsOfArcana;
import net.elindis.ruinsofarcana.entity.FrostBoltEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

    @Environment(value= EnvType.CLIENT)
    public class FrostBoltEntityRenderer extends ProjectileEntityRenderer<FrostBoltEntity> {
        public static final Identifier TEXTURE = new Identifier(RuinsOfArcana.MOD_ID, "textures/entity/projectiles/frost_bolt.png");

        public FrostBoltEntityRenderer(EntityRendererFactory.Context context) {
            super(context);
        }



        @Override
        public Identifier getTexture(FrostBoltEntity entity) {
            return TEXTURE;
        }
    }


