package net.elindis.ruinsofarcana.mixin;

import net.elindis.ruinsofarcana.item.ModItems;
import net.elindis.ruinsofarcana.world.dimension.ModDimensions;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow public abstract boolean removeStatusEffect(StatusEffect type);

    // Used for the Circlet of Rebirth to act like a totem.
    @Inject(at = @At("HEAD"), method = "tryUseTotem", cancellable = true)
    private void useCirclet(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity thisEntity = ((LivingEntity)(Object)this);
        ItemStack headSlot = thisEntity.getEquippedStack(EquipmentSlot.HEAD);
        if (headSlot.isOf(ModItems.CIRCLET_OF_REBIRTH)) {

            headSlot.damage(35, thisEntity.getRandom(), (ServerPlayerEntity) thisEntity);
            if (headSlot.getDamage() >= headSlot.getMaxDamage()) {
                headSlot.decrement(1);
            }

            thisEntity.setHealth(10.0f);
            thisEntity.clearStatusEffects();
            thisEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
            thisEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
            thisEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0));
            thisEntity.world.playSound(null, thisEntity.getX(), thisEntity.getY(), thisEntity.getZ(),
                    SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS,1, 1);
            for (int i = 0; i < 80; i++) {
                double xPos = thisEntity.getX() + (Random.create().nextFloat());
                double yPos = thisEntity.getY() + (Random.create().nextFloat()/2);
                double zPos = thisEntity.getZ() + (Random.create().nextFloat());
                double xDelta = Random.create().nextFloat()/4;
                double yDelta = Random.create().nextFloat()/4;
                double zDelta = Random.create().nextFloat()/4;
//                thisEntity.world.addParticle(ParticleTypes.GLOW, xPos, yPos, zPos, xDelta, yDelta, zDelta);
                ((ServerWorld) thisEntity.getWorld()).spawnParticles((ServerPlayerEntity) thisEntity, (ParticleEffect) ParticleTypes.GLOW,
                        true, xPos, yPos+1, zPos,1, xDelta, yDelta, zDelta, 0.2);

            }
            cir.setReturnValue(true);
        }
    }

    @Inject(at = @At("HEAD"), method = "canTarget(Lnet/minecraft/entity/LivingEntity;)Z", cancellable = true)
    private void pacifyMob(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity thisEntity = ((LivingEntity)(Object)this);
        if (thisEntity.hasStatusEffect(StatusEffects.BLINDNESS)) {
            cir.setReturnValue(false);
        }
    }
    @Inject(at = @At("HEAD"), method = "baseTick")
    private void dimensionAirControl(CallbackInfo ci) {
        LivingEntity thisEntity = ((LivingEntity)(Object)this);
        if (thisEntity.hasStatusEffect(StatusEffects.WATER_BREATHING) || thisEntity.hasStatusEffect(StatusEffects.CONDUIT_POWER)
        || thisEntity.isInvulnerable()) return;
        if (thisEntity instanceof PlayerEntity playerEntity) {
            if (playerEntity.getAbilities().creativeMode) return;
        }
        if (thisEntity.getWorld().getRegistryKey().equals(ModDimensions.SANCTUARY)) {
            thisEntity.setAir(thisEntity.getAir()-1);
            if (thisEntity.getAir() <= -20) {
                thisEntity.setAir(0);
//                Vec3d vec3d = thisEntity.getVelocity();
//                for (int i = 0; i < 8; ++i) {
//                    double f = thisEntity.getRandom().nextDouble() - thisEntity.getRandom().nextDouble();
//                    double g = thisEntity.getRandom().nextDouble() - thisEntity.getRandom().nextDouble();
//                    double h = thisEntity.getRandom().nextDouble() - thisEntity.getRandom().nextDouble();
//                    thisEntity.world.addParticle(ParticleTypes.BUBBLE, thisEntity.getX() + f, thisEntity.getY() + g,
//                            thisEntity.getZ() + h, vec3d.x, vec3d.y, vec3d.z);
//                }
                thisEntity.damage(DamageSource.DROWN, 2.0f);
            }
        }
    }
    @Inject(at = @At("HEAD"), method = "getNextAirOnLand", cancellable = true)
    private void dimensionAirControl(int air, CallbackInfoReturnable<Integer> cir) {
        LivingEntity thisEntity = ((LivingEntity)(Object)this);
        if (thisEntity.hasStatusEffect(StatusEffects.WATER_BREATHING) || thisEntity.hasStatusEffect(StatusEffects.CONDUIT_POWER)) {
            return;
        }
        if (thisEntity instanceof PlayerEntity playerEntity) {
            if (playerEntity.getAbilities().creativeMode) playerEntity.setAir(playerEntity.getMaxAir());
        }
        if (thisEntity.getWorld().getRegistryKey().equals(ModDimensions.SANCTUARY)) {
            cir.setReturnValue(thisEntity.getAir());
        }
    }
}
