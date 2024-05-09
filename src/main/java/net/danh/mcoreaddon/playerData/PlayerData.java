package net.danh.mcoreaddon.playerData;

import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.experience.Profession;
import net.danh.mcoreaddon.MCoreAddon;
import net.danh.mcoreaddon.mythicdrop.MythicProfession;
import net.danh.mcoreaddon.mythicdrop.MythicXP;
import net.xconfig.bukkit.model.SimpleConfigurationManager;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class PlayerData {

    private final Player p;
    private File playerdataFile;
    private FileConfiguration playerdata;

    public PlayerData(Player p) {
        this.p = p;
    }

    public void loadData() {
        create();
        MythicXP.booster.put(p, Math.max(1.0, get().getDouble("booster.permanently")));
        MythicXP.booster_temporary_times.put(p, Math.max(0, get().getInt("booster.temporary.times")));
        MythicXP.booster_temporary_multi.put(p, Math.max(1.0, get().getDouble("booster.temporary.multi")));
        for (Profession profession : MMOCore.plugin.professionManager.getAll()) {
            MythicProfession.booster_profession.put(p.getName() + "_" + profession.getId(), Math.max(1.0, get().getDouble("booster_profession." + profession.getId() + ".permanently")));
            MythicProfession.booster_temporary_times_profession.put(p.getName() + "_" + profession.getId(), Math.max(0, get().getInt("booster_profession." + profession.getId() + ".temporary.times")));
            MythicProfession.booster_temporary_multi_profession.put(p.getName() + "_" + profession.getId(), Math.max(1.0, get().getDouble("booster_profession." + profession.getId() + ".temporary.multi")));
        }
    }

    public void saveData() {
        create();
        get().set("name", p.getName());
        get().set("booster.permanently", MythicXP.booster.get(p));
        get().set("booster.temporary.times", MythicXP.booster_temporary_times.get(p));
        get().set("booster.temporary.multi", MythicXP.booster_temporary_multi.get(p));
        for (Profession profession : MMOCore.plugin.professionManager.getAll()) {
            get().set("booster_profession." + profession.getId() + ".permanently", MythicProfession.booster_profession.get(p.getName() + "_" + profession.getId()));
            get().set("booster_profession." + profession.getId() + ".temporary.times", MythicProfession.booster_temporary_times_profession.get(p.getName() + "_" + profession.getId()));
            get().set("booster_profession." + profession.getId() + ".temporary.multi", MythicProfession.booster_temporary_multi_profession.get(p.getName() + "_" + profession.getId()));
        }
        save();
        reload();
    }

    private String getFileName() {
        return "playerData/" + p.getName() + "_" + p.getUniqueId() + ".yml";
    }

    public Player getPlayer() {
        return p;
    }

    public void create() {
        playerdataFile = new File(MCoreAddon.getMCore().getDataFolder(), getFileName());
        if (!playerdataFile.exists()) SimpleConfigurationManager.get().build("", true, getFileName());
        playerdata = new YamlConfiguration();

        try {
            playerdata.load(playerdataFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration get() {
        return playerdata;
    }

    public void reload() {
        playerdata = YamlConfiguration.loadConfiguration(playerdataFile);
    }

    public void save() {
        try {
            playerdata.save(playerdataFile);
        } catch (IOException ignored) {
        }
    }
}
