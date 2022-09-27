package net.elindis.ruinsofarcana.item.equipment;

import net.elindis.ruinsofarcana.entity.FrostBoltEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.Vanishable;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.function.Predicate;

public abstract class WandItem extends RangedWeaponItem implements Vanishable {

    public int expCost = 5;
    public float readyPercentage = 0.5f;

    public WandItem(Settings settings) {
        super(settings);
    }

    // TODO: SOUND PLAYs WHEN THE SPELL IS READY TO BE RELEASED
    // TODO: PREDICATE CHANGING TEXTURE WHEN CASTING

    protected void doSpellEffect(World world, LivingEntity user, PlayerEntity playerEntity, float f) {
        // Example
        FrostBoltEntity frostBoltEntity = new FrostBoltEntity(world, user);
        frostBoltEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(),
                0.0F, f * 2.5F, 0.5F);
        world.spawnEntity(frostBoltEntity);
    }

    protected void playFailureSound(World world, PlayerEntity playerEntity) {
        // Example
        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
            SoundEvents.BLOCK_BEACON_DEACTIVATE, SoundCategory.PLAYERS, 0.7F, 1.5F);
    }

    protected void playSuccessSound(World world, PlayerEntity playerEntity, float f) {
        // Example
        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
            SoundEvents.BLOCK_NOTE_BLOCK_CHIME, SoundCategory.PLAYERS, 0.8F, 1F /
                (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
                SoundEvents.BLOCK_SMALL_AMETHYST_BUD_BREAK, SoundCategory.PLAYERS, 0.8F, 1F /
                        (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
    }

    public float getReadyPercentage() {
        return this.readyPercentage;
    }

    public int getExpCost() {
        return this.expCost;
    }

    protected ParticleEffect getParticleType() {
        return ParticleTypes.SMOKE;
    }

    // Raycaster methods
    public boolean isRaycaster() {
        return false;
    }
    @Override
    public int getRange() {
        return 15;
    }

    private void doUsageParticles(World world, LivingEntity user) {
        int random2 = world.getRandom().nextInt(9);
        if (random2 > 6) {
            float random = (user.getRandom().nextFloat()/10)-0.05f;
            float initialOffsetX = 0;
            float initialOffsetZ = 0;
            float additionalOffsetX = 0;
            float additionalOffsetZ = 0;

            if (user.getMainHandStack().isOf(this.asItem())) {

                initialOffsetX += (-MathHelper.cos(MathHelper.RADIANS_PER_DEGREE * (user.getHeadYaw()-60))*(1f/3));
                initialOffsetZ += (-MathHelper.sin(MathHelper.RADIANS_PER_DEGREE * (user.getHeadYaw()-60))*(1f/3));
                if (user.getPitch()<-30) {
                    float pitch = user.getPitch()*-1/30;
                    initialOffsetX*=1/pitch;
                    initialOffsetZ*=1/pitch;
                    additionalOffsetX = -MathHelper.cos(MathHelper.RADIANS_PER_DEGREE*(user.getHeadYaw()+30))/3;
                    additionalOffsetX *= pitch/2;
                    additionalOffsetZ = -MathHelper.sin(MathHelper.RADIANS_PER_DEGREE*(user.getHeadYaw()+30))/3;
                    additionalOffsetZ *= pitch/2;
                }
            }
            if (user.getOffHandStack().isOf(this.asItem())) {
                initialOffsetX += (MathHelper.cos(MathHelper.RADIANS_PER_DEGREE * (user.getHeadYaw()+60))/3);
                initialOffsetZ += (MathHelper.sin(MathHelper.RADIANS_PER_DEGREE * (user.getHeadYaw()+60))/3);
                if (user.getPitch()<-30) {
                    float pitch = user.getPitch()*-1/30;
                    initialOffsetX*=1/pitch;
                    initialOffsetZ*=1/pitch;
                    additionalOffsetX = MathHelper.cos(MathHelper.RADIANS_PER_DEGREE*(user.getHeadYaw()-30))/3;
                    additionalOffsetX *= pitch/2;
                    additionalOffsetZ = MathHelper.sin(MathHelper.RADIANS_PER_DEGREE*(user.getHeadYaw()-30))/3;
                    additionalOffsetZ *= pitch/2;
                }
            }

            double xPos = user.getX() + initialOffsetX + additionalOffsetX;
            double yPos = user.getEyeY()-(MathHelper.sin((MathHelper.RADIANS_PER_DEGREE* user.getPitch(MinecraftClient.getInstance().getTickDelta())))/2);
            double zPos = user.getZ() + initialOffsetZ+ additionalOffsetZ;
            world.addParticle(getParticleType(), xPos, yPos+0.2, zPos, random, random, random);
        }
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        doUsageParticles(world, user);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        // Check if the LivingEntity is a PlayerEntity.
        if (user instanceof PlayerEntity playerEntity) {

            // You can only cast if the spell is at least 50% charged.
            int i = this.getMaxUseTime(stack) - remainingUseTicks;
            float f = getPullProgress(i);
            if (((double)f > getReadyPercentage())) {

                // The conditions for the spell to be castable. On spell success:
                if (playerEntity.totalExperience > getExpCost() || playerEntity.getAbilities().creativeMode) {
                    if (!isRaycaster()) {

                        // Server-side effects.
                        if (!world.isClient) {
                            // Apply costs. The spell requires 5 xp to cast by default.
                            playerEntity.addExperience(-getExpCost());
                            playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
                        }
                        // Execute the spell effect.
                        doSpellEffect(world, user, playerEntity, f);

                        // We play a shooting sounds because the spell went off!
                        playSuccessSound(world, playerEntity, f);
                    }
                    if (isRaycaster()) {
                        var raycastType = user.raycast(getRange(),MinecraftClient.getInstance().getTickDelta(),true).getType();
                        if (!raycastType.equals(HitResult.Type.MISS)) {

                            // Server-side effects.
                            if (!world.isClient) {
                                // Apply costs. The spell requires 5 xp to cast by default.
                                playerEntity.addExperience(-getExpCost());
                                playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
                            }
                            // Execute the spell effect.
                            doSpellEffect(world, user, playerEntity, f);

                            // We play a shooting sounds because the spell went off!
                            playSuccessSound(world, playerEntity, f);
                        } else {
                            playFailureSound(world, playerEntity);
                        }

                    }

                }

                // On spell failure:
                else {
                    // We play a sad magic sound.
                    playFailureSound(world, playerEntity);
                }
            }
        }
    }


    public static float getPullProgress(int useTicks) {
        float f = (float)useTicks / 20.0f;
        if ((f = (f * f + f * 2.0f) / 3.0f) > 1.0f) {
            f = 1.0f;
        }
        return f;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return null;
    }


}
