package net.elindis.ruinsofarcana.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class ManufactoryRecipe implements Recipe<SimpleInventory> {

    private final Identifier id;
    private final ItemStack output;
    private final DefaultedList<Ingredient> recipeItems;

    public ManufactoryRecipe(Identifier id, ItemStack output, DefaultedList<Ingredient> recipeItems) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;

    }

    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        // EXTEND THIS IF YOUR RECIPES INCLUDE MORE THAN TWO ITEMS
        if(world.isClient()) {return false;}
        // comparing recipe indexes 0, 1, and 2 to slots at indexes 1, 2, and 3.
        if (recipeItems.get(0).test(inventory.getStack(1))) {
            if (recipeItems.get(1).test(inventory.getStack(2))) {
                return recipeItems.get(2).test(inventory.getStack(3));
            }

        }
        // Makes the recipes shapeless
//        if (recipeItems.get(1).test(inventory.getStack(1))) {
//            return recipeItems.get(0).test(inventory.getStack(2));
//        }

        return false;
    }

    @Override
    public ItemStack craft(SimpleInventory inventory) {
        return output;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput() {
        return output.copy();
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    //


    public static class Type implements RecipeType<ManufactoryRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "manufactory";
    }

    public static class Serializer implements RecipeSerializer<ManufactoryRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "manufactory";
        // this is the name given in the json file

        @Override
        public ManufactoryRecipe read(Identifier id, JsonObject json) {
            ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "output"));

            JsonArray ingredients = JsonHelper.getArray(json, "ingredients");
            // CHANGE THE SIZE INT ACCORDING TO HOW MANY ITEM SLOTS YOUR RECIPE WILL CHECK. CHANGED TO 3
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(3, Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new ManufactoryRecipe(id, output, inputs);
        }

        @Override
        public ManufactoryRecipe read(Identifier id, PacketByteBuf buf) {
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromPacket(buf));
            }

            ItemStack output = buf.readItemStack();
            return new ManufactoryRecipe(id, output, inputs);
        }

        @Override
        public void write(PacketByteBuf buf, ManufactoryRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());
            for (Ingredient ing : recipe.getIngredients()) {
                ing.write(buf);
            }
            buf.writeItemStack(recipe.getOutput());
        }
    }
}
