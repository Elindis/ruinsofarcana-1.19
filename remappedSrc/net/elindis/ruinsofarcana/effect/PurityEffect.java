package net.elindis.ruinsofarcana.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;


public class PurityEffect extends StatusEffect {
    protected PurityEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.world.isClient()) {
            for (StatusEffect status:
                 pLivingEntity.getActiveStatusEffects().keySet()) {
                if (!status.equals(ModEffects.PURITY)) {
                    pLivingEntity.removeStatusEffect(status);
                }

            }
        }
        super.applyUpdateEffect(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

}
