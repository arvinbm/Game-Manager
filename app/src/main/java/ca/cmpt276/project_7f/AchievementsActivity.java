package ca.cmpt276.project_7f;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import ca.cmpt276.project_7f.model.Config;
import ca.cmpt276.project_7f.model.ConfigManager;
import ca.cmpt276.project_7f.model.Game;

//Activity that displays all the Achievement options from 1-10
public class AchievementsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private int indexOfConfigInList;
    private ImageView btn_back;
    private EditText numberOfPlayer_tv;
    private Spinner spinner_difficulty_achievement;
    private Spinner spinner_theme;
    private TextView tv0;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView tv5;
    private TextView tv6;
    private TextView tv7;
    private TextView tv8;
    private TextView tv9;
    private TextView achievement0;
    private TextView achievement1;
    private TextView achievement2;
    private TextView achievement3;
    private TextView achievement4;
    private TextView achievement5;
    private TextView achievement6;
    private TextView achievement7;
    private TextView achievement8;
    private TextView achievement9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achivements);
    }

    @Override
    protected void onResume() {
        super.onResume();

        extractDataFromIntent();
        initial();
        textWatcher();
        populateDifficultySpinner();
        populateThemeSpinner();
        onClickButtons();
    }

    private void populateDifficultySpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                (this, R.array.difficulty_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_difficulty_achievement.setAdapter(adapter);
        spinner_difficulty_achievement.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (numberOfPlayer_tv.length() != 0) {
            showData();
        }

        else {
            tv0.setText("-------");
            tv1.setText("-------");
            tv2.setText("-------");
            tv3.setText("-------");
            tv4.setText("-------");
            tv5.setText("-------");
            tv6.setText("-------");
            tv7.setText("-------");
            tv8.setText("-------");
            tv9.setText("-------");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void populateThemeSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                (this, R.array.theme_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_theme.setAdapter(adapter);
        spinner_theme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String theme = parent.getItemAtPosition(position).toString();
                setTheme(theme);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void textWatcher() {
        numberOfPlayer_tv.addTextChangedListener(textWatcher);
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (numberOfPlayer_tv.length() == 0) {
                tv0.setText("-------");
                tv1.setText("-------");
                tv2.setText("-------");
                tv3.setText("-------");
                tv4.setText("-------");
                tv5.setText("-------");
                tv6.setText("-------");
                tv7.setText("-------");
                tv8.setText("-------");
                tv9.setText("-------");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (numberOfPlayer_tv.length() == 0) {
                tv0.setText("-------");
                tv1.setText("-------");
                tv2.setText("-------");
                tv3.setText("-------");
                tv4.setText("-------");
                tv5.setText("-------");
                tv6.setText("-------");
                tv7.setText("-------");
                tv8.setText("-------");
                tv9.setText("-------");
            }
            else {
                showData();
            }
        }
    };

    private void showData() {
        String difficulty = spinner_difficulty_achievement.getSelectedItem().toString();
        if (Objects.equals(difficulty, "Normal") || Objects.equals(difficulty, "Hard")
        || Objects.equals(difficulty, "Easy")) {
            ArrayList<String> rangesArray = getTheRangesBasedOnTheDifficultyLevel(difficulty);
            tv0.setText(rangesArray.get(0));
            tv1.setText(rangesArray.get(1));
            tv2.setText(rangesArray.get(2));
            tv3.setText(rangesArray.get(3));
            tv4.setText(rangesArray.get(4));
            tv5.setText(rangesArray.get(5));
            tv6.setText(rangesArray.get(6));
            tv7.setText(rangesArray.get(7));
            tv8.setText(rangesArray.get(8));
            tv9.setText(rangesArray.get(9));
        }
        else {
            Toast.makeText(this, "Difficulty level is invalid", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private ArrayList<String> getTheRangesBasedOnTheDifficultyLevel(String difficulty) {
        ConfigManager instance = ConfigManager.getInstance();
        Config configByIndex = instance.getConfigByIndex(indexOfConfigInList);
        String name = configByIndex.getName();
        String value = numberOfPlayer_tv.getText().toString();
        int numOfPlayers = Integer.parseInt(value);

        ArrayList<String> rangesArray = new ArrayList<>();
        // We pass an empty string for the theme in Achievement activity because
        // theme is not important in this activity for the game object
        Game game = new Game(name, numOfPlayers, null, difficulty, "","");

        switch(difficulty) {
            case "Normal":
                rangesArray = game.getStringOfRanges(1);
                break;

            case "Hard":
                rangesArray = game.getStringOfRanges(1.25);
                break;

            case "Easy":
                rangesArray = game.getStringOfRanges(0.75);
                break;
        }

        return rangesArray;
    }

    private void extractDataFromIntent() {
        Intent intent = getIntent();
        indexOfConfigInList = intent.getIntExtra("indexOfConfigInList",-1);
    }

    private void initial() {
        numberOfPlayer_tv = findViewById(R.id.et_numPlayer_achievement);
        btn_back = findViewById(R.id.achievements_back_button);
        spinner_difficulty_achievement = findViewById(R.id.spinner_difficulty_achievment);
        spinner_theme = findViewById(R.id.spinner_theme_achievement);
        tv0 = findViewById(R.id.tv_range0);
        tv1 = findViewById(R.id.tv_range1);
        tv2 = findViewById(R.id.tv_range2);
        tv3 = findViewById(R.id.tv_range3);
        tv4 = findViewById(R.id.tv_range4);
        tv5 = findViewById(R.id.tv_range5);
        tv6 = findViewById(R.id.tv_range6);
        tv7 = findViewById(R.id.tv_range7);
        tv8 = findViewById(R.id.tv_range8);
        tv9 = findViewById(R.id.tv_range9);
        achievement0 = findViewById(R.id.tv_achievement1);
        achievement1 = findViewById(R.id.tv_achievement2);
        achievement2 = findViewById(R.id.tv_achievement3);
        achievement3 = findViewById(R.id.tv_achievement4);
        achievement4 = findViewById(R.id.tv_achievement5);
        achievement5 = findViewById(R.id.tv_achievement6);
        achievement6 = findViewById(R.id.tv_achievement7);
        achievement7 = findViewById(R.id.tv_achievement8);
        achievement8 = findViewById(R.id.tv_achievement9);
        achievement9 = findViewById(R.id.tv_achievement10);
    }

    private void setTheme(String theme) {
        if (Objects.equals(theme, "Disney Characters")) {
            Resources res = getResources();
            String[] disneyCharacters = res.getStringArray(R.array.achievement_level_disney);
            achievement0.setText(disneyCharacters[0]);
            achievement1.setText(disneyCharacters[1]);
            achievement2.setText(disneyCharacters[2]);
            achievement3.setText(disneyCharacters[3]);
            achievement4.setText(disneyCharacters[4]);
            achievement5.setText(disneyCharacters[5]);
            achievement6.setText(disneyCharacters[6]);
            achievement7.setText(disneyCharacters[7]);
            achievement8.setText(disneyCharacters[8]);
            achievement9.setText(disneyCharacters[9]);

        }
        else if (Objects.equals(theme, "Marvel Heroes")) {
            Resources res = getResources();
            String[] marvelCharacters = res.getStringArray(R.array.achievement_level_marvel);
            achievement0.setText(marvelCharacters[0]);
            achievement1.setText(marvelCharacters[1]);
            achievement2.setText(marvelCharacters[2]);
            achievement3.setText(marvelCharacters[3]);
            achievement4.setText(marvelCharacters[4]);
            achievement5.setText(marvelCharacters[5]);
            achievement6.setText(marvelCharacters[6]);
            achievement7.setText(marvelCharacters[7]);
            achievement8.setText(marvelCharacters[8]);
            achievement9.setText(marvelCharacters[9]);

        }
        else {
            Resources res = getResources();
            String[] crazyAnimals = res.getStringArray(R.array.achievement_level_animals);
            achievement0.setText(crazyAnimals[0]);
            achievement1.setText(crazyAnimals[1]);
            achievement2.setText(crazyAnimals[2]);
            achievement3.setText(crazyAnimals[3]);
            achievement4.setText(crazyAnimals[4]);
            achievement5.setText(crazyAnimals[5]);
            achievement6.setText(crazyAnimals[6]);
            achievement7.setText(crazyAnimals[7]);
            achievement8.setText(crazyAnimals[8]);
            achievement9.setText(crazyAnimals[9]);
        }
    }

    public static Intent makeIntent(Context context, int indexOfConfigInList)
    {
        Intent intent = new Intent(context, AchievementsActivity.class);
        intent.putExtra("indexOfConfigInList", indexOfConfigInList);
        return intent;
    }

    private void onClickButtons() {
        btn_back.setOnClickListener(v->onBackClick());
    }

    private void onBackClick() {
        finish();
    }
}