package net.elindis.ruinsofarcana.item.inscription;

import net.elindis.ruinsofarcana.spell.InfernoSpell;


public class InfernoInscriptionItem extends InscriptionItem {
	public InfernoInscriptionItem(Settings settings) {
		super(settings);
	}
	public String getInscriptionName() {
		return InfernoSpell.getName();
	}
}
