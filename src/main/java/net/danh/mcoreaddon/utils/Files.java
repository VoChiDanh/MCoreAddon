package net.danh.mcoreaddon.utils;

import net.xconfig.bukkit.model.SimpleConfigurationManager;
import org.bukkit.configuration.file.FileConfiguration;

public class Files {

    public static FileConfiguration getConfig() {
        return SimpleConfigurationManager.get().get("config.yml");
    }

    public static FileConfiguration getMessage() {
        return SimpleConfigurationManager.get().get("message.yml");
    }

    public static void createFiles() {
        SimpleConfigurationManager.get().build("", false, "config.yml", "message.yml", "playerData/example_data.yml");

    }

    public static void reloadFile() {
        SimpleConfigurationManager.get().reload("config.yml", "message.yml", "playerData/example_data.yml");
    }

    public static void saveFile() {
        SimpleConfigurationManager.get().save("config.yml", "message.yml", "playerData/example_data.yml");
    }
}
