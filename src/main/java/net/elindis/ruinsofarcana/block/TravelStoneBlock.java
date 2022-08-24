package net.elindis.ruinsofarcana.block;

import net.elindis.ruinsofarcana.effect.ModEffects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TravelStoneBlock extends Block {
    public TravelStoneBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (entity instanceof LivingEntity && !world.isClient) {
            ((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(ModEffects.SPEED,
                    40, 0, false, false, false));
        }
    }
}
