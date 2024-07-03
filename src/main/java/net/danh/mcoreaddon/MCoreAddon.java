package net.danh.mcoreaddon;

import net.danh.mcoreaddon.cmd.MCA_CMD;
import net.danh.mcoreaddon.events.GainEXP;
import net.danh.mcoreaddon.events.JoinQuit;
import net.danh.mcoreaddon.mythicdrop.MythicReg;
import net.danh.mcoreaddon.playerData.PlayerData;
import net.danh.mcoreaddon.utils.Files;
import net.danh.mcoreaddon.utils.LicenseKey;
import net.xconfig.bukkit.model.SimpleConfigurationManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class MCoreAddon extends JavaPlugin {

    private static MCoreAddon mCoreAddon;

    public static MCoreAddon getMCore() {
        return mCoreAddon;
    }

    @Override
    public void onEnable() {
        mCoreAddon = this;
        SimpleConfigurationManager.register(mCoreAddon);
        Files.createFiles();
        if (!LicenseKey.checkKey()) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        registerEvents(new JoinQuit(), new MythicReg(), new GainEXP());
        new MCA_CMD();
    }

    @Override
    public void onDisable() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            new PlayerData(p).saveData();
        }
        Files.saveFile();
    }

    private void registerEvents(Listener... listeners) {
        Arrays.asList(listeners).forEach(listener -> getServer().getPluginManager().registerEvents(listener, mCoreAddon));
    }
}
