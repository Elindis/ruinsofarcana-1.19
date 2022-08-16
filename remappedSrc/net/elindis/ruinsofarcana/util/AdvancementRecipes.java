package net.elindis.ruinsofarcana.util;

import net.elindis.ruinsofarcana.item.ModItems;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class AdvancementRecipes {

    public static boolean checkRecipeAgainstAdvancements(ItemStack output, PlayerEntity playerEntity) {

        if (hasLightBowRecipe(playerEntity) && output.isOf(ModItems.LIGHT_BOW)) {
            return true;
        }

//        if (hasArkeniteIngotRecipe(playerEntity) && output.isOf(ModItems.ARKENITE_INGOT)) {
//            return true;
//        }

        return false;
    }

    private static boolean hasLightBowRecipe(PlayerEntity playerEntity) {
        Identifier id = new Identifier("ruinsofarcana:lightbow");
        return checkAdvancement(playerEntity, id);
    }
    private static boolean hasArkeniteIngotRecipe(PlayerEntity playerEntity) {
        Identifier id = new Identifier("ruinsofarcana:arkenite_ingot");
        return checkAdvancement(playerEntity, id);
    }

    private static boolean checkAdvancement (PlayerEntity playerEntity, Identifier id) {
        ServerPlayerEntity player = (ServerPlayerEntity) playerEntity;
        Advancement advancement = player.getServer().getAdvancementLoader().get(id);
        if (advancement != null) {
            return getAdvancement(player, id).isDone();
        }
        return false;
    }

    private static AdvancementProgress getAdvancement (ServerPlayerEntity playerEntity, Identifier id) {
            return (playerEntity.getAdvancementTracker().getProgress(
                    Objects.requireNonNull(playerEntity.getServer()).getAdvancementLoader().get(id)));
    }




}
