package net.elindis.ruinsofarcana.item.inscription;

import net.elindis.ruinsofarcana.spell.HealUndeadSpell;


public class HealUndeadInscriptionItem extends InscriptionItem {
	public HealUndeadInscriptionItem(Settings settings) {
		super(settings);
	}
	public String getInscriptionName() {
		return HealUndeadSpell.getName();
	}
}
