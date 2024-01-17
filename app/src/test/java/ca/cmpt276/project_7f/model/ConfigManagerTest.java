package ca.cmpt276.project_7f.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
    All tests passed!
    Need to run those tests individually instead of running them at the same time
    due to singleton within target testing ConfigManager class.
 */
class ConfigManagerTest {

    @Test
    void setConfigList() {
        ArrayList<Config> testList = new ArrayList<>();
        testList.add(new Config());
        testList.add(new Config());
        testList.add(new Config());
        ConfigManager instance = ConfigManager.getInstance();
        instance.setConfigList(testList);
        assertEquals(testList,instance.getConfigList());
    }

    @Test
    void getConfigList() {
        ArrayList<Config> testList = new ArrayList<>();
        ConfigManager instance = ConfigManager.getInstance();
        instance.setConfigList(testList);
        assertEquals(testList,instance.getConfigList());
    }

    @Test
    void getInstance() {
        assertEquals(ConfigManager.getInstance(),ConfigManager.getInstance());
    }

    @Test
    void getSizeOfConfigListSize0() {
        ConfigManager instance = ConfigManager.getInstance();
        int sizeOfConfigList = instance.getSizeOfConfigList();
        assertEquals(0,sizeOfConfigList);
    }

    @Test
    void getSizeOfConfigListSize3() {
        ConfigManager instance = ConfigManager.getInstance();
        instance.addConfig(new Config());
        instance.addConfig(new Config());
        instance.addConfig(new Config());
        int sizeOfConfigList = instance.getSizeOfConfigList();
        assertEquals(3,sizeOfConfigList);
    }

    @Test
    void isNameExistedYes() {
        ConfigManager instance = ConfigManager.getInstance();
        Config config = new Config();
        config.setName("name");
        instance.addConfig(config);
        assertTrue(instance.isNameExisted("name"));
    }

    @Test
    void isNameExistedNo() {
        ConfigManager instance = ConfigManager.getInstance();
        Config config = new Config();
        config.setName("name");
        instance.addConfig(config);
        assertFalse(instance.isNameExisted("Name"));
    }

    @Test
    void addConfig() {
        ConfigManager instance = ConfigManager.getInstance();
        Config config1 = new Config();
        instance.addConfig(config1);
        ArrayList<Config> configList = instance.getConfigList();
        Config config2 = configList.get(configList.size() - 1);
        assertEquals(config1,config2);
    }

    @Test
    void editConfig() {
        ConfigManager instance = ConfigManager.getInstance();
        Config config1 = new Config();
        instance.addConfig(config1);
        Config configEdit = new Config();
        configEdit.setName("EDIT");
        instance.editConfig(0, configEdit);
        ArrayList<Config> configList = instance.getConfigList();
        Config config2 = configList.get(configList.size() - 1);
        assertEquals(configEdit,config2);
    }

    @Test
    void removeConfig() {
        ConfigManager instance = ConfigManager.getInstance();
        Config config1 = new Config();
        instance.addConfig(config1);
        Config configEdit = new Config();
        configEdit.setName("EDIT");
        instance.editConfig(0, configEdit);
        instance.removeConfig(0);
        assertEquals(0,instance.getSizeOfConfigList());
    }

    @Test
    void getConfigByNameNotNUll() {
        ConfigManager instance = ConfigManager.getInstance();
        Config config1 = new Config();
        instance.addConfig(config1);
        Config configEdit = new Config();
        configEdit.setName("EDIT");
        instance.editConfig(0, configEdit);
        Config configByName = instance.getConfigByName("EDIT");
        assertEquals(configEdit,configByName);
    }

    @Test
    void getConfigByNameIsNUll() {
        ConfigManager instance = ConfigManager.getInstance();
        Config config1 = new Config();
        instance.addConfig(config1);
        Config configEdit = new Config();
        configEdit.setName("EDIT");
        instance.editConfig(0, configEdit);
        Config configByName = instance.getConfigByName("edit");
        assertNull(configByName);
    }

    @Test
    void getConfigByIndex() {
        ConfigManager instance = ConfigManager.getInstance();
        Config config1 = new Config();
        instance.addConfig(config1);
        Config configEdit = new Config();
        configEdit.setName("EDIT");
        instance.editConfig(0, configEdit);
        ArrayList<Config> configList = instance.getConfigList();
        Config config11 = configList.get(0);
        Config config22 = instance.getConfigByIndex(0);
        assertEquals(config11,config22);
    }
}