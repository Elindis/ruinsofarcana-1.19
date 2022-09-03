package net.elindis.ruinsofarcana.block.entity.renderer;

import net.elindis.ruinsofarcana.block.entity.SingularityBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class SingularityEntityRenderer<T extends SingularityBlockEntity> implements BlockEntityRenderer<SingularityBlockEntity>  {
    public SingularityEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        super();
    }

    @Override
    public void render(SingularityBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
//         Commented out code is how to implement the end gateway effect!
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        this.renderSides(entity, matrix4f, vertexConsumers.getBuffer(this.getLayer()));
    }

    private void renderSides(SingularityBlockEntity entity, Matrix4f matrix, VertexConsumer vertexConsumer) {
        float f = this.getBottomYOffset();
        float g = this.getTopYOffset();
        this.renderSide(entity, matrix, vertexConsumer, 0.375f, 0.625f, 0.375f, 0.625f, 0.625f, 0.625f, 0.625f, 0.625f, Direction.SOUTH);
        this.renderSide(entity, matrix, vertexConsumer, 0.375f, 0.625f, 0.625f, 0.375f, 0.375f, 0.375f, 0.375f, 0.375f, Direction.NORTH);
        this.renderSide(entity, matrix, vertexConsumer, 0.625f, 0.625f, 0.625f, 0.375f, 0.375f, 0.625f, 0.625f, 0.375f, Direction.EAST);
        this.renderSide(entity, matrix, vertexConsumer, 0.375f, 0.375f, 0.375f, 0.625f, 0.375f, 0.625f, 0.625f, 0.375f, Direction.WEST);
        this.renderSide(entity, matrix, vertexConsumer, 0.375f, 0.625f, f, f, 0.375f, 0.375f, 0.625f, 0.625f, Direction.DOWN);
        this.renderSide(entity, matrix, vertexConsumer, 0.375f, 0.625f, g, g, 0.625f, 0.625f, 0.375f, 0.375f, Direction.UP);
    }

    private void renderSide(SingularityBlockEntity entity, Matrix4f model, VertexConsumer vertices, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4, Direction side) {
        if (entity.shouldDrawSide(side)) {
            vertices.vertex(model, x1, y1, z1).next();
            vertices.vertex(model, x2, y1, z2).next();
            vertices.vertex(model, x2, y2, z3).next();
            vertices.vertex(model, x1, y2, z4).next();
        }
    }

    protected float getTopYOffset() {
        return 0.625f;
    }

    protected float getBottomYOffset() {
        return 0.375f;
    }

    protected RenderLayer getLayer() {
        return RenderLayer.getEndGateway();
    }

    @Override
    public boolean rendersOutsideBoundingBox(SingularityBlockEntity blockEntity) {
        return BlockEntityRenderer.super.rendersOutsideBoundingBox(blockEntity);
    }

    @Override
    public int getRenderDistance() {
        return BlockEntityRenderer.super.getRenderDistance();
    }

    @Override
    public boolean isInRenderDistance(SingularityBlockEntity blockEntity, Vec3d pos) {
        return BlockEntityRenderer.super.isInRenderDistance(blockEntity, pos);
    }
}
