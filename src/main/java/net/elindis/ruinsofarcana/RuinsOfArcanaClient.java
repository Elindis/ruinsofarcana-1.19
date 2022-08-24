package net.elindis.ruinsofarcana;

import net.elindis.ruinsofarcana.block.ModBlocks;
import net.elindis.ruinsofarcana.block.entity.ModBlockEntities;
import net.elindis.ruinsofarcana.block.entity.renderer.PedestalBlockEntityRenderer;
import net.elindis.ruinsofarcana.block.entity.renderer.SingularityEntityRenderer;
import net.elindis.ruinsofarcana.block.entity.renderer.TransmutationTableBlockEntityRenderer;
import net.elindis.ruinsofarcana.entity.EntitySpawnPacket;
import net.elindis.ruinsofarcana.entity.ModEntities;
import net.elindis.ruinsofarcana.entity.renderer.FrostBoltEntityRenderer;
import net.elindis.ruinsofarcana.entity.renderer.LightArrowEntityRenderer;
import net.elindis.ruinsofarcana.particle.JetParticle;
import net.elindis.ruinsofarcana.particle.ModParticles;
import net.elindis.ruinsofarcana.particle.SingularityParticle;
import net.elindis.ruinsofarcana.particle.SpiralParticle;
import net.elindis.ruinsofarcana.screen.ManufactoryScreen;
import net.elindis.ruinsofarcana.screen.ModScreenHandlers;
import net.elindis.ruinsofarcana.util.ModModelPredicateProvider;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

public class RuinsOfArcanaClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {


        // Blocks
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.RICE_CROP, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MYRTLE_LEAVES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MYRTLE_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.RESEARCH_PARCHMENT_0, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.RESEARCH_PARCHMENT_1, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.RESEARCH_PARCHMENT_2, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.RESEARCH_PARCHMENT_3, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.RESEARCH_PARCHMENT_4, RenderLayer.getCutout());

        // Block entities
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.AURIC_PIPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PEDESTAL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.TRANSMUTATION_TABLE, RenderLayer.getCutout());

        // Block entity renderers
        BlockEntityRendererRegistry.register(ModBlockEntities.PEDESTAL, PedestalBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.TRANSMUTATION_TABLE, TransmutationTableBlockEntityRenderer::new);
//        BlockEntityRendererRegistry.register(ModBlockEntities.SINGULARITY, SingularityEntityRenderer::new);

        // Projectile entities and thrown entities
        EntityRendererRegistry.register(ModEntities.FIREBALL_ENTITY_TYPE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.FROST_BOLT_ENTITY_ENTITY_TYPE, FrostBoltEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.LIGHT_ARROW_ENTITY_TYPE, LightArrowEntityRenderer::new);

        // Screen handlers
        ScreenRegistry.register(ModScreenHandlers.MANUFACTORY_SCREEN_HANDLER, ManufactoryScreen::new);

        // Misc
        EntitySpawnPacket.receiveEntityPacket();
        ModModelPredicateProvider.registerModModels();
        ParticleFactoryRegistry.getInstance().register(ModParticles.SINGULARITY_PARTICLE, SingularityParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.SPIRAL_PARTICLE, SpiralParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.JET_PARTICLE, JetParticle.Factory::new);


    }
}
