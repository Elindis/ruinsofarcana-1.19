package net.elindis.ruinsofarcana.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.math.MathHelper;


public class ShieldingEffect extends StatusEffect {
    protected ShieldingEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.world.isClient() && pLivingEntity.getAbsorptionAmount() < 20f) {
            pLivingEntity.setAbsorptionAmount(pLivingEntity.getAbsorptionAmount()+1);
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
        entity.setAbsorptionAmount(MathHelper.clamp(entity.getAbsorptionAmount()-20, 0, 100));
        super.onRemoved(entity, attributes, amplifier);
    }
}
