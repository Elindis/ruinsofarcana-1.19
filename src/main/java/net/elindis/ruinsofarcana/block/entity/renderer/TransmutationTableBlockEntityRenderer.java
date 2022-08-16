package net.elindis.ruinsofarcana.block.entity.renderer;

import net.elindis.ruinsofarcana.block.entity.TransmutationTableBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

public class TransmutationTableBlockEntityRenderer implements BlockEntityRenderer<TransmutationTableBlockEntity> {
    public TransmutationTableBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}
    @Override
    public void render(TransmutationTableBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemStack transmutationTableInventory = entity.getItems().get(0);

        matrices.push();
        double offset = Math.sin((entity.getWorld().getTime() + tickDelta) / 8.0) / 8.0;
        matrices.translate(0.5, 1.5+offset, 0.5);
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((entity.getWorld().getTime() + tickDelta) * 2));
        MinecraftClient.getInstance().getItemRenderer().renderItem(transmutationTableInventory, ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 0);
        matrices.pop();
    }

    @Override
    public boolean rendersOutsideBoundingBox(TransmutationTableBlockEntity blockEntity) {
        return BlockEntityRenderer.super.rendersOutsideBoundingBox(blockEntity);
    }

    @Override
    public int getRenderDistance() {
        return BlockEntityRenderer.super.getRenderDistance();
    }

    @Override
    public boolean isInRenderDistance(TransmutationTableBlockEntity blockEntity, Vec3d pos) {
        return BlockEntityRenderer.super.isInRenderDistance(blockEntity, pos);
    }
}
