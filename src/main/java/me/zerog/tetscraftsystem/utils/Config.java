package me.zerog.tetscraftsystem.utils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Config {

    private static File file;
    private static FileConfiguration customFile;


    public static void setup(){
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("TetsCraftSystem").getDataFolder(), "config.yml");
        try{
            if(!file.exists()){
                file.createNewFile();
            }
        }catch (IOException e){}
        customFile = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get(){
        return customFile;
    }

    public static void reload(){
        customFile = YamlConfiguration.loadConfiguration(file);
    }
}
