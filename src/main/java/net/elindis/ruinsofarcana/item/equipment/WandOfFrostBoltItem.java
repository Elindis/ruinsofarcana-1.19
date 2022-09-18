package net.elindis.ruinsofarcana.item.equipment;

import net.elindis.ruinsofarcana.entity.FrostBoltEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Vanishable;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class WandOfFrostBoltItem extends WandItem implements Vanishable {
    private final int expCost = 3;
    public WandOfFrostBoltItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getExpCost() {
        return this.expCost;
    }
    @Override
    protected ParticleEffect getParticleType() {
        return ParticleTypes.SNOWFLAKE;
    }

    @Override
    protected void doSpellEffect(World world, LivingEntity user, PlayerEntity playerEntity, float f) {
        // Example
        if (world.isClient) return;
        FrostBoltEntity frostBoltEntity = new FrostBoltEntity(world, user);
        frostBoltEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(),
                0.0F, f * 2.5F, 0.5F);
        world.spawnEntity(frostBoltEntity);
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
