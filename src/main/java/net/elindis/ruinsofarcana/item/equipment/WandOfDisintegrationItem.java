package net.elindis.ruinsofarcana.item.equipment;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.Vanishable;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class WandOfDisintegrationItem extends RangedWeaponItem implements Vanishable {
    public WandOfDisintegrationItem(Settings settings) {
        super(settings);
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return null;
    }
    @Override
    public int getRange() {
        return 15;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
            user.setCurrentHand(hand);

            return TypedActionResult.consume(itemStack);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        int random2 = world.getRandom().nextInt(9);
        if (random2 > 6) {
            float random = user.getRandom().nextFloat()/10;
            float initialOffsetX = 0;
            float initialOffsetZ = 0;
            if (user.getMainHandStack().isOf(this.asItem())) {
                initialOffsetX += (-MathHelper.cos(MathHelper.RADIANS_PER_DEGREE * (user.getHeadYaw()-60))/3);
                initialOffsetZ += (-MathHelper.sin(MathHelper.RADIANS_PER_DEGREE * (user.getHeadYaw()-60))/3);
            }
            if (user.getOffHandStack().isOf(this.asItem())) {
                initialOffsetX += (MathHelper.cos(MathHelper.RADIANS_PER_DEGREE * (user.getHeadYaw()+60))/3);
                initialOffsetZ += (MathHelper.sin(MathHelper.RADIANS_PER_DEGREE * (user.getHeadYaw()+60))/3);
            }
            double xPos = user.getX() + initialOffsetX;
            double yPos = user.getEyeY()-(MathHelper.sin((MathHelper.RADIANS_PER_DEGREE*user.getPitch(1)))/2);
            double zPos = user.getZ() + initialOffsetZ;
            world.addParticle(ParticleTypes.LAVA, xPos, yPos+0.2, zPos, random, random, random);
        }
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        // Check if the LivingEntity is a PlayerEntity.
        if (user instanceof PlayerEntity playerEntity) {

            // You can only cast if the spell is at least 75% charged.
            int i = this.getMaxUseTime(stack) - remainingUseTicks;
            float f = getPullProgress(i);
                if (((double)f > 0.50)) {

                        // The conditions for the spell to be castable. On spell success:
                        if (playerEntity.totalExperience > 5 || playerEntity.getAbilities().creativeMode) {

                            // Server-side effects.
                            if (!world.isClient) {

                                // Apply costs. The spell requires 5 xp to cast by default.
                                // TODO: make this a variable, make an enchantment or rite that modifies it
                                playerEntity.addExperience(-5);

                                System.out.println("Adding whirlwind effect");
                                //spawn a fast projectile
                                //it spawns particles in front of itself and behind itself to make it look more like a laser

                            }

                            // We play a shooting sounds because the spell went off!
                            world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
                                SoundEvents.BLOCK_NOTE_BLOCK_CHIME, SoundCategory.PLAYERS, 0.8F, 1F /
                                    (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                            world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
                                    SoundEvents.BLOCK_SMALL_AMETHYST_BUD_BREAK, SoundCategory.PLAYERS, 0.8F, 1F /
                                            (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                        }

                        // On spell failure:
                        else {
                            // We play a sad magic sounds.
                            world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
                                SoundEvents.BLOCK_BEACON_DEACTIVATE, SoundCategory.PLAYERS, 0.7F, 1.5F);
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
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }
    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }


}
