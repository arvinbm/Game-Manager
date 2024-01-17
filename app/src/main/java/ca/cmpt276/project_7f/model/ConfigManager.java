package ca.cmpt276.project_7f.model;

import java.util.ArrayList;

// managing the list of configuration.
public class ConfigManager {
    // does not allow same name !

    private ArrayList<Config> configList;
    private static ConfigManager instance;

    public void setConfigList(ArrayList<Config> configList) {
        this.configList = configList;
    }

    public ArrayList<Config> getConfigList() {
        return configList;
    }

    private ConfigManager() {
        configList = new ArrayList<>();
    }

    public static ConfigManager getInstance()
    {
        // use instance because we only have one config list.
        if(instance == null)
        {
            instance = new ConfigManager();
        }
        return instance;
    }

    public int getSizeOfConfigList()
    {
        return configList.size();
    }

    public boolean isNameExisted(String configName)
    {
        for(int i = 0; i < configList.size(); i++)
        {
            Config config = configList.get(i);
            String name = config.getName();
            if(configName.equals(name))
            {
                return true;
            }
        }
        return false;
    }

    public void addConfig(Config config)
    {
        configList.add(config);
    }

    public void editConfig(int indexInConfigList, Config newConfig)
    {
        Config oldConfig = configList.get(indexInConfigList);
        String oldConfigName = oldConfig.getName();
        configList.set(indexInConfigList,newConfig);
        // update games as well.
        // if there are some games, then update. If no games, do nothing.
        GameManager instanceOfGameManager = GameManager.getInstance();
        if(instanceOfGameManager.getSizeOfGameListByName(oldConfigName) > 0)
        {
            instanceOfGameManager.updateGamesWhenConfigChanges(oldConfigName,newConfig);
        }
    }

    public void removeConfig(int indexInConfigList)
    {
        Config config = configList.get(indexInConfigList);
        String configName = config.getName();
        configList.remove(indexInConfigList);
        // remove games as well.
        // if there are some games, then remove. If no games, do nothing.
        GameManager instanceOfGameManager = GameManager.getInstance();
        if(instanceOfGameManager.getSizeOfGameListByName(configName) > 0)
        {
            instanceOfGameManager.removeGames(configName);
        }
    }

    public Config getConfigByName(String name)
    {
        for(int i = 0; i < configList.size(); i++)
        {
            Config config = configList.get(i);
            if(config.getName().equals(name))
            {
                return config;
            }
        }
        return null;
    }

    public Config getConfigByIndex(int index)
    {
        return configList.get(index);
    }

}
