package ca.cmpt276.project_7f.model;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import ca.cmpt276.project_7f.R;

// managing the list of game.
public class GameManager {
    private ArrayList<Game> gameList;
    private static GameManager instance;

    private GameManager()
    {
        gameList = new ArrayList<>();
    }

    public ArrayList<Game> getGameList() {
        return gameList;
    }

    public ArrayList<Game> getGameListByConfigName(String _configName)
    {
        ArrayList<Game> targetGameList = new ArrayList<>();
        for (int i = 0; i < gameList.size(); i++)
        {
            Game game = gameList.get(i);
            String configName = game.getConfigName();
            if(configName.equals(_configName))
            {
                targetGameList.add(game);
            }
        }
        return targetGameList;
    }

    // return the list with considering all games as normal different.
    public ArrayList<Integer> getCountOfEachAchievementInCorrespondingGameList(Context context, String _configName)
    {
        ArrayList<Integer> resultList = new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,0));
        ArrayList<Game> gameListByConfigName = getGameListByConfigName(_configName);
        for(int i = 0; i < gameListByConfigName.size(); i++)
        {
            Game game = gameListByConfigName.get(i);
            String achievement = game.getAchievement();

            String[] stringsA = context.getResources().getStringArray(R.array.achievement_level_animals);
            String[] stringsB = context.getResources().getStringArray(R.array.achievement_level_disney);
            String[] stringsC = context.getResources().getStringArray(R.array.achievement_level_marvel);
            for(int j = 0; j < 10; j++)
            {
                if ((achievement.equals(stringsA[j])) || achievement.equals(stringsB[j]) || achievement.equals(stringsC[j]))
                {
                    Integer integer = resultList.get(j);
                    resultList.set(j, ++integer);
                }
            }
        }
        return resultList;
    }

    public void setGameList(ArrayList<Game> gameList) {
        this.gameList = gameList;
    }

    public static GameManager getInstance()
    {
        // use instance because we only have one game list.
        if(instance == null)
        {
            instance = new GameManager();
        }
        return instance;
    }

    public int getSizeOfGameListByName(String configName)
    {
        int size = 0;
        for(int i = 0; i < gameList.size(); i++)
        {
            Game game = gameList.get(i);
            String configNameFromGame = game.getConfigName();
            if(configName.equals(configNameFromGame))
            {
                size++;
            }
        }
        return size;
    }

    public ArrayList<String> getDisplayedStringListByName(String configName)
    {
        ArrayList<String> stringList = new ArrayList<>();
        for(int i = 0; i < gameList.size(); i++)
        {
            Game game = gameList.get(i);
            String configNameFromGame = game.getConfigName();
            if(configName.equals(configNameFromGame))
            {
                String stringOfDisplayGame = game.getStringOfDisplayGame();
                stringList.add(stringOfDisplayGame);
            }
        }
        if(stringList.size()==0)
            return null;
        else
            return stringList;
    }

    public void addGame(String configName, int numOfPlayer, ArrayList<Integer> scoreList, String difficulty, String theme, String photoJson)
    {
        Game game = new Game(configName,numOfPlayer,scoreList,difficulty,theme,photoJson);
        gameList.add(game);
    }

    public void removeGames(String configName)
    {
        for(int i = 0; i < gameList.size(); i++)
        {
            Game game = gameList.get(i);
            if(configName.equals(game.getConfigName()))
            {
                gameList.remove(i);
            }
        }
    }

    public void updateGamesWhenConfigChanges(String oldConfigName, Config newConfig)
    {
        String updatedConfigName = newConfig.getName();

        for(int i = 0; i < gameList.size(); i++)
        {
            Game game = gameList.get(i);
            String difficulty = game.getDifficulty();
            if(difficulty == null)
            {
                difficulty = "Normal";
            }
            if(oldConfigName.equals(game.getConfigName()))
            {
                game.setConfigName(updatedConfigName);
                switch (difficulty) {
                    case "Normal":
                        game.computeAchievement(1);
                        break;
                    case "Easy":
                        game.computeAchievement(0.75);
                        break;
                    case "Hard":
                        game.computeAchievement(1.25);
                        break;
                }
            }
        }
    }

    public void updateOneGameWhenGameChanges(
            String configName, int indexInGameList, String difficulty, ArrayList<Integer> scoreList,
            int numberOfPlayers, String strTheme, String photoJson)
    {
        Game targetGame = getGame(configName,indexInGameList);
        targetGame.setDifficulty(difficulty);
        targetGame.setScoreList(scoreList);
        targetGame.setNumOfPlayers(numberOfPlayers);
        targetGame.setTheme(strTheme);
        targetGame.setImageString(photoJson);

        // Determine the level of difficulty
        if (Objects.equals(difficulty, "Normal")) {
            targetGame.computeAchievement(1);
        }
        else if (Objects.equals(difficulty, "Hard")) {
            targetGame.computeAchievement(1.25);
        }
        else if (Objects.equals(difficulty, "Easy")) {
            targetGame.computeAchievement(0.75);
        }
    }

    public Game getGame(String configName, int indexInGameList)
    {
        ArrayList<Game> tempGameList = new ArrayList<>();
        for(int i = 0; i < gameList.size(); i++)
        {
            Game game = gameList.get(i);
            if(configName.equals(game.getConfigName()))
            {
                tempGameList.add(game);
            }
        }
        if(tempGameList.size()!=0)
            return tempGameList.get(indexInGameList);
        else
            return null;
    }



}
