package net.elindis.ruinsofarcana.block;

import net.elindis.ruinsofarcana.block.entity.ModBlockEntities;
import net.elindis.ruinsofarcana.block.entity.PedestalBlockEntity;
import net.elindis.ruinsofarcana.item.inventory.ImplementedInventory;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.listener.GameEventListener;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

public class PedestalBlock extends BlockWithEntity implements BlockEntityProvider, ImplementedInventory {
    public PedestalBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state){
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PedestalBlockEntity(pos, state);
    }
    public void saveData(World world, BlockEntity pedestalBlockEntity) {
        pedestalBlockEntity.markDirty();
        pedestalBlockEntity.toUpdatePacket();
        if (world != null && !world.isClient()) {
            world.updateListeners(pedestalBlockEntity.getPos(), pedestalBlockEntity.getCachedState(),
                    pedestalBlockEntity.getCachedState(), Block.NOTIFY_ALL);
        }

    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        // If we're on the server:
        if (!world.isClient) {

            // Grabs the inventory of the block entity at this blockstate's position.
            Inventory pedestalInventory = (Inventory) world.getBlockEntity(pos);

            // If the pedestal has an item, then give it back to the player, save the pedestal's data, and return.
            assert pedestalInventory != null;
            if (!pedestalInventory.getStack(0).isEmpty()) {

                player.getInventory().offerOrDrop(pedestalInventory.getStack(0));
                pedestalInventory.setStack(0, ItemStack.EMPTY);

                saveData(world, (Objects.requireNonNull(world.getBlockEntity(pos))));
                world.playSound(null, pos, SoundEvents.ENTITY_ITEM_FRAME_REMOVE_ITEM, SoundCategory.BLOCKS,
                        1f, 1f + (player.getRandom().nextInt(5) / 10f ));

                return ActionResult.SUCCESS;
            }

            //  If the pedestal's inventory is empty, then:
            if (pedestalInventory.getStack(0).isEmpty()) {

                // Grab what the player is holding, copy it, and set the count to 1.
                ItemStack playerHeldItemStack = player.getStackInHand(hand);
                ItemStack copyOfPlayerHeldItemStack = playerHeldItemStack.copy();
                copyOfPlayerHeldItemStack.setCount(1);

                // Set the pedestal's inventory to the single item we retrieved.
                pedestalInventory.setStack(0, copyOfPlayerHeldItemStack);

                // Remove an item from the player's hand, save data, and return.
                player.getStackInHand(hand).decrement(1);
                saveData(world, Objects.requireNonNull(world.getBlockEntity(pos)));
                world.playSound(null, pos, SoundEvents.ENTITY_ITEM_FRAME_ADD_ITEM, SoundCategory.BLOCKS,
                        1f, 1f + (player.getRandom().nextInt(5) / 10f ));
                return ActionResult.SUCCESS;
            }
        }

        // If we're on the client:
        return ActionResult.SUCCESS;

    }

    // This allows the item to be retrieved when the block is broken while holding one.
    // It's a common method, but fixed to allow the items to spread from a block higher than normal.
    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity thisBlockEntity = world.getBlockEntity(pos);
            if (thisBlockEntity instanceof PedestalBlockEntity pedestalBlockEntity) {
                BlockPos itemScattererPos = thisBlockEntity.getPos().add(0,1,0);
                ItemScatterer.spawn(world, itemScattererPos, pedestalBlockEntity);
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (world.isClient)  {
            return checkType(type, ModBlockEntities.PEDESTAL, (PedestalBlockEntity::tick));
        } else {
            return checkType(type, ModBlockEntities.PEDESTAL, (PedestalBlockEntity::serverTick));
        }
    }


    @Nullable
    @Override
    public <T extends BlockEntity> GameEventListener getGameEventListener(ServerWorld world, T blockEntity) {
        return super.getGameEventListener(world, blockEntity);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return null;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return ImplementedInventory.super.getAvailableSlots(side);
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction side) {
        return ImplementedInventory.super.canInsert(slot, stack, side);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction side) {
        return ImplementedInventory.super.canExtract(slot, stack, side);
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
    public void markDirty() {
        ImplementedInventory.super.markDirty();
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
}
