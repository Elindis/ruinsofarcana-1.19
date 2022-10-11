package net.elindis.ruinsofarcana.item.inscription;

import net.elindis.ruinsofarcana.spell.FreezeSpell;
import net.elindis.ruinsofarcana.spell.InfernoSpell;


public class FreezeInscriptionItem extends InscriptionItem {
	public FreezeInscriptionItem(Settings settings) {
		super(settings);
	}
	public String getInscriptionName() {
		return FreezeSpell.getName();
	}
}
