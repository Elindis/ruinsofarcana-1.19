package net.elindis.ruinsofarcana.potion;

import net.elindis.ruinsofarcana.RuinsOfArcana;
import net.elindis.ruinsofarcana.effect.ModEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.potion.Potion;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class ModPotions {
    public static Potion EXPLODE_POTION;
    public static Potion FROST_POTION;
    public static Potion PURITY_POTION;

//    public static Potion registerPotionExplode(String name) {
//        return Registry.register(Registry.POTION, new Identifier(RuinsOfAkhTal.MOD_ID, name),
//                new Potion(new StatusEffectInstance(ModEffects.EXPLODE)));
//    }
    public static Potion registerPotionFrost(String name) {
        return Registry.register(Registry.POTION, new Identifier(RuinsOfArcana.MOD_ID, name),
                new Potion(
                        new StatusEffectInstance(StatusEffects.SLOWNESS, 400, 3),
                        new StatusEffectInstance(StatusEffects.WITHER, 400, 1)));
    }
    public static Potion registerPotionPurity(String name) {
        return Registry.register(Registry.POTION, new Identifier(RuinsOfArcana.MOD_ID, name),
                new Potion(
                        new StatusEffectInstance(ModEffects.PURITY, 400)));
    }

    public static void registerPotions() {

//        EXPLODE_POTION = registerPotionExplode("explode_potion");
        FROST_POTION = registerPotionFrost("frost_potion");
        PURITY_POTION = registerPotionPurity("purity_potion");
    }
}
