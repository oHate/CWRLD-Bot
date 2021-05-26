package com.customwrld.bot.tickets;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public class TicketProcedure {

    @Getter private static final Set<TicketProcedure> procedures = new HashSet<>();

    @Getter private final String discordId;
    @Getter private final String messageId;
    @Getter private final Ticket ticket;

    public TicketProcedure(String discordId, String messageId, Ticket ticket) {
        this.discordId = discordId;
        this.messageId = messageId;
        this.ticket = ticket;

        procedures.add(this);
    }

    public static TicketProcedure getById(String discordId) {
        for (TicketProcedure procedure : procedures) {
            if (procedure.discordId.equals(discordId)) {
                return procedure;
            }
        }

        return null;
    }

    public static boolean canStartProcedure(String discordId, String messageId) {
        TicketProcedure procedure = TicketProcedure.getById(discordId);

        if(procedure != null) {
            if(!procedure.ticket.reactionMessageId.equals(messageId)) {
                procedure.finish();
                return true;
            }
            return false;
        }
        return true;
    }

    public void finish() {
        procedures.remove(this);
    }
}
