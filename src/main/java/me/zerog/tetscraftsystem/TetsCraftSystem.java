package me.zerog.tetscraftsystem;

import me.zerog.tetscraftsystem.inventories.CraftMenuInventory;
import me.zerog.tetscraftsystem.commands.TetsCraftSystemCommands;
import me.zerog.tetscraftsystem.listeners.Listeners;
import me.zerog.tetscraftsystem.utils.ConfigData;
import me.zerog.tetscraftsystem.utils.Config;
import me.zerog.tetscraftsystem.utils.PersistentData;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class TetsCraftSystem extends JavaPlugin {

    public static Plugin pl;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new Listeners(), this);
        getCommand("crafts").setExecutor(new CraftMenuInventory());
        getCommand("tcs").setExecutor(new TetsCraftSystemCommands());

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        Config.setup();
        Config.get().addDefault("default_upgrade_cost", 2);
        Config.get().addDefault("default_points_per_craft", 0.01);
        Config.get().addDefault("level_cost_formula", "2^l");
        Config.get().options().copyDefaults(true);
        Config.save();

        ConfigData.put();

        pl = this;


        for(Player p : Bukkit.getOnlinePlayers()){
            for(String str : ConfigData.getSpecializations()) {
                if (!p.getPersistentDataContainer().has(new NamespacedKey(TetsCraftSystem.pl, str))) {
                    if(ConfigData.resetOnAdd()){
                        for (String spec : ConfigData.getSpecializations()) {
                            PersistentData.setData(p, PersistentDataType.INTEGER, spec, 0);
                        }
                        PersistentData.setData(p, PersistentDataType.DOUBLE, "craft_points", 1.1);
                        break;
                    }else{
                        PersistentData.setData(p, PersistentDataType.INTEGER, str, 0);
                        if (p.isOp()) {
                            p.sendMessage(ChatColor.GREEN + "Added " + str);
                        }
                    }
                }
            }
            if(!PersistentData.hasData(p, "craft_points")){
                PersistentData.setData(p, PersistentDataType.DOUBLE, "craft_points", 1.1);
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
