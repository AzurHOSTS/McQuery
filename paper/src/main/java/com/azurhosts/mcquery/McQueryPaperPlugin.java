package com.azurhosts.mcquery;

import com.azurhosts.mcquery.logs.LogsManager;

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
        preparePlugin();
    }

    @Override
    protected void onStop() {
        saveConfig();
    }

    private void preparePlugin() {
        if (!Bootstrap.debugEnabled()) return;

        String nms = LogsManager.isFolia() ? "FOLIA"
                : LogsManager.isPaper() ? "PAPER"
                : "UNKNOWN";

        String version = switch (nms) {
            case "FOLIA" -> "Folia";
            case "PAPER" -> "Paper";
            default -> "CraftBukkit or unrecognized NMS";
        };

        LogsManager.Logger.info("NMS Version: " + version);
    }
}
