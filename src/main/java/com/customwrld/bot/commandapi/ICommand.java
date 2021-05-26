package com.customwrld.bot.commandapi;

import net.dv8tion.jda.api.Permission;

import java.util.Collections;
import java.util.List;

public interface ICommand {

    Permission permission();

    String commandName();

    String usage();

    void handle(CommandContext ctx);

    default List<String> getAliases() {
        return Collections.emptyList();
    }
}