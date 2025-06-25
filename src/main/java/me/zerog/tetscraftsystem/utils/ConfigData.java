package me.zerog.tetscraftsystem.utils;

import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class ConfigData {

    private static ConfigurationSection section;

    private static List<String> specializations_list;
    private static List<String> excluded_materials;
    private static List<String> precise_materials;
    private static List<String> always_gives_exp;
    private static List<String> always_restricted;
    private static double default_points_per_craft;
    private static double starting_points;
    private static boolean hide_unknown;
    private static boolean reset_on_add;
    private static boolean disabled;

    public static String cannot_craft = "";
    public static String cannot_craft2 = "";
    public static String needed_level = "";
    public static String current_level = "";
    public static String current_xp = "";
    public static String upgrade_cost = "";
    public static String you_gained = "";
    public static String points = "";
    public static String craft_menu = "";
    public static String max_level = "";
    public static String upgraded_to = "";
    public static String level = "";
    public static String dont_have_enough_xp = "";
    public static String item_blocked = "";

    public static void put(){
        section = Config.get().getConfigurationSection("");

        putTranslations();
        putSpecializationsList();
        putExcludedMaterials();
        putPreciseMaterials();
        putAlwaysGivesExp();
        putAlwaysRestricted();
        putDefaultPointsPerCraft();
        putStartingPoints();
        putHideUnknown();
        putResetOnAdd();
        putDisabled();
    }

    private static void putSpecializationsList(){
        List<String> list = new ArrayList<>(section.getKeys(false));
        list.remove("default_upgrade_cost");
        list.remove("default_points_per_craft");
        list.remove("level_cost_formula");
        list.remove("excluded_materials");
        list.remove("precise_materials");
        list.remove("always_restricted");
        list.remove("always_gives_exp");
        list.remove("reset_on_add");
        list.remove("hide_unknown_branch_messages");
        list.remove("disabled");
        list.remove("starting_points");

        //Translations
        list.remove("cannot_craft");
        list.remove("cannot_craft2");
        list.remove("needed_level");
        list.remove("current_level");
        list.remove("current_xp");
        list.remove("upgrade_cost");
        list.remove("you_gained");
        list.remove("points");
        list.remove("craft_menu");
        list.remove("max_level");
        list.remove("upgraded_to");
        list.remove("level");
        list.remove("dont_have_enough_xp");
        list.remove("item_blocked");
        specializations_list = list;
    }

    private static void putExcludedMaterials(){
        excluded_materials = Config.get().getStringList("excluded_materials");
    }

    private static void putPreciseMaterials(){
        precise_materials = Config.get().getStringList("precise_materials");
    }

    private static void putAlwaysGivesExp(){
        always_gives_exp = Config.get().getStringList("always_gives_exp");
    }

    private static void putAlwaysRestricted(){
        always_restricted = Config.get().getStringList("always_restricted");
    }

    private static void putDefaultPointsPerCraft(){
        default_points_per_craft = Config.get().getDouble("default_points_per_craft");
    }

    private static void putStartingPoints(){
        starting_points = Config.get().getDouble("starting_points");
    }

    private static void putHideUnknown(){
        hide_unknown = Config.get().getBoolean("hide_unknown_branch_messages");
    }

    private static void putResetOnAdd(){
        reset_on_add = Config.get().getBoolean("reset_on_add");
    }

    private static void putDisabled(){
        disabled = Config.get().getBoolean("disabled");
    }

    private static void putTranslations(){
        section = Config.get().getConfigurationSection("");
        cannot_craft = section.getString("cannot_craft", "You cannot craft this");
        cannot_craft2 = section.getString("cannot_craft2", "You cannot craft this. You need to unlock previous branches first!");
        needed_level = section.getString("needed_level", "Needed level");
        current_level = section.getString("current_level", "Current level");
        current_xp = section.getString("current_xp", "Current xp");
        upgrade_cost = section.getString("upgrade_cost", "Upgrade cost");
        you_gained = section.getString("you_gained", "You gained");
        points = section.getString("points", "points");
        craft_menu = section.getString("craft_menu", "Craft menu");
        max_level = section.getString("max_level", "Max level");
        upgraded_to = section.getString("upgraded_to", "Upgraded to");
        level = section.getString("level", "level");
        dont_have_enough_xp = section.getString("dont_have_enough_xp", "You don't have enough xp to do that!");
        item_blocked = section.getString("item_blocked", "This item is blocked!");
    }

    public static List<String> getSpecializations(){
        return specializations_list;
    }

    public static List<String> getExcludedMaterials(){
        return excluded_materials;
    }

    public static List<String> getPreciseMaterials(){
        return precise_materials;
    }

    public static List<String> getAlwaysGivesExp(){
        return always_gives_exp;
    }

    public static List<String> getAlwaysRestricted(){
        return always_restricted;
    }

    public static double getDefaultPointsPerCraft(){
        return default_points_per_craft;
    }

    public static double getStartingPoints() {
        return starting_points;
    }

    public static boolean getHideUnknown(){
        return hide_unknown;
    }

    public static boolean resetOnAdd(){
        return reset_on_add;
    }

    public static boolean isDisabled(){
        return disabled;
    }
}
