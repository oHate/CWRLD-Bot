package com.customwrld.bot.config;

import lombok.Getter;
import net.dv8tion.jda.api.entities.Activity;

import java.awt.*;
import java.util.List;

public class Config {

    @Getter private String botToken;
    @Getter private Activity botActivity;
    @Getter private String botPrefix;
    @Getter private String botGuild;
    @Getter private Color botColor;

    @Getter private String staffRole;
    @Getter private String mutedRole;
    @Getter private List<String> filteredChannels;

    @Getter private String welcomeChannel;
    @Getter private String memberRole;

    @Getter private String ticketMessage;
    @Getter private String ticketChannel;
    @Getter private String ticketCategory;

    @Getter private String mongoHost;
    @Getter private int mongoPort;
    @Getter private String mongoDatabase;
    @Getter private boolean mongoAuthEnabled;
    @Getter private String mongoUsername;
    @Getter private String mongoPassword;

    public Config() {
        ConfigFile file = new ConfigFile();

        file.load();

        botToken = file.discordSection.botToken;
        botActivity = Activity.playing(file.discordSection.botActivity);
        botPrefix = file.discordSection.botPrefix;
        botGuild = file.discordSection.botGuild;
        botColor = new Color(Integer.parseInt(file.discordSection.botColor));

        staffRole = file.staffSection.staffRole;
        mutedRole = file.staffSection.mutedRole;
        filteredChannels = file.staffSection.filteredChannels;

        welcomeChannel = file.welcomeSection.welcomeChannel;
        memberRole = file.welcomeSection.memberRole;

        ticketMessage = file.ticketSection.ticketMessage;
        ticketChannel = file.ticketSection.ticketChannel;
        ticketCategory = file.ticketSection.ticketCategory;

        mongoHost = file.mongoSection.mongoHost;
        mongoPort = file.mongoSection.mongoPort;
        mongoDatabase = file.mongoSection.mongoDatabase;
        mongoAuthEnabled = file.mongoSection.authenticationSection.mongoAuthEnabled;
        mongoUsername = file.mongoSection.authenticationSection.mongoUsername;
        mongoPassword = file.mongoSection.authenticationSection.mongoPassword;
    }

}
