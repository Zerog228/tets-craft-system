package me.zerog.tetscraftsystem.utils;

import me.zerog.tetscraftsystem.TetsCraftSystem;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class PersistentData {

    public static boolean hasData(Player p, String key){
        return p.getPersistentDataContainer().has(new NamespacedKey(TetsCraftSystem.pl, key));
    }

    public static void setData(Player p, PersistentDataType dataType, String key, String value){
        PersistentDataContainer container = p.getPersistentDataContainer();
        container.set(new NamespacedKey(TetsCraftSystem.pl, key), dataType, value);
    }

    public static void setData(Player p, PersistentDataType dataType, String key, int value){
        PersistentDataContainer container = p.getPersistentDataContainer();
        container.set(new NamespacedKey(TetsCraftSystem.pl, key), dataType, value);
    }

    public static void setData(Player p, PersistentDataType dataType, String key, double value){
        PersistentDataContainer container = p.getPersistentDataContainer();
        container.set(new NamespacedKey(TetsCraftSystem.pl, key), dataType, value);
    }

    public static int getDataInt(Player p, String key){
        PersistentDataContainer container = p.getPersistentDataContainer();
        return container.get(new NamespacedKey(TetsCraftSystem.pl, key), PersistentDataType.INTEGER);
    }

    public static String getDataString(Player p, String key){
        PersistentDataContainer container = p.getPersistentDataContainer();
        return container.get(new NamespacedKey(TetsCraftSystem.pl, key), PersistentDataType.STRING);
    }

    public static double getDataDouble(Player p, String key){
        PersistentDataContainer container = p.getPersistentDataContainer();
        return container.get(new NamespacedKey(TetsCraftSystem.pl, key), PersistentDataType.DOUBLE);
    }
}
