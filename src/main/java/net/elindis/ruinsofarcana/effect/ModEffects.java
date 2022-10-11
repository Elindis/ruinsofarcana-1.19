package net.elindis.ruinsofarcana.effect;

import net.elindis.ruinsofarcana.RuinsOfArcana;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class ModEffects {
    public static StatusEffect SHIELDING;
    public static StatusEffect SPEED;
    public static StatusEffect PURITY;
    public static StatusEffect NIGHTVISION;
    public static StatusEffect REPEL;
    public static StatusEffect FIRESTORM;
    public static StatusEffect SNOWSTORM;
    public static StatusEffect CONFUSION;

    public static StatusEffect registerStatusEffectShielding(String name) {
        return Registry.register(Registry.STATUS_EFFECT, new Identifier(RuinsOfArcana.MOD_ID, name),
                new ShieldingEffect(StatusEffectCategory.BENEFICIAL, 00000000));
    }

    public static StatusEffect registerStatusEffectConfusion(String name) {
        return Registry.register(Registry.STATUS_EFFECT, new Identifier(RuinsOfArcana.MOD_ID, name),
                new ConfusionEffect(StatusEffectCategory.HARMFUL, 4277574));
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

    public static StatusEffect registerStatusEffectFirestorm(String name) {
        return Registry.register(Registry.STATUS_EFFECT, new Identifier(RuinsOfArcana.MOD_ID, name),
                new FirestormEffect(StatusEffectCategory.HARMFUL, 00000000));
    }
    public static StatusEffect registerStatusEffectSnowstorm(String name) {
        return Registry.register(Registry.STATUS_EFFECT, new Identifier(RuinsOfArcana.MOD_ID, name),
                new SnowstormEffect(StatusEffectCategory.HARMFUL, 00000000));
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

        SHIELDING = registerStatusEffectShielding("shielding");
        CONFUSION = registerStatusEffectConfusion("confusion");
        SPEED = registerStatusEffectSpeed("speed");
        PURITY = registerStatusEffectPurity("purity");
        NIGHTVISION = registerStatusEffectNightvision("nightvision");
        REPEL = registerStatusEffectRepel("repel");
        FIRESTORM = registerStatusEffectFirestorm("firestorm");
        SNOWSTORM = registerStatusEffectSnowstorm("snowstorm");
    }

}