package net.elindis.ruinsofarcana.block.entity;

import net.elindis.ruinsofarcana.particle.ModParticles;
import net.elindis.ruinsofarcana.util.ModParticleUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SingularityBlockEntity extends BlockEntity {

    // Constructor
    public SingularityBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SINGULARITY, pos, state);
    }
    int progress = 0;

    public static void tick(World world, BlockPos pos, BlockState state, SingularityBlockEntity blockEntity) {
        world.addParticle(ModParticles.SPIRAL_PARTICLE, pos.getX()+0.5f, pos.getY()+0.5f, pos.getZ()+0.5f, 0, 0,0);
        world.addParticle(ModParticles.JET_PARTICLE, pos.getX()+0.5f, pos.getY()+0.5f, pos.getZ()+0.5f, 0.1, 0,.1);

//        int i = ((SingularityBlockEntity)blockEntity).getDrawnSidesCount();
//        for (int j = 0; j < i; ++j) {
//            double d = (double)pos.getX()+ world.random.nextDouble();
//            double e = (double)pos.getY()+ world.random.nextDouble();
//            double f = (double)pos.getZ()+ world.random.nextDouble();
//            double g = (world.random.nextDouble() - 0.5) * 0.5;
//            double h = (world.random.nextDouble() - 0.5) * 0.5;
//            double k = (world.random.nextDouble() - 0.5) * 0.5;
//            int l = world.random.nextInt(2) * 2 - 1;
//            if (world.random.nextBoolean()) {
//                f = (double)pos.getZ() + 0.25 + 0.25 * (double)l;
//                k = world.random.nextFloat() * 2.0f * (float)l;
//            } else {
//                d = (double)pos.getX() + 0.25 + 0.25 * (double)l;
//                g = world.random.nextFloat() * 2.0f * (float)l;
//            }
//            world.addParticle(ModParticles.SINGULARITY_PARTICLE, d, e, f, g, h, k);
//        }
    }
    // 1 - current/max = an equation that goes from 1 to 0 linearly
    // xpos = sin(age) = oscillating. add zpos = sin(age) and you have a circle
    // combine all these for a spiral?

    public static void serverTick(@NotNull World world, BlockPos blockPos, BlockState blockState, SingularityBlockEntity blockEntity) {
//        ModParticleUtil.doSingularityParticles(blockEntity, ModParticles.SINGULARITY_PARTICLE2, 10);
    }

    public boolean shouldDrawSide(Direction direction) {
        return Block.shouldDrawSide(this.getCachedState(), this.world, this.getPos(), direction, this.getPos().offset(direction));
    }

    public int getDrawnSidesCount() {
        int i = 0;
        for (Direction direction : Direction.values()) {
            i += this.shouldDrawSide(direction) ? 1 : 0;
        }
        return i;
    }

    @Override
    @Nullable
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }
    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbtCompound = new NbtCompound();
        this.writeNbt(nbtCompound);
        return nbtCompound;
    }

    public void saveData() {
        this.markDirty();
        if (world != null) {
            world.updateListeners(getPos(), getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
        }
    }

    public static void updateInClientWorld(SingularityBlockEntity pedestalBlockEntity) {
        ((ServerWorld) pedestalBlockEntity.world).getChunkManager().markForUpdate(pedestalBlockEntity.pos);
    }
}
