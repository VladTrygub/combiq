package ru.atott.combiq.data.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.client.Client;

import java.io.IOException;
import java.io.InputStream;

@Deprecated
public class DataUtils {

    public static String getIndexMapping(String indexFile) throws IOException {
        InputStream indexStream = DataUtils.class.getResourceAsStream(indexFile);
        String indexJson = IOUtils.toString(indexStream, "utf-8");
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = (JsonObject) parser.parse(indexJson);
        JsonObject mappingsJsonObject = jsonObject.getAsJsonObject("mappings");
        return mappingsJsonObject.toString();
    }

    public static void putMapping(Client client, String index, String typeMappingsJson) {
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = (JsonObject) parser.parse(typeMappingsJson);
        jsonObject.entrySet().forEach(mapping -> {
            String type = mapping.getKey();
            String json = "{\"" + type+ "\":" + mapping.getValue().toString() + "}";
            putMapping(client, index, type, json);
        });
    }

    public static void putMapping(Client client, String index, String type, String typeMappingJson) {
        PutMappingRequest putMappingRequest = new PutMappingRequest(index);
        putMappingRequest.type(type);
        putMappingRequest.source(typeMappingJson);
        client.admin().indices().putMapping(putMappingRequest).actionGet();
    }
}
