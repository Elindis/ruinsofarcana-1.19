package net.elindis.ruinsofarcana.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.FoodComponents;

public class ModFoodComponents extends FoodComponents {

    // Food
    public static final FoodComponent RAW_RICE = (new FoodComponent.Builder()).hunger(1).saturationModifier(0.3F)
            .statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 600, 0), 0.3F).snack().build();

    public static final FoodComponent COOKED_RICE = (new FoodComponent.Builder()).hunger(4).saturationModifier(0.6F).build();

    public static final FoodComponent RICE_BALL = (new FoodComponent.Builder()).hunger(6).saturationModifier(0.6F).build();

    public static final FoodComponent FISH_ROLL = (new FoodComponent.Builder()).hunger(8).saturationModifier(0.8F).build();

    // Vials
    public static final FoodComponent SPEED = (new FoodComponent.Builder())
            .statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 1200, 0), 1F).snack().alwaysEdible().build();
    public static final FoodComponent REGENERATION = (new FoodComponent.Builder())
            .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 1200, 0), 1F).snack().alwaysEdible().build();


}
