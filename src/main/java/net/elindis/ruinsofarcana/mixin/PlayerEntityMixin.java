package net.elindis.ruinsofarcana.mixin;

import net.elindis.ruinsofarcana.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {


    // This is so enemies will drop cooked meat when attacked by the sun sword.
    @Inject(at = @At("HEAD"), method = "attack")
    private void sunSwordFire(Entity target, CallbackInfo ci) {
        PlayerEntity self = ((PlayerEntity)(Object)this);
        if (self.getMainHandStack().isOf(ModItems.SOLAR_SWORD)) {
            if (!target.isAttackable()) {
                return;
            }
            if (target.handleAttack(self)) {
                return;
            }
            target.setOnFireFor(5);
        }
    }
}
