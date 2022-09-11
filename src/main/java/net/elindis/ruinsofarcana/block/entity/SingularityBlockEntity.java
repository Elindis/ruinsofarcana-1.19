package net.elindis.ruinsofarcana.block.entity;

import net.elindis.ruinsofarcana.block.ModBlocks;
import net.elindis.ruinsofarcana.item.ModItems;
import net.elindis.ruinsofarcana.item.inventory.ImplementedInventory;
import net.elindis.ruinsofarcana.particle.ModParticles;
import net.elindis.ruinsofarcana.recipe.SingularityRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
public class SingularityBlockEntity extends BlockEntity implements ImplementedInventory {

    // TODO: REFACTOR FOR CLEANLINESS
    // TODO: Spit out items from jets

    // Constructor
    public SingularityBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SINGULARITY, pos, state);
    }
    public static void tick(World world, BlockPos pos, BlockState state, SingularityBlockEntity blockEntity) {
        doParticles(world, pos);

        // Sucking in the player. See the serverside for more detailed comments if you need them.
        Vec3d singularityVector = new Vec3d(pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f);
        BlockPos fixedBlockPos = new BlockPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        Box singularityGravityBox = new Box(pos).expand(5);
        float gravity = 0.125f;
        List<Entity> entityList = world.getOtherEntities(null, singularityGravityBox);
        if (entityList.isEmpty()) return;
        for (Entity entity : entityList) {
            if (entity instanceof PlayerEntity && shouldSuck(entity)) {
                double distance = singularityVector.distanceTo(entity.getPos()) + 0.01;
                entity.addVelocity(
                        (fixedBlockPos.getX() - (entity.getX() - 0.5)) * gravity / distance,
                        (fixedBlockPos.getY() - (entity.getY() - 0.5)) * gravity / distance,
                        (fixedBlockPos.getZ() - (entity.getZ() - 0.5)) * gravity / distance);
            }
        }
    }

    private static void doParticles(World world, BlockPos pos) {
        world.addParticle(ModParticles.SPIRAL_PARTICLE, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, 0, 0, 0);
        world.addParticle(ModParticles.SPIRAL_PARTICLE, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, 0, 0, 0);
        world.addParticle(ModParticles.JET_PARTICLE, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, 0.1, 0, .1);
        world.addParticle(ModParticles.CORE_PARTICLE, pos.getX()+ 0.5f, pos.getY()+ 0.5f, pos.getZ()+ 0.5f, pos.getX()+ 0.5f, pos.getY()+ 0.5f, pos.getZ()+ 0.5f);
        int rand = Random.create().nextBetween(0,20);
        if (rand == 0) {
            world.addParticle(ModParticles.SINGULARITY_PARTICLE,
                    pos.getX() + (Random.create().nextFloat()-0.5)*2, pos.getY() + (Random.create().nextFloat()-0.5)*2,
                    pos.getZ() + (Random.create().nextFloat()-0.5)*2, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f);
        }
    }

    // Maybe you should make a Stable and an Unstable version. The stable one is the one that crafts blocks, and the unstable one turns everything into exp.
    public static void serverTick(@NotNull World world, BlockPos blockPos, BlockState blockState, SingularityBlockEntity blockEntity) {
        if (world.isClient) return;

        // Position of this entity's block.
        Vec3d singularityVector = new Vec3d(blockPos.getX() + 0.5f, blockPos.getY(), blockPos.getZ() + 0.5f);
        BlockPos fixedBlockPos = new BlockPos(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);

        // Size and strength of the black hole's gravity field.
        Box singularityGravityBox = new Box(blockPos).expand(5);
        double gravity = 0.125d;

        // Is this the same as a stream? I think it's faster. This breaks blocks in the wild in a 11x11x11 radius,
        // and the shape ends up as a cube. I would prefer a sphere, but performance is a consideration.
        // Now removes fluids.
        if (world.getRandom().nextInt(100) == 0) {
            BlockPos.iterateOutwards(fixedBlockPos, 5, 5, 5).forEach(blockPos1 -> {
                if (world.getRandom().nextInt(100) == 0 && !world.getBlockState(blockPos1).isIn(BlockTags.WITHER_IMMUNE)
                        && !world.getBlockState(blockPos1).isOf(ModBlocks.SINGULARITY)) {
                    world.breakBlock(blockPos1, true);
                }
                if (!world.getFluidState(blockPos1).isEmpty() && world.getRandom().nextInt(100) < 20) {
                    world.setBlockState(blockPos1, Blocks.AIR.getDefaultState());
                }
            });
        }

        // Gathers a list of all the entities in the 11x11x11 area of the black hole.
        List<Entity> entityList = world.getOtherEntities(null, singularityGravityBox);
        SimpleInventory inventory = new SimpleInventory(1);

        // For every entity in that list, we do stuff:
        for (Entity entity : entityList) {

            // Entities we want to be affected by the black hole gravity get sucked in:
            if (!(entity instanceof ExperienceOrbEntity)) {

                // We use distance to taper the gravity's strength. Add +0.01 in order to prevent division by 0.
                double distance = singularityVector.distanceTo(entity.getPos()) + 0.01;

                // Subtracting two vectors gives you the direction. Multiply by our inverse gravity and divide by
                // the distance in order to get realistic physics behaviour.
                if (shouldSuck(entity)) {
                    entity.addVelocity(
                            (fixedBlockPos.getX() - (entity.getX() - 0.5)) * gravity / distance,
                            (fixedBlockPos.getY() - (entity.getY() - 0.5)) * gravity / distance,
                            (fixedBlockPos.getZ() - (entity.getZ() - 0.5)) * gravity / distance);
                }

                // If the entity is alive and nearby, we damage it:
                if (entity instanceof LivingEntity && distance < 1) {
                    entity.damage(DamageSource.WITHER, 4);
                }

                // If the entity is a projectile, we destroy it:
                if (entity instanceof ProjectileEntity && distance < 1) entity.discard();

                // If the entity is a block item and nearby, we craft it:
                if (entity instanceof ItemEntity && distance < 0.35) {
                    if (((ItemEntity) entity).getStack().getItem() instanceof BlockItem) {

                        // The inventory is set to the complete stack of the itementity's itemstack.
                        // The itementity is then discarded.
                        inventory.setStack(0, ((ItemEntity) entity).getStack());
                        inventory.getStack(0).setCount(((ItemEntity) entity).getStack().getCount());
                        entity.discard();

                        // Every blockitem gets crushed. We take the stack, find out how many items are in it, and give
                        // each item a 10% chance of spawning an exp orb.
                        for (int i = 0; i < inventory.getStack(0).getCount(); i++) {
                            float randomExp = Random.create().nextFloat();
                            if (randomExp > 0.9f) {
                                ExperienceOrbEntity.spawn((ServerWorld) world, singularityVector, 1);
                            }
                        }

                        // We check for a crafting recipe match.
                        Optional<SingularityRecipe> match =
                                world.getRecipeManager().getFirstMatch(SingularityRecipe.Type.INSTANCE, inventory, world);

                        // If the recipe is valid, we replace the stack in the inventory with the result, and spawn it.
                        if (match.isPresent()) {

                            // Create a result inventory, and set its stack to the crafting result with the same count
                            // as the input inventory, then spawn it and empty the input
                            // The size of this inventory is actually the max count of outputs in the recipe
                            SimpleInventory resultInventory = new SimpleInventory(9);
                            ItemStack outputItemStack = new ItemStack(match.get().craft(inventory).getItem(), match.get().getOutput().getCount());

//                             So, we just duplicate the output into the result inventory based on the count
//                            // 1 iron block = 9 iron ingots, 1 in each slot. I think!
                            for (int i = 0; i < outputItemStack.getCount(); i++) {
                                resultInventory.setStack(i, outputItemStack.getItem().getDefaultStack());
                                resultInventory.getStack(i).setCount(inventory.getStack(0).getCount());
                            }
                            inventory.clear();
                            ItemScatterer.spawn(world, blockPos, resultInventory);
                        }
                    }
                }
            }
        }
    }

    private static boolean shouldSuck(Entity entity) {
        // Items should only be sucked if they're blockitems.
        if (entity instanceof ItemEntity) {
            Item item = ((ItemEntity) entity).getStack().getItem();
            // TODO: Make this a tag instead... of items that shouldn't be sucked in, since they are few.
            if (!(item instanceof BlockItem)) {
                return false;
            }
        }
        // Player shouldn't be sucked if they're wearing a gravity belt or in creative mode.
        if (entity instanceof PlayerEntity) {
            if (((PlayerEntity) entity).getInventory().getArmorStack(1).isOf(ModItems.GRAVITY_BELT) || ((PlayerEntity) entity).getAbilities().creativeMode) {
                return false;
            }
        }
        return true;
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
    @Override
    public DefaultedList<ItemStack> getItems() {
        return null;
    }
    public boolean shouldDrawSide(Direction direction) {
        return Block.shouldDrawSide(this.getCachedState(), this.world, this.getPos(), direction, this.getPos().offset(direction));
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
}
