package com.customwrld.bot.profile.punishment;

import com.customwrld.bot.util.TimeUtil;
import com.customwrld.bot.util.Util;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Punishment {

    @Getter private UUID uuid;
    @Getter private PunishmentType type;
    @Getter @Setter private String addedBy;
    @Getter private long addedAt;
    @Getter private String addedReason;
    @Getter private long duration;
    @Getter @Setter private String removedBy;
    @Getter @Setter private long removedAt;
    @Getter @Setter private String removedReason;
    @Getter @Setter private boolean removed;

    public Punishment(UUID uuid, PunishmentType type, long addedAt, String addedReason, long duration) {
        this.uuid = uuid;
        this.type = type;
        this.addedAt = addedAt;
        this.addedReason = addedReason;
        this.duration = duration;
    }

    public boolean isPermanent() {
        return duration == Integer.MAX_VALUE;
    }

    public boolean isActive() {
        return !this.removed && (this.isPermanent() || this.getMillisRemaining() < 0L);
    }

    public long getMillisRemaining() {
        return (addedAt + duration) - System.currentTimeMillis();
    }

    public boolean hasExpired() {
        return (!isPermanent()) && (System.currentTimeMillis() >= addedAt + duration);
    }

    public String getTimeRemaining() {
        if (removed) {
            return "Removed";
        }

        if (isPermanent()) {
            return "Permanent";
        }

        if (hasExpired()) {
            return "Expired";
        }

        return TimeUtil.millisToRoundedTime((addedAt + duration) - System.currentTimeMillis());
    }

    public String getContext() {
        if (!(type == PunishmentType.BAN || type == PunishmentType.MUTE)) {
            return removed ? type.getUndoContext() : type.getContext();
        }

        if (isPermanent()) {
            return (removed ? type.getUndoContext() : "permanently " + type.getContext());
        } else {
            return (removed ? type.getUndoContext() : "temporarily " + type.getContext());
        }
    }

    public void sendEmbed(User user, TextChannel channel) {
        EmbedBuilder embed = Util.builder(user)
                .setTitle("**" + user.getAsTag() + " has been " + getContext() + ".**")
                .addField("**Reason**", addedReason, false);

        if(!this.isPermanent()) {
            embed.addField("**Duration**", TimeUtil.millisToRoundedTime(getDuration()), false);
        }

        channel.sendMessage(embed.build()).queue();
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Punishment && ((Punishment) object).uuid.equals(uuid);
    }

    public static Punishment fromSimple(Map<String, String> simple) {
        Punishment punishment = new Punishment(UUID.fromString(simple.get("uuid")),
                PunishmentType.valueOf(simple.get("punishmentType")),
                Long.parseLong(simple.get("addedAt")),
                simple.get("addedReason"),
                Long.parseLong(simple.get("duration"))
        );

        punishment.setAddedBy(!simple.get("addedBy").equals("null") ? simple.get("addedBy") : null);

        String isRemoved = simple.get("removed");
        if (isRemoved != null && isRemoved.equals("true")) {
            punishment.setRemoved(true);
            punishment.setRemovedAt(Long.parseLong(simple.get("removedAt")));
            punishment.setRemovedBy(!simple.get("removedBy").equals("null") ? simple.get("removedBy") : null);
            punishment.setRemovedReason(simple.get("removedReason"));
        }

        return punishment;
    }

    public HashMap<String, String> simplify() {
        HashMap<String, String> simple = new HashMap<>();
        simple.put("uuid", uuid.toString());
        simple.put("punishmentType", type.toString());
        simple.put("addedAt", String.valueOf(addedAt));
        simple.put("addedBy", addedBy != null ? addedBy : "null");
        simple.put("addedReason", addedReason);
        simple.put("duration", String.valueOf(duration));

        if (removed) {
            simple.put("removed", "true");
            simple.put("removedAt", String.valueOf(removedAt));
            simple.put("removedBy", removedBy != null ? removedBy : "null");
            simple.put("removedReason", removedReason);
        }

        return simple;
    }

}