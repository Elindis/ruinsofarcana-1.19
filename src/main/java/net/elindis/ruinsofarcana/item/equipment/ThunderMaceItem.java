package net.elindis.ruinsofarcana.item.equipment;

import net.elindis.ruinsofarcana.sound.ModSounds;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class ThunderMaceItem extends SwordItem {

    // TODO: EnderDragonEntity launchLivingentities(); -> adapt to thunder mace launch code

    public ThunderMaceItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // Applies additional knockback when enchanted. Necessary because we're using a general knockback method.
        // Also applies additional sweeping damage, because we're using a general sweep method.
        float knockbackLevel = EnchantmentHelper.getLevel(Enchantments.KNOCKBACK, stack);
        int sweepingLevel = EnchantmentHelper.getLevel(Enchantments.SWEEPING, stack);

        stack.damage(1, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        dealKnockback(1.5f + knockbackLevel/2,
                MathHelper.sin(attacker.getYaw() * ((float)Math.PI / 180)),
                -MathHelper.cos(attacker.getYaw() * ((float)Math.PI / 180)),
                target);
        doSweep((PlayerEntity) attacker, target, sweepingLevel);
        return true;
    }

    // This mace deals enhanced knockback. It's equivalent to a sword with Knockback III, with extra vertical knockback.
    // Unresolved bug: Knockback doesn't work against other players. Idk how to fix this.

    public void dealKnockback(double strength, double x, double z, LivingEntity target) {
        if ((strength *= 1.0 - target.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)) <= 0.0) {
            return;
        }
        target.velocityDirty = true;
        Vec3d vec3d = target.getVelocity();
        Vec3d vec3d2 = new Vec3d(x, 0.0, z).normalize().multiply(strength);

        target.setVelocity(vec3d.x / 2.0 - vec3d2.x, target.isOnGround() ? Math.min(0.4, vec3d.y / 2.0 + strength) : vec3d.y, vec3d.z / 2.0 - vec3d2.z);
        target.addVelocity(0, 0.5, 0);
    }

    // This sweep is an enhanced version of the sword's sweep. It has a double-size hitbox, and the targets also
    // take the enhanced knockback inherent to the mace.
    // Also enhanced the range. The squared distance is 16 instead of 9.

    public void doSweep(PlayerEntity playerEntity, LivingEntity target, int level) {
        List<LivingEntity> list = playerEntity.world.getNonSpectatingEntities(LivingEntity.class, target.getBoundingBox().expand(2, 0.5, 2));
        for (LivingEntity livingEntity : list) {
            if (livingEntity == playerEntity || livingEntity == target || playerEntity.isTeammate(livingEntity) || livingEntity instanceof ArmorStandEntity && ((ArmorStandEntity) livingEntity).isMarker() || !(playerEntity.squaredDistanceTo(livingEntity) < 16.0))
                continue;
            dealKnockback(2f,
                    MathHelper.sin(playerEntity.getYaw() * ((float) Math.PI / 180)),
                    -MathHelper.cos(playerEntity.getYaw() * ((float) Math.PI / 180)),
                    livingEntity);
            livingEntity.damage(DamageSource.player(playerEntity), 4+level);
        }
        playThunderMaceSound(playerEntity);
        playerEntity.spawnSweepAttackParticles();

    }

    private void playThunderMaceSound(PlayerEntity playerEntity) {
        int waterModifier = 1;
        if (playerEntity.isSubmergedInWater()) {
            waterModifier *= 2;
        }
        playerEntity.world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
                ModSounds.THUNDER_MACE, playerEntity.getSoundCategory(), 0.6f,
                (playerEntity.getRandom().nextFloat()) + 0.5f / 2*waterModifier);
        playerEntity.world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
                SoundEvents.ENTITY_PLAYER_BIG_FALL, playerEntity.getSoundCategory(), 1.6f,
                 0.6f / ((playerEntity.getRandom().nextFloat() * 0.8f + 0.6f) * 0.5f)/waterModifier);
    }
}


