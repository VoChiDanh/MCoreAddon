package net.danh.mcoreaddon.mythicdrop;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.drops.DropMetadata;
import io.lumine.mythic.api.drops.ILocationDrop;
import io.lumine.mythic.api.skills.SkillCaster;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.experience.EXPSource;
import net.danh.mcoreaddon.booster.Boosters;
import net.danh.mcoreaddon.utils.Number;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Optional;

public class MythicXP implements ILocationDrop {

    public static HashMap<Player, Double> booster = new HashMap<>();
    public static HashMap<Player, Integer> booster_temporary_times = new HashMap<>();
    public static HashMap<Player, Double> booster_temporary_multi = new HashMap<>();
    protected final int xp;
    protected final String range;

    public MythicXP(MythicLineConfig config) {
        xp = config.getInteger(new String[]{"mmocore-xp", "xp"}, 1);
        range = config.getString(new String[]{"range", "r"}, null);
    }

    public void exp(Player p, Location location) {
        if (xp > 0) {
            if (rangeCheck(p)) {
                if (booster.containsKey(p)) {
                    if (booster.get(p) > 1d) {
                        Boosters.giveExp(p, xp, location);
                    } else {
                        PlayerData.get(p.getUniqueId()).giveExperience(xp, EXPSource.SOURCE, location.add(0, 1.5, 0), true);
                    }
                } else {
                    PlayerData.get(p.getUniqueId()).giveExperience(xp, EXPSource.SOURCE, location.add(0, 1.5, 0), true);
                }
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
        Optional<SkillCaster> skillCaster = dropMetadata.getDropper();
        if (abstractEntity.isPresent() && skillCaster.isPresent()) {
            Player p = Bukkit.getPlayer(abstractEntity.get().getName());
            Location location = new Location(Bukkit.getWorld(skillCaster.get().getLocation().getWorld().getName()), skillCaster.get().getLocation().getBlockX(), skillCaster.get().getLocation().getBlockY(), skillCaster.get().getLocation().getBlockZ());
            if (p != null) {
                exp(p, location);
            }
        }
    }
}
