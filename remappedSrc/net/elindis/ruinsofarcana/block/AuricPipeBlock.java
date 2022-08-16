/*
 * Decompiled with CFR 0.1.1 (FabricMC 57d88659).
 */
package net.elindis.ruinsofarcana.block;

import net.elindis.ruinsofarcana.block.entity.ModBlockEntities;
import net.elindis.ruinsofarcana.block.entity.ModHopperBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.Hopper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AuricPipeBlock
extends BlockWithEntity {

    // TODO: Allow upwards transfer
    public static final DirectionProperty FACING = Properties.FACING;
    public static final BooleanProperty ENABLED = Properties.ENABLED;

    // For graphically representing connections
    public static final BooleanProperty UP = BooleanProperty.of("up");
    public static final BooleanProperty DOWN = BooleanProperty.of("down");
    public static final BooleanProperty NORTH = BooleanProperty.of("north");
    public static final BooleanProperty SOUTH = BooleanProperty.of("south");
    public static final BooleanProperty EAST = BooleanProperty.of("east");
    public static final BooleanProperty WEST = BooleanProperty.of("west");


    private static final VoxelShape DEFAULT_SHAPE = Block.createCuboidShape(4.0, 4.0, 4.0, 12.0, 12.0, 12.0);

    private static final VoxelShape UP_SHAPE = Block.createCuboidShape(5.0, 12.0, 5.0, 11.0, 16.0, 11.0);
    private static final VoxelShape DOWN_SHAPE = Block.createCuboidShape(5.0, 0.0, 5.0, 11.0, 4.0, 11.0);
    private static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(5.0, 5.0, 0.0, 11.0, 11.0, 4.0);
    private static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(5.0, 5.0, 12.0, 11.0, 11.0, 16.0);
    private static final VoxelShape EAST_SHAPE = Block.createCuboidShape(12.0, 5.0, 5.0, 16.0, 11.0, 11.0);
    private static final VoxelShape WEST_SHAPE = Block.createCuboidShape(0.0, 5.0, 5.0, 4.0, 11.0, 11.0);

//    private static final VoxelShape DOWN_SHAPE = VoxelShapes.union(DEFAULT_SHAPE, Block.createCuboidShape(4.0, 0.0, 4.0, 12.0, 4.0, 12.0));
//    private static final VoxelShape EAST_SHAPE = VoxelShapes.union(DEFAULT_SHAPE, Block.createCuboidShape(12.0, 4.0, 4.0, 16.0, 12.0, 12.0));
//    private static final VoxelShape NORTH_SHAPE = VoxelShapes.union(DEFAULT_SHAPE, Block.createCuboidShape(6.0, 4.0, 0.0, 10.0, 8.0, 4.0));
//    private static final VoxelShape SOUTH_SHAPE = VoxelShapes.union(DEFAULT_SHAPE, Block.createCuboidShape(6.0, 4.0, 12.0, 10.0, 8.0, 16.0));
//    private static final VoxelShape WEST_SHAPE = VoxelShapes.union(DEFAULT_SHAPE, Block.createCuboidShape(0.0, 4.0, 4.0, 8.0, 12.0, 12.0));
    private static final VoxelShape DOWN_RAYCAST_SHAPE = Hopper.INSIDE_SHAPE;
    private static final VoxelShape EAST_RAYCAST_SHAPE = VoxelShapes.union(Hopper.INSIDE_SHAPE, Block.createCuboidShape(12.0, 8.0, 6.0, 16.0, 10.0, 10.0));
    private static final VoxelShape NORTH_RAYCAST_SHAPE = VoxelShapes.union(Hopper.INSIDE_SHAPE, Block.createCuboidShape(6.0, 8.0, 0.0, 10.0, 10.0, 4.0));
    private static final VoxelShape SOUTH_RAYCAST_SHAPE = VoxelShapes.union(Hopper.INSIDE_SHAPE, Block.createCuboidShape(6.0, 8.0, 12.0, 10.0, 10.0, 16.0));
    private static final VoxelShape WEST_RAYCAST_SHAPE = VoxelShapes.union(Hopper.INSIDE_SHAPE, Block.createCuboidShape(0.0, 8.0, 6.0, 4.0, 10.0, 10.0));


    public AuricPipeBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(FACING, Direction.DOWN)).with(ENABLED, true)
                .with(UP, false).with(DOWN, false).with(NORTH, false).with(SOUTH, false).with(EAST, false).with(WEST, false));

    }

    public VoxelShape getVoxelShape(BlockState state) {
        VoxelShape voxelShape = DEFAULT_SHAPE;
        if (state.get(UP)) {
            voxelShape = VoxelShapes.union(voxelShape, UP_SHAPE);
        }
        if (state.get(DOWN)) {
            voxelShape = VoxelShapes.union(voxelShape, DOWN_SHAPE);
        }
        if (state.get(NORTH)) {
            voxelShape = VoxelShapes.union(voxelShape, NORTH_SHAPE);
        }
        if (state.get(SOUTH)) {
            voxelShape = VoxelShapes.union(voxelShape, SOUTH_SHAPE);
        }
        if (state.get(EAST)) {
            voxelShape = VoxelShapes.union(voxelShape, EAST_SHAPE);
        }
        if (state.get(WEST)) {
            voxelShape = VoxelShapes.union(voxelShape, WEST_SHAPE);
        }
        return voxelShape;
    }
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
//        switch (state.get(FACING)) {
//            case DOWN: {
//                return DEFAULT_SHAPE;
//            }
//            case UP: {
//                return DEFAULT_SHAPE;
//            }
//            case NORTH: {
//                return DEFAULT_SHAPE;
//            }
//            case SOUTH: {
//                return DEFAULT_SHAPE;
//            }
//            case WEST: {
//                return DEFAULT_SHAPE;
//            }
//            case EAST: {
//                return DEFAULT_SHAPE;
//            }
//        }
        return this.getVoxelShape(state);
    }

    @Override
    public VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
//        switch (state.get(FACING)) {
//            case DOWN: {
//                return DEFAULT_SHAPE;
//            }
//            case UP: {
//                return DEFAULT_SHAPE;
//            }
//            case NORTH: {
//                return DEFAULT_SHAPE;
//            }
//            case SOUTH: {
//                return DEFAULT_SHAPE;
//            }
//            case WEST: {
//                return DEFAULT_SHAPE;
//            }
//            case EAST: {
//                return DEFAULT_SHAPE;
//            }
//        }
        return this.getVoxelShape(state);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getSide().getOpposite();
        BlockState placementState = (BlockState)((BlockState)this.getDefaultState().with(FACING, direction)).with(ENABLED, true);

//        BlockState placementState = (BlockState)((BlockState)this.getDefaultState().with(FACING, direction.getAxis() == Direction.Axis.Y ? Direction.DOWN : direction)).with(ENABLED, true);
        return placementState;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ModHopperBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : AuricPipeBlock.checkType(type, ModBlockEntities.AURIC_PIPE, ModHopperBlockEntity::serverTick);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        BlockEntity blockEntity;
        if (itemStack.hasCustomName() && (blockEntity = world.getBlockEntity(pos)) instanceof ModHopperBlockEntity) {
            ((ModHopperBlockEntity)blockEntity).setCustomName(itemStack.getName());
        }
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (oldState.isOf(state.getBlock())) {
            return;
        }
        this.updateEnabled(world, pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return ActionResult.PASS;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        this.updateEnabled(world, pos, state);
    }

    private void updateEnabled(World world, BlockPos pos, BlockState state) {
        addBits(world, pos, state);
        boolean bl;
        boolean bl2 = bl = !world.isReceivingRedstonePower(pos);
        if (bl != state.get(ENABLED)) {
            world.setBlockState(pos, (BlockState)state.with(ENABLED, bl), Block.NO_REDRAW);
        }
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.isOf(newState.getBlock())) {
            return;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ModHopperBlockEntity) {
            ItemScatterer.spawn(world, pos, (Inventory)((ModHopperBlockEntity)blockEntity));
            world.updateComparators(pos, this);
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return 15;
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState)state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, ENABLED, UP, DOWN, NORTH, SOUTH, EAST, WEST);
    }

    private void addBits(World world, BlockPos pos, BlockState state) {
        world.setBlockState(pos, state.with(NORTH, checkNorth(world, pos)).with(SOUTH, checkSouth(world, pos))
                .with(EAST, checkEast(world, pos)).with(WEST, checkWest(world, pos)).with(UP, checkUp(world, pos))
                .with(DOWN, checkDown(world, pos)));
    }

    private boolean checkNorth(World world, BlockPos pos) {
        return  (world.getBlockState(pos.add(0,0,-1)).hasBlockEntity());
    }
    private boolean checkSouth(World world, BlockPos pos) {
        return  (world.getBlockState(pos.add(0,0,1)).hasBlockEntity());
    }
    private boolean checkEast(World world, BlockPos pos) {
        return  (world.getBlockState(pos.add(1,0,0)).hasBlockEntity());
    }
    private boolean checkWest(World world, BlockPos pos) {
        return  (world.getBlockState(pos.add(-1,0,0)).hasBlockEntity());
    }
    private boolean checkUp(World world, BlockPos pos) {
        return  (world.getBlockState(pos.add(0,1,0)).hasBlockEntity());
    }
    private boolean checkDown(World world, BlockPos pos) {
        return  (world.getBlockState(pos.add(0,-1,0)).hasBlockEntity());
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
//        BlockEntity blockEntity = world.getBlockEntity(pos);
//        if (blockEntity instanceof ModHopperBlockEntity) {
//            ModHopperBlockEntity.onEntityCollided(world, pos, state, entity, (ModHopperBlockEntity)blockEntity);
//        }
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }
}

