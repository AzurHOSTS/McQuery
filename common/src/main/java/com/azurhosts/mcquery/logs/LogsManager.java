package com.azurhosts.mcquery.logs;

import com.azurhosts.mcquery.Bootstrap;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;

import java.util.logging.Level;

import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

/**
 * MCQuery - Classe de gestion des logs pour le Bootstrap
 * Mainteneur : RedSavant, OxiWan (contact@azurhosts.com)
 * Auteur(s)   : RedSavant (rscomeback@outlook.fr)
 * Distribué sous GNU General Public License v3.0
 * Voir LICENSE, CONTRIBUTING.md pour plus de détails.
 * NOTICE (GPL v3 Section 7b) : L'attribution au mainteneur doit être conservée dans toute redistribution.
 *
 **/

public class LogsManager {

    public class Logger {

        private static final MiniMessage MINI = MiniMessage.miniMessage();

        public static void info(String message) {
            send(message, Level.INFO);
        }

        public static void warn(String message) {
            send(message, WARNING);
        }

        public static void error(String message) {
            send(message, SEVERE);
        }

        public static void send(String message, Level level) {
            if (isPaper()) {
                String prefix = "";
                if (level == Level.WARNING) prefix = "<yellow>[WARN]</yellow> ";
                else if (level == Level.SEVERE) prefix = "<red>[ERROR]</red> ";
                Bukkit.getConsoleSender().sendMessage(MINI.deserialize(prefix + message));
            } else {
                Bootstrap.getInstance().getLogger().log(level, stripMiniMessage(message));
            }
        }

        private static String stripMiniMessage(String message) {
            return MINI.stripTags(message);
        }
    }

    public static boolean isPaper() {
        try {
            Class.forName("io.papermc.paper.configuration.Configuration");
            return true;
        }
        catch (Throwable ignored) {}
        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            return true;
        }
        catch (Throwable ignored) {}
        String name = Bukkit.getName();
        return name != null && name.toLowerCase().contains("paper");
    }

    public static boolean isFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
