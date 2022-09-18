package net.elindis.ruinsofarcana.item.equipment;

import net.elindis.ruinsofarcana.effect.ModEffects;
import net.elindis.ruinsofarcana.entity.FrostBoltEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.Vanishable;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class WandOfWhirlwindItem extends WandItem implements Vanishable {
    private final int expCost = 5;
    public WandOfWhirlwindItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getExpCost() {
        return this.expCost;
    }
    @Override
    protected ParticleEffect getParticleType() {
        return ParticleTypes.SMOKE;
    }

    @Override
    protected void doSpellEffect(World world, LivingEntity user, PlayerEntity playerEntity, float f) {
        // Example
        if (world.isClient) return;
        playerEntity.addStatusEffect(new StatusEffectInstance(ModEffects.REPEL,
                600, 0, false, false, false));
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
        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
                SoundEvents.BLOCK_NOTE_BLOCK_CHIME, SoundCategory.PLAYERS, 0.8F, 1F /
                        (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
                SoundEvents.BLOCK_SMALL_AMETHYST_BUD_BREAK, SoundCategory.PLAYERS, 0.8F, 1F /
                        (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
    }
}
