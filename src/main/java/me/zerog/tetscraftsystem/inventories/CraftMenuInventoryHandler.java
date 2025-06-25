package me.zerog.tetscraftsystem.inventories;

import me.zerog.tetscraftsystem.utils.ConfigData;
import me.zerog.tetscraftsystem.utils.Config;
import me.zerog.tetscraftsystem.utils.EvalClass;
import me.zerog.tetscraftsystem.utils.PersistentData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Objects;

public class CraftMenuInventoryHandler {
    public static void craftInventoryClick(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        Material item = Objects.requireNonNull(e.getCurrentItem()).getType();
        double craft_points = PersistentData.getDataDouble(p, "craft_points");
        List<String> list = ConfigData.getSpecializations();
        for(String specialization : list){
            ConfigurationSection section = Config.get().getConfigurationSection(specialization);
            String parent = section.getString("parent");
            int parent_level = section.getInt("parent_level");
            if(item == Material.getMaterial(section.getString("item_material"))){
                int specialization_level = PersistentData.getDataInt(p, specialization);
                int max_level = section.getInt("max_level");
                if(specialization_level>=max_level){
                    p.sendMessage(ChatColor.RED+ConfigData.max_level);
                    return;
                }
                if(parent != null && PersistentData.getDataInt(p, parent) < parent_level){
                    return;
                }

                String costFormula = Config.get().getString("level_cost_formula");
                costFormula = costFormula.replace("l", String.valueOf(specialization_level));
                try {
                    double result = EvalClass.eval(costFormula);
                    if(section.contains("additional_cost")){
                        result+= section.getDouble("additional_cost");
                    }
                    if(craft_points>=result){
                        String translation = specialization;
                        ChatColor color = ChatColor.AQUA;
                        if(section.contains("translation")){
                            translation = section.getString("translation");
                        }
                        if(section.contains("color")){
                            color = ChatColor.valueOf(section.getString("color"));
                        }
                        PersistentData.setData(p, PersistentDataType.DOUBLE, "craft_points", craft_points-result);
                        PersistentData.setData(p, PersistentDataType.INTEGER, specialization, specialization_level+1);
                        p.sendMessage(color+translation+ChatColor.GREEN+" "+ConfigData.upgraded_to+" "+ChatColor.YELLOW+(specialization_level+1)+ChatColor.GREEN+" "+ConfigData.level+"!");
                    }else {
                        p.sendMessage(ChatColor.RED+ConfigData.dont_have_enough_xp);
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return;
            }
        }
        e.setCancelled(true);
    }
}
