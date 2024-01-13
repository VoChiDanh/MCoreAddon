package net.danh.mcoreaddon.mythicdrop;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.drops.DropMetadata;
import io.lumine.mythic.api.drops.ILocationDrop;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.experience.EXPSource;
import net.danh.mcoreaddon.listener.PlayerExperienceGainBoosterEvent;
import net.danh.mcoreaddon.utils.Number;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Optional;

public class MythicXP implements ILocationDrop {

    protected final double xp;
    protected final String range;
    public static HashMap<Player, Double> booster = new HashMap<>();

    public MythicXP(MythicLineConfig config) {
        xp = config.getDouble(new String[]{"mmocore-xp", "xp"}, 0d);
        range = config.getString(new String[]{"range", "r"}, null);
    }

    public void exp(Player player) {
        if (xp > 0d) {
            if (booster.containsKey(player)) {
                if (booster.get(player) > 0d) {
                    if (rangeCheck(player)) {
                        double exp = xp * booster.get(player);
                        PlayerData.get(player).giveExperience(exp, EXPSource.SOURCE);
                        PlayerExperienceGainBoosterEvent event = new PlayerExperienceGainBoosterEvent(PlayerData.get(player.getUniqueId()), exp, EXPSource.SOURCE);
                        Bukkit.getPluginManager().callEvent(event);
                    }
                } else {
                    PlayerData.get(player.getUniqueId()).giveExperience(xp, EXPSource.SOURCE);
                }
            } else {
                PlayerData.get(player.getUniqueId()).giveExperience(xp, EXPSource.SOURCE);
            }
        }
    }

    public boolean rangeCheck(Player p) {
        if (range != null) {
            if (range.contains("-")) {
                int min_level = Number.getInteger(range.split("-")[0]);
                int max_level = Number.getInteger(range.split("-")[1]);
                int player_level = PlayerData.get(p.getUniqueId()).getLevel();
                return (player_level >= min_level) && (player_level <= max_level);
            } else if (range.contains(">=")) {
                int level = Number.getInteger(range.replace(">=", ""));
                int player_level = PlayerData.get(p.getUniqueId()).getLevel();
                return player_level >= level;
            } else if (range.contains(">")) {
                int level = Number.getInteger(range.replace(">", ""));
                int player_level = PlayerData.get(p.getUniqueId()).getLevel();
                return player_level > level;
            } else if (range.contains("<=")) {
                int level = Number.getInteger(range.replace("<=", ""));
                int player_level = PlayerData.get(p.getUniqueId()).getLevel();
                return player_level <= level;
            } else if (range.contains("<")) {
                int level = Number.getInteger(range.replace("<", ""));
                int player_level = PlayerData.get(p.getUniqueId()).getLevel();
                return player_level < level;
            } else return false;
        } else return true;
    }

    @Override
    public void drop(AbstractLocation abstractLocation, DropMetadata dropMetadata, double v) {
        Optional<AbstractEntity> abstractEntity = dropMetadata.getCause();
        if (abstractEntity.isPresent()) {
            Player p = Bukkit.getPlayer(abstractEntity.get().getName());
            if (p != null) {
                exp(p);
            }
        }
    }
}
