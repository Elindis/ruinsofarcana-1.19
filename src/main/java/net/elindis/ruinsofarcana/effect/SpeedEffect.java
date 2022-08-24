package net.elindis.ruinsofarcana.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;


public class SpeedEffect extends StatusEffect {
    protected SpeedEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity pLivingEntity, int pAmplifier) {
        // Not necessary. This it taken care of in registration.
//        if (!pLivingEntity.world.isClient()) {
//            float newSpeed = (float)((float)pLivingEntity.getAttributeBaseValue(EntityAttributes.GENERIC_MOVEMENT_SPEED)*2d);
//            pLivingEntity.setMovementSpeed(newSpeed);
//        }
        super.applyUpdateEffect(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

}
