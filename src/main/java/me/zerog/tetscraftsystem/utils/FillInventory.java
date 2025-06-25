package me.zerog.tetscraftsystem.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FillInventory {
    public static void fillInventory(Inventory inv){
        ItemStack stack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta meta = stack.getItemMeta();
        meta.displayName(Component.newline().content(""));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ITEM_SPECIFICS);
        stack.setItemMeta(meta);
        for(int i = inv.getSize(); i > 0; i--){
            inv.setItem(i-1, stack);
        }
    }
}
