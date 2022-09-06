package net.elindis.ruinsofarcana.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;


public class ConfusionEffect extends StatusEffect {
    protected ConfusionEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }
    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);
    }

    @Override
    public void applyUpdateEffect(LivingEntity pLivingEntity, int pAmplifier) {

        // We don't want to affect bosses. The warden is okay, though.
        if (isBossMonster(pLivingEntity)) return;

        // This status effect gathers all the nearby undead monsters, and adds them to the hitlist, and then...
        List<Entity> entityList = pLivingEntity.world.getOtherEntities(pLivingEntity, pLivingEntity.getBoundingBox().expand(10));
        List<LivingEntity> mobList = new ArrayList<>();
        for (Entity entity: entityList) {
            if (entity instanceof LivingEntity && entity instanceof Monster && !(entity instanceof PlayerEntity)) {

                // If the amplifier is > 0, the mob won't attack other confused mobs, but the mob has to be undead.
                // This is to facilitate the creation of undead armies.
                if (pAmplifier > 0 && ((LivingEntity) entity).isUndead()) {
                    if (!((LivingEntity) entity).hasStatusEffect(ModEffects.CONFUSION)) {
                        mobList.add((LivingEntity) entity);
                    }
                } else {
                    // If the amplifier is 0, then we can just attack any mob as normal.
                    mobList.add((LivingEntity) entity);
                }
            }
        }

        LivingEntity closestTarget =  pLivingEntity.world.getClosestEntity(mobList, TargetPredicate.DEFAULT, pLivingEntity, pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ());
        pLivingEntity.setAttacker(closestTarget);
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

        // If the amplifier is > 0, then non-boss undead mobs are PERMANENTLY DOMINATED.
        if (amplifier > 0 && !(entity instanceof PlayerEntity) && !isBossMonster(entity) && entity.isUndead()) {
            entity.addStatusEffect(new StatusEffectInstance(ModEffects.CONFUSION, 65791, 1,false,
                    true, false));
        }
        super.onRemoved(entity, attributes, amplifier);
    }

    private boolean isBossMonster(LivingEntity pLivingEntity) {
        return pLivingEntity instanceof WitherEntity || pLivingEntity instanceof EnderDragonEntity;
    }
}
