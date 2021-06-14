package com.customwrld.bot.flame;

import java.util.HashMap;
import java.util.Map;

public class FlameUtil {

    public static <K, V> Map<K, V> genericMapToType(Map<?, ?> map, Class<K> keyType, Class<V> valueType) {
        HashMap<K, V> convertedMap = new HashMap<>();
        for(Map.Entry<?, ?> ent : map.entrySet()) {
            convertedMap.put(keyType.cast(ent.getKey()), valueType.cast(ent.getValue()));
        }
        return convertedMap;
    }

}
