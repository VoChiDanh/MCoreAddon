package net.danh.mcoreaddon.cmd;

import net.danh.mcoreaddon.MCoreAddon;
import net.danh.mcoreaddon.api.cmd.CMDBase;
import net.danh.mcoreaddon.mythicdrop.MythicXP;
import net.danh.mcoreaddon.utils.ColorUtils;
import net.danh.mcoreaddon.utils.Files;
import net.danh.mcoreaddon.utils.Number;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.StringUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MCA_CMD extends CMDBase {
    public MCA_CMD() {
        super("MCoreA");
    }

    @Override
    public void execute(CommandSender c, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {
                if (c.hasPermission("mcoreaddon.admin")) {
                    Files.getMessage().getStringList("admin.help").forEach(s -> c.sendMessage(ColorUtils.colorize(s)));
                }
                Files.getMessage().getStringList("user.help").forEach(s -> c.sendMessage(ColorUtils.colorize(s)));
            }
            if (c instanceof Player) {
                if (args[0].equalsIgnoreCase("booster")) {
                    if (MythicXP.booster.get((Player) c) > 1d || (MythicXP.booster_temporary_times.get((Player) c) > 0 && MythicXP.booster_temporary_multi.get((Player) c) > 1d)) {
                        Files.getMessage().getStringList("user.booster.active_booster").forEach(s -> c.sendMessage(ColorUtils.colorize((s
                                .replace("<p_multi>", String.valueOf(MythicXP.booster.get((Player) c)))
                                .replace("<t_multi>", String.valueOf(MythicXP.booster_temporary_multi.get((Player) c)))
                                .replace("<t_times>", Number.getTime(MythicXP.booster_temporary_times.get((Player) c)))
                                .replace("<player>", c.getName())))));
                    } else {
                        c.sendMessage(ColorUtils.colorize(Files.getMessage().getString("user.booster.no_booster")));
                    }
                }
            }
            if (args[0].equalsIgnoreCase("reload")) {
                if (c.hasPermission("mcoreaddon.admin")) {
                    Files.reloadFile();
                    c.sendMessage(ColorUtils.colorize(Files.getMessage().getString("admin.reload")));
                }
            }
        }
        if (args.length == 3) {
            if (c.hasPermission("mcoreaddon.admin")) {
                if (args[0].equalsIgnoreCase("booster")) {
                    double booster_multi = Number.getDouble(args[2]);
                    double b_multi = Double.parseDouble(new DecimalFormat("#.##").format(booster_multi).replace(",", "."));
                    if (booster_multi > 0d) {
                        Player p = Bukkit.getPlayer(args[1]);
                        if (p != null) {
                            MythicXP.booster.replace(p, b_multi);
                            c.sendMessage(ColorUtils.colorize(Objects.requireNonNull(Files.getMessage().getString("admin.booster.add_booster"))
                                    .replace("<multi>", String.valueOf(b_multi))
                                    .replace("<player>", p.getName())));
                            Files.getMessage().getStringList("user.booster.active_booster").forEach(s -> p.sendMessage(ColorUtils.colorize((s
                                    .replace("<p_multi>", String.valueOf(MythicXP.booster.get(p)))
                                    .replace("<t_multi>", String.valueOf(MythicXP.booster_temporary_multi.get(p)))
                                    .replace("<t_times>", Number.getTime(MythicXP.booster_temporary_times.get(p)))
                                    .replace("<player>", c.getName())))));
                        } else {
                            c.sendMessage(ColorUtils.colorize(Files.getMessage().getString("admin.player_is_null")));
                        }
                    } else {
                        c.sendMessage(ColorUtils.colorize(Files.getMessage().getString("admin.booster.above_zero")));
                    }
                }
            }
        }
        if (args.length == 4) {
            if (c.hasPermission("mcoreaddon.admin")) {
                if (args[0].equalsIgnoreCase("booster")) {
                    double booster_multi = Number.getDouble(args[2]);
                    int booster_times = Number.getInteger(args[3]) * 60;
                    double b_multi = Double.parseDouble(new DecimalFormat("#.##").format(booster_multi).replace(",", "."));
                    if (booster_multi > 0d) {
                        Player p = Bukkit.getPlayer(args[1]);
                        if (p != null) {
                            MythicXP.booster_temporary_times.replace(p, booster_times);
                            MythicXP.booster_temporary_multi.replace(p, booster_multi);
                            c.sendMessage(ColorUtils.colorize(Objects.requireNonNull(Files.getMessage().getString("admin.booster.add_booster"))
                                    .replace("<multi>", String.valueOf(b_multi))
                                    .replace("<player>", p.getName())));
                            Files.getMessage().getStringList("user.booster.active_booster").forEach(s -> p.sendMessage(ColorUtils.colorize((s
                                    .replace("<p_multi>", String.valueOf(MythicXP.booster.get(p)))
                                    .replace("<t_multi>", String.valueOf(MythicXP.booster_temporary_multi.get(p)))
                                    .replace("<t_times>", Number.getTime(MythicXP.booster_temporary_times.get(p)))
                                    .replace("<player>", c.getName())))));
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    for (Player p : Bukkit.getOnlinePlayers()) {
                                        if (p.isOnline()) {
                                            if (MythicXP.booster_temporary_times.containsKey(p) && MythicXP.booster_temporary_multi.containsKey(p)) {
                                                if (MythicXP.booster_temporary_times.get(p) > 0) {
                                                    if (MythicXP.booster_temporary_multi.get(p) > 1.0) {
                                                        int times = MythicXP.booster_temporary_times.get(p);
                                                        if (Math.abs(times) > 0) {
                                                            MythicXP.booster_temporary_times.replace(p, --times);
                                                            if (Math.abs(times) == 0) {
                                                                p.sendMessage(ColorUtils.colorize(Objects.requireNonNull(Files.getMessage().getString("user.booster.expired_booster"))
                                                                        .replace("<t_multi>", String.valueOf(MythicXP.booster_temporary_multi.get(p)))));
                                                                MythicXP.booster_temporary_multi.replace(p, 1.0);
                                                                MythicXP.booster_temporary_times.replace(p, 0);
                                                                cancel();
                                                            }
                                                        } else {
                                                            p.sendMessage(ColorUtils.colorize(Objects.requireNonNull(Files.getMessage().getString("user.booster.expired_booster"))
                                                                    .replace("<t_multi>", String.valueOf(MythicXP.booster_temporary_multi.get(p)))));
                                                            MythicXP.booster_temporary_multi.replace(p, 1.0);
                                                            MythicXP.booster_temporary_times.replace(p, 0);
                                                            cancel();
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }.runTaskTimer(MCoreAddon.getMCore(), 20L, 20L);
                        } else {
                            c.sendMessage(ColorUtils.colorize(Files.getMessage().getString("admin.player_is_null")));
                        }
                    } else {
                        c.sendMessage(ColorUtils.colorize(Files.getMessage().getString("admin.booster.above_zero")));
                    }
                }
            }
        }
    }

    @Override
    public List<String> TabComplete(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        if (args.length == 1) {
            if (sender.hasPermission("mcoreaddon.admin")) {
                commands.add("reload");
            }
            commands.add("booster");
            commands.add("help");
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }
        if (args.length == 2) {
            if (sender.hasPermission("mcoreaddon.admin")) {
                if (args[0].equalsIgnoreCase("booster")) {
                    Bukkit.getOnlinePlayers().forEach(player -> commands.add(player.getName()));
                }
            }
            StringUtil.copyPartialMatches(args[1], commands, completions);
        }
        if (args.length == 3) {
            if (sender.hasPermission("mcoreaddon.admin")) {
                if (args[0].equalsIgnoreCase("booster")) {
                    if (Bukkit.getPlayer(args[1]) != null) {
                        for (double i = 1.5; i <= 5; i += 0.5) {
                            commands.add(String.valueOf(i));
                        }
                    }
                }
            }
            StringUtil.copyPartialMatches(args[2], commands, completions);
        }
        if (args.length == 4) {
            if (sender.hasPermission("mcoreaddon.admin")) {
                if (args[0].equalsIgnoreCase("booster")) {
                    if (Bukkit.getPlayer(args[1]) != null) {
                        commands.add("<minutes>");
                    }
                }
            }
            StringUtil.copyPartialMatches(args[3], commands, completions);
        }
        Collections.sort(completions);
        return completions;
    }
}
