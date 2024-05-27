package net.danh.mcoreaddon.events;

import net.Indyuce.mmocore.api.event.PlayerExperienceGainEvent;
import net.danh.mcoreaddon.booster.Boosters;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GainEXP implements Listener {

    @EventHandler
    public void xpGain(PlayerExperienceGainEvent e) {
        int exp = (int) e.getExperience();
        if (e.getProfession() != null) {
            e.setExperience(Boosters.getProfessionExp(e.getPlayer(), e.getProfession().getId(), exp));
        } else e.setExperience(Boosters.getExp(e.getPlayer(), exp));
    }
}
