package net.danh.mcoreaddon.listener;

import net.Indyuce.mmocore.api.event.PlayerDataEvent;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.experience.EXPSource;
import net.Indyuce.mmocore.experience.Profession;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import javax.annotation.Nullable;

public class PlayerExperienceGainBoosterEvent extends PlayerDataEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    // if null, this is the main experience
    private final Profession profession;
    private final EXPSource source;

    private double experience;
    private boolean cancelled;

    public PlayerExperienceGainBoosterEvent(PlayerData player, double experience, EXPSource source) {
        this(player, null, experience, source);
    }

    public PlayerExperienceGainBoosterEvent(PlayerData player, @Nullable Profession profession, double experience, EXPSource source) {
        super(player);

        this.profession = profession;
        this.experience = experience;
        this.source = source;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public double getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean hasProfession() {
        return profession != null;
    }

    public Profession getProfession() {
        return profession;
    }

    public EXPSource getSource() {
        return source;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}