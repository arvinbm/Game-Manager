package ca.cmpt276.project_7f.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

import ca.cmpt276.project_7f.model.Config;
import ca.cmpt276.project_7f.model.ConfigManager;
import ca.cmpt276.project_7f.model.Game;
import ca.cmpt276.project_7f.model.GameManager;

// save and read data from shared preference
public class SharedPreferencesUtils {
    public static void saveDataOfConfigManager(Context context)
    {
        ConfigManager instanceOfConfigM = ConfigManager.getInstance();
        SharedPreferences shared_preferences = context.getSharedPreferences("shared_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = shared_preferences.edit();
        String jsonString = GsonUtils.getJsonStringFromObject(instanceOfConfigM);
        editor.putString("config_manager",jsonString);
        editor.apply();
    }

    public static void loadDataOfConfigManager(Context context)
    {
        SharedPreferences shared_preferences = context.getSharedPreferences("shared_preferences", MODE_PRIVATE);
        String config_manager = shared_preferences.getString("config_manager", null);
        if(config_manager != null)
        {
            ConfigManager savedCM = GsonUtils.getObjectFromJsonString(config_manager, ConfigManager.class);
            ArrayList<Config> configList = savedCM.getConfigList();
            ConfigManager instanceOfConfigM = ConfigManager.getInstance();
            instanceOfConfigM.setConfigList(configList);
        }
    }

    public static void saveDataOfGameManager(Context context)
    {
        GameManager instanceOfGameM = GameManager.getInstance();
        SharedPreferences shared_preferences = context.getSharedPreferences("shared_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = shared_preferences.edit();
        String jsonString = GsonUtils.getJsonStringFromObject(instanceOfGameM);
        editor.putString("game_manager",jsonString);
        editor.apply();
    }

    public static void loadDataOfGameManager(Context context)
    {
        SharedPreferences shared_preferences = context.getSharedPreferences("shared_preferences", MODE_PRIVATE);
        String game_manager = shared_preferences.getString("game_manager", null);
        if(game_manager != null)
        {
            GameManager savedGM = GsonUtils.getObjectFromJsonString(game_manager, GameManager.class);
            ArrayList<Game> gameList = savedGM.getGameList();
            GameManager instanceOfGameM = GameManager.getInstance();
            instanceOfGameM.setGameList(gameList);
        }
    }
}
