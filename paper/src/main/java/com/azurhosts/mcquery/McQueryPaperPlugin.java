package com.azurhosts.mcquery;

/**
 * MCQuery - Classe principale du plugin avec compatibilité Paper
 * Mainteneur : RedSavant, OxiWan (contact@azurhosts.com)
 * Auteur(s)   : RedSavant (rscomeback@outlook.fr)
 * Distribué sous GNU General Public License v3.0
 * Voir LICENSE, CONTRIBUTING.md pour plus de détails.
 * NOTICE (GPL v3 Section 7b) : L'attribution au mainteneur doit être conservée dans toute redistribution.
 *
 **/

public class McQueryPaperPlugin extends Bootstrap{

    @Override
    protected void onStart() {
        saveDefaultConfig();
    }

    @Override
    protected void onStop() {
        saveConfig();
    }
}
