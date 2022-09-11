package net.elindis.ruinsofarcana.mixin;

import net.elindis.ruinsofarcana.item.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class EntityMixin {

    // This allows the Moon Lance to prevent the player from freezing when equipped.
    @Inject(at = @At("HEAD"), method = "canFreeze", cancellable = true)
    public void canFreeze(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = ((LivingEntity)(Object)this);
        if (self.getStackInHand(Hand.MAIN_HAND).isOf(ModItems.FROZEN_LANCE)
                || self.getStackInHand(Hand.OFF_HAND).isOf(ModItems.FROZEN_LANCE)) {
            cir.setReturnValue(false);
        }
}}
