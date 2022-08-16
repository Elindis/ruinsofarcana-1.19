package net.elindis.ruinsofarcana.item.equipment;

import net.elindis.ruinsofarcana.entity.FireballEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class WandOfFireballItem extends BowItem {
    public WandOfFireballItem(Settings settings) {
        super(settings);
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

            // Sets the projectile sprite. Since we're using a ThrownEntity, it's a billboard, so use something round.
            ItemStack itemStack = Items.FIRE_CHARGE.getDefaultStack();

            // You can only cast if the spell is at least 80% charged.
            int i = this.getMaxUseTime(stack) - remainingUseTicks;
            float f = getPullProgress(i);
                if (!((double)f < 0.8)) {

                        // The conditions for the spell to be castable. On spell success:
                        if (playerEntity.experienceLevel > 0 || playerEntity.getAbilities().creativeMode) {

                            // Server side effects.
                            if (!world.isClient) {

                                // Apply costs. The spell requires 1 level to cast.
                                playerEntity.applyEnchantmentCosts(null, 1);

                                // Creates the item with the appropriate properties

                                FireballEntity fireballEntity = new FireballEntity(world, user, 3f);
                                fireballEntity.setItem(itemStack);
                                fireballEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(),
                                        0.0F, f * 2.5F, 0.5F);
                                world.spawnEntity(fireballEntity);

                                // Probably useless?
                                playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
                            }

                            // Client side effects. We play a shooting sounds because the spell went off!
                            world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
                                SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F /
                                    (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                        }

                        // On spell failure:
                        else {
                            // We play a sad magic sounds.
                            world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
                                SoundEvents.BLOCK_BEACON_DEACTIVATE, SoundCategory.PLAYERS, 0.5F, 1.5F);
                        }
                }
        }
    }


}
