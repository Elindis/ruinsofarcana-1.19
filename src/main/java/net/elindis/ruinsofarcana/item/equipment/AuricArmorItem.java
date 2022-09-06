package net.elindis.ruinsofarcana.item.equipment;

import net.elindis.ruinsofarcana.effect.ModEffects;
import net.elindis.ruinsofarcana.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class AuricArmorItem extends ArmorItem {

    public AuricArmorItem(ArmorMaterial material, EquipmentSlot equipmentSlot, Settings settings) {
        super(material, equipmentSlot, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient) {
            if (entity instanceof PlayerEntity player) {

                if (isGoggles(player)) {
                    player.removeStatusEffect(StatusEffects.BLINDNESS);
                    player.removeStatusEffect(StatusEffects.DARKNESS);
                    player.removeStatusEffect(StatusEffects.NAUSEA);
                    if (!player.hasStatusEffect(StatusEffects.NIGHT_VISION)) {
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION,
                                240, 0, false, false, false));
                    } else {
                        if (player.getActiveStatusEffects().containsKey(StatusEffects.NIGHT_VISION)) {
                            if (player.getActiveStatusEffects().get(StatusEffects.NIGHT_VISION).getDuration() < 221) {
                                player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION,
                                        240, 0, false, false, false));
                            }
                        }
                    }
                }
                if (isNecklace(player)) {
                    if (!player.hasStatusEffect(StatusEffects.CONDUIT_POWER)) {
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.CONDUIT_POWER,
                                100, 0, false, false, false));
                    } else {
                        if (player.getActiveStatusEffects().containsKey(StatusEffects.CONDUIT_POWER)) {
                            if (player.getActiveStatusEffects().get(StatusEffects.CONDUIT_POWER).getDuration() < 21) {
                                player.addStatusEffect(new StatusEffectInstance(StatusEffects.CONDUIT_POWER,
                                        100, 0, false, false, false));
                            }
                        }
                    }

                }
                if (isBelt(player)) {
                    if (!player.hasStatusEffect(StatusEffects.SLOW_FALLING)) {
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING,
                                100, 0, false, false, false));
                    } else {
                        if (player.getActiveStatusEffects().containsKey(StatusEffects.SLOW_FALLING)) {
                            if (player.getActiveStatusEffects().get(StatusEffects.SLOW_FALLING).getDuration() < 21) {
                                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING,
                                        100, 0, false, false, false));
                            }
                        }
                    }
                    if (!player.hasStatusEffect(StatusEffects.JUMP_BOOST)) {
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST,
                                100, 0, false, false, false));
                    } else {
                        if (player.getActiveStatusEffects().containsKey(StatusEffects.JUMP_BOOST)) {
                            if (player.getActiveStatusEffects().get(StatusEffects.JUMP_BOOST).getDuration() < 21) {
                                player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST,
                                        100, 0, false, false, false));
                            }
                        }
                    }
                }
                if (isBoots(player)) {
                    if (!player.hasStatusEffect(ModEffects.SPEED)) {
                        player.addStatusEffect(new StatusEffectInstance(ModEffects.SPEED,
                                100, 0, false, false, false));
                    } else {
                        if (player.getActiveStatusEffects().containsKey(ModEffects.SPEED)) {
                            if (player.getActiveStatusEffects().get(ModEffects.SPEED).getDuration() < 21) {
                                player.addStatusEffect(new StatusEffectInstance(ModEffects.SPEED,
                                        100, 0, false, false, false));
                            }
                        }
                    }
                }
            }
        }


        super.inventoryTick(stack, world, entity, slot, selected);
    }

    private boolean isGoggles(PlayerEntity player) {
        return player.getInventory().getArmorStack(3).getItem() == ModItems.TRUESIGHT_GOGGLES;
    }
    private boolean isNecklace(PlayerEntity player) {
        return player.getInventory().getArmorStack(2).getItem() == ModItems.DIVING_NECKLACE;
    }
    private boolean isBelt(PlayerEntity player) {
        return player.getInventory().getArmorStack(1).getItem() == ModItems.GRAVITY_BELT;
    }
    private boolean isBoots(PlayerEntity player) {
        return player.getInventory().getArmorStack(0).getItem() == ModItems.TRAVELERS_BOOTS;
    }
}
