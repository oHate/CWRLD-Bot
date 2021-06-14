package com.customwrld.bot.timer.timers;

import com.customwrld.bot.Bot;
import com.customwrld.bot.timer.api.Timer;
import com.customwrld.bot.timer.api.TimerType;
import com.customwrld.bot.profile.Profile;
import com.customwrld.bot.profile.punishment.Punishment;
import com.customwrld.bot.util.Logger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;

public class BanTimer extends Timer {

    private Profile profile;
    private Punishment punishment;
    private Guild guild;

    public BanTimer(Profile profile, Punishment punishment) {
        super(TimerType.COUNTDOWN, Bot.getBot().getTimerManager());
        setDuration(punishment.getMillisRemaining());
        this.profile = profile;
        this.punishment = punishment;
    }

    @Override
    public void tick() {}

    @Override
    public void onStart() {
        Bot bot = Bot.getBot();
        this.guild = bot.getGuild();
    }

    @Override
    public void onComplete() {
        if(!punishment.isRemoved()) {
            punishment.setRemoved(true);
            punishment.setRemovedReason("Punishment Expired");
            punishment.setRemovedAt(System.currentTimeMillis());
            punishment.setRemovedBy(null);
            profile.save();
        }

        ErrorHandler UNKNOWN_BAN = new ErrorHandler().handle(ErrorResponse.UNKNOWN_BAN, (error) -> Logger.log(Logger.LogType.WARNING, "[" + profile.getDiscordId() + "] UNKNOWN BAN."));

        guild.retrieveBanById(profile.getDiscordId()).queue(ban -> guild.unban(ban.getUser()).queue(), UNKNOWN_BAN);
    }

    @Override
    public void onCancel() {}

}
