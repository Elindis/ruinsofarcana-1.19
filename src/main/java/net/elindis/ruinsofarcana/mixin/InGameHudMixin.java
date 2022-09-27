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
public abstract class InGameHudMixin {

    private static final Identifier GOGGLES = new Identifier(RuinsOfArcana.MOD_ID, "textures/hud/goggle_overlay.png");

    @Inject(at = @At("HEAD"), method = "renderHotbar")
    private void renderGoggles(CallbackInfo info) {
        if (MinecraftClient.getInstance().options.getPerspective().isFirstPerson()) {
			assert MinecraftClient.getInstance().player != null;
			ItemStack itemStack = MinecraftClient.getInstance().player.getInventory().getArmorStack(3);
			if (itemStack.isOf(ModItems.TRUESIGHT_GOGGLES)) {
				this.renderOverlay();
			}
        }
	}
	private void renderOverlay() {
		int scaledWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
		int scaledHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, (float) 1);
		RenderSystem.setShaderTexture(0, InGameHudMixin.GOGGLES);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
		bufferBuilder.vertex(0.0, scaledHeight, -90.0).texture(0.0f, 1.0f).next();
		bufferBuilder.vertex(scaledWidth, scaledHeight, -90.0).texture(1.0f, 1.0f).next();
		bufferBuilder.vertex(scaledWidth, 0.0, -90.0).texture(1.0f, 0.0f).next();
		bufferBuilder.vertex(0.0, 0.0, -90.0).texture(0.0f, 0.0f).next();
		tessellator.draw();
		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
	}

}
