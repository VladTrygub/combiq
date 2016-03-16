package ru.atott.combiq.web.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Component;
import ru.atott.combiq.service.bean.UserType;

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

    public static String pluralizeQuestionLabel(Long count){
        int lastDigit= (int) (count%10);
        String answer="";
        if(lastDigit==1) answer=" вопрос";
        else if(lastDigit>1&&lastDigit<5)answer=" вопроса";
        else answer=" вопросов";
        return count+answer;
    }

    public static String getHeadAvatarUrl(UserType userType, String userAvatarUrl) {
        if (userAvatarUrl == null) {
            return "/static/images/icons/user-48.png";
        }

        switch (userType) {
            case github:
                return userAvatarUrl.contains("?") ? userAvatarUrl + "&s=46" : userAvatarUrl + "?s=46";
            case vk:
                return userAvatarUrl;
            case stackexchange:
                return userAvatarUrl;
            case facebook:
                return userAvatarUrl;
            default:
                return userAvatarUrl;
        }
    }
}
