package net.elindis.ruinsofarcana.block.entity;

import net.elindis.ruinsofarcana.item.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GravityInverterBlockEntity extends BlockEntity  {

    // Constructor
    public GravityInverterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GRAVITY_INVERTER, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, GravityInverterBlockEntity blockEntity) {
        Box box = new Box(pos).expand(1, 32, 1).withMinY(pos.getY());
        List<PlayerEntity> entityList = world.getPlayers(TargetPredicate.DEFAULT, null, box);
        for (Entity player: entityList) {
            if (player.getVelocity().getY() < 0.5f) {
                List<ItemStack> equipped = new ArrayList<>();
                player.getArmorItems().forEach(equipped::add);
                System.out.println(equipped);
                if (!equipped.get(1).isOf(ModItems.GRAVITY_BELT)) {
                    player.addVelocity(0, 0.15, 0);
                }
            }
        }

    }

    public static void serverTick(@NotNull World world, BlockPos pos, BlockState state, GravityInverterBlockEntity blockEntity) {
        Box box = new Box(pos).expand(2, 32, 2).withMinY(pos.getY());
        List<Entity> entityList = world.getOtherEntities(null, box);
        for (Entity entity: entityList) {
            if (entity.getVelocity().getY() < 0.5f && entity instanceof ItemEntity) {
                entity.addVelocity(0, 0.05, 0);
            }
            else {
                entity.addVelocity(0, 0.15, 0);
            }
        }
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

    public static void updateInClientWorld(GravityInverterBlockEntity blockEntity) {
        ((ServerWorld) blockEntity.world).getChunkManager().markForUpdate(blockEntity.pos);
    }
}
