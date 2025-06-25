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
    private static boolean hide_unknown;
    private static boolean reset_on_add;
    private static boolean disabled;

    public static void put(){
        section = Config.get().getConfigurationSection("");

        putSpecializationsList();
        putExcludedMaterials();
        putPreciseMaterials();
        putAlwaysGivesExp();
        putAlwaysRestricted();
        putDefaultPointsPerCraft();
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

    private static void putHideUnknown(){
        hide_unknown = Config.get().getBoolean("hide_unknown_branch_messages");
    }

    private static void putResetOnAdd(){
        reset_on_add = Config.get().getBoolean("reset_on_add");
    }

    private static void putDisabled(){
        disabled = Config.get().getBoolean("disabled");
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
