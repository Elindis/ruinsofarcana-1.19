package net.elindis.ruinsofarcana.effect;

import net.elindis.ruinsofarcana.block.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;


public class FrozenEffect extends StatusEffect {
    protected FrozenEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }
    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);
        if (entity instanceof PathAwareEntity) {
            ((PathAwareEntity) entity).setAiDisabled(true);
            entity.setSilent(true);
        }
    }

    @Override
    public void applyUpdateEffect(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.world.getBlockState(pLivingEntity.getBlockPos()).isOf(ModBlocks.THIN_ICE)) {
            pLivingEntity.removeStatusEffect(ModEffects.FROZEN);
        }
    }



    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int i;
        i = 50 >> amplifier;
        if (i > 0) {
            return duration % i == 0;
        } else {
            return true;
        }
    }
    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);
        if (entity instanceof PathAwareEntity) {
            ((PathAwareEntity) entity).setAiDisabled(false);
            entity.setSilent(false);
            BlockPos.stream(entity.getBoundingBox()).forEach(blockPos -> {
                if (entity.world.getBlockState(blockPos).isOf(ModBlocks.THIN_ICE)) {
                    entity.world.breakBlock(blockPos, false);
                }
            });
        }
    }
}
