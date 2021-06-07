package com.customwrld.bot.commandapi;

import java.util.Collections;
import java.util.List;

public interface ICommand {

    CommandPermission permission();

    String commandName();

    String usage();

    void handle(CommandContext ctx);

    default List<String> getAliases() {
        return Collections.emptyList();
    }
}