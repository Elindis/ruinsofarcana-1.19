package net.elindis.ruinsofarcana.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class SingularityRecipe implements Recipe<SimpleInventory> {

    private final Identifier id;
    private final ItemStack output;
    private final DefaultedList<Ingredient> recipeItems;
//    private final int outputChance;

    public SingularityRecipe(Identifier id, ItemStack output, DefaultedList<Ingredient> recipeItems) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
//        this.outputChance = outputChance;
    }

    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        if (world.isClient) return false;
        if (inventory.getStack(0).isEmpty()) return false;

        world.getServer().sendMessage(Text.literal(this.id.toString()));

        return recipeItems.get(0).test(inventory.getStack(0));
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
//    public int getChance() {
//        return outputChance;
//    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static ItemStack outputFromJson(JsonObject json) {
        Item item = SingularityRecipe.getItem(json);
        if (json.has("data")) {
            throw new JsonParseException("Disallowed data tag found");
        }
        int i = JsonHelper.getInt(json, "count", 1);

        if (i < 1) {
            throw new JsonSyntaxException("Invalid output count: " + i);
        }

        return new ItemStack(item, i);
    }
    public static int chanceFromJson(JsonObject json) {
        if (json.has("data")) {
            throw new JsonParseException("Disallowed data tag found");
        }
        int chance = JsonHelper.getInt(json, "chance", 100);

        if (chance < 0 || chance > 100) {
            throw new JsonSyntaxException("Invalid chance: " + chance);
        }
        return (chance);
    }

    public static Item getItem(JsonObject json) {
        String string = JsonHelper.getString(json, "item");
        Item item = Registry.ITEM.getOrEmpty(new Identifier(string)).orElseThrow(() -> new JsonSyntaxException("Unknown item '" + string + "'"));
        if (item == Items.AIR) {
            throw new JsonSyntaxException("Invalid item: " + string);
        }
//        MinecraftClient.getInstance().getServer().sendMessage(Text.literal("getting an item, lol"));
        return item;
    }

    public static class Type implements RecipeType<SingularityRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "singularity";
    }

    public static class Serializer implements RecipeSerializer<SingularityRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "singularity";
        // this is the name given in the json file

        @Override
        public SingularityRecipe read(Identifier id, JsonObject json) {
            //ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "output"));

            JsonArray ingredients = JsonHelper.getArray(json, "ingredients");
            // CHANGE THE SIZE INT ACCORDING TO HOW MANY ITEM SLOTS YOUR RECIPE WILL CHECK
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(ingredients.size(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }
            ItemStack output = SingularityRecipe.outputFromJson(JsonHelper.getObject(json, "output"));
//            int outputChance = SingularityRecipe.chanceFromJson(JsonHelper.getObject(json, "output"));
            return new SingularityRecipe(id, output, inputs);
        }

        @Override
        public SingularityRecipe read(Identifier id, PacketByteBuf buf) {
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromPacket(buf));
            }

            ItemStack output = buf.readItemStack();
//            int outputChance = buf.readIntLE();
            return new SingularityRecipe(id, output, inputs);
        }

        @Override
        public void write(PacketByteBuf buf, SingularityRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());
//            buf.writeIntLE(recipe.getChance());
            for (Ingredient ing : recipe.getIngredients()) {
                ing.write(buf);
            }
            buf.writeItemStack(recipe.getOutput());
        }
    }
}
