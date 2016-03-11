package ru.atott.combiq.web.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Component;

@Component
public class ViewUtils {
    public String toJson(Object object) {
        return serializeToJson(object);
    }

    public static String serializeToJson(Object object) {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(object);
    }

    public static JsonObject parseJson(String json) {
        JsonParser parser = new JsonParser();
        return (JsonObject) parser.parse(json);
    }

    public static String getCountWord(Long count){
        int lastDigit= (int) (count%10);
        String answer="";
        if(lastDigit==1) answer="вопрос";
        else if(lastDigit>1&&lastDigit<5)answer="вопроса";
        else answer="вопросов";
        return answer;
    }
}
