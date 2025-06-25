package me.zerog.tetscraftsystem.inventories;

import me.zerog.tetscraftsystem.utils.*;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import static me.zerog.tetscraftsystem.utils.TextComponents.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CraftMenuInventory implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("crafts") && sender instanceof Player p && (!ConfigData.isDisabled() || p.isOp())){
            openMenu(p);
        }
        return true;
    }

    public static void openMenu(Player p) {
        Inventory inv = Bukkit.createInventory(p, 54, Component.newline().content(ChatColor.DARK_AQUA + "Меню крафта"));
        FillInventory.fillInventory(inv);
        List<String> specializations = ConfigData.getSpecializations();
        for (String spec : specializations) {
            ConfigurationSection section = Config.get().getConfigurationSection(spec);
            int slot = section.getInt("upgrade_slot");
            int max_level = section.getInt("max_level");
            String parent = section.getString("parent");
            int required_parent_level = section.getInt("parent_level");

            //DEBUG
            /*System.out.println(spec+" - spec.");
            if(parent != null){
                System.out.println("Player has parent data - " + PersistentData.hasData(p, parent));
                System.out.println("Required parent level - "+required_parent_level);
                if(PersistentData.hasData(p, parent)){
                    System.out.println("Current parent level - " +PersistentData.getDataInt(p, parent));
                }
            }*/

            //System.out.println("Tried adding "+spec);

            if (parent == null || PersistentData.hasData(p, parent) && PersistentData.getDataInt(p, parent) >= required_parent_level) {
                //System.out.println("Added "+spec);
                Material material = Material.getMaterial(section.getString("item_material"));
                int specialization_level = PersistentData.getDataInt(p, spec);

                if(material == null){
                    material = Material.STICK;
                }

                ItemStack stack = new ItemStack(material, 1);
                if(specialization_level != 0){
                    stack.setAmount(specialization_level);
                }

                ItemMeta meta = stack.getItemMeta();
                String translation = spec;
                ChatColor color = ChatColor.AQUA;
                if (section.contains("translation")) {
                    translation = section.getString("translation");
                }
                if (section.contains("color")) {
                    color = ChatColor.valueOf(section.getString("color"));
                }

                String costFormula = Config.get().getString("level_cost_formula");
                assert costFormula != null;
                costFormula = costFormula.replace("l", String.valueOf(specialization_level));

                List<String> stringLore = section.getStringList("level_"+specialization_level+"_lore");
                List<Component> lore = new ArrayList<>(stringLore.size());
                if(!stringLore.isEmpty()){
                    stringLore.forEach((description) -> {
                        lore.add(yellow(description));
                    });
                    lore.add(Component.newline().content(" "));
                }

                DecimalFormat format = new DecimalFormat("0.00#");
                double current_points = PersistentData.getDataDouble(p, "craft_points");
                try {
                    if (specialization_level < max_level) {
                        double new_cost = EvalClass.eval(costFormula);
                        if(section.contains("additional_cost")) {
                            new_cost += section.getDouble("additional_cost");
                        }
                        lore.add(Component.newline().content(ChatColor.DARK_AQUA + "Ваш опыт - " + ChatColor.WHITE + "" + format.format(current_points)));
                        lore.add(Component.newline().content(ChatColor.AQUA + "Текущий уровень - " + ChatColor.WHITE + "" + specialization_level));
                        lore.add(Component.newline().content(ChatColor.BLUE + "Стоимость улучшения - " + ChatColor.WHITE + new_cost));
                    } else {
                        lore.add(Component.newline().content(ChatColor.DARK_AQUA + "Ваш опыт - " + ChatColor.WHITE + "" + format.format(current_points)));
                        lore.add(Component.newline().content(ChatColor.AQUA + "Достигнут максимальный уровень"));
                        meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
                    }
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    meta.lore(lore);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                meta.displayName(Component.newline().content(color + translation));
                stack.setItemMeta(meta);
                inv.setItem(slot, stack);
                //System.out.println("Added item to "+slot+" slot "+material+" - mat, amount - "+stack.getAmount());
            }
        }
        p.openInventory(inv);
    }
}
