package net.elindis.ruinsofarcana.recipe;

import net.elindis.ruinsofarcana.RuinsOfArcana;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModRecipes {
    public static void registerRecipes() {
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(RuinsOfArcana.MOD_ID, ManufactoryRecipe.Serializer.ID),
                ManufactoryRecipe.Serializer.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, new Identifier(RuinsOfArcana.MOD_ID, ManufactoryRecipe.Type.ID),
                ManufactoryRecipe.Type.INSTANCE);

        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(RuinsOfArcana.MOD_ID, TransmutationTableRecipe.Serializer.ID),
                TransmutationTableRecipe.Serializer.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, new Identifier(RuinsOfArcana.MOD_ID, TransmutationTableRecipe.Type.ID),
                TransmutationTableRecipe.Type.INSTANCE);
    }
}
