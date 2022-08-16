package net.elindis.ruinsofarcana.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.elindis.ruinsofarcana.RuinsOfArcana;
import net.elindis.ruinsofarcana.item.ModItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class GogglesMixin {

    private static final Identifier GOGGLES = new Identifier(RuinsOfArcana.MOD_ID, "textures/hud/goggle_overlay.png");

    @Inject(at = @At("HEAD"), method = "render")
    private void renderGoggles(CallbackInfo info) {
        if (MinecraftClient.getInstance().options.getPerspective().isFirstPerson()) {
            assert MinecraftClient.getInstance().player != null;
            ItemStack itemStack = MinecraftClient.getInstance().player.getInventory().getArmorStack(3);
            if (itemStack.isOf(ModItems.TRUESIGHT_GOGGLES.asItem())) {

                if (MinecraftClient.getInstance().getWindow() == null) return;

                double scaledHeightGoggles = MinecraftClient.getInstance().getWindow().getScaledHeight();
                double scaledWidthGoggles = MinecraftClient.getInstance().getWindow().getScaledWidth();

                RenderSystem.enableBlend();

                RenderSystem.enableDepthTest();
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.defaultBlendFunc();

                float opacity = 1f;
                Identifier texture = GOGGLES;

                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(false);
                RenderSystem.defaultBlendFunc();
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, opacity);
                RenderSystem.setShaderTexture(0, texture);
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferBuilder = tessellator.getBuffer();
                bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
                bufferBuilder.vertex(0.0, scaledHeightGoggles, -90.0).texture(0.0F, 1.0F).next();
                bufferBuilder.vertex(scaledWidthGoggles, scaledHeightGoggles, -90.0).texture(1.0F, 1.0F).next();
                bufferBuilder.vertex(scaledWidthGoggles, 0.0, -90.0).texture(1.0F, 0.0F).next();
                bufferBuilder.vertex(0.0, 0.0, -90.0).texture(0.0F, 0.0F).next();
                tessellator.draw();
                RenderSystem.depthMask(true);
                RenderSystem.enableDepthTest();
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            }
        }
    }

}
