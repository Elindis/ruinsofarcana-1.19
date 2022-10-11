package net.elindis.ruinsofarcana.item.inscription;

import net.elindis.ruinsofarcana.spell.DominateUndeadSpell;


public class DominateUndeadInscriptionItem extends InscriptionItem {
	public DominateUndeadInscriptionItem(Settings settings) {
		super(settings);
	}
	public String getInscriptionName() {
		return DominateUndeadSpell.getName();
	}
}
