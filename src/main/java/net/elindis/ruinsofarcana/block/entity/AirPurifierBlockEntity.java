package net.elindis.ruinsofarcana.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
public class AirPurifierBlockEntity extends BlockEntity  {

    private final Box areaEffect;

    public AirPurifierBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.AIR_PURIFIER, pos, state);
        areaEffect = new Box(pos).expand(20);
    }
    public static void tick(World world, BlockPos pos, BlockState state, AirPurifierBlockEntity blockEntity) {
    }

    public static void serverTick(@NotNull World world, BlockPos pos, BlockState state, AirPurifierBlockEntity blockEntity) {
        // Should restore 2 oxygen once per second
        if (world.getRandom().nextFloat() > 0.95f) {
            List<Entity> playerEntityList = world.getOtherEntities(null, blockEntity.areaEffect);
            for (Entity entity : playerEntityList) {
                if (entity instanceof LivingEntity livingEntity) {
                    if (livingEntity.getBlockPos().isWithinDistance(pos, 20)) {
                        livingEntity.setAir(livingEntity.getAir()+40);
                    }
                }
            }

        }


    }
}
