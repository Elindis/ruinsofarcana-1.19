package net.elindis.ruinsofarcana.item.equipment;

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
                    if (!player.hasStatusEffect(StatusEffects.WATER_BREATHING)) {
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING,
                                100, 0, false, false, false));
                    } else {
                        if (player.getActiveStatusEffects().containsKey(StatusEffects.WATER_BREATHING)) {
                            if (player.getActiveStatusEffects().get(StatusEffects.WATER_BREATHING).getDuration() < 21) {
                                player.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING,
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
                }
                if (isBoots(player)) {
                    if (!player.hasStatusEffect(StatusEffects.SPEED)) {
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED,
                                100, 0, false, false, false));
                    } else {
                        if (player.getActiveStatusEffects().containsKey(StatusEffects.SPEED)) {
                            if (player.getActiveStatusEffects().get(StatusEffects.SPEED).getDuration() < 21) {
                                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED,
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
