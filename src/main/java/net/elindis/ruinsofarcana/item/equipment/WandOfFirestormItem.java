package net.elindis.ruinsofarcana.item.equipment;

import net.elindis.ruinsofarcana.block.ModBlocks;
import net.elindis.ruinsofarcana.effect.ModEffects;
import net.elindis.ruinsofarcana.entity.FireballEntity;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.jetbrains.annotations.NotNull;

public class WandOfFirestormItem extends WandItem {
    private final int expCost = 10;
    public WandOfFirestormItem(Settings settings) {
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

    public boolean isRaycaster() {
        return true;
    }
    @Override
    public int getRange() {
        return 40;
    }

    @Override
    public HitResult.@NotNull Type raycastType() {
        return HitResult.Type.BLOCK;
    }
    @Override
    protected void doSpellEffect(World world, LivingEntity user, PlayerEntity playerEntity, float f) {
        if (world.isClient) return;

        HitResult hitResult = user.raycast(getRange(), MinecraftClient.getInstance().getTickDelta(), true);
//        world.createExplosion(null,hitResult.getPos().x,hitResult.getPos().y,hitResult.getPos().z,1f, Explosion.DestructionType.NONE);
        ArmorStandEntity firestormEntity = new ArmorStandEntity(world, hitResult.getPos().x, hitResult.getPos().y, hitResult.getPos().z);

        firestormEntity.setInvisible(true);
        firestormEntity.setInvulnerable(true);
        firestormEntity.setNoGravity(true);
        firestormEntity.setBoundingBox(new Box(0,0,0,0,0,0));
        firestormEntity.addStatusEffect(new StatusEffectInstance(ModEffects.FIRESTORM,
                600, 0, false, false, false));

        BlockPos.iterateOutwards(firestormEntity.getBlockPos(), 2, 1, 2).forEach(blockPos1 -> {
            if (Random.create().nextBetween(0,3) == 0) {
                if (world.getBlockState(blockPos1.up()).isOf(Blocks.AIR)) {
                    world.setBlockState(blockPos1.up(), Blocks.FIRE.getDefaultState());
                }
            }
        });

        world.spawnEntity(firestormEntity);
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
