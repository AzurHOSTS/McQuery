package com.azurhosts.mcquery.query;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nullable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * MCQuery - Classe de gestion des instruction et packets de la query
 * Mainteneur : RedSavant, OxiWan (contact@azurhosts.com)
 * Auteur(s)   : RedSavant (rscomeback@outlook.fr)
 * Distribué sous GNU General Public License v3.0
 * Voir LICENSE, CONTRIBUTING.md pour plus de détails.
 * NOTICE (GPL v3 Section 7b) : L'attribution au mainteneur doit être conservée dans toute redistribution.
 *
 **/

public class UDPServer implements Runnable {

    private final DatagramSocket socket;
    private volatile boolean running = true;

    public UDPServer(int port) throws Exception {
        this.socket = new DatagramSocket(port);
    }

    @Override
    public void run() {
        byte[] buf = new byte[1024];
        while (running) {
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                String instruction = new String(packet.getData(), 0, packet.getLength()).trim();
                String response = handleInstruction(instruction);

                byte[] responseBytes = response.getBytes();
                DatagramPacket reply = new DatagramPacket(
                        responseBytes, responseBytes.length,
                        packet.getAddress(), packet.getPort()
                );
                socket.send(reply);
            } catch (Exception e) {
                if (running) e.printStackTrace();
            }
        }
    }

    private String handleInstruction(String instruction) {
        if (instruction.equals("GET_PLAYERS")) {
            JsonObject root = new JsonObject();
            root.addProperty("type", "GET_PLAYERS");
            root.addProperty("timestamp", System.currentTimeMillis());
            root.addProperty("server_tps", Math.min(20.0, Bukkit.getTPS()[0]));

            JsonArray players = new JsonArray();

            for (Player p : Bukkit.getOnlinePlayers()) {
                JsonObject player = new JsonObject();

                player.addProperty("name", p.getName());
                player.addProperty("display_name", PlainTextComponentSerializer.plainText().serialize(p.displayName()));
                player.addProperty("uuid", p.getUniqueId().toString());
                player.addProperty("ip", p.getAddress() != null ? p.getAddress().getAddress().getHostAddress() : null);
                player.addProperty("ping", p.getPing());
                player.addProperty("locale", p.locale().toString());
                player.addProperty("client_brand", p.getClientBrandName());

                player.addProperty("gamemode", p.getGameMode().name());
                player.addProperty("is_op", p.isOp());
                player.addProperty("is_flying", p.isFlying());
                player.addProperty("allow_flight", p.getAllowFlight());
                player.addProperty("is_sneaking", p.isSneaking());
                player.addProperty("is_sprinting", p.isSprinting());
                player.addProperty("is_sleeping", p.isSleeping());
                player.addProperty("is_blocked", p.isSleepingIgnored());
                player.addProperty("is_dead", p.isDead());

                player.addProperty("health", p.getHealth());
                player.addProperty("max_health", p.getAttribute(Attribute.MAX_HEALTH).getValue());
                player.addProperty("absorption", p.getAbsorptionAmount());
                player.addProperty("food_level", p.getFoodLevel());
                player.addProperty("saturation", p.getSaturation());
                player.addProperty("exhaustion", p.getExhaustion());
                player.addProperty("air", p.getRemainingAir());
                player.addProperty("max_air", p.getMaximumAir());
                player.addProperty("fire_ticks", p.getFireTicks());
                player.addProperty("freeze_ticks", p.getFreezeTicks());
                player.addProperty("arrow_count", p.getArrowsInBody());
                player.addProperty("exp", p.getExp());
                player.addProperty("level", p.getLevel());
                player.addProperty("total_exp", p.getTotalExperience());

                Location loc = p.getLocation();
                JsonObject pos = new JsonObject();
                pos.addProperty("world", loc.getWorld().getName());
                pos.addProperty("x", loc.getX());
                pos.addProperty("y", loc.getY());
                pos.addProperty("z", loc.getZ());
                pos.addProperty("yaw", loc.getYaw());
                pos.addProperty("pitch", loc.getPitch());
                pos.addProperty("biome", loc.getBlock().getBiome().toString());
                pos.addProperty("light_level", loc.getBlock().getLightLevel());
                player.add("pos", pos);

                JsonObject inventory = new JsonObject();

                inventory.add("mainhand", serializeItem(p.getInventory().getItemInMainHand()));
                inventory.add("offhand", serializeItem(p.getInventory().getItemInOffHand()));

                JsonObject armor = new JsonObject();
                armor.add("helmet", serializeItem(p.getInventory().getHelmet()));
                armor.add("chestplate", serializeItem(p.getInventory().getChestplate()));
                armor.add("leggings", serializeItem(p.getInventory().getLeggings()));
                armor.add("boots", serializeItem(p.getInventory().getBoots()));
                inventory.add("armor", armor);

                JsonArray contents = new JsonArray();
                for (int i = 0; i < 36; i++) {
                    JsonObject slot = serializeItem(p.getInventory().getItem(i));
                    slot.addProperty("slot", i);
                    contents.add(slot);
                }
                inventory.add("contents", contents);
                inventory.addProperty("held_slot", p.getInventory().getHeldItemSlot());

                player.add("inventory", inventory);

                JsonArray enderchest = new JsonArray();
                for (int i = 0; i < p.getEnderChest().getSize(); i++) {
                    JsonObject slot = serializeItem(p.getEnderChest().getItem(i));
                    slot.addProperty("slot", i);
                    enderchest.add(slot);
                }
                player.add("enderchest", enderchest);

                JsonArray effects = new JsonArray();
                for (PotionEffect effect : p.getActivePotionEffects()) {
                    JsonObject e = new JsonObject();
                    e.addProperty("type", effect.getType().getKey().getKey());
                    e.addProperty("amplifier", effect.getAmplifier());
                    e.addProperty("duration_ticks", effect.getDuration());
                    e.addProperty("ambient", effect.isAmbient());
                    e.addProperty("particles", effect.hasParticles());
                    effects.add(e);
                }
                player.add("potion_effects", effects);

                JsonObject session = new JsonObject();
                session.addProperty("first_played", p.getFirstPlayed());
                session.addProperty("last_played", p.getLastLogin());
                session.addProperty("play_time_ticks", p.getStatistic(Statistic.PLAY_ONE_MINUTE));
                session.addProperty("deaths", p.getStatistic(Statistic.DEATHS));
                session.addProperty("player_kills", p.getStatistic(Statistic.PLAYER_KILLS));
                session.addProperty("mob_kills", p.getStatistic(Statistic.MOB_KILLS));
                session.addProperty("damage_dealt", p.getStatistic(Statistic.DAMAGE_DEALT));
                session.addProperty("damage_taken", p.getStatistic(Statistic.DAMAGE_TAKEN));
                session.addProperty("walked_cm", p.getStatistic(Statistic.WALK_ONE_CM));
                player.add("stats", session);

                if (p.getScoreboard().getEntryTeam(p.getName()) != null) {
                    Team team = p.getScoreboard().getEntryTeam(p.getName());
                    player.addProperty("team", team.getName());
                }

                players.add(player);
            }

            root.add("players", players);
            return new Gson().toJson(root);
        }

        return "{\"error\":\"unknown_instruction\"}";
    }

    private JsonObject serializeItem(@Nullable ItemStack item) {
        JsonObject obj = new JsonObject();
        if (item == null || item.getType() == Material.AIR) {
            obj.addProperty("type", "AIR");
            return obj;
        }

        obj.addProperty("type", item.getType().name());
        obj.addProperty("amount", item.getAmount());

        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasDisplayName()) {
                obj.addProperty("display_name",
                        PlainTextComponentSerializer.plainText().serialize(meta.displayName()));
            }
            if (meta.hasLore()) {
                JsonArray lore = new JsonArray();
                meta.lore().forEach(line ->
                        lore.add(PlainTextComponentSerializer.plainText().serialize(line)));
                obj.add("lore", lore);
            }
            obj.addProperty("custom_model_data", meta.hasCustomModelData() ? meta.getCustomModelData() : 0);
            obj.addProperty("unbreakable", meta.isUnbreakable());

            if (!item.getEnchantments().isEmpty()) {
                JsonObject enchants = new JsonObject();
                item.getEnchantments().forEach((ench, lvl) ->
                        enchants.addProperty(ench.getKey().getKey(), lvl));
                obj.add("enchantments", enchants);
            }

            if (meta instanceof Damageable damageable) {
                int damage = damageable.getDamage();
                obj.addProperty("durability", damage);
                obj.addProperty("max_durability", item.getType().getMaxDurability());
            }
        }

        return obj;
    }

    public void stop() {
        running = false;
        socket.close();
    }
}