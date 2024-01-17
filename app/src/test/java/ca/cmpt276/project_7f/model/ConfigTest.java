package ca.cmpt276.project_7f.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ConfigTest {

    @Test
    void testToString() {
        Config config = new Config();
        config.setName("NAME");
        config.setGreatScore(100);
        config.setPoorScore(0);
        String toString1 = config.toString();
        String toString2 = "Config{" +
                "configName='" + config.getName() + '\'' +
                ", poorScore=" + config.getPoorScore() +
                ", greatScore=" + config.getGreatScore() +
                '}';
        assertEquals(toString1,toString2);
    }

    @Test
    void getName() {
        Config config = new Config();
        config.setName("NAME");
        assertEquals("NAME",config.getName());
    }

    @Test
    void getNameLarge() {
        Config config = new Config();
        config.setName("ARVINBAYATMAESHSFUSTUDENT");
        assertEquals("ARVINBAYATMAESHSFUSTUDENT",config.getName());
    }

    @Test
    void getPoorScore() {
        Config config = new Config();
        config.setPoorScore(10);
        assertEquals(10, config.getPoorScore());
    }

    @Test
    void getPoorScoreLarge() {
        Config config = new Config();
        config.setPoorScore(999999999);
        assertEquals(999999999, config.getPoorScore());
    }

    @Test
    void getGreatScore() {
        Config config = new Config();
        config.setGreatScore(100);
        assertEquals(100, config.getGreatScore());
    }

    @Test
    void setGreatScoreLarge() {
        Config config = new Config();
        config.setGreatScore(999999999);
        assertEquals(999999999, config.getGreatScore());
    }
}