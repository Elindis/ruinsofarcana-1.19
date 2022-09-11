package net.elindis.ruinsofarcana.block;

import net.elindis.ruinsofarcana.block.entity.GravityInverterBlockEntity;
import net.elindis.ruinsofarcana.block.entity.ModBlockEntities;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.listener.GameEventListener;
import org.jetbrains.annotations.Nullable;

public class GravityInverterBlock extends BlockWithEntity {
    public GravityInverterBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state){
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new GravityInverterBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (world.isClient)  {
            return checkType(type, ModBlockEntities.GRAVITY_INVERTER, (GravityInverterBlockEntity::tick));
        } else {
            return checkType(type, ModBlockEntities.GRAVITY_INVERTER, (GravityInverterBlockEntity::serverTick));
        }
    }


    @Nullable
    @Override
    public <T extends BlockEntity> GameEventListener getGameEventListener(ServerWorld world, T blockEntity) {
        return super.getGameEventListener(world, blockEntity);
    }

}
