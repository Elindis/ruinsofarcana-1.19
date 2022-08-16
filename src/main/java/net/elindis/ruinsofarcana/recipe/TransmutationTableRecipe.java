package net.elindis.ruinsofarcana.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class TransmutationTableRecipe implements Recipe<SimpleInventory> {

    private final Identifier id;
    private final ItemStack output;
    private final DefaultedList<Ingredient> recipeItems;

    public TransmutationTableRecipe(Identifier id, ItemStack output, DefaultedList<Ingredient> recipeItems) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;

    }

    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        if (world.isClient) return false;
        if (inventory.getStack(0).isEmpty()) return false;

        world.getServer().sendMessage(Text.literal(this.id.toString()));
        world.getServer().sendMessage(Text.literal(inventory.size() +" items in inventory"));

        int matchableItems = recipeItems.size();

        if (matchableItems == inventory.size()) {

            // Patch for duplicate items
            for (int i = 0; i < inventory.size(); i++) {

                int item1 = occursTimesInInventory(inventory, inventory.getStack(i));
                world.getServer().sendMessage(Text.literal(inventory.getStack(i)+" occurs "+item1+" times in inventory"));

                int item2 = occursTimesInRecipe(inventory.getStack(i));
                world.getServer().sendMessage(Text.literal(inventory.getStack(i)+" occurs "+item2+" times in recipe"));

                if (item1 != item2) return false;
            }
            return true;
        }
        return false;
    }

    // If the recipeitem matches ANY of the inventory's items, do progress.
    // However, this doesn't allow us to have recipes with duplicates in them.
    // So, the above patch should take care of that.
    // And actually, this old logic doesn't seem to be needed anymore...

//            for (int i = 0; i < matchableItems; i++) {
//                if (recipeItems.get(i).test(inventory.getStack(0)) ||
//                        recipeItems.get(i).test(inventory.getStack(1)) ||
//                        recipeItems.get(i).test(inventory.getStack(2)) ||
//                        recipeItems.get(i).test(inventory.getStack(3)) ||
//                        recipeItems.get(i).test(inventory.getStack(4))) {
//                    matchedItems++;
//                }
//            }

    private int occursTimesInInventory(SimpleInventory inventory, ItemStack itemStack) {
        int count = 0;
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.getStack(i).isOf(itemStack.getItem())) count++;
        }
        return count;
    }
    private int occursTimesInRecipe(ItemStack itemStack) {
        int count = 0;
        for (int i = 0; i < recipeItems.size(); i++) {
            if (recipeItems.get(i).test(itemStack)) count++;
        }
        return count;
    }

    @Override
    public ItemStack craft(SimpleInventory inventory) {
        return this.getOutput().copy();
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


    public static class Type implements RecipeType<TransmutationTableRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "transmutation_table";
    }

    public static class Serializer implements RecipeSerializer<TransmutationTableRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "transmutation_table";
        // this is the name given in the json file

        @Override
        public TransmutationTableRecipe read(Identifier id, JsonObject json) {
            ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "output"));

            JsonArray ingredients = JsonHelper.getArray(json, "ingredients");
            // CHANGE THE SIZE INT ACCORDING TO HOW MANY ITEM SLOTS YOUR RECIPE WILL CHECK
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(ingredients.size(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }
            return new TransmutationTableRecipe(id, output, inputs);
        }

        @Override
        public TransmutationTableRecipe read(Identifier id, PacketByteBuf buf) {
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromPacket(buf));
            }

            ItemStack output = buf.readItemStack();
            return new TransmutationTableRecipe(id, output, inputs);
        }

        @Override
        public void write(PacketByteBuf buf, TransmutationTableRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());
            for (Ingredient ing : recipe.getIngredients()) {
                ing.write(buf);
            }
            buf.writeItemStack(recipe.getOutput());
        }
    }
}
