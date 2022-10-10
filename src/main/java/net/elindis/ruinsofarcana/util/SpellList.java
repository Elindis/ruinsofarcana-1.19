package net.elindis.ruinsofarcana.util;


import net.elindis.ruinsofarcana.spell.InfernoSpell;
import net.elindis.ruinsofarcana.spell.Spell;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;

import java.util.Map;

public class SpellList {

    public static Spell getCurrentSpell(ItemStack stack) {
        assert stack.getNbt() != null;
        NbtList spellList = stack.getNbt().getList("spell_list", 8);
        int selectedSpellIndex = stack.getNbt().getInt("spell_selected");
        String selectedSpell = spellList.getString(selectedSpellIndex);
        Spell currentSpell = spellMap.get(selectedSpell);
        if (currentSpell == null) {
            return new Spell();
        }
        return currentSpell;
    }

    // A list of all the spells in the mod by their string IDs. Think of this as Spell "registration".

    public static Map<String, Spell> spellMap = Map.of(
            "Empty", new Spell(),
//            "Fireball", new InfernoSpell()
            "Inferno", new InfernoSpell()
//            "Frost Bolt", new InfernoSpell(),
//            "Blizzard", new InfernoSpell(),
//            "Cyclone", new InfernoSpell(),
//            "Whirlwind", new InfernoSpell()
    );

}
