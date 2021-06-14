//package com.customwrld.bot.flame;
//
//import com.customwrld.bot.Bot;
//import com.customwrld.bot.profile.punishment.Punishment;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.model.Filters;
//import lombok.Getter;
//import lombok.Setter;
//import org.bson.Document;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Getter
//@Setter
//public class Profile {
//
//    @Getter
//    private static final MongoCollection<Document> collection = Bot.getBot().getMongoDatabase().getCollection("flame-profiles");
//    @Getter
//    private static final Map<UUID, Profile> profiles = new HashMap<>();
//
//    private final UUID uuid;
//    private final List<String> permissions;
//    private final List<String> punishments;
//    private final List<Punishment> cachedPunishments;
//    private final List<Grant> grants;
//    private final List<UUID> ignored;
//    private final List<UUID> friends;
//    private final Options options;
//    private final Staff staffProfile;
//    private Grant activeGrant;
//    private String name;
//    private String currentAddress;
//    private String discordId;
//    private Integer gold;
//    private Long firstSeen;
//    private Long lastSeen;
//    private Long experience;
//    private Map<String, Integer> gameModeGold;
//    private boolean loaded;
//
//    public Profile(UUID uuid) {
//        this.uuid = uuid;
//        this.permissions = new ArrayList<>();
//        this.punishments = new ArrayList<>();
//        this.cachedPunishments = new ArrayList<>();
//        this.grants = new ArrayList<>();
//        this.ignored = new ArrayList<>();
//        this.friends = new ArrayList<>();
//        this.gameModeGold = new HashMap<>();
//        this.options = new Options();
//        this.staffProfile = new Staff();
//    }
//
//    public static Profile getByUuid(UUID uuid) {
//        Document document = collection.find(Filters.eq("_id", uuid.toString())).first();
//
//        if (document != null) {
//            Profile profile = new Profile(UUID.fromString(document.getString("_id")));
//
//            profile.name = document.getString("name");
//
//            profile.firstSeen = Long.parseLong(document.getString("firstSeen"));
//            profile.lastSeen = Long.parseLong(document.getString("lastSeen"));
//            profile.currentAddress = document.getString("currentAddress");
//
//            Document optionsDocument = (Document) document.get("options");
//            profile.options.setReceivingFriendRequests(optionsDocument.getBoolean("receivingFriendRequests"));
//            profile.options.setReceivingPublicChat(optionsDocument.getBoolean("receivingPublicChat"));
//            profile.options.setReceivingConversations(optionsDocument.getBoolean("receivingConversations"));
//            profile.options.setReceivingMessageSounds(optionsDocument.getBoolean("receivingMessageSounds"));
//
//            Document staffDocument = (Document) document.get("staff");
//            profile.staffProfile.setTwoFactorKey(staffDocument.getString("twoFactorKey"));
//            profile.staffProfile.setOperator(staffDocument.getBoolean("operator"));
//            profile.staffProfile.setReceivingStaffMessages(staffDocument.getBoolean("receivingStaffMessages"));
//            profile.staffProfile.setTwoFactor(staffDocument.getBoolean("twoFactor"));
//            profile.staffProfile.setLocked(staffDocument.getBoolean("locked"));
//
//            profile.discordId = document.getString("discordId");
//
//            profile.gold = document.getInteger("gold");
//
//            profile.gameModeGold.clear();
//            profile.gameModeGold = FlameUtil.genericMapToType(document.get("gameModeGold", Map.class), String.class, Integer.class);
//
//            profile.experience = document.getLong("experience");
//
//            profile.grants.clear();
//            for (Map<?, ?> obj : document.getList("grants", Map.class, new ArrayList<>())) {
//                profile.grants.add(Grant.fromSimple(
//                        FlameUtil.genericMapToType(obj, String.class, String.class)));
//            }
//
//            profile.ignored.clear();
//            for (String uuid : document.getList("ignored", String.class)) {
//                UUID ignoredUUID = UUID.fromString(uuid);
//                profile.ignored.add(ignoredUUID);
//            }
//
//            profile.friends.clear();
//            for (String uuid : document.getList("friends", String.class)) {
//                UUID friendUUID = UUID.fromString(uuid);
//                profile.friends.add(friendUUID);
//            }
//
//            profile.punishments.clear();
//            profile.punishments.addAll(document.getList("punishments", String.class));
//
//            profile.cachedPunishments.clear();
//            profile.cachedPunishments.addAll(profile.punishments.stream().map(Punishment::getById).filter(Objects::nonNull).collect(Collectors.toList()));
//
//            profile.permissions.clear();
//            profile.permissions.addAll(document.getList("permissions", String.class));
//
//            return profile;
//        }
//
//        return null;
//    }
//
//    public boolean isIgnoring(Profile profile) {
//        return ignored.contains(profile.getUuid());
//    }
//
//    public Punishment getActivePunishmentByType(Punishment.Type type) {
//        for (Punishment punishment : this.cachedPunishments) {
//            if (punishment.getType() == type && !punishment.isRemoved() && !punishment.hasExpired()) {
//                return punishment;
//            }
//        }
//
//        return null;
//    }
//
//    public int getPunishmentCountByType(Punishment.Type type) {
//        int i = 0;
//
//        for (Punishment punishment : this.cachedPunishments) {
//
//            if (punishment.getType() == type) i++;
//        }
//
//        return i;
//    }
//
//    public Rank getActiveRank() {
//        return this.activeGrant.getRank();
//    }
//
//    private void checkGrants(boolean save) {
//        Player player = getPlayer();
//
//        Iterator<Grant> iterator = this.grants.iterator();
//        List<Grant> activeGrants = new ArrayList<>();
//
//        while (iterator.hasNext()) {
//            Grant grant = iterator.next();
//
//            if (grant.getRank() == null || Rank.getRanks().get(grant.getRank().getUuid()) == null) {
//                iterator.remove();
//            } else if (!grant.isRemoved() && !grant.hasExpired()) {
//                activeGrants.add(grant);
//            }
//
//        }
//
//        activeGrants.sort(Grant.COMPARATOR);
//
//        for (Grant grant : this.grants) {
//            if (!grant.isRemoved() && grant.hasExpired()) {
//                grant.setRemovedAt(System.currentTimeMillis());
//                grant.setRemovedReason("Grant Expired");
//                grant.setRemoved(true);
//
//                if (player != null) {
//                    Grant.expireGrant(this, grant);
//                }
//            }
//        }
//
//        if (activeGrants.isEmpty()) {
//            Grant defaultGrant = new Grant(UUID.randomUUID(), Rank.getDefaultRank(), null, System.currentTimeMillis(), "Default", Integer.MAX_VALUE);
//
//            this.grants.add(defaultGrant);
//            this.activeGrant = defaultGrant;
//
//            if (player != null) {
//                Grant.loadNewGrant(this, defaultGrant);
//            }
//        } else {
//            this.activeGrant = activeGrants.get(0);
//        }
//
//        if(save) {
//            this.save();
//        }
//    }
//
//    public List<String> getAllPermissions() {
//        List<String> permissions = new ArrayList<>();
//
//        permissions.addAll(this.getRankPermissions());
//        permissions.addAll(this.getPermissions());
//
//        return permissions;
//    }
//
//    public List<String> getRankPermissions() {
//        List<String> permissions = new ArrayList<>();
//        for (Grant grant : this.grants) {
//            permissions.addAll(grant.getRank().getAllPermissions());
//        }
//        return permissions;
//    }
//
//    public void reload() {
//        Document document = collection.find(Filters.eq("_id", this.uuid.toString())).first();
//
//        if (document != null) {
//
//
//            checkPunishments();
//            checkGrants(false);
//            checkPlayer();
//
//            this.loaded = true;
//        }
//
//    }
//
//    @Getter
//    @Setter
//    private static class Options {
//
//        private boolean receivingFriendRequests = true;
//        private boolean receivingPublicChat = true;
//        private boolean receivingConversations = true;
//        private boolean receivingMessageSounds = true;
//
//    }
//
//    @Getter
//    @Setter
//    private static class Staff {
//
//        private String twoFactorKey = "";
//        private boolean receivingStaffMessages = true;
//        private boolean twoFactor = false;
//        private boolean locked = false;
//        private boolean operator = false;
//
//    }
//
//}