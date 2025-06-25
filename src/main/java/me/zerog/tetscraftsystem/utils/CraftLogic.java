package me.zerog.tetscraftsystem.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.text.DecimalFormat;
import java.util.List;

public class CraftLogic {

    private static DecimalFormat format = new DecimalFormat("0.00#");

    public static void isAllowed(CraftItemEvent e){
        if(!ConfigData.isDisabled()){
            Player p = (Player) e.getWhoClicked();
            Material material = e.getRecipe().getResult().getType();
            int innerMultiplicative = 1;
            double additional_multiplier = 1;

            List<String> precise_blocks = ConfigData.getPreciseMaterials();

            //RESTRICTED CHECK
            List<String> always_restricted = ConfigData.getAlwaysRestricted();
            if (always_restricted.contains(material.toString()) && !precise_blocks.contains(material.toString())) {
                p.sendMessage(TextComponents.red("Этот предмет крафтить ЗАПРЕЩЕНО!"));
                e.setCancelled(true);
            }
            for (String restricted : always_restricted) {
                if (material.toString().contains(restricted) && !precise_blocks.contains(material.toString())) {
                    p.sendMessage(TextComponents.red("Этот предмет крафтить ЗАПРЕЩЕНО!"));
                    e.setCancelled(true);
                    return;
                }
            }

            List<String> specializations_list = ConfigData.getSpecializations();
            List<String> excluded = ConfigData.getExcludedMaterials();
            List<String> always_exp = ConfigData.getAlwaysGivesExp();
            double default_points = ConfigData.getDefaultPointsPerCraft();
            for (String alwaysGrantsExp : always_exp) {
                if (material.toString().contains(alwaysGrantsExp)) {
                    checkClick(e, 1.0, default_points, 1, p);
                    return;
                }
            }
            for (String excludedFromExp : excluded) {
                if (material.toString().contains(excludedFromExp)) {
                    innerMultiplicative = 0;
                }
            }
            for (String str : specializations_list) {
                ConfigurationSection section = Config.get().getConfigurationSection(str);
                int levels = section.getInt("max_level");
                for (int i = 1; i < levels + 1; i++) {
                    List<String> allowed_block = section.getStringList("level_" + i);

                    checkAllowed:
                    for (String block : allowed_block) {
                        try{
                            additional_multiplier = Double.parseDouble(block.split(":")[1]);
                            block = block.split(":")[0];
                        }catch (Exception ignored){}

                        if (material.toString().contains(block)) {
                            int level = PersistentData.getDataInt(p, str);
                            String translation = str;
                            ChatColor color = ChatColor.AQUA;
                            if (section.getString("translation") != null) {
                                translation = section.getString("translation");
                            }
                            if (section.getString("color") != null) {
                                color = ChatColor.valueOf(section.getString("color"));
                            }

                            for (String precise : precise_blocks) {
                                if (material.toString().contains(precise)) {
                                    if (block.equals(precise)) {
                                        if (level >= i) {
                                            double points_multiplier = section.getDouble("points_multiplier");
                                            checkClick(e, i * innerMultiplicative * additional_multiplier, default_points, points_multiplier, p);
                                        } else {
                                            if (ConfigData.getHideUnknown() && section.getString("parent") != null && PersistentData.hasData(p, section.getString("parent")) && PersistentData.getDataInt(p, section.getString("parent")) < section.getInt("parent_level")) {
                                                p.sendMessage(ChatColor.RED + "Вы не можете это крафтить! Вам необходимо улучшить ветви крафтов!");
                                                e.setCancelled(true);
                                                return;
                                            }

                                            p.sendMessage(ChatColor.RED + "Вы не можете это крафтить!");
                                            p.sendMessage(ChatColor.RED + "Необходимый уровень - " + color + translation + " " + ChatColor.WHITE + i + color + "!");
                                            e.setCancelled(true);
                                        }
                                        return;
                                    } else {
                                        continue checkAllowed;
                                    }
                                }
                            }

                            if (level >= i) {
                                double points_multiplier = section.getDouble("points_multiplier");
                                checkClick(e, i * innerMultiplicative * additional_multiplier, default_points, points_multiplier, p);
                            } else {
                                if (ConfigData.getHideUnknown() && section.getString("parent") != null && PersistentData.hasData(p, section.getString("parent")) && PersistentData.getDataInt(p, section.getString("parent")) < section.getInt("parent_level")) {
                                    p.sendMessage(ChatColor.RED + "Вы не можете это крафтить! Вам необходимо улучшить ветви крафтов!");
                                    e.setCancelled(true);
                                    return;
                                }
                                p.sendMessage(ChatColor.RED + "Вы не можете это крафтить!");
                                p.sendMessage(ChatColor.RED + "Необходимый уровень - " + color + translation + " " + ChatColor.WHITE + i + color + "!");
                                e.setCancelled(true);
                            }
                            return;
                        }
                    }
                }
            }
        }
    }

    private static void checkClick(CraftItemEvent e, double level_multiplier, double default_points, double points_multiplier, Player p) {

        ItemStack result = e.getRecipe().getResult();
        CraftingInventory craftingInventory = e.getInventory();
        Inventory inventory = p.getInventory();

        if(e.getClick().isShiftClick()){
            //Count real crafted items
            int minCraftAmount = 65;
            for (ItemStack craftingStack : craftingInventory.getMatrix()){
                if(craftingStack != null && craftingStack.getType() != Material.AIR && craftingStack.getAmount() < minCraftAmount){
                    minCraftAmount = craftingStack.getAmount();
                }
            }
            int craftedAmount = result.getAmount() * minCraftAmount;

            //Count free space
            int freeSpace = 0;
            for(ItemStack stack : inventory.getStorageContents()){
                if(stack == null || stack.getType() == Material.AIR){
                    freeSpace+=64;
                }else{
                    if (stack.asOne().equals(result.asOne()) && stack.getAmount() < 64) {
                        freeSpace += 64 - stack.getAmount();
                    }
                }
            }

            if(freeSpace > 0){
                if (craftedAmount <= freeSpace) {
                    addPoints(p, level_multiplier * default_points * points_multiplier * craftedAmount);
                } else {
                    addPoints(p, level_multiplier * default_points * points_multiplier * (freeSpace % result.getAmount() == 0 ? freeSpace : freeSpace % result.getAmount() * result.getAmount()));
                }
            }
        }else {
            if(e.getCursor().getAmount() <= 64 - result.getAmount() && (e.getCursor().asOne().equals(result.asOne()) || e.getCursor().getType() == Material.AIR)){
                addPoints(p, level_multiplier * default_points * points_multiplier * result.getAmount());
            }
        }
    }

    private static void addPoints(Player p, double points){
        double current_points = PersistentData.getDataDouble(p, "craft_points");
        PersistentData.setData(p, PersistentDataType.DOUBLE, "craft_points", current_points + points);
        p.sendActionBar(Component.newline().content(ChatColor.YELLOW + "Вы получили " + ChatColor.GREEN + format.format(points) + ChatColor.YELLOW + " опыта крафта!"));
    }
}
