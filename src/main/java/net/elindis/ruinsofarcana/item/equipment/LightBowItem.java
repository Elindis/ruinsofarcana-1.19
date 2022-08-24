package net.elindis.ruinsofarcana.item.equipment;

import net.elindis.ruinsofarcana.entity.LightArrowEntity;
import net.elindis.ruinsofarcana.sound.ModSounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
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

public class LightBowItem extends RangedWeaponItem implements Vanishable {

    // This bow is equivalent to a bow enchanted with Punch I, Flame, and Infinity. It can be enchanted with
    // Unbreaking and Mending also, and I should add an enchantment called Intensity that increases its power.

    public static final int field_30855 = 20;
    public static final int RANGE = 15;
    public LightBowItem(Settings settings) {
        super(settings);
    }
    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }
    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }
    @Override
    public Predicate<ItemStack> getProjectiles() {
        return BOW_PROJECTILES;
    }
    @Override
    public int getRange() {
        return 15;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);

        world.playSound(null, user.getX(), user.getY(), user.getZ(), ModSounds.CYCLONE, SoundCategory.PLAYERS, 0.7f, 1.2f / (world.getRandom().nextFloat() * 0.4f + 1.2f) + 0.5f);
        return TypedActionResult.consume(itemStack);
    }
    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        MinecraftClient.getInstance().getSoundManager().stopSounds(ModSounds.CYCLONE_ID, SoundCategory.PLAYERS);
        int i;
        float f;
        if (!(user instanceof PlayerEntity)) {
            return;
        }
        PlayerEntity playerEntity = (PlayerEntity)user;

        if ((double)(f = BowItem.getPullProgress(i = this.getMaxUseTime(stack) - remainingUseTicks)) < 0.4) {
            return;
        }
        if (!world.isClient) {
            PersistentProjectileEntity lightArrowEntity = new LightArrowEntity(world, user);
            lightArrowEntity.setShotFromCrossbow(true);
            lightArrowEntity.setPierceLevel((byte) 3);
            lightArrowEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0f, f * 2.5f, 0.5f);
            if (f == 1.0f) {
                lightArrowEntity.setCritical(true);
            }

            /*===============    Change this when you implement the Intensity enchantment.    ===================>*/
            int intensity = 2;
            lightArrowEntity.setDamage(lightArrowEntity.getDamage() + intensity * 0.5 + 0.5);
            lightArrowEntity.setPunch(1);

            stack.damage(1, playerEntity, p -> p.sendToolBreakStatus(playerEntity.getActiveHand()));
            world.spawnEntity(lightArrowEntity);
        }
        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ITEM_CROSSBOW_SHOOT, SoundCategory.PLAYERS, 0.7f, 1.2f / (world.getRandom().nextFloat() * 0.4f + 1.2f) + f * 0.5f);
        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), ModSounds.LIGHT_BOW_SHOT, SoundCategory.PLAYERS, 2.0f, 1.3f / (world.getRandom().nextFloat() * 0.4f + 1.2f) + f * 0.5f);

        playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
    }
    public static float getPullProgress(int useTicks) {
        float f = (float)useTicks / 20.0f;
        if ((f = (f * f + f * 2.0f) / 3.0f) > 1.0f) {
            f = 1.0f;
        }
        return f;
    }
//    public PersistentProjectileEntity createArrow(World world, LivingEntity shooter) {
//        return new LightArrowEntity(world, shooter);
//    }
}
