package net.danh.mcoreaddon.mythicdrop;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.drops.DropMetadata;
import io.lumine.mythic.api.drops.ILocationDrop;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.experience.EXPSource;
import net.danh.mcoreaddon.listener.PlayerExperienceGainBoosterEvent;
import net.danh.mcoreaddon.utils.Files;
import net.danh.mcoreaddon.utils.Number;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

public class MythicXP implements ILocationDrop {

    public static HashMap<Player, Double> booster = new HashMap<>();
    public static HashMap<Player, Integer> booster_temporary_times = new HashMap<>();
    public static HashMap<Player, Double> booster_temporary_multi = new HashMap<>();
    protected final double xp;
    protected final String range;

    public MythicXP(MythicLineConfig config) {
        xp = config.getDouble(new String[]{"mmocore-xp", "xp"}, 0d);
        range = config.getString(new String[]{"range", "r"}, null);
    }

    public void exp(Player p) {
        if (xp > 0d) {
            if (rangeCheck(p)) {
                if (booster.containsKey(p)) {
                    if (booster.get(p) > 1d) {
                        switch (getBoosterMode()) {
                            case PLUS -> {
                                if (booster_temporary_multi.get(p) > 1d) {
                                    double exp = Double.parseDouble(new DecimalFormat("#.##").format(xp * (booster.get(p) + booster_temporary_multi.get(p))).replace(",", "."));
                                    PlayerData.get(p).giveExperience(exp, EXPSource.SOURCE);
                                    PlayerExperienceGainBoosterEvent event = new PlayerExperienceGainBoosterEvent(PlayerData.get(p.getUniqueId()), exp, EXPSource.SOURCE);
                                    Bukkit.getPluginManager().callEvent(event);
                                } else {
                                    double exp = Double.parseDouble(new DecimalFormat("#.##").format(xp * booster.get(p)).replace(",", "."));
                                    PlayerData.get(p).giveExperience(exp, EXPSource.SOURCE);
                                    PlayerExperienceGainBoosterEvent event = new PlayerExperienceGainBoosterEvent(PlayerData.get(p.getUniqueId()), exp, EXPSource.SOURCE);
                                    Bukkit.getPluginManager().callEvent(event);
                                }
                            }
                            case ADD -> {
                                if (booster_temporary_multi.get(p) > 1d) {
                                    double exp = Double.parseDouble(new DecimalFormat("#.##").format(xp * booster.get(p) + xp * booster_temporary_multi.get(p)).replace(",", "."));
                                    PlayerData.get(p).giveExperience(exp, EXPSource.SOURCE);
                                    PlayerExperienceGainBoosterEvent event = new PlayerExperienceGainBoosterEvent(PlayerData.get(p.getUniqueId()), exp, EXPSource.SOURCE);
                                    Bukkit.getPluginManager().callEvent(event);
                                } else {
                                    double exp = Double.parseDouble(new DecimalFormat("#.##").format(xp * booster.get(p)).replace(",", "."));
                                    PlayerData.get(p).giveExperience(exp, EXPSource.SOURCE);
                                    PlayerExperienceGainBoosterEvent event = new PlayerExperienceGainBoosterEvent(PlayerData.get(p.getUniqueId()), exp, EXPSource.SOURCE);
                                    Bukkit.getPluginManager().callEvent(event);
                                }
                            }
                            case HIGHER -> {
                                if (booster_temporary_multi.get(p) > 1d) {
                                    double exp = Double.parseDouble(new DecimalFormat("#.##").format(xp * Math.max(booster.get(p), booster_temporary_multi.get(p))).replace(",", "."));
                                    PlayerData.get(p).giveExperience(exp, EXPSource.SOURCE);
                                    PlayerExperienceGainBoosterEvent event = new PlayerExperienceGainBoosterEvent(PlayerData.get(p.getUniqueId()), exp, EXPSource.SOURCE);
                                    Bukkit.getPluginManager().callEvent(event);
                                } else {
                                    double exp = Double.parseDouble(new DecimalFormat("#.##").format(xp * booster.get(p)).replace(",", "."));
                                    PlayerData.get(p).giveExperience(exp, EXPSource.SOURCE);
                                    PlayerExperienceGainBoosterEvent event = new PlayerExperienceGainBoosterEvent(PlayerData.get(p.getUniqueId()), exp, EXPSource.SOURCE);
                                    Bukkit.getPluginManager().callEvent(event);
                                }
                            }
                            case DIV -> {
                                if (booster_temporary_multi.get(p) > 1d) {
                                    double exp = xp * Double.parseDouble(new DecimalFormat("#.##").format((booster.get(p) + booster_temporary_multi.get(p)) / 2).replace(",", "."));
                                    PlayerData.get(p).giveExperience(exp, EXPSource.SOURCE);
                                    PlayerExperienceGainBoosterEvent event = new PlayerExperienceGainBoosterEvent(PlayerData.get(p.getUniqueId()), exp, EXPSource.SOURCE);
                                    Bukkit.getPluginManager().callEvent(event);
                                } else {
                                    double exp = Double.parseDouble(new DecimalFormat("#.##").format(xp * booster.get(p)).replace(",", "."));
                                    PlayerData.get(p).giveExperience(exp, EXPSource.SOURCE);
                                    PlayerExperienceGainBoosterEvent event = new PlayerExperienceGainBoosterEvent(PlayerData.get(p.getUniqueId()), exp, EXPSource.SOURCE);
                                    Bukkit.getPluginManager().callEvent(event);
                                }
                            }
                            default -> throw new IllegalStateException("Unexpected value mode: " + getBoosterMode());
                        }
                    } else {
                        PlayerData.get(p.getUniqueId()).giveExperience(xp, EXPSource.SOURCE);
                    }
                } else {
                    PlayerData.get(p.getUniqueId()).giveExperience(xp, EXPSource.SOURCE);
                }
            } else {
                PlayerData.get(p.getUniqueId()).giveExperience(xp, EXPSource.SOURCE);
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

    public BoosterMode getBoosterMode() {
        return switch (Objects.requireNonNull(Files.getConfig().getString("booster.mode"))) {
            case "div", "DIV" -> BoosterMode.DIV;
            case "add", "ADD" -> BoosterMode.ADD;
            case "higher", "HIGHER" -> BoosterMode.HIGHER;
            default -> BoosterMode.PLUS;
        };
    }

    public enum BoosterMode {
        DIV, PLUS, ADD, HIGHER
    }
}
