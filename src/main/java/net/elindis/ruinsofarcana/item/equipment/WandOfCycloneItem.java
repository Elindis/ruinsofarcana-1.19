package net.elindis.ruinsofarcana.item.equipment;

import net.elindis.ruinsofarcana.entity.FrostBoltEntity;
import net.elindis.ruinsofarcana.sound.ModSounds;
import net.elindis.ruinsofarcana.util.ModParticles;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.Vanishable;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class WandOfCycloneItem extends RangedWeaponItem implements Vanishable {
    public WandOfCycloneItem(Settings settings) {
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
        if (user instanceof PlayerEntity playerEntity) {
            int i = this.getMaxUseTime(stack) - remainingUseTicks;
            if (i >= 10) {
                int j = 3;

                playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
                if (j > 0) {
                    float f = playerEntity.getYaw();
                    float g = playerEntity.getPitch();
                    float h = -MathHelper.sin(f * 0.017453292F) * MathHelper.cos(g * 0.017453292F);
                    float k = -MathHelper.sin(g * 0.017453292F);
                    float l = MathHelper.cos(f * 0.017453292F) * MathHelper.cos(g * 0.017453292F);
                    float m = MathHelper.sqrt(h * h + k * k + l * l);
                    float n = 3.0F * ((1.0F + (float)j) / 4.0F);
                    h *= n / m;
                    k *= n / m;
                    l *= n / m;
                    playerEntity.addVelocity((double)h, (double)k, (double)l);
                    playerEntity.useRiptide(20);


                    doParticles(world, playerEntity);
                    if (playerEntity.isOnGround()) {
                        float o = 1.1999999F;
                        playerEntity.move(MovementType.SELF, new Vec3d(0.0, 1.1999999284744263, 0.0));
                    }

                    SoundEvent soundEvent;
                    soundEvent = ModSounds.CYCLONE;
                    world.playSoundFromEntity((PlayerEntity)null, playerEntity, soundEvent, SoundCategory.PLAYERS, 1.0F, 1.0F);
                }
            }
        }
    }

    private void doParticles(World world, PlayerEntity playerEntity) {
        for (int o = 0; o < 90; o++) {
            float dx = world.getRandom().nextFloat()/2;
            float dy = world.getRandom().nextFloat()/2;
            float dz = world.getRandom().nextFloat()/2;
            float vx = world.getRandom().nextFloat()/8;
            float vy = world.getRandom().nextFloat()/8;
            float vz = world.getRandom().nextFloat()/8;
            world.addParticle(ParticleTypes.WHITE_ASH, playerEntity.getX()+dx,
                    playerEntity.getY()+dy+0.5f, playerEntity.getZ()+dz, vx, vy, vz);
        }
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
