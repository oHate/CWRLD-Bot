package com.customwrld.bot.pigeon.converters;

import com.customwrld.bot.modules.Server;
import com.customwrld.pigeon.Converter;
import com.customwrld.pigeon.converters.ListConverter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Type;

public class ServerConverter extends Converter<Server> {

    public ServerConverter() { super(Server.class, true, false); }

    @Override
    public Server convertToValue(Type fieldType, JsonElement element) {
        return Utils.convertToValue(element);
    }

    @Override
    public JsonElement convertToSimple(Type fieldType, Server value) {
        return Utils.convertToSimple(value);
    }

    public static class Utils {
        public static Server convertToValue(JsonElement element) {
            var data = element.getAsJsonObject();

            return new Server(
                    data.get("name").getAsString(),
                    data.get("type").getAsString(),
                    data.get("uptime").getAsLong(),
                    data.get("tps").getAsDouble(),
                    ListConverter.Utils.convertToValue(data.get("players"), String.class)
            );
        }

        public static JsonElement convertToSimple(Server value) {
            JsonObject data = new JsonObject();

            data.add("name", new JsonPrimitive(value.getName()));
            data.add("type", new JsonPrimitive(value.getType()));
            data.add("uptime", new JsonPrimitive(value.getUptime()));
            data.add("tps", new JsonPrimitive(value.getTps()));
            data.add("players", ListConverter.Utils.convertToSimple(value.getPlayers(), String.class));

            return data;
        }
    }

}
