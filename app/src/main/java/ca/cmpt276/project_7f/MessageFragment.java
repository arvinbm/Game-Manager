package ca.cmpt276.project_7f;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import java.util.ArrayList;
import ca.cmpt276.project_7f.model.Game;
import ca.cmpt276.project_7f.model.GameManager;

// customized dialog.
public class MessageFragment extends AppCompatDialogFragment {

    private String achievement;
    private Spinner spinner;
    private String[] animalsAchievements;
    private String[] disneyAchievements;
    private String[] marvelAchievement;
    private View view;
    private StringBuilder stringBuilder;
    private AlertDialog alertDialog;
    private Animation animation;
    private ImageView imageView_celebrate;
    private int score;
    private double diff;
    private Game game;

    public void setter(String configName, int indexOfGameInList,boolean isAddMode)
    {
        GameManager instanceOfGM = GameManager.getInstance();
        if(!isAddMode)
        {
            game = instanceOfGM.getGame(configName, indexOfGameInList);
            score = game.getScore();
            achievement = game.getAchievement();
        }
        else
        {
            ArrayList<Game> gameList = instanceOfGM.getGameList();
            game = gameList.get(gameList.size() - 1);
            score = game.getScore();
            achievement = game.getAchievement();
        }
    }

    private void initial()
    {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.message_layout, null);
        imageView_celebrate = view.findViewById(R.id.imageView_celebrate);
        animation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        stringBuilder = new StringBuilder();
        stringBuilder.append(achievement);
    }

    private int getIndexInAchievementList(String achievementName)
    {
        animalsAchievements = getContext().getResources().getStringArray(R.array.achievement_level_animals);
        disneyAchievements = getContext().getResources().getStringArray(R.array.achievement_level_disney);
        marvelAchievement = getContext().getResources().getStringArray(R.array.achievement_level_marvel);

        int ret = 0;
        for(int i = 0; i < 10; i++)
        {
            if ((achievementName.equals(animalsAchievements[i])) || achievementName.equals(disneyAchievements[i]) || achievementName.equals(marvelAchievement[i]))
            {
                ret = i;
                break;
            }
        }
        return ret;
    }

    private void animation()
    {
        view.setAnimation(animation);
    }

    private void spinner()
    {
        spinner = view.findViewById(R.id.spinner_celebration);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.theme_list, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String selection = spinner.getSelectedItem().toString();
                String[] themes = getContext().getResources().getStringArray(R.array.theme_list);
                String animals = themes[0];
                String disney = themes[1];
                String marvel = themes[2];
                int indexInAchievementList = getIndexInAchievementList(achievement);

                if(selection.equals(animals))
                {
                    String temp = animalsAchievements[indexInAchievementList];
                    stringBuilder.replace(0,stringBuilder.capacity()-1,temp);
                    alertDialog.setMessage("You have got the achievement:\n " + "< " + stringBuilder.toString() + ">.\n" + "Number of point to achieve the next level is <" + diff + ">.");
                }
                else if(selection.equals(disney))
                {
                    String temp = disneyAchievements[indexInAchievementList];
                    stringBuilder.replace(0,stringBuilder.capacity()-1,temp);
                    alertDialog.setMessage("You have got the achievement:\n " + "< " + stringBuilder.toString() + ">.\n" + "Number of point to achieve the next level is <" + diff + ">.");
                }
                else if(selection.equals(marvel))
                {
                    String temp = marvelAchievement[indexInAchievementList];
                    stringBuilder.replace(0,stringBuilder.capacity()-1,temp);
                    alertDialog.setMessage("You have got the achievement:\n " + "< " + stringBuilder.toString() + ">.\n" + "Number of point to achieve the next level is <" + diff + ">.");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }

    private void computeDiff()
    {
        double upperBound = game.getUpperBound();
        if(upperBound == -1)
        {
            diff = 0;
        }
        else
        {
            int score = game.getScore();
            diff = upperBound - score;
        }
    }


    @NonNull
    @SuppressLint("MissingInflatedId")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        initial();
        computeDiff();
        animation();
        spinner();
        generateDialog();
        return alertDialog;

    }

    private void generateDialog() {
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    getActivity().finish();
                }
            }
        };

        alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("Congratulations!")
                .setMessage("You have got the achievement:\n " + "< " + stringBuilder.toString() + ">.\n" + "Number of point to achieve the next level is <" + diff + ">.")
                .setView(view)
                .setNeutralButton("Replay the animation",null)
                .setPositiveButton(android.R.string.ok, listener)
                .create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imageView_celebrate.startAnimation(animation);
                    }
                });
            }
        });
    }
}
