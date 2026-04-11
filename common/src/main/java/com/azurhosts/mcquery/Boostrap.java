package com.azurhosts.mcquery;

import com.azurhosts.mcquery.logs.LogsManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

/**
 * MCQuery - Classe de démarrage du plugin.
 * Mainteneur : RedSavant, OxiWan (contact@azurhosts.com)
 * Auteur(s)   : RedSavant (rscomeback@outlook.fr)
 * Distribué sous GNU General Public License v3.0
 * Voir LICENSE, CONTRIBUTING.md pour plus de détails.
 * NOTICE (GPL v3 Section 7b) : L'attribution au mainteneur doit être conservée dans toute redistribution.
 *
 * @version 1.0.0
 **/

public abstract class Boostrap extends JavaPlugin {

    private static Boostrap instance;
    private final static String version = "1.0.0";

    @Override
    public final void onEnable() {
        instance = this;
        onStart();
    }
    @Override
    public final void onDisable() {
        onStop();
    }

    protected abstract void onStart();
    protected abstract void onStop();

    @SuppressWarnings("unchecked")
    public static <T extends Boostrap> T getInstance() { return (T) instance;}

    public static String getVersion() {
        return version;
    }

}
