package net.danh.mcoreaddon.mythicdrop;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.drops.DropMetadata;
import io.lumine.mythic.api.drops.ILocationDrop;
import io.lumine.mythic.api.skills.SkillCaster;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.experience.EXPSource;
import net.danh.mcoreaddon.booster.Boosters;
import net.danh.mcoreaddon.listener.PlayerExperienceGainBoosterEvent;
import net.danh.mcoreaddon.utils.Number;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Optional;

public class MythicProfession implements ILocationDrop {

    public static HashMap<String, Double> booster_profession = new HashMap<>();
    public static HashMap<String, Integer> booster_temporary_times_profession = new HashMap<>();
    public static HashMap<String, Double> booster_temporary_multi_profession = new HashMap<>();
    protected final int xp;
    protected final String profession;
    protected final String range;

    public MythicProfession(MythicLineConfig config) {
        xp = config.getInteger(new String[]{"profession-xp", "pxp"}, 1);
        profession = config.getString(new String[]{"profession", "p"}, null);
        range = config.getString(new String[]{"range", "r"}, null);
    }

    public void exp(Player p, Location location) {
        if (xp > 0) {
            if (rangeCheck(p)) {
                if (MMOCore.plugin.professionManager.has(profession)) {
                    if (booster_profession.containsKey(p.getName() + "_" + profession)) {
                        if (booster_profession.get(p.getName() + "_" + profession) > 1d) {
                            Boosters.giveProfessionExp(p, profession, xp, location);
                            PlayerExperienceGainBoosterEvent playerExperienceGainBoosterEvent = new PlayerExperienceGainBoosterEvent(PlayerData.get(p.getUniqueId()), MMOCore.plugin.professionManager.get(profession), xp, EXPSource.SOURCE);
                            Bukkit.getPluginManager().callEvent(playerExperienceGainBoosterEvent);
                        } else {
                            PlayerData.get(p.getUniqueId()).getCollectionSkills().giveExperience(MMOCore.plugin.professionManager.get(profession), xp, EXPSource.SOURCE, location.add(0, 1.5, 0), true);
                            PlayerExperienceGainBoosterEvent playerExperienceGainBoosterEvent = new PlayerExperienceGainBoosterEvent(PlayerData.get(p.getUniqueId()), MMOCore.plugin.professionManager.get(profession), xp, EXPSource.SOURCE);
                            Bukkit.getPluginManager().callEvent(playerExperienceGainBoosterEvent);
                        }
                    } else {
                        PlayerData.get(p.getUniqueId()).getCollectionSkills().giveExperience(MMOCore.plugin.professionManager.get(profession), xp, EXPSource.SOURCE, location.add(0, 1.5, 0), true);
                        PlayerExperienceGainBoosterEvent playerExperienceGainBoosterEvent = new PlayerExperienceGainBoosterEvent(PlayerData.get(p.getUniqueId()), MMOCore.plugin.professionManager.get(profession), xp, EXPSource.SOURCE);
                        Bukkit.getPluginManager().callEvent(playerExperienceGainBoosterEvent);
                    }
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
