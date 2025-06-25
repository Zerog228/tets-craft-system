package me.zerog.tetscraftsystem.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class TextComponents {

    public static Component white(String content){
        return Component.newline().content(content).color(NamedTextColor.WHITE);
    }

    public static Component yellow(String content){
        return Component.newline().content(content).color(NamedTextColor.YELLOW);
    }

    public static Component gray(String content){
        return Component.newline().content(content).color(NamedTextColor.GRAY);
    }

    public static Component green(String content){
        return Component.newline().content(content).color(NamedTextColor.GREEN);
    }

    public static Component red(String content){
        return Component.newline().content(content).color(NamedTextColor.RED);
    }
}
