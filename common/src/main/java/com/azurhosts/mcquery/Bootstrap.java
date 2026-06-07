package com.azurhosts.mcquery;

import com.azurhosts.mcquery.logs.LogsManager;
import com.azurhosts.mcquery.query.UDPServer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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

public abstract class Bootstrap extends JavaPlugin {

    private static Bootstrap instance;
    private final static String version = "1.0.0";

    public static final Map<String, Integer> WORLD_INDEX = new LinkedHashMap<>();

    private volatile static boolean autoSave = true;

    private UDPServer udpServer;
    private Thread udpThread;

    @Override
    public final void onEnable() {
        instance = this;
        onStart();
        startQuery();
        checkAutoSave();
        buildWorldIndex();
    }
    @Override
    public final void onDisable() {
        onStop();
        if (udpServer != null ) {
            udpServer.stop();
        }
    }

    protected abstract void onStart();
    protected abstract void onStop();

    @SuppressWarnings("unchecked")
    public static <T extends Bootstrap> T getInstance() { return (T) instance;}

    public static String getVersion() {
        return version;
    }

    private void startQuery() {
        int port;

        File portFile = new File(getDataFolder(), "port.txt");

        try {
            getDataFolder().mkdir();
            portFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(portFile));
            String line = reader.readLine();
            reader.close();

            if (line == null || !line.matches("\\d+")) {
                if (debugEnabled()) {
                    LogsManager.Logger.error("port.txt ne contient pas un port valide <gray>(null ou pas des chiffres)</gray>");
                }
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
            port = Integer.parseInt(line.trim());

        } catch (FileNotFoundException e) {
            if (debugEnabled()) {
                LogsManager.Logger.error("port.txt introuvable dans " + getDataFolder().getName());
            }
            getServer().getPluginManager().disablePlugin(this);
            return;

        } catch (IOException e) {
            if (debugEnabled()) {
                LogsManager.Logger.error("Erreur de lecture de port.txt : " + e.getMessage());
            }
            getServer().getPluginManager().disablePlugin(this);
            return;

        } catch (NumberFormatException e) {
            if (debugEnabled()) {
                LogsManager.Logger.error("port.txt ne contient pas un port valide");
            }
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        try {
            udpServer = new UDPServer(port);
            udpThread = new Thread(udpServer);
            udpThread.setDaemon(true);
            udpThread.start();
            if (debugEnabled()) {
                LogsManager.Logger.info("UDP server démarré sur le port " + port);
            }
        } catch (Exception e) {
            if (debugEnabled()) {
                LogsManager.Logger.error("Impossible de démarrer le serveur UDP : " + e.getMessage());
            }
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    public static boolean debugEnabled() {
        return Bootstrap.getInstance().getConfig().getBoolean("debug.enable");
    }

    public static boolean checkAutoSave() {
        autoSave = Bukkit.getWorlds().stream().allMatch(World::isAutoSave);
        return autoSave;
    }

    private static void buildWorldIndex() {
        if (WORLD_INDEX.isEmpty()) {
            int i = 0;
            for (World world : Bukkit.getWorlds()) {
                WORLD_INDEX.put(world.getName(), i++);
            }
        }
    }

    public static World getWorldById(int id) {
        return WORLD_INDEX.entrySet().stream()
                .filter(e -> e.getValue() == id)
                .map(e -> Bukkit.getWorld(e.getKey()))
                .findFirst()
                .orElse(null);
    }
}
