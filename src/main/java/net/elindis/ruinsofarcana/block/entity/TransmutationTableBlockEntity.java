package net.elindis.ruinsofarcana.block.entity;
import net.elindis.ruinsofarcana.item.ModItems;
import net.elindis.ruinsofarcana.item.inventory.ImplementedInventory;
import net.elindis.ruinsofarcana.recipe.TransmutationTableRecipe;
import net.elindis.ruinsofarcana.util.AdvancementRecipes;
import net.elindis.ruinsofarcana.util.ModParticleUtil;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class TransmutationTableBlockEntity extends BlockEntity implements ImplementedInventory {

    // TODO: an incorrect recipe/not having the advancement causes BAD THINGS to happen
    // Data
    private DefaultedList<ItemStack> itemInTransmutationTable = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private boolean shouldDoParticles;
    private int dataInt;

    //protected final PropertyDelegate propertyDelegate;


    // Constructor
    public TransmutationTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TRANSMUTATION_TABLE, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, TransmutationTableBlockEntity blockEntity) {
        if (blockEntity.shouldDoParticles) {
            world.addParticle(ParticleTypes.GLOW, pos.getX(), pos.getY()+1, pos.getZ(), 0.1d, 0.1d, 0.1d);
        }

    }

    public static void serverTick(@NotNull World world, BlockPos blockPos, BlockState blockState, TransmutationTableBlockEntity transmutationTableBlockEntity) {

    }
    public static void craftItem(TransmutationTableBlockEntity entity, PlayerEntity playerEntity) {

        World world = entity.world;

        assert world != null;
        PedestalBlockEntity pedestalBlockEntity1 = null;
        if (world.getBlockEntity(entity.pos.add(3, 0, 0)) instanceof PedestalBlockEntity) {
            pedestalBlockEntity1 = (PedestalBlockEntity) world.getBlockEntity(entity.pos.add(3, 0, 0));
        }
        PedestalBlockEntity pedestalBlockEntity2 = null;
        if (world.getBlockEntity(entity.pos.add(-3, 0, 0)) instanceof PedestalBlockEntity) {
            pedestalBlockEntity2 = (PedestalBlockEntity) world.getBlockEntity(entity.pos.add(-3, 0, 0));
        }
        PedestalBlockEntity pedestalBlockEntity3 = null;
        if (world.getBlockEntity(entity.pos.add(0, 0, 3)) instanceof PedestalBlockEntity) {
            pedestalBlockEntity3 = (PedestalBlockEntity) world.getBlockEntity(entity.pos.add(0, 0, 3));
        }
        PedestalBlockEntity pedestalBlockEntity4 = null;
        if (world.getBlockEntity(entity.pos.add(0, 0, -3)) instanceof PedestalBlockEntity) {
            pedestalBlockEntity4 = (PedestalBlockEntity) world.getBlockEntity(entity.pos.add(0, 0, -3));
        }

        if (pedestalBlockEntity1 == null || pedestalBlockEntity2 == null || pedestalBlockEntity3 == null || pedestalBlockEntity4 == null) {
            return;
        }

        // Create a list of the pedestals
        ArrayList<PedestalBlockEntity> allPedestals = new ArrayList<>();

        allPedestals.add(pedestalBlockEntity1);
        allPedestals.add(pedestalBlockEntity2);
        allPedestals.add(pedestalBlockEntity3);
        allPedestals.add(pedestalBlockEntity4);

        // Build the simpleInventory's size based on the number of pedestals with items in them, plus 1.

        int inventorySize = 1;
        for (int i = 0; i < allPedestals.size(); i++) {
            if (allPedestals.get(i).getStack(0) != ItemStack.EMPTY && allPedestals.get(i).getStack(0) != null) {
                inventorySize++;
            }
        }

        // Add the player's offhand. That's part of the recipe now - the player must hold the instructions for the ritual.
//        if (playerEntity.getMainHandStack().isIn(ModTags.Items.RESEARCH_NOTES)) inventorySize++;

        SimpleInventory inventory = new SimpleInventory(inventorySize);

        // Basically, what this does is it builds the inventory only if the pedestal contains an item
        // Except for the transmutation table, which is always built into slot 0, even when empty.
        // And also the player's held item, which is always built into slot 1.
        int inventoryStack = 1;
        if (entity.getStack(0) != ItemStack.EMPTY && entity.getStack(0) != null) inventory.setStack(0, entity.getStack(0));

        for (PedestalBlockEntity pedestal : allPedestals) {
            if (pedestal.getStack(0) != ItemStack.EMPTY &&
                    pedestal.getStack(0) != null) {

                inventory.setStack(inventoryStack, pedestal.getStack(0));
                world.getServer().sendMessage(Text.literal("built inventory" + inventoryStack));
                inventoryStack++;
            }
        }
        // Added offhand to the crafting table.
//        if (playerEntity.getMainHandStack().isIn(ModTags.Items.RESEARCH_NOTES)) inventory.setStack(inventoryStack, playerEntity.getMainHandStack());
        world.getServer().sendMessage(Text.literal(inventory.size() +" inventory size"));


        // Check for a crafting recipe match
        Optional<TransmutationTableRecipe> match =
                world.getRecipeManager().getFirstMatch(TransmutationTableRecipe.Type.INSTANCE, inventory, world);

        if (match.isPresent()) {
            playerEntity.world.getServer().sendMessage(Text.literal("crafting!"));


            if (!AdvancementRecipes.checkRecipeAgainstAdvancements(match.get().getOutput(), playerEntity)) {
                return;
            }


            // Remove their items and save their data
            for (PedestalBlockEntity allPedestal : allPedestals) {
                allPedestal.setStack(0, ItemStack.EMPTY);
                allPedestal.saveData();
            }

            // Craft the item

            entity.setStack(0, (match.get().craft(inventory)));
            entity.saveData();

            // Do special effects
            world.playSound(null, entity.getPos(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, SoundCategory.BLOCKS, 0.6f, 2f);
            world.playSound(null, entity.getPos(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, .8f, 1);

            ModParticleUtil.doTransmutationTableParticles(entity, ParticleTypes.GLOW, 40);
            for (PedestalBlockEntity allPedestal : allPedestals) {
                ModParticleUtil.doTransmutationTableParticles(allPedestal, ParticleTypes.LARGE_SMOKE, 10);
            }

        }
    }


    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.itemInTransmutationTable;
    }

    // Networking
    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("transmutationTable.dataInt", dataInt);
        Inventories.writeNbt(nbt, itemInTransmutationTable);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.itemInTransmutationTable = DefaultedList.ofSize(1, ItemStack.EMPTY);
        Inventories.readNbt(nbt, this.itemInTransmutationTable);

        dataInt = nbt.getInt("transmutationTable.dataInt");
        Inventories.readNbt(nbt, itemInTransmutationTable);
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
    public static void updateInClientWorld(TransmutationTableBlockEntity transmutationTableBlockEntity) {
        ((ServerWorld) transmutationTableBlockEntity.world).getChunkManager().markForUpdate(transmutationTableBlockEntity.pos);
    }

    private static boolean hasLightBowRecipe(PlayerEntity playerEntity) {
        Identifier id = new Identifier("ruinsofarcana:lightbow");
        return checkAdvancement(playerEntity, id);
    }
    private static boolean hasArkeniteIngotRecipe(PlayerEntity playerEntity) {
        Identifier id = new Identifier("ruinsofarcana:arkenite_ingot");
        return checkAdvancement(playerEntity, id);
    }

    private static boolean checkAdvancement (PlayerEntity playerEntity, Identifier id) {
        ServerPlayerEntity player = (ServerPlayerEntity) playerEntity;
        return getAdvancement(player, id).isDone();
    }

    private static AdvancementProgress getAdvancement (ServerPlayerEntity playerEntity, Identifier id) {
        return (playerEntity).getAdvancementTracker().getProgress(
                Objects.requireNonNull(playerEntity.getServer()).getAdvancementLoader().get(id));
    }

    public static boolean checkRecipeAgainstAdvancements(ItemStack output, PlayerEntity playerEntity) {

        if (hasLightBowRecipe(playerEntity) && output.isOf(ModItems.LIGHT_BOW)) {
            return true;
        }

//        if (hasArkeniteIngotRecipe(playerEntity) && output.isOf(ModItems.ARKENITE_INGOT)) {
//            return true;
//        }

        return false;
    }


}
