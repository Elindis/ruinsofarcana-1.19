package net.elindis.ruinsofarcana.mixin;

import net.elindis.ruinsofarcana.item.ModItems;
import net.minecraft.block.PowderSnowBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(PowderSnowBlock.class)
public abstract class PowderSnowBlockMixin {

    // This allows the Moon Lance to allow the user to walk on powder snow.
    @Inject(at = @At("HEAD"), method = "canWalkOnPowderSnow", cancellable = true)
    private static void walkOnSnow(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (!(entity instanceof PlayerEntity)) return;
        if (entity.getHandItems() == null) return;
        List<ItemStack> handEquipped = new ArrayList<>();
        entity.getHandItems().iterator().forEachRemaining(handEquipped::add);
        if (handEquipped.get(0).isEmpty() || handEquipped.get(1).isEmpty()) return;
        if (handEquipped.get(0).isOf(ModItems.FROZEN_LANCE) || handEquipped.get(1).isOf(ModItems.FROZEN_LANCE)) {
            System.out.println("FROZEN LANCE IS WORKING");
            cir.setReturnValue(true);
        }
    }
}

