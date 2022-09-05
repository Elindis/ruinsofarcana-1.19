package net.elindis.ruinsofarcana.effect;

import net.elindis.ruinsofarcana.RuinsOfArcana;
import net.elindis.ruinsofarcana.sound.ModSounds;
import net.elindis.ruinsofarcana.sound.WhirlwindSound;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class ModEffects {
    public static StatusEffect EXPLODE;
    public static StatusEffect SPEED;
    public static StatusEffect PURITY;
    public static StatusEffect NIGHTVISION;
    public static StatusEffect REPEL;

    public static StatusEffect registerStatusEffectExplode(String name) {
        return Registry.register(Registry.STATUS_EFFECT, new Identifier(RuinsOfArcana.MOD_ID, name),
                new ExplosionEffect(StatusEffectCategory.HARMFUL, 00000000));
    }
    public static StatusEffect registerStatusEffectRepel(String name) {
        return Registry.register(Registry.STATUS_EFFECT, new Identifier(RuinsOfArcana.MOD_ID, name),
                new RepelEffect(StatusEffectCategory.BENEFICIAL, 00000000));
    }

    public static StatusEffect registerStatusEffectNightvision(String name) {
        return Registry.register(Registry.STATUS_EFFECT, new Identifier(RuinsOfArcana.MOD_ID, name),
                new NightvisionEffect(StatusEffectCategory.BENEFICIAL, 00000000));
    }
    public static StatusEffect registerStatusEffectPurity(String name) {
        return Registry.register(Registry.STATUS_EFFECT, new Identifier(RuinsOfArcana.MOD_ID, name),
                new PurityEffect(StatusEffectCategory.BENEFICIAL, 16777205));
    }

    public static StatusEffect registerStatusEffectSpeed(String name) {
        return Registry.register(Registry.STATUS_EFFECT, new Identifier(RuinsOfArcana.MOD_ID, name),
                // Color of 00000000 makes the particles invisible.
                new SpeedEffect(StatusEffectCategory.BENEFICIAL, 00000000)
                        .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED,
                                // Gives the same amount of speed as sprinting does.
                                // A UUID allows this effect to *stack* with vanilla Speed effects. Zoom!
                                "3b17f2c0-0416-11ed-b939-0242ac120002", 0.3,
                                EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
    }
    public static void registerEffects() {

        EXPLODE = registerStatusEffectExplode("explode");
        SPEED = registerStatusEffectSpeed("speed");
        PURITY = registerStatusEffectPurity("purity");
        NIGHTVISION = registerStatusEffectNightvision("nightvision");
        REPEL = registerStatusEffectRepel("repel");
    }

}