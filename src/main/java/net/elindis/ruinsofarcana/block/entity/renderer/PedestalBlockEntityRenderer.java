package net.elindis.ruinsofarcana.block.entity.renderer;

import net.elindis.ruinsofarcana.block.entity.PedestalBlockEntity;
import net.minecraft.block.entity.EndPortalBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

public class PedestalBlockEntityRenderer<T extends EndPortalBlockEntity> implements BlockEntityRenderer<PedestalBlockEntity> {
    public PedestalBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}
    @Override
    public void render(PedestalBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemStack pedestalInventory = entity.getItems().get(0);

        matrices.push();
        double offset = Math.sin((entity.getWorld().getTime() + tickDelta) / 8.0) / 8.0;
        matrices.translate(0.5, 1.5+offset, 0.5);
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((entity.getWorld().getTime() + tickDelta) * 2));
        MinecraftClient.getInstance().getItemRenderer().renderItem(pedestalInventory, ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 0);
        matrices.pop();

        // Commented out code is how to implement the end gateway effect!
//        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
//        this.renderSides(entity, matrix4f, vertexConsumers.getBuffer(this.getLayer()));
    }
//    private void renderSides(PedestalBlockEntity entity, Matrix4f matrix, VertexConsumer vertexConsumer) {
//        float f = this.getBottomYOffset();
//        float g = this.getTopYOffset();
//        this.renderSide(entity, matrix, vertexConsumer, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, Direction.SOUTH);
//        this.renderSide(entity, matrix, vertexConsumer, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, Direction.NORTH);
//        this.renderSide(entity, matrix, vertexConsumer, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, Direction.EAST);
//        this.renderSide(entity, matrix, vertexConsumer, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, Direction.WEST);
//        this.renderSide(entity, matrix, vertexConsumer, 0.0f, 1.0f, f, f, 0.0f, 0.0f, 1.0f, 1.0f, Direction.DOWN);
//        this.renderSide(entity, matrix, vertexConsumer, 0.0f, 1.0f, g, g, 1.0f, 1.0f, 0.0f, 0.0f, Direction.UP);
//    }
//
//    private void renderSide(PedestalBlockEntity entity, Matrix4f model, VertexConsumer vertices, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4, Direction side) {
//        if (((PedestalBlockEntity)entity).shouldDrawSide(side)) {
//            vertices.vertex(model, x1, y1, z1).next();
//            vertices.vertex(model, x2, y1, z2).next();
//            vertices.vertex(model, x2, y2, z3).next();
//            vertices.vertex(model, x1, y2, z4).next();
//        }
//    }
//
//    protected float getTopYOffset() {
//        return 0.75f;
//    }
//
//    protected float getBottomYOffset() {
//        return 0.375f;
//    }
//
//    protected RenderLayer getLayer() {
//        return RenderLayer.getEndGateway();
//    }

    @Override
    public boolean rendersOutsideBoundingBox(PedestalBlockEntity blockEntity) {
        return BlockEntityRenderer.super.rendersOutsideBoundingBox(blockEntity);
    }

    @Override
    public int getRenderDistance() {
        return BlockEntityRenderer.super.getRenderDistance();
    }

    @Override
    public boolean isInRenderDistance(PedestalBlockEntity blockEntity, Vec3d pos) {
        return BlockEntityRenderer.super.isInRenderDistance(blockEntity, pos);
    }
}
