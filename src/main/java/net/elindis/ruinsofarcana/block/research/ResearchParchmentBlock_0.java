package net.elindis.ruinsofarcana.block.research;

import net.elindis.ruinsofarcana.block.ModBlocks;
import net.elindis.ruinsofarcana.item.ModItems;
import net.elindis.ruinsofarcana.sound.ModSounds;
import net.elindis.ruinsofarcana.util.ModParticleUtil;
import net.elindis.ruinsofarcana.util.ModTags;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class ResearchParchmentBlock_0 extends Block {

    // DONE: make placeable in-world as a block; it acts as a research substrate
    // TODO: scribe's pen+research adds the first progress to the research
    // DONE: progress the research 4 more times to complete it
    // DONE: progress is indicated by the amount of writing texture on the parchment
    // TODO: progress items include:
    //  - charcoal rubbing; (charcoal+paper on a pristine stone tablet)
    //  - ancient text fragment; (found in a container)
    //  - pottery transcriptions; (acquired in the same manner as research)
    //  - other artifact researches; (magnifying glass+book&quill on pristine artifact)
    // TODO: tier of knowledge is decided by tier of artifact (lost, forbidden, corrupt)

    // Scribe's pen (feather, glow ink) has 15 durability(?) - the amount required to complete 3 researches
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
    public static final BooleanProperty HAS_THEORY = BooleanProperty.of("has_transmutation");
    public static final BooleanProperty HAS_FORMULA = BooleanProperty.of("has_formula");
    public static final BooleanProperty HAS_SCHEMATIC = BooleanProperty.of("has_schematic");
    // TODO: Add IntProperty RESEARCH_PROGRESS and simplify to one blockstate!

    public ResearchParchmentBlock_0(Settings settings) {
        super(settings);
        this.setDefaultState((this.stateManager.getDefaultState())
                .with(FACING, Direction.NORTH)
                .with(HAS_FORMULA, false).with(HAS_THEORY, false).with(HAS_SCHEMATIC, false));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        // We don't want the method to fire if the scribe's pen is on cooldown
        if (player.getItemCooldownManager().isCoolingDown(player.getOffHandStack().getItem()) ||
                player.getItemCooldownManager().isCoolingDown(player.getOffHandStack().getItem())) {
            return  ActionResult.PASS;
        }

        // Don't do anything on client other than play the animation
        if (world.isClient) return ActionResult.SUCCESS;

        // Check for the scribe's pen and a research material
        if (player.getMainHandStack().isOf(ModItems.AURIC_INGOT) && player.getOffHandStack().isOf(ModItems.SCRIBES_PEN)) {


            // Debug
            world.getServer().sendMessage(Text.literal("Used research parchment block (left-handed)"));

            // Decrements the research material and damages the scribe's pen
            player.getOffHandStack().damage(1, player, p -> p.sendToolBreakStatus(player.getActiveHand()));
            player.getMainHandStack().decrement(1);

            // Advances to the next research stage

            // TODO: When progressing research with relic research, make sure progress includes the relevant property
            // TODO: For this item, we ONLY progress with a Relic Research item
            world.getServer().sendMessage(Text.literal("Set HAS_THEORY to true"));
            world.setBlockState(pos, ModBlocks.RESEARCH_PARCHMENT_0.getDefaultState().with(FACING, world.getBlockState(pos).get(FACING)).with(HAS_THEORY, true));

            // Effects
            doEffects(world, pos, player);
        }

        // Check for the scribe's pen and a research material
        if (player.getMainHandStack().isOf(ModItems.SCRIBES_PEN) && player.getOffHandStack().isOf(ModItems.AURIC_INGOT)) {

            // Debug
            world.getServer().sendMessage(Text.literal("Used research parchment block (right-handed)"));

            // Decrements the research material and damages the scribe's pen
            player.getMainHandStack().damage(1, player, p -> p.sendToolBreakStatus(player.getActiveHand()));
            player.getOffHandStack().decrement(1);

            // Advances to the next research stage
            world.setBlockState(pos, ModBlocks.RESEARCH_PARCHMENT_1.getDefaultState().with(FACING, world.getBlockState(pos).get(FACING)));

            // Effects
            doEffects(world, pos, player);
        }
        return ActionResult.PASS;
    }

    private void doEffects(World world, BlockPos pos, PlayerEntity player) {
        world.playSound(null, pos, ModSounds.RESEARCH_PROGRESS, SoundCategory.NEUTRAL, 1,
                0.8f + (player.getRandom().nextFloat())/3);
        BlockPos particlePos = new BlockPos(pos.getX()+0.5f, pos.getY()+0.5f, pos.getZ()+0.5f);
        ModParticleUtil.doBlockParticles(world, particlePos, player, ParticleTypes.ENCHANT, 8, 0.01f);
        ModParticleUtil.doBlockParticles(world, particlePos, player, ParticleTypes.GLOW, 3, 0.01f);
    }
    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (world.isClient) return;
        boolean hasFormula = false;
        boolean hasSchematic = false;
        boolean hasTheory = false;
        if (itemStack.hasNbt()) {
            assert itemStack.getNbt() != null;
            world.getServer().sendMessage(Text.literal("itemstack HAS nbt"));
            if (itemStack.getNbt().getBoolean("has_formula")) hasFormula = true;
            if (itemStack.getNbt().getBoolean("has_schematic")) hasSchematic = true;
            if (itemStack.getNbt().getBoolean("has_theory")) hasTheory = true; world.getServer().sendMessage(Text.literal("HAS_THEORY is true. congrats"));
        }
        world.setBlockState(pos, state.with(HAS_SCHEMATIC, hasSchematic).with(HAS_FORMULA, hasFormula).with(HAS_THEORY, hasTheory));
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        this.spawnBreakParticles(world, player, pos, state);
        if (state.isIn(BlockTags.GUARDED_BY_PIGLINS)) {
            PiglinBrain.onGuardedBlockInteracted(player, false);
        }
        if (!world.isClient) {
            ItemStack itemStack = new ItemStack(ModBlocks.RESEARCH_PARCHMENT_0.asItem());
            boolean hasFormula = getStateWithProperties(world.getBlockState(pos)).get(HAS_FORMULA);
            boolean hasSchematic = getStateWithProperties(world.getBlockState(pos)).get(HAS_SCHEMATIC);
            boolean hasTheory = getStateWithProperties(world.getBlockState(pos)).get(HAS_THEORY);
            NbtCompound nbt = new NbtCompound();
            nbt.putBoolean("has_formula", hasFormula);
            nbt.putBoolean("has_schematic", hasSchematic);
            nbt.putBoolean("has_theory", hasTheory);
            world.getServer().sendMessage(Text.literal("Writing NBT"));
            itemStack.setNbt(nbt);
            ItemScatterer.spawn(world, pos.getX()+0.5f, pos.getY()+0.5f, pos.getZ()+0.5f, itemStack);
        }

        world.emitGameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Emitter.of(player, state));
    }

    // onplaced and onmined: play paper sounds from librarian
    // get properties from nbt
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getPlayerFacing();
        return this.getDefaultState().with(FACING, direction);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return !world.isAir(pos.down()) && !world.getBlockState(pos.down()).isIn(ModTags.Blocks.RESEARCH_PARCHMENTS);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(HAS_THEORY).add(HAS_FORMULA).add(HAS_SCHEMATIC);
    }
}
