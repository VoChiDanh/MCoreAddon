package net.danh.mcoreaddon.booster;

import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.experience.EXPSource;
import net.Indyuce.mmocore.experience.Profession;
import net.danh.mcoreaddon.api.calculator.Calculator;
import net.danh.mcoreaddon.mythicdrop.MythicProfession;
import net.danh.mcoreaddon.mythicdrop.MythicXP;
import net.danh.mcoreaddon.utils.Files;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Objects;

public class Boosters {

    public static void giveExp(Player p, int xp, Location location) {
        if (MythicXP.booster.containsKey(p)) {
            if (MythicXP.booster.get(p) > 1d) {
                if (MythicXP.booster_temporary_multi.get(p) > 1d) {
                    String booster_all_string = Objects.requireNonNull(Files.getConfig().getString("booster.mode.all"))
                            .replace("<xp>", String.valueOf(xp))
                            .replace("<p_multi>", String.valueOf(MythicXP.booster.get(p)))
                            .replace("<t_multi>", String.valueOf(MythicXP.booster_temporary_multi.get(p)))
                            .replace(",", ".");
                    String booster_all_calculator = Calculator.calculator(booster_all_string, 0);
                    int exp = Math.abs((int) Double.parseDouble(booster_all_calculator));
                    PlayerData.get(p).giveExperience(exp, EXPSource.SOURCE, location.add(0, 1.5, 0), true);
                } else {
                    String booster_permanently_string = Objects.requireNonNull(Files.getConfig().getString("booster.mode.permanently"))
                            .replace("<xp>", String.valueOf(xp))
                            .replace("<p_multi>", String.valueOf(MythicXP.booster.get(p)))
                            .replace(",", ".");
                    String booster_permanently_calculator = Calculator.calculator(booster_permanently_string, 0);
                    int exp = Math.abs((int) Double.parseDouble(booster_permanently_calculator));
                    PlayerData.get(p).giveExperience(exp, EXPSource.SOURCE, location.add(0, 1.5, 0), true);
                }
            } else {
                PlayerData.get(p).giveExperience(xp, EXPSource.SOURCE, location.add(0, 1.5, 0), true);
            }
        } else {
            PlayerData.get(p).giveExperience(xp, EXPSource.SOURCE, location.add(0, 1.5, 0), true);
        }
    }

    public static void giveProfessionExp(Player p, String profession, int xp, Location location) {
        if (MMOCore.plugin.professionManager.has(profession)) {
            Profession prf = MMOCore.plugin.professionManager.get(profession);
            if (MythicProfession.booster_profession.containsKey(p.getName() + "_" + profession)) {
                if (MythicProfession.booster_profession.get(p.getName() + "_" + profession) > 1d) {
                    if (MythicProfession.booster_temporary_multi_profession.get(p.getName() + "_" + profession) > 1d) {
                        String booster_all_string = Objects.requireNonNull(Files.getConfig().getString("booster.mode.all"))
                                .replace("<xp>", String.valueOf(xp))
                                .replace("<p_multi>", String.valueOf(MythicProfession.booster_profession.get(p.getName() + "_" + profession)))
                                .replace("<t_multi>", String.valueOf(MythicProfession.booster_temporary_multi_profession.get(p.getName() + "_" + profession)))
                                .replace(",", ".");
                        String booster_all_calculator = Calculator.calculator(booster_all_string, 0);
                        int exp = Math.abs((int) Double.parseDouble(booster_all_calculator));
                        PlayerData.get(p.getUniqueId()).getCollectionSkills().giveExperience(prf, exp, EXPSource.SOURCE, location.add(0, 1.5, 0), true);
                    } else {
                        String booster_permanently_string = Objects.requireNonNull(Files.getConfig().getString("booster.mode.permanently"))
                                .replace("<xp>", String.valueOf(xp))
                                .replace("<p_multi>", String.valueOf(MythicProfession.booster_profession.get(p.getName() + "_" + profession)))
                                .replace(",", ".");
                        String booster_permanently_calculator = Calculator.calculator(booster_permanently_string, 0);
                        int exp = Math.abs((int) Double.parseDouble(booster_permanently_calculator));
                        PlayerData.get(p.getUniqueId()).getCollectionSkills().giveExperience(prf, exp, EXPSource.SOURCE, location.add(0, 1.5, 0), true);
                    }
                } else {
                    PlayerData.get(p.getUniqueId()).getCollectionSkills().giveExperience(prf, xp, EXPSource.SOURCE, location.add(0, 1.5, 0), true);
                }
            } else {
                PlayerData.get(p.getUniqueId()).getCollectionSkills().giveExperience(prf, xp, EXPSource.SOURCE, location.add(0, 1.5, 0), true);
            }
        }
    }
}
