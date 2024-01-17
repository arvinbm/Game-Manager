package ca.cmpt276.project_7f.utils;

import com.google.gson.Gson;

// Gson to convert data between string and object.
public class GsonUtils {
    public static String getJsonStringFromObject(Object object)
    {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static <T>T getObjectFromJsonString(String jsonString, Class<T> tClass)
    {
        Gson gson = new Gson();
        return gson.fromJson(jsonString,tClass);
    }
}

