package net.elindis.ruinsofarcana.item.equipment;

import net.elindis.ruinsofarcana.effect.ModEffects;
import net.elindis.ruinsofarcana.sound.ModSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.Vanishable;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class WandOfCycloneItem extends WandItem implements Vanishable {
    private final int expCost = 1;
    public WandOfCycloneItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getExpCost() {
        return this.expCost;
    }

    @Override
    protected void doSpellEffect(World world, LivingEntity user, PlayerEntity playerEntity, float f) {
        float strength = 3;
        float yaw = playerEntity.getYaw();
        float pitch = playerEntity.getPitch();
        float h = -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
        float k = -MathHelper.sin(pitch * 0.017453292F);
        float l = MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
        float m = MathHelper.sqrt(h * h + k * k + l * l);
        float n = 3.0F * ((1.0F + strength) / 4.0F);
        h *= n / m;
        k *= n / m;
        l *= n / m;
        playerEntity.addVelocity((double)h, (double)k, (double)l);
        playerEntity.useRiptide(20);
        if (playerEntity.isOnGround()) {
            float o = 1.1999999F;
            playerEntity.move(MovementType.SELF, new Vec3d(0.0, 1.1999999284744263, 0.0));
        }
    }

    @Override
    protected void playFailureSound(World world, PlayerEntity playerEntity) {
        // Example
        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
                SoundEvents.BLOCK_BEACON_DEACTIVATE, SoundCategory.PLAYERS, 0.7F, 1.5F);
    }
    @Override
    protected void playSuccessSound(World world, PlayerEntity playerEntity, float f) {
        // Example
        SoundEvent soundEvent;
        soundEvent = ModSounds.CYCLONE;
        world.playSoundFromEntity((PlayerEntity)null, playerEntity, soundEvent, SoundCategory.PLAYERS, 1.0F, 1.0F);
    }
}
