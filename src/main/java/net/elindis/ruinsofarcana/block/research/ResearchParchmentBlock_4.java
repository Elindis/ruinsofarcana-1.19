package net.elindis.ruinsofarcana.block.research;

import net.elindis.ruinsofarcana.item.ModItems;
import net.elindis.ruinsofarcana.sound.ModSounds;
import net.elindis.ruinsofarcana.util.ModParticleUtil;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
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

public class ResearchParchmentBlock_4 extends Block {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);

    public static final BooleanProperty HAS_THEORY = BooleanProperty.of("has_transmutation");
    public static final BooleanProperty HAS_RECIPE = BooleanProperty.of("has_recipe");
    public static final BooleanProperty HAS_SCHEMATIC = BooleanProperty.of("has_schematic");

    public ResearchParchmentBlock_4(Settings settings) {
        super(settings);
        this.setDefaultState((this.stateManager.getDefaultState())
                .with(FACING, Direction.NORTH)
                .with(HAS_RECIPE, false).with(HAS_THEORY, false).with(HAS_SCHEMATIC, false));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        // Don't do anything on client other than play the animation
        if (world.isClient) return ActionResult.SUCCESS;

        // Check for the scribe's pen and a research material
        if (player.getMainHandStack().isOf(ModItems.AURIC_INGOT) && player.getOffHandStack().isOf(ModItems.ARCANISTS_STAFF)) {

            // Debug
            world.getServer().sendMessage(Text.literal("Used research parchment block (left-handed)"));

            // Hand-specific
            player.getOffHandStack().damage(1, player, p -> p.sendToolBreakStatus(player.getActiveHand()));
            player.getMainHandStack().decrement(1);

            // Important
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            ItemScatterer.spawn(world, pos.getX()+0.5f, pos.getY()+0.5f, pos.getZ()+0.5f, ModItems.ANCIENT_NOTE_GENERIC.getDefaultStack());

            // Effects
            doEffects(world, pos, player);
        }

        // Check for the scribe's pen and a research material
        if (player.getMainHandStack().isOf(ModItems.ARCANISTS_STAFF) && player.getOffHandStack().isOf(ModItems.AURIC_INGOT)) {

            // Debug
            world.getServer().sendMessage(Text.literal("Used research parchment block (right-handed)"));

            // Hand-specific
            player.getMainHandStack().damage(1, player, p -> p.sendToolBreakStatus(player.getActiveHand()));
            player.getOffHandStack().decrement(1);

            // Important
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            ItemScatterer.spawn(world, pos.getX()+0.5f, pos.getY()+0.5f, pos.getZ()+0.5f, ModItems.ANCIENT_NOTE_GENERIC.getDefaultStack());

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

    // onplaced and onmined: play paper sounds from librarian
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return !world.isAir(pos.down());
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
        builder.add(FACING).add(HAS_THEORY).add(HAS_RECIPE).add(HAS_SCHEMATIC);
    }
}
