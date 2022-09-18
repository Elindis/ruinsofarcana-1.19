package net.elindis.ruinsofarcana.item.equipment;

import net.elindis.ruinsofarcana.entity.FireballEntity;
import net.elindis.ruinsofarcana.entity.FrostBoltEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class WandOfFireballItem extends WandItem {
    private final int expCost = 10;
    public WandOfFireballItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getExpCost() {
        return this.expCost;
    }
    @Override
    protected ParticleEffect getParticleType() {
        return ParticleTypes.LAVA;
    }

    @Override
    protected void doSpellEffect(World world, LivingEntity user, PlayerEntity playerEntity, float f) {
        // Example
        if (world.isClient) return;
        FireballEntity fireballEntity = new FireballEntity(world, user, 3f);
        fireballEntity.setItem(Items.FIRE_CHARGE.getDefaultStack());
        fireballEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(),
                0.0F, f * 2.5F, 0.5F);
        world.spawnEntity(fireballEntity);
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
                SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F /
                        (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
    }
    

}
