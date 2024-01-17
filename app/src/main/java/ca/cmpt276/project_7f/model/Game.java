package ca.cmpt276.project_7f.model;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

// game
public class Game {
    private String configName;
    private int numOfPlayers;
    private int score;
    private String achievement;
    private final String time;
    private String difficulty;
    private String theme;
    private ArrayList<Integer> scoreList;
    private String imageString;

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    public ArrayList<Integer> getScoreList() {
        return scoreList;
    }

    public void setScoreList(ArrayList<Integer> scoreList) {
        this.scoreList = scoreList;
        computeTotalScore();
    }

    private void computeTotalScore()
    {
        int sum = 0;
        for (int i = 0; i < scoreList.size(); i++)
        {
            sum = sum + scoreList.get(i);
        }
        score = sum;
    }

    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getTheme() {
        return theme;
    }

    public Game(String _configName, int _numOfPlayer, ArrayList<Integer> _scoreList, String difficulty, String theme, String imageString) {
        configName = _configName;
        numOfPlayers = _numOfPlayer;
        scoreList = _scoreList;
        this.difficulty = difficulty;
        this.theme = theme;
        this.imageString = imageString;
        if (_scoreList != null) {
            double difficultyLevel = convertStrDifficultyToDouble(difficulty);
            computeAchievement(difficultyLevel);
            computeTotalScore();
        }
        time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd @ HH:mm a"));
    }


    private double convertStrDifficultyToDouble(String difficultyStr) {
        double difficulty = 1.0;
        switch(difficultyStr) {
            case "Normal":
                difficulty =  1.0;
                break;

            case "Hard":
                difficulty =  1.25;
                break;

            case "Easy":
                difficulty =  0.75;
                break;
        }
        return difficulty;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getTime()
    {
        return time;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public int getScore() {
        return score;
    }

    public String getAchievement() {
        computeAchievement(convertStrDifficultyToDouble(difficulty));
        return achievement;
    }

    @Override
    public String toString() {
        return "Game{" +
                "configName='" + configName + '\'' +
                ", numOfPlayers=" + numOfPlayers +
                ", score=" + score +
                ", achievement='" + achievement + '\'' +
                ", time='" + time + '\'' +
                ", difficulty='" + difficulty + '\'' +
                '}';
    }

    public ArrayList<String> getStringOfRanges(double difficulty) {
        ConfigManager configManager = ConfigManager.getInstance();
        Config configByName = configManager.getConfigByName(configName);

        // Create an array of strings which stores the ranges
        ArrayList<String> ranges = new ArrayList<>();

        int greatScore = configByName.getGreatScore();
        int poorScore = configByName.getPoorScore();
        double highestExpectedLevel = greatScore * numOfPlayers * difficulty;
        double lowestExpectedLevel = poorScore * numOfPlayers * difficulty;
        double unit = (highestExpectedLevel - lowestExpectedLevel) / 8;

        ranges.add(highestExpectedLevel + " and above");

        for (int i = 8; i > 0; --i) {
            double lowerLimit = lowestExpectedLevel + unit * (i - 1);
            double higherLevel = lowestExpectedLevel + unit * i;
            String range = lowerLimit + " - " + higherLevel;
            ranges.add(range);
        }

        ranges.add(lowestExpectedLevel + " and below");

        return ranges;
    }

    public void computeAchievement(double difficulty)
    {
        ConfigManager configManager = ConfigManager.getInstance();
        Config configByName = configManager.getConfigByName(configName);

        if(configByName == null)
        {
            throw new IllegalArgumentException("No such name found.");
        }

        else {
            int greatScore = configByName.getGreatScore();
            int poorScore = configByName.getPoorScore();
            double highestExpectedLevel = greatScore * numOfPlayers * difficulty;
            double lowestExpectedLevel = poorScore * numOfPlayers * difficulty;
            double unit = (highestExpectedLevel - lowestExpectedLevel) / 8;

            ArrayList<String> AchievementBasedOnTheme = populateAchievementBasedOnTheme();

            if (score >= highestExpectedLevel)
                achievement = AchievementBasedOnTheme.get(0);

            else if (score >= lowestExpectedLevel + unit * 7 && score < lowestExpectedLevel + unit * 8)
                achievement = AchievementBasedOnTheme.get(1);

            else if (score >= lowestExpectedLevel + unit * 6 && score < lowestExpectedLevel + unit * 7)
                achievement = AchievementBasedOnTheme.get(2);

            else if (score >= lowestExpectedLevel + unit * 5 && score < lowestExpectedLevel + unit * 6)
                achievement = AchievementBasedOnTheme.get(3);

            else if (score >= lowestExpectedLevel + unit * 4 && score < lowestExpectedLevel + unit * 5)
                achievement = AchievementBasedOnTheme.get(4);

            else if (score >= lowestExpectedLevel + unit * 3 && score < lowestExpectedLevel + unit * 4)
                achievement = AchievementBasedOnTheme.get(5);

            else if (score >= lowestExpectedLevel + unit * 2 && score < lowestExpectedLevel + unit * 3)
                achievement = AchievementBasedOnTheme.get(6);

            else if (score >= lowestExpectedLevel + unit && score < lowestExpectedLevel + unit * 2)
                achievement = AchievementBasedOnTheme.get(7);

            else if (score >= lowestExpectedLevel && score < lowestExpectedLevel + unit)
                achievement = AchievementBasedOnTheme.get(8);

            else if (score < lowestExpectedLevel)
                achievement = AchievementBasedOnTheme.get(9);
        }
    }

    public double getUpperBound()
    {
        ConfigManager configManager = ConfigManager.getInstance();
        Config configByName = configManager.getConfigByName(configName);
        int greatScore = configByName.getGreatScore();
        int poorScore = configByName.getPoorScore();
        double highestExpectedLevel = greatScore * numOfPlayers * convertStrDifficultyToDouble(difficulty);
        double lowestExpectedLevel = poorScore * numOfPlayers * convertStrDifficultyToDouble(difficulty);
        double unit = (highestExpectedLevel - lowestExpectedLevel) / 8;
        double ret = 0;

        if (score >= highestExpectedLevel)
            ret = -1;
        else if (score >= lowestExpectedLevel + unit * 7 && score < lowestExpectedLevel + unit * 8)
            ret = lowestExpectedLevel + unit * 8;
        else if (score >= lowestExpectedLevel + unit * 6 && score < lowestExpectedLevel + unit * 7)
            ret = lowestExpectedLevel + unit * 7;
        else if (score >= lowestExpectedLevel + unit * 5 && score < lowestExpectedLevel + unit * 6)
            ret = lowestExpectedLevel + unit * 6;
        else if (score >= lowestExpectedLevel + unit * 4 && score < lowestExpectedLevel + unit * 5)
            ret = lowestExpectedLevel + unit * 5;
        else if (score >= lowestExpectedLevel + unit * 3 && score < lowestExpectedLevel + unit * 4)
            ret = lowestExpectedLevel + unit * 4;
        else if (score >= lowestExpectedLevel + unit * 2 && score < lowestExpectedLevel + unit * 3)
            ret = lowestExpectedLevel + unit * 3;
        else if (score >= lowestExpectedLevel + unit && score < lowestExpectedLevel + unit * 2)
            ret = lowestExpectedLevel + unit * 2;
        else if (score >= lowestExpectedLevel + unit * 0 && score < lowestExpectedLevel + unit * 1)
            ret = lowestExpectedLevel + unit * 1;
        else if (score < lowestExpectedLevel)
            ret = lowestExpectedLevel;

        return ret;
    }

    private ArrayList<String> populateAchievementBasedOnTheme() {
        ArrayList<String>  AchievementBasedOnTheme = new ArrayList<>();

        switch (theme) {
            case "Crazy Animals":
                AchievementBasedOnTheme.add("Victorious Whales");
                AchievementBasedOnTheme.add("Glorious Giraffes");
                AchievementBasedOnTheme.add("Brave Bears");
                AchievementBasedOnTheme.add("Pretty Penguins");
                AchievementBasedOnTheme.add("Reckless Racoons");
                AchievementBasedOnTheme.add("Crazy Crows");
                AchievementBasedOnTheme.add("Rowdy Rats");
                AchievementBasedOnTheme.add("Fat Flies");
                AchievementBasedOnTheme.add("Lazy Lizards");
                AchievementBasedOnTheme.add("Slow Snakes");
                break;

            case "Marvel Heroes":
                AchievementBasedOnTheme.add("Spider-Mans");
                AchievementBasedOnTheme.add("Iron Mans");
                AchievementBasedOnTheme.add("Captain Americas");
                AchievementBasedOnTheme.add("Thors");
                AchievementBasedOnTheme.add("Black Panthers");
                AchievementBasedOnTheme.add("Ant-Mans");
                AchievementBasedOnTheme.add("Wolverines");
                AchievementBasedOnTheme.add("Hulks");
                AchievementBasedOnTheme.add("Black Windows");
                AchievementBasedOnTheme.add("Thanos");
                break;

            case "Disney Characters":
                AchievementBasedOnTheme.add("Cookie Monsters");
                AchievementBasedOnTheme.add("Kim Possibles");
                AchievementBasedOnTheme.add("Aladdins");
                AchievementBasedOnTheme.add("Peter Pans");
                AchievementBasedOnTheme.add("Tarzans");
                AchievementBasedOnTheme.add("Pinocchios");
                AchievementBasedOnTheme.add("Bambis");
                AchievementBasedOnTheme.add("Elmos");
                AchievementBasedOnTheme.add("Olafs");
                AchievementBasedOnTheme.add("Shreks");
                break;
        }

        return AchievementBasedOnTheme;
    }

    public String getStringOfDisplayGame()
    {
        return "Time created: " + time + "\n"
                + "Combined score: " + score + "\n"
                + "Achievement: " + getAchievement() + "\n"
                + "Difficulty: " + difficulty + "\n"
                + "Theme: " + theme;
    }
}
