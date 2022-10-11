package net.elindis.ruinsofarcana.item.inscription;

import net.elindis.ruinsofarcana.spell.RaiseZombieSpell;


public class RaiseZombieInscriptionItem extends InscriptionItem {
	public RaiseZombieInscriptionItem(Settings settings) {
		super(settings);
	}
	public String getInscriptionName() {
		return RaiseZombieSpell.getName();
	}
}
