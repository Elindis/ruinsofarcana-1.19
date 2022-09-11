package net.elindis.ruinsofarcana.block;

import net.elindis.ruinsofarcana.item.ModItems;
import net.elindis.ruinsofarcana.util.ModParticleUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class AmphoraBlock extends Block {
    private static final VoxelShape DEFAULT_SHAPE = Stream.of(
            Block.createCuboidShape(5, 0, 5, 11, 1, 11),
            Block.createCuboidShape(5, 2, 5, 11, 4, 11),
            Block.createCuboidShape(6, 1, 6, 10, 2, 10),
            Block.createCuboidShape(4, 4, 4, 12, 6, 12),
            Block.createCuboidShape(3, 6, 3, 13, 10, 13),
            Block.createCuboidShape(6, 10, 6, 10, 14, 10),
            Block.createCuboidShape(5, 14, 5, 11, 16, 11)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();

    public static final IntProperty LIQUID_TYPE = IntProperty.of("liquid_type", 0, 2);

    public AmphoraBlock(Settings settings) {
        super(settings);
        this.setDefaultState((this.stateManager.getDefaultState())
                .with(LIQUID_TYPE, 0));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return DEFAULT_SHAPE;
    }

    @Override
    public VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
        return DEFAULT_SHAPE;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        // Client-side handling
        if (world.isClient) {
            if (player.getStackInHand(hand).isOf(Items.BUCKET) && !getStateWithProperties(state).get(LIQUID_TYPE).equals(2)) {
                player.playSound(SoundEvents.ITEM_BUCKET_FILL, 1f, 0.8f+(player.getRandom().nextFloat()/3));
                return ActionResult.SUCCESS;
            }
            if (player.getStackInHand(hand).isOf(Items.BUCKET) && getStateWithProperties(state).get(LIQUID_TYPE).equals(2)) {
                player.playSound(SoundEvents.ITEM_BUCKET_FILL_LAVA, 1f, 0.8f+(player.getRandom().nextFloat()/3));
                return ActionResult.SUCCESS;
            }
            if (player.getStackInHand(hand).isOf(Items.GLASS_BOTTLE) && getStateWithProperties(state).get(LIQUID_TYPE).equals(0)) {
                player.playSound(SoundEvents.ITEM_BOTTLE_FILL, 1f, 0.8f+(player.getRandom().nextFloat()/3));
                return ActionResult.SUCCESS;
            }
            return ActionResult.PASS;
        }

        // Server-side handling
        if (getStateWithProperties(state).get(LIQUID_TYPE).equals(0)) {
            if (player.getStackInHand(hand).isOf(ModItems.WAND_OF_TRANSMUTATION)) {
                world.setBlockState(pos, ModBlocks.ALCHEMISTS_AMPHORA.getDefaultState().with(LIQUID_TYPE, 1));
                doSpecialEffects(world, pos, player);
            }

            if (player.getStackInHand(hand).isOf(Items.BUCKET)) {
                player.getStackInHand(hand).decrement(1);
                player.getInventory().offerOrDrop(new ItemStack(Items.WATER_BUCKET));
                return ActionResult.PASS;
            }
            if (player.getStackInHand(hand).isOf(Items.GLASS_BOTTLE)) {
                player.getStackInHand(hand).decrement(1);
                ItemStack waterBottle = PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WATER);
                player.getInventory().offerOrDrop(waterBottle);
                return ActionResult.PASS;
            }
        }
        if (getStateWithProperties(state).get(LIQUID_TYPE).equals(1)) {
            if (player.getStackInHand(hand).isOf(ModItems.WAND_OF_TRANSMUTATION)) {
                world.setBlockState(pos, ModBlocks.ALCHEMISTS_AMPHORA.getDefaultState().with(LIQUID_TYPE, 2));
                doSpecialEffects(world, pos, player);
            }
            if (player.getStackInHand(hand).isOf(Items.BUCKET)) {
                player.getStackInHand(hand).decrement(1);
                player.getInventory().offerOrDrop(new ItemStack(Items.MILK_BUCKET));
                return ActionResult.PASS;
            }
        }
        if (getStateWithProperties(state).get(LIQUID_TYPE).equals(2)) {
            if (player.getStackInHand(hand).isOf(ModItems.WAND_OF_TRANSMUTATION)) {
                world.setBlockState(pos, ModBlocks.ALCHEMISTS_AMPHORA.getDefaultState().with(LIQUID_TYPE, 0));
                doSpecialEffects(world, pos, player);
            }
            if (player.getStackInHand(hand).isOf(Items.BUCKET)) {
                player.getStackInHand(hand).decrement(1);
                player.getInventory().offerOrDrop(new ItemStack(Items.LAVA_BUCKET));
                return ActionResult.PASS;
            }
        }
        return ActionResult.PASS;
    }

    private void doSpecialEffects(World world, BlockPos pos, PlayerEntity player) {
        ModParticleUtil.doBlockParticles(world, pos, player, ParticleTypes.GLOW, 40, 0.1f);
        ModParticleUtil.doBlockParticles(world, pos, player, ParticleTypes.LARGE_SMOKE, 10, 0.1f);
        world.playSound(null, pos, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, SoundCategory.BLOCKS, 0.6f, 2f);
        world.playSound(null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS,
                .8f, 0.8f+(player.getRandom().nextFloat()/3));
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (world.isClient) return;
        int liquidType = 0;
        if (itemStack.hasNbt()) {
            assert itemStack.getNbt() != null;
            world.getServer().sendMessage(Text.literal("itemstack HAS nbt"));
            liquidType = (itemStack.getNbt().getInt("liquid_type"));
            if (liquidType != 0) world.getServer().sendMessage(Text.literal("itemstack nbt isn't water!"));
        }
        world.setBlockState(pos, state.with(LIQUID_TYPE, liquidType));
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        this.spawnBreakParticles(world, player, pos, state);
        if (!world.isClient) {
            ItemStack itemStack = new ItemStack(ModBlocks.ALCHEMISTS_AMPHORA.asItem());
            int liquidType = getStateWithProperties(world.getBlockState(pos)).get(LIQUID_TYPE);
            NbtCompound nbt = new NbtCompound();
            nbt.putInt("liquid_type", liquidType);
            world.getServer().sendMessage(Text.literal("Writing NBT"));
            itemStack.setNbt(nbt);
            if (!player.getAbilities().creativeMode) ItemScatterer.spawn(world, pos.getX()+0.5f, pos.getY()+0.5f, pos.getZ()+0.5f, itemStack);
        }
        world.emitGameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Emitter.of(player, state));
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIQUID_TYPE);
    }
}
