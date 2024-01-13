package net.danh.mcoreaddon.playerData;

import net.danh.mcoreaddon.mythicdrop.MythicXP;
import net.xconfig.bukkit.model.SimpleConfigurationManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class PlayerData {

    private final Player p;

    public PlayerData(Player p) {
        this.p = p;
    }

    public void loadData() {
        SimpleConfigurationManager.get().build("", true, getFileName());
        FileConfiguration fileConfiguration = SimpleConfigurationManager.get().get(getFileName());
        if (fileConfiguration == null) {
            MythicXP.booster.put(p, 1.0);
        } else {
            MythicXP.booster.put(p, Math.max(1.0, fileConfiguration.getDouble("booster")));
        }
    }

    public void saveData() {
        FileConfiguration fileConfiguration = SimpleConfigurationManager.get().get(getFileName());
        fileConfiguration.set("name", p.getName());
        fileConfiguration.set("booster", MythicXP.booster.get(p));
        SimpleConfigurationManager.get().save(getFileName());
    }

    private String getFileName() {
        return "playerData/" + p.getName() + "_" + p.getUniqueId() + ".yml";
    }

    public Player getPlayer() {
        return p;
    }
}
