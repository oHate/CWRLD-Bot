package com.customwrld.bot.timer.timers;

import com.customwrld.bot.Bot;
import com.customwrld.bot.util.config.Config;
import com.customwrld.bot.timer.api.Timer;
import com.customwrld.bot.timer.api.TimerType;
import com.customwrld.bot.profile.Profile;
import com.customwrld.bot.profile.punishment.Punishment;
import com.customwrld.bot.util.Logger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;

public class MuteTimer extends Timer {

    private final Profile profile;
    private final Punishment punishment;
    private Guild guild;
    private Config config;

    public MuteTimer(Profile profile, Punishment punishment) {
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
        this.config = bot.getConfig();
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

        ErrorHandler UNKNOWN_MEMBER = new ErrorHandler().handle(ErrorResponse.UNKNOWN_MEMBER, (error) -> Logger.log(Logger.LogType.WARNING, "[" + profile.getDiscordId() + "] UNKNOWN MEMBER."));

        this.guild.retrieveMemberById(this.profile.getDiscordId()).queue(member -> {
            Role role = this.guild.getRoleById(this.config.getMutedRole());

            if(role != null && member.getRoles().contains(role)) {
                this.guild.removeRoleFromMember(member, role).queue();
            }
        }, UNKNOWN_MEMBER);
    }

    @Override
    public void onCancel() {}

}
