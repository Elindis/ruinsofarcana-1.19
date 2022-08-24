package net.elindis.ruinsofarcana.block.entity;
import net.elindis.ruinsofarcana.item.inventory.ImplementedInventory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Predicate;

public class PedestalBlockEntity extends BlockEntity implements ImplementedInventory {

    // Data
    private DefaultedList<ItemStack> itemInPedestal = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private int dataInt;

    // Constructor
    public PedestalBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PEDESTAL, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, PedestalBlockEntity blockEntity) {

    }
//    public boolean shouldDrawSide(Direction direction) {
//        return Block.shouldDrawSide(this.getCachedState(), this.world, this.getPos(), direction, this.getPos().offset(direction));
//    }

    public static void serverTick(@NotNull World world, BlockPos blockPos, BlockState blockState, PedestalBlockEntity pedestalBlockEntity) {

    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.itemInPedestal;
    }

    // Networking
    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("pedestal.dataInt", dataInt);
        Inventories.writeNbt(nbt, itemInPedestal);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.itemInPedestal = DefaultedList.ofSize(1, ItemStack.EMPTY);
        Inventories.readNbt(nbt, this.itemInPedestal);

        dataInt = nbt.getInt("pedestal.dataInt");
        Inventories.readNbt(nbt, itemInPedestal);
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

    @Override
    public int[] getAvailableSlots(Direction side) {
        return ImplementedInventory.super.getAvailableSlots(side);
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction side) {
        return false;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction side) {
        return false;
    }

    @Override
    public int size() {
        return ImplementedInventory.super.size();
    }

    @Override
    public boolean isEmpty() {
        return ImplementedInventory.super.isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        return ImplementedInventory.super.getStack(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int count) {
        return ImplementedInventory.super.removeStack(slot, count);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return ImplementedInventory.super.removeStack(slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        ImplementedInventory.super.setStack(slot, stack);
    }

    @Override
    public int getMaxCountPerStack() {
        return ImplementedInventory.super.getMaxCountPerStack();
    }

    @Override
    public void clear() {
        ImplementedInventory.super.clear();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return ImplementedInventory.super.canPlayerUse(player);
    }

    @Override
    public void onOpen(PlayerEntity player) {
        ImplementedInventory.super.onOpen(player);
    }

    @Override
    public void onClose(PlayerEntity player) {
        ImplementedInventory.super.onClose(player);
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        return ImplementedInventory.super.isValid(slot, stack);
    }

    @Override
    public int count(Item item) {
        return ImplementedInventory.super.count(item);
    }

    @Override
    public boolean containsAny(Set<Item> items) {
        return ImplementedInventory.super.containsAny(items);
    }

    @Override
    public boolean containsAny(Predicate<ItemStack> predicate) {
        return ImplementedInventory.super.containsAny(predicate);
    }
    public static void updateInClientWorld(PedestalBlockEntity pedestalBlockEntity) {
        ((ServerWorld) pedestalBlockEntity.world).getChunkManager().markForUpdate(pedestalBlockEntity.pos);
    }
}
