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
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ResearchParchmentBlock extends Block {

    // TODO: supplemental materials include:
    //  - supplemental research; (pen+paper on a pristine stone tablet)
    //  - ancient text fragment; (found in a container)
    // TODO: tier of knowledge is influenced by tier of artifact (lost, forbidden, corrupt)

    // TODO: SUCCESS RATE DECIDED BY AMOUNT OF SUPPLEMENTAL MATERIAL (UP TO 3)
    // TODO: success rate decreases when tier increases
    // TODO: tier is a chance based on number of forbidden or above artifacts

    // TODO: you can also give success rate information using the magnifying glass (and stage progress particles)
    // TODO: relics and supplementary materials should have success rate +x% in their tooltips
    // TODO: (including the forbidden artifact negative modifier). high-grade artifacts can boost success rate too.
    // TODO: this is the "editing/proofreading" step
    // TODO: additionally, atmosphere bonuses to success. Bookshelves, lecterns, candles, lanterns. Think of more. This bonus has a cap (30%?)
    // TODO: ANOTHER atmosphere bonus from a special tech. what should it be? a brain in a jar? :P

    // Scribe's pen (feather, glow ink) has 15 durability(?) - the amount required to complete 3 researches
    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final IntProperty PROGRESS = IntProperty.of("progress", 0, 5);

//    public static final BooleanProperty HAS_THEORY = BooleanProperty.of("has_transmutation");
//    public static final BooleanProperty HAS_FORMULA = BooleanProperty.of("has_formula");
//    public static final BooleanProperty HAS_SCHEMATIC = BooleanProperty.of("has_schematic");

    public boolean hasTheory = false;
    public boolean hasFormula = false;
    public boolean hasSchematic = false;
    public int successChance = 50;


    public ResearchParchmentBlock(Settings settings) {
        super(settings);
        this.setDefaultState((this.stateManager.getDefaultState())
                .with(FACING, Direction.NORTH)
//                .with(HAS_FORMULA, false).with(HAS_THEORY, false).with(HAS_SCHEMATIC, false)

                .with(PROGRESS, 1));
    }

    // TODO: Implement relic type functionality using tags.
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) return ActionResult.SUCCESS;
        if (player.getStackInHand(hand).isOf(ModItems.AURIC_INGOT)) {
            this.hasTheory = true;
        }
        // Check for the scribe's pen
        if (player.getStackInHand(hand).isOf(ModItems.ARCANISTS_STAFF)) {

            // Don't do anything on client other than play the animation
            if (world.isClient) return ActionResult.SUCCESS;

            if (state.get(PROGRESS) >= 5) {
                System.out.println("RESEARch COMPLETE :D");
                return ActionResult.PASS;
            }

            // First, compose a list of the nearby blocks.
            List<BlockPos> relicList = getBlocksNESW(pos);

            // For each nearby block, we:
            for (BlockPos relic: relicList) {

                // Get the blockstate
                BlockState relicState = world.getBlockState(relic);
                System.out.println(relicState);

                // ... and check whether the blockstate has the PRISTINE and QUALITY properties.
                if (!(relicState.contains(GenericRelicBlock.PRISTINE) && relicState.contains(GenericRelicBlock.QUALITY))) continue;
                System.out.println(relicState.getProperties().iterator());

                // Then we check whether it actually is PRISTINE.
                if (GenericRelicBlock.getPristine(relicState)) {
                    System.out.println("is pristine");

                    // And we get the relic's quality.
                    int relicQuality = GenericRelicBlock.getQuality(relicState);
                    System.out.println(relicQuality);

                    // If the quality is 0, then we check the next relic.
                    // If the quality is 1 or more, then we can progress the research.
                    if (relicQuality == 0) continue;
                    if (relicQuality > 0) {
                        // Effects are played upon success.
                        doEffects(world, pos, player);

                        // This research parchment has the new progress added.
                        // Make sure the PROGRESS never goes above 5!
                        int researchProgress = state.get(PROGRESS);
                        if (relicQuality+researchProgress > 5) {
                            world.setBlockState(pos, getStateWithProperties(state).with(PROGRESS,5));
                        } else {
                            world.setBlockState(pos, getStateWithProperties(state).with(PROGRESS,relicQuality+researchProgress));
                        }

                        // The relic is then set to pristine, and
                        GenericRelicBlock.setPristine(world, relic, relicState, false);
                        System.out.println("set pristine?");

                        // If the updated progress is sufficient, then research is complete.
                        // This is where the money methods go.
                        if (researchProgress+relicQuality >= 5) {
                            System.out.println("RESEARCH COMPLETE! ^.~");
                            // Decrements the research material and damages the scribe's pen
                            player.getStackInHand(hand).damage(1, player, p -> p.sendToolBreakStatus(player.getActiveHand()));
                        }
                    }
                    // If everything is successful, then the loop is broken. We don't need to research more than once.
                    break;
                }
            }
        }
        return ActionResult.PASS;
    }

    @NotNull
    private List<BlockPos> getBlocksNESW(BlockPos pos) {
        List<BlockPos> relicList = new ArrayList<>();
        BlockPos relicN = pos.north();
        BlockPos relicE = pos.east();
        BlockPos relicS = pos.south();
        BlockPos relicW = pos.west();
        relicList.add(relicN);
        relicList.add(relicE);
        relicList.add(relicS);
        relicList.add(relicW);
        return relicList;
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
        boolean formula = false;
        boolean schematic = false;
        boolean theory = false;
        int progress = 0;
        if (itemStack.hasNbt()) {
            assert itemStack.getNbt() != null;
            world.getServer().sendMessage(Text.literal("itemstack HAS nbt"));
            if (itemStack.getNbt().getBoolean("has_formula")) formula = true;
            if (itemStack.getNbt().getBoolean("has_schematic")) schematic = true;
            if (itemStack.getNbt().getBoolean("has_theory")) theory = true;
            progress += itemStack.getNbt().getInt("progress");
        }
//        world.setBlockState(pos, state.with(HAS_SCHEMATIC, hasSchematic).with(HAS_FORMULA, hasFormula).with(HAS_THEORY, hasTheory).with(PROGRESS, progress));
        world.setBlockState(pos, state.with(PROGRESS, progress));
        this.hasTheory = theory;
        this.hasFormula = formula;
        this.hasSchematic = schematic;

    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        this.spawnBreakParticles(world, player, pos, state);
        if (state.isIn(BlockTags.GUARDED_BY_PIGLINS)) {
            PiglinBrain.onGuardedBlockInteracted(player, false);
        }
        if (!world.isClient) {
            ItemStack itemStack = new ItemStack(ModBlocks.RESEARCH_PARCHMENT.asItem());
//            boolean hasFormula = getStateWithProperties(world.getBlockState(pos)).get(HAS_FORMULA);
//            boolean hasSchematic = getStateWithProperties(world.getBlockState(pos)).get(HAS_SCHEMATIC);
//            boolean hasTheory = getStateWithProperties(world.getBlockState(pos)).get(HAS_THEORY);
            boolean hasFormula = this.hasFormula;
            boolean hasSchematic = this.hasSchematic;
            boolean hasTheory = this.hasTheory;
            System.out.println(hasTheory+" hasTheory");
            int progress = getStateWithProperties(state).get(PROGRESS);
            NbtCompound nbt = new NbtCompound();
            nbt.putBoolean("has_formula", hasFormula);
            nbt.putBoolean("has_schematic", hasSchematic);
            nbt.putBoolean("has_theory", hasTheory);
            nbt.putInt("progress", progress);
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
        builder.add(FACING)
//                .add(HAS_THEORY).add(HAS_FORMULA).add(HAS_SCHEMATIC)
                .add(PROGRESS);
    }
}
