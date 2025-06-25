package me.zerog.tetscraftsystem.listeners;

import me.zerog.tetscraftsystem.TetsCraftSystem;
import me.zerog.tetscraftsystem.inventories.CraftMenuInventory;
import me.zerog.tetscraftsystem.inventories.CraftMenuInventoryHandler;
import me.zerog.tetscraftsystem.utils.ConfigData;
import me.zerog.tetscraftsystem.utils.CraftLogic;
import me.zerog.tetscraftsystem.utils.PersistentData;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

import static me.zerog.tetscraftsystem.utils.TextComponents.green;

public class Listeners implements Listener {

    private static List<String> specializations = ConfigData.getSpecializations();

    @EventHandler
    private static void inventoryClickEvent(InventoryClickEvent e){
        if (InventoryAction.NOTHING != e.getAction() && e.getView().title().equals(Component.newline().content(ChatColor.DARK_AQUA+ConfigData.craft_menu))) {
            CraftMenuInventoryHandler.craftInventoryClick(e);
            CraftMenuInventory.openMenu((Player) e.getView().getPlayer());
            e.setCancelled(true);
        }
    }

    @EventHandler
    private static void playerJoinEvent(PlayerJoinEvent e){
        Player p = e.getPlayer();
        for(String str : ConfigData.getSpecializations()) {
            if (!p.getPersistentDataContainer().has(new NamespacedKey(TetsCraftSystem.pl, str))) {
                if(ConfigData.resetOnAdd()){
                    for (String spec : ConfigData.getSpecializations()) {
                        PersistentData.setData(p, PersistentDataType.INTEGER, spec, 0);
                    }
                    PersistentData.setData(p, PersistentDataType.DOUBLE, "craft_points", 1.1);
                    break;

                }else {
                    PersistentData.setData(p, PersistentDataType.INTEGER, str, 0);
                    if(p.isOp()){
                        p.sendMessage(ChatColor.GREEN+"Added "+str);
                    }
                }
            }
        }
        if(!PersistentData.hasData(p, "craft_points")){
            PersistentData.setData(p, PersistentDataType.DOUBLE, "craft_points", 1.1);
        }
    }

    @EventHandler
    private static void craftItemEvent(CraftItemEvent e){
        CraftLogic.isAllowed(e);
    }
}
