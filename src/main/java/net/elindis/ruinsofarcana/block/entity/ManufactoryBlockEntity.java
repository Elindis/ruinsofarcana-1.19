package net.elindis.ruinsofarcana.block.entity;

import net.elindis.ruinsofarcana.item.inventory.ImplementedInventory;
import net.elindis.ruinsofarcana.recipe.ManufactoryRecipe;
import net.elindis.ruinsofarcana.screen.ManufactoryScreenHandler;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static net.elindis.ruinsofarcana.block.ManufactoryBlock.FACING;

public class ManufactoryBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory =
            // changed from 4 to 5 to account for all 5 slots
            DefaultedList.ofSize(5, ItemStack.EMPTY);

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 200;
    private int fuelTime = 0;
    private int maxFuelTime = 0;

    public ManufactoryBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MANUFACTORY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                switch (index) {
                    case 0: return ManufactoryBlockEntity.this.progress;
                    case 1: return ManufactoryBlockEntity.this.maxProgress;
                    case 2: return ManufactoryBlockEntity.this.fuelTime;
                    case 3: return ManufactoryBlockEntity.this.maxFuelTime;
                    default: return 0;
                }
            }

            public void set(int index, int value) {
                switch(index) {
                    case 0: ManufactoryBlockEntity.this.progress = value; break;
                    case 1: ManufactoryBlockEntity.this.maxProgress = value; break;
                    case 2: ManufactoryBlockEntity.this.fuelTime = value; break;
                    case 3: ManufactoryBlockEntity.this.maxFuelTime = value; break;
                }
            }

            public int size() {
                return 4;
            }
        };
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }



    @Override
    // This method first checks the slot, and then it determines the rules for each slot. We do this by accessing the
    // blockState at this blockEntity's location, checking its FACING property, and then making rules based on that by
    // using the four compass directions of the Direction parameter.
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction side) {

        // Check the result slot first. This might be an optimization?
        if (slot == 4) {
            return false;
        }

        // Let's now iterate through the usable slots.
        switch (slot) {
            case 0:
                // This is the fuel slot. It should only accept fuel items, and they should only come through the top.
                if (AbstractFurnaceBlockEntity.canUseAsFuel(stack) && side.equals(Direction.UP)) {
                    return true;
                }
                break;

            case 1:
                // This is the first crafting slot. It only accepts items from the LEFT!
                switch (getThisBlockState().get(FACING)) {
                    case NORTH:
                        if (side.equals(Direction.WEST)) {return true;}
                        break;
                    case EAST:
                        if (side.equals(Direction.NORTH)) {return true;}
                        break;
                    case SOUTH:
                        if (side.equals(Direction.EAST)) {return true;}
                        break;
                    case WEST:
                        if (side.equals(Direction.SOUTH)) {return true;}
                        break;
                    default: return false;
                }
                break;
            case 2:
                // This is the second crafting slot. It only accepts items from the BACK!
                switch (getThisBlockState().get(FACING)) {
                    case NORTH:
                        if (side.equals(Direction.SOUTH)) {return true;}
                        break;
                    case EAST:
                        if (side.equals(Direction.WEST)) {return true;}
                        break;
                    case SOUTH:
                        if (side.equals(Direction.NORTH)) {return true;}
                        break;
                    case WEST:
                        if (side.equals(Direction.EAST)) {return true;}
                        break;
                    default: return false;
                }
                break;

            case 3:
                // This is the third crafting slot. It only accepts items from the RIGHT!
                switch (getThisBlockState().get(FACING)) {
                    case NORTH:
                        if (side.equals(Direction.EAST)) {return true;}
                        break;
                    case EAST:
                        if (side.equals(Direction.SOUTH)) {return true;}
                        break;
                    case SOUTH:
                        if (side.equals(Direction.WEST)) {return true;}
                        break;
                    case WEST:
                        if (side.equals(Direction.NORTH)) {return true;}
                        break;
                    default: return false;
                }
                break;
        }
        return false;
    }

    public BlockState getThisBlockState() {
        return this.world.getBlockState(this.pos);
    }


    @Override
    public boolean canExtract(int slot, ItemStack stack, @Nullable Direction dir) {
        // Only the result slot should be extractable.
        return slot == 4;
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Enchanted Assembler");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new ManufactoryScreenHandler(syncId, inv, this, this.propertyDelegate);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("blaster.progress", progress);
        nbt.putInt("blaster.fuelTime", fuelTime);
        nbt.putInt("blaster.maxFuelTime", maxFuelTime);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, inventory);
        super.readNbt(nbt);
        progress = nbt.getInt("blaster.progress");
        fuelTime = nbt.getInt("blaster.fuelTime");
        maxFuelTime = nbt.getInt("blaster.maxFuelTime");
    }

    private void consumeFuel() {
        if(!getStack(0).isEmpty()) {
            this.fuelTime = FuelRegistry.INSTANCE.get(this.removeStack(0, 1).getItem());
            this.maxFuelTime = this.fuelTime;
        }
    }

    // Removed requirement to have redstone power
    public static void tick(World world, BlockPos pos, BlockState state, ManufactoryBlockEntity entity) {
        if(isConsumingFuel(entity)) {
            entity.fuelTime--;
        }
        if (hasRecipe(entity)) {
            if(hasFuelInFuelSlot(entity) && !isConsumingFuel(entity)) {
                entity.consumeFuel();
            }
            if(isConsumingFuel(entity)) {
                    entity.progress++;
                    if(entity.progress >= entity.maxProgress)
                        { craftItem(entity); }
                }

            if((!hasFuelInFuelSlot(entity) && !isConsumingFuel(entity)) ) {
                entity.resetProgress();
            }
        }
        else
        {
            entity.resetProgress();
        }
    }

    private static boolean hasFuelInFuelSlot(ManufactoryBlockEntity entity) {
        return !entity.getStack(0).isEmpty();
    }

    private static boolean isConsumingFuel(ManufactoryBlockEntity entity) {
        return entity.fuelTime > 0;
    }

    private static boolean hasRecipe(ManufactoryBlockEntity entity) {
        World world = entity.world;
        SimpleInventory inventory = new SimpleInventory(entity.inventory.size());
        for (int i = 0; i < entity.inventory.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        Optional<ManufactoryRecipe> match = world.getRecipeManager()
                .getFirstMatch(ManufactoryRecipe.Type.INSTANCE, inventory, world);

        return match.isPresent() && canInsertAmountIntoOutputSlot(inventory)
                && canInsertItemIntoOutputSlot(inventory, match.get().getOutput());
    }

    private static void craftItem(ManufactoryBlockEntity entity) {
        World world = entity.world;
        SimpleInventory inventory = new SimpleInventory(entity.inventory.size());
        for (int i = 0; i < entity.inventory.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        Optional<ManufactoryRecipe> match = world.getRecipeManager()
                .getFirstMatch(ManufactoryRecipe.Type.INSTANCE, inventory, world);

        if(match.isPresent()) {
            entity.removeStack(1,1);
            entity.removeStack(2,1);
            // added slot 3
            entity.removeStack(3, 1);

            //changed to slot 4
            entity.setStack(4, new ItemStack(match.get().getOutput().getItem(),
                    entity.getStack(4).getCount() + 1));

            entity.resetProgress();
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    // changed from 3 to 4 in both these methods
    private static boolean canInsertItemIntoOutputSlot(SimpleInventory inventory, ItemStack output) {
        return inventory.getStack(4).getItem() == output.getItem() || inventory.getStack(4).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleInventory inventory) {
        return inventory.getStack(4).getMaxCount() > inventory.getStack(4).getCount();
    }
}
