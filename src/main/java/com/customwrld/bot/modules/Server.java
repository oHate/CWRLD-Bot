package com.customwrld.bot.modules;

import lombok.Getter;

import java.util.List;

@Getter
public class Server {

    private final String name;
    private final String type;
    private final Long uptime;
    private final Double tps;
    private final List<String> players;

    public Server(String name, String type, Long uptime, Double tps, List<String> players) {
        this.name = name;
        this.type = type;
        this.uptime = uptime;
        this.tps = tps;
        this.players = players;
    }

}
