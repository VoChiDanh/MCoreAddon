package net.danh.mcoreaddon.cmd;

import net.danh.mcoreaddon.api.CMDBase;
import net.danh.mcoreaddon.mythicdrop.MythicXP;
import net.danh.mcoreaddon.utils.ColorUtils;
import net.danh.mcoreaddon.utils.Files;
import net.danh.mcoreaddon.utils.Number;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.checkerframework.checker.units.qual.C;

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
            if (c instanceof Player) {
                if (args[0].equalsIgnoreCase("booster")) {
                    if (MythicXP.booster.get((Player) c) > 1d) {
                        c.sendMessage(ColorUtils.colorize(Objects.requireNonNull(Files.getMessage().getString("user.booster.active_booster"))
                                .replace("<multi>", String.valueOf(MythicXP.booster.get((Player) c)))
                                .replace("<player>", c.getName())));
                    } else {
                        c.sendMessage(ColorUtils.colorize(Files.getMessage().getString("no_booster")));
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
                            p.sendMessage(ColorUtils.colorize(Objects.requireNonNull(Files.getMessage().getString("user.booster.active_booster"))
                                    .replace("<multi>", String.valueOf(b_multi))
                                    .replace("<player>", c.getName())));
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
        Collections.sort(completions);
        return completions;
    }
}
