package me.zerog.tetscraftsystem.commands;

import me.zerog.tetscraftsystem.TetsCraftSystem;
import me.zerog.tetscraftsystem.utils.ConfigData;
import me.zerog.tetscraftsystem.utils.Config;
import me.zerog.tetscraftsystem.utils.PersistentData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static me.zerog.tetscraftsystem.utils.TextComponents.*;

import java.util.List;

public class TetsCraftSystemCommands implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("tcs")){
            if(args.length > 0 && !args[0].isEmpty()) {
                if (args[0].equals("reload")) {
                    Config.reload();

                    ConfigData.put();

                    for (Player p : Bukkit.getOnlinePlayers()) {
                        for (String str : ConfigData.getSpecializations()) {
                            if (!p.getPersistentDataContainer().has(new NamespacedKey(TetsCraftSystem.pl, str))) {
                                PersistentData.setData(p, PersistentDataType.INTEGER, str, 0);
                                p.sendMessage(ChatColor.GREEN + "Added " + str);
                            }
                        }
                        if (!PersistentData.hasData(p, "craft_points")) {
                            PersistentData.setData(p, PersistentDataType.DOUBLE, "craft_points", 1.1);
                        }
                    }
                    sender.sendMessage("Reloaded successfully!");
                }
                if (args[0].equals("resetSpecs")) {
                    if(args.length > 1){
                        if (args[1].isEmpty()) {
                            if (sender instanceof Player p) {
                                for (String str : ConfigData.getSpecializations()) {
                                    PersistentData.setData(p, PersistentDataType.INTEGER, str, 0);
                                    p.sendMessage(green("Reseted specializations " + str));
                                }
                                PersistentData.setData(p, PersistentDataType.DOUBLE, "craft_points", 1.1);
                            } else {
                                sender.sendMessage(red("You are not online player!)"));
                            }
                        } else {
                            Player p = Bukkit.getPlayer(args[1]);
                            if (p != null) {
                                for (String str : ConfigData.getSpecializations()) {
                                    PersistentData.setData(p, PersistentDataType.INTEGER, str, 0);
                                    sender.sendMessage(green("Reseted " + str + " for " + p.getName()));
                                }
                                PersistentData.setData(p, PersistentDataType.DOUBLE, "craft_points", 1.1);
                            } else {
                                sender.sendMessage(red("Player " + args[1] + " is offline or does not exists!"));
                            }
                        }
                    }
                }
                if (args[0].equals("getInfo")) {
                    if(args.length > 1){
                        if (args[1].isEmpty()) {
                            if (sender instanceof Player p) {
                                p.sendMessage(green("==== You (" + p.getName() + ") ===="));
                                p.sendMessage(yellow("Craft points: " + PersistentData.getDataDouble(p, "craft_points")));
                                for (String spec : ConfigData.getSpecializations()) {
                                    p.sendMessage(yellow("---- " + spec + " ----"));
                                    p.sendMessage(yellow("Has spec - " + PersistentData.hasData(p, spec)));
                                    if (PersistentData.hasData(p, spec)) {
                                        p.sendMessage(yellow("Level: " + PersistentData.getDataInt(p, spec)));
                                    }
                                    p.sendMessage(yellow("-----------------------"));
                                }
                            } else {
                                sender.sendMessage(red("You are not online player!"));
                            }
                        } else {
                            Player p = Bukkit.getPlayer(args[1]);
                            if (p != null) {
                                sender.sendMessage(green("==== " + p.getName() + " ===="));
                                sender.sendMessage(yellow("Craft points: " + PersistentData.getDataDouble(p, "craft_points")));
                                for (String spec : ConfigData.getSpecializations()) {
                                    sender.sendMessage(yellow("---- " + spec + " ----"));
                                    sender.sendMessage(yellow("Has spec - " + PersistentData.hasData(p, spec)));
                                    if (PersistentData.hasData(p, spec)) {
                                        sender.sendMessage(yellow("Level: " + PersistentData.getDataInt(p, spec)));
                                    }
                                    sender.sendMessage(yellow("-----------------------"));
                                }
                            } else {
                                sender.sendMessage(red("Player " + args[1] + " is offline or does not exists!"));
                            }
                        }
                    }
                }
                if (args[0].equals("getPoints")) {
                    if(args.length > 1){
                        if (args[1].isEmpty()) {
                            if (sender instanceof Player p) {
                                p.sendMessage(yellow("Craft points: " + PersistentData.getDataDouble(p, "craft_points")));
                            } else {
                                sender.sendMessage(red("You are not online player!"));
                            }
                        } else {
                            Player p = Bukkit.getPlayer(args[1]);
                            if (p != null) {
                                sender.sendMessage(yellow("Craft points: " + PersistentData.getDataDouble(p, "craft_points")));
                            } else {
                                sender.sendMessage(red("Player " + args[1] + " is offline or does not exists!"));
                            }
                        }
                    }
                }
                if (args[0].equals("setPoints")) {
                    if(args.length > 1){
                        if (args[1].isEmpty() && args[2].isEmpty()) {
                            sender.sendMessage("Command use: /setPoints [target] [amount]");
                        } else {
                            Player p = Bukkit.getPlayer(args[1]);
                            if (p != null) {
                                try {
                                    double new_points = Double.parseDouble(args[2]);
                                    PersistentData.setData(p, PersistentDataType.DOUBLE, "craft_points", new_points);
                                    sender.sendMessage(green("Successfully set " + p.getName() + "'s craft points to " + new_points + "!"));
                                } catch (Exception ignored) {
                                    sender.sendMessage(red("Unexpected value! use format \"#.##\""));
                                }
                            } else {
                                sender.sendMessage(red("Player " + args[1] + " is offline or does not exists!"));
                            }
                        }
                    }
                }
                if (args[0].equals("addPoints")) {
                    if(args.length > 1){
                        if (args[1].isEmpty() && args[2].isEmpty()) {
                            sender.sendMessage(yellow("Command use: /addPoints [target] [amount]"));
                        } else {
                            Player p = Bukkit.getPlayer(args[1]);
                            if (p != null) {
                                try {
                                    double new_points = Double.parseDouble(args[2]);
                                    double old_points = PersistentData.getDataDouble(p, "craft_points");
                                    PersistentData.setData(p, PersistentDataType.DOUBLE, "craft_points", new_points + old_points);
                                    sender.sendMessage(green("Successfully set " + p.getName() + "'s craft points to " + (new_points + old_points) + "! Was - " + old_points + ". Added - " + new_points + "!"));
                                } catch (Exception ignored) {
                                    sender.sendMessage(red("Unexpected value! use format \"#.##\""));
                                }
                            } else {
                                sender.sendMessage(red("Player " + args[1] + " is offline or does not exists!"));
                            }
                        }
                    }
                }
            } else {
                sender.sendMessage(green("Admin commands available: "));
                sender.sendMessage(yellow("/reload - reloads plugins configs"));
                sender.sendMessage(yellow("/resetSpecs [target] - resets specializations for selected player/you"));
                sender.sendMessage(yellow("/getInfo [target] - gets full info of selected player/you"));
                sender.sendMessage(yellow("/getPoints [target] - gets points of selected player/you"));
                sender.sendMessage(yellow("/setPoints [target] [amount](double) - sets points of selected player"));
                sender.sendMessage(yellow("/addPoints [target] [amount](double) - gets points of selected player"));
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args[0].isEmpty()){
            return List.of("reload", "resetSpecs", "getInfo", "getPoints", "setPoints", "addPoints");
        }
        return null;
    }
}
