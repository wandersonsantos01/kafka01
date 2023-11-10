package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class GsonDeserializer<T> implements Deserializer<T> {

    public static final String TYPE_CONFIG = "org.example.type_config";
    private final Gson gson = new GsonBuilder().create();
    private Class<T> type;

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        String typeName = String.valueOf(configs.get(TYPE_CONFIG));
        try {
            this.type = (Class<T>) Class.forName(typeName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class type deserialization not exists", e);
        }
    }

    @Override
    public T deserialize(String s, byte[] bytes) {
        return null;
    }

    @Override
    public T deserialize(String topic, Headers headers, byte[] data) {
        return gson.fromJson(new String(data), type);
    }

    @Override
    public void close() {
        Deserializer.super.close();
    }

}
