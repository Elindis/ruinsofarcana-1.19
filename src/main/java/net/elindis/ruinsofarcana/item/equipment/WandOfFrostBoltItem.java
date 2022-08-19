package net.elindis.ruinsofarcana.item.equipment;

import net.elindis.ruinsofarcana.entity.FrostBoltEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.Vanishable;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class WandOfFrostBoltItem extends RangedWeaponItem implements Vanishable {
    public WandOfFrostBoltItem(Settings settings) {
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
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        // Check if the LivingEntity is a PlayerEntity.
        if (user instanceof PlayerEntity playerEntity) {

            // You can only cast if the spell is at least 75% charged.
            int i = this.getMaxUseTime(stack) - remainingUseTicks;
            float f = getPullProgress(i);
                if (!((double)f < 0.75)) {

                        // The conditions for the spell to be castable. On spell success:
                        if (playerEntity.totalExperience > 5 || playerEntity.getAbilities().creativeMode) {

                            // Server-side effects.
                            if (!world.isClient) {

                                // Apply costs. The spell requires 5 xp to cast by default.
                                // TODO: make this a variable, make an enchantment or rite that modifies it
                                playerEntity.addExperience(-5);

                                // Creates the item with the appropriate properties

                                //FrostBoltItem frostBoltItem = (FrostBoltItem)(ModItems.MAGIC_ARROW);
                                FrostBoltEntity frostBoltEntity = new FrostBoltEntity(world, user);
                                frostBoltEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(),
                                        0.0F, f * 2.5F, 0.5F);
                                world.spawnEntity(frostBoltEntity);
                                playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
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
        float f = (float)useTicks / 5.0f;
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
