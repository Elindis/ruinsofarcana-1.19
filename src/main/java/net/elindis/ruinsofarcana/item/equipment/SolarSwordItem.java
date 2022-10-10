package net.elindis.ruinsofarcana.item.equipment;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.elindis.ruinsofarcana.util.ModParticleUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.Vanishable;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class SolarSwordItem extends ToolItem implements Vanishable {
    private final float attackDamage;
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

    public SolarSwordItem(ToolMaterial toolMaterial,float attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, settings);
        this.attackDamage = attackDamage + toolMaterial.getAttackDamage();
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", this.attackDamage, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", attackSpeed, EntityAttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }

    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return !miner.isCreative();

    }
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.add(tooltip.size(), Text.literal("Fire damage: 2"));
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if ((double)state.getHardness(world, pos) != 0.0) {
            stack.damage(2, miner, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        }
        return true;
    }
    @Override
    public boolean hasGlint(ItemStack stack) {
        return false;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        playSound(attacker);
        ModParticleUtil.doLivingEntityParticles(target, ParticleTypes.LAVA, 20);
        if (!attacker.world.isClient) {
            stack.damage(1, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        }
        return true;
    }

    public void inventoryTick(ItemStack stack, World world, Entity user, int slot, boolean selected) {
        if (user instanceof PlayerEntity) {
            // changed from getmainhand || getoffhand
            if (((PlayerEntity) user).isHolding(this.asItem())) {

                boolean bl = ((PlayerEntity) user).hasStatusEffect(StatusEffects.FIRE_RESISTANCE);
                if (bl) {
                    float duration = Objects.requireNonNull(((PlayerEntity) user).getStatusEffect(StatusEffects.FIRE_RESISTANCE)).getDuration();
                    if (duration < 21) {
                        ((PlayerEntity) user).addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE,
                                41, 0, true, true, true));
                    }
                }
                if (!bl) {
                    ((PlayerEntity) user).addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE,
                            41, 0, true, true, true));
                }
            }
        }
    }

    private void playSound(LivingEntity playerEntity) {
        playerEntity.world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
                SoundEvents.BLOCK_SMALL_AMETHYST_BUD_BREAK, playerEntity.getSoundCategory(), 1.6f,
                 0.8f + (playerEntity.getRandom().nextFloat()/3));
    }
    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        if (state.isOf(Blocks.COBWEB)) {
            return 15.0f;
        }
        Material material = state.getMaterial();
        if (material == Material.PLANT || material == Material.REPLACEABLE_PLANT || state.isIn(BlockTags.LEAVES) || material == Material.GOURD) {
            return 1.5f;
        }
        return 1.0f;
    }
    @Override
    public boolean isSuitableFor(BlockState state) {
        return state.isOf(Blocks.COBWEB);
    }
    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND) {
            return this.attributeModifiers;
        }
        return super.getAttributeModifiers(slot);
    }
}


