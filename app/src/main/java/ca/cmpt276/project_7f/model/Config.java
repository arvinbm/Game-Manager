package ca.cmpt276.project_7f.model;

// configuration
public class Config {
    private String configName;
    private int poorScore;
    private int greatScore;
    private String imageString;

    public Config() {
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    public String getName() {
        return configName;
    }

    public void setName(String name) {
        this.configName = name;
    }

    public int getPoorScore() {
        return poorScore;
    }

    public void setPoorScore(int poorScore) {
        this.poorScore = poorScore;
    }

    public int getGreatScore() {
        return greatScore;
    }

    public void setGreatScore(int greatScore) {
        this.greatScore = greatScore;
    }

    @Override
    public String toString() {
        return "Config{" +
                "configName='" + configName + '\'' +
                ", poorScore=" + poorScore +
                ", greatScore=" + greatScore +
                '}';
    }
}
