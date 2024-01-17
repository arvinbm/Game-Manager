package ca.cmpt276.project_7f;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ca.cmpt276.project_7f.model.Game;
import ca.cmpt276.project_7f.model.GameManager;
import ca.cmpt276.project_7f.utils.SharedPreferencesUtils;

// adding a new game.
public class GameActivity extends AppCompatActivity {

    private static final String INDEX_OF_GAME_IN_LIST = "indexOfGameInList";
    private static final String CONFIG_NAME = "configName";
    private EditText et_numPlayer;
    private TextView tv_game_toolbar_title;
    private LinearLayout linearlayoutForScores;
    private Button btn_generateEditTexts;
    private ImageView btn_saveGame;
    private ImageView btn_back;
    private int indexOfGameInList;
    private String configName;
    private boolean isAddMode;
    private Spinner spinner_difficulty_game;
    private Spinner spinner_theme_game;
    private SoundPool soundPool;
    private int soundGameId;
    private ImageView btn_goToPhoto;

    private String imageString;
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == 100) {
                        Intent intent = result.getData();
                        if(intent != null) {
                            imageString = intent.getStringExtra("imageString");
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }

    @Override
    protected void onResume() {
        super.onResume();

        initial();
        initialSoundPool();
        onButtonsClick();
        extractDataFromIntent();
        populateDifficultySpinner();
        populateThemeSpinner();
        setMode();
    }

    private void populateDifficultySpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                (this, R.array.difficulty_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_difficulty_game.setAdapter(adapter);
    }

    private void populateThemeSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                (this, R.array.theme_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_theme_game.setAdapter(adapter);
    }

    private void onGenerateEditTextButtonClick() {
        // check validity for number of players.
        if(et_numPlayer.length() == 0)
        {
            Toast.makeText(this,"number player must not be empty.",Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        String s = et_numPlayer.getText().toString();
        int numPlayer = Integer.parseInt(s);
        if(numPlayer == 0)
        {
            Toast.makeText(this,"number player must not be 0.",Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        // Add already entered scores into an ArrayList
        ArrayList<Integer> scoreList = addScoresToArrayList();

        // remove old views
        linearlayoutForScores.removeAllViews();

        // add new view
        addEditTextToLinerLayout(numPlayer,scoreList);
    }

    // This method adds previously added scores to an ArrayList to populate the edit texts
    // when the number of players are changed.
    private ArrayList<Integer> addScoresToArrayList() {
        ArrayList<Integer> scoreList = new ArrayList<>();
        for (int i = 0; i < linearlayoutForScores.getChildCount(); ++i) {
            EditText etScore = (EditText) linearlayoutForScores.getChildAt(i);
            if (etScore.length() == 0) {
                // Do not do anything if the edit text empty
            }
            else {
                scoreList.add(Integer.parseInt(etScore.getText().toString()));
            }
        }
        return scoreList;
    }

    private void addEditTextToLinerLayout(int numOfPlayers, ArrayList<Integer> scoreList) {
        int count = 0;
        for(int i = 0; i < numOfPlayers; i++)
        {
            EditText editText = new EditText(this);
            if(scoreList != null && count < scoreList.size())
            {
                int score = scoreList.get(i);
                editText.setText(String.valueOf(score));
                count++;
            }
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            linearlayoutForScores.addView(editText);
        }
    }

    private void initial() {
        et_numPlayer = findViewById(R.id.spinner_difficulty_achievment);
        btn_saveGame = findViewById(R.id.game_save_button_game);
        btn_back = findViewById(R.id.game_back_button);
        tv_game_toolbar_title = findViewById(R.id.tv_game_toolbar_title);
        btn_generateEditTexts = findViewById(R.id.btn_generateEditTexts);
        linearlayoutForScores = findViewById(R.id.linearlayoutForScores);
        spinner_difficulty_game = findViewById(R.id.spinner_difficulty_game);
        spinner_theme_game = findViewById(R.id.spinner_theme_game);
        btn_goToPhoto = findViewById(R.id.iv_camera_game);
    }

    private void initialSoundPool()
    {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build();

        soundGameId = soundPool.load(this, R.raw.game_win ,1);
    }


    private void setMode() {
        isAddMode = indexOfGameInList == -1;
        if(isAddMode)
        {
            tv_game_toolbar_title.setText("Add Game");
        }
        else
        {
            tv_game_toolbar_title.setText("Edit Game");
            showData();
        }
    }

    private void showData() {
        GameManager instanceOfGameM = GameManager.getInstance();
        Game game = instanceOfGameM.getGame(configName, indexOfGameInList);
        int numOfPlayers = game.getNumOfPlayers();
        String difficulty = game.getDifficulty();
        String theme = game.getTheme();
        ArrayList<Integer> scoreList = game.getScoreList();
        et_numPlayer.setText(String.valueOf(numOfPlayers));
        // set difficulty spinner selection
        for (int i = 0; i < spinner_difficulty_game.getCount(); i++)
        {
            String str = spinner_difficulty_game.getItemAtPosition(i).toString();
            if(difficulty.equals(str))
            {
                spinner_difficulty_game.setSelection(i);
                break;
            }
        }
        // set theme spinner selection
        for(int i = 0; i < spinner_theme_game.getCount(); i++)
        {
            String str = spinner_theme_game.getItemAtPosition(i).toString();
            if(theme.equals(str))
            {
                spinner_theme_game.setSelection(i);
                break;
            }
        }
        //show score list
        addEditTextToLinerLayout(numOfPlayers,scoreList);
    }

    private void extractDataFromIntent() {
        Intent intent = getIntent();
        indexOfGameInList = intent.getIntExtra(INDEX_OF_GAME_IN_LIST,-1);
        configName = intent.getStringExtra(CONFIG_NAME);
    }

    private void onButtonsClick() {
        btn_saveGame.setOnClickListener(v->onSaveClick());
        btn_back.setOnClickListener(v->onBackClick());
        btn_generateEditTexts.setOnClickListener(v-> onGenerateEditTextButtonClick());
        btn_goToPhoto.setOnClickListener(v-> goToPhotoPage());
    }

    private void goToPhotoPage() {
        Intent intent = PhotoActivity.makeIntentForGame(getApplicationContext(), indexOfGameInList, configName);
        activityResultLauncher.launch(intent);
    }

    private void onBackClick() {
        finish();
    }

    private void onSaveClick() {
        // Check if null
        if(et_numPlayer.length() == 0)
        {
            Toast.makeText(this,"All values must be not empty.",Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if(linearlayoutForScores.getChildCount() == 0)
        {
            Toast.makeText(this,"Input score list must be generated ",Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        processGame();
    }

    private void processGame() {
        // Get input data
        String strNumOfPlayers = et_numPlayer.getText().toString();
        int numOfPlayers = Integer.parseInt(strNumOfPlayers);
        String strDifficultyInGame = spinner_difficulty_game.getSelectedItem().toString();
        String strTheme = spinner_theme_game.getSelectedItem().toString();

        // read score list
        if(linearlayoutForScores.getChildCount() != numOfPlayers)
        {
            Toast.makeText(this,"num scores must equal to num players",Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        ArrayList<Integer> scoreList = new ArrayList<>();
        for(int i = 0; i < linearlayoutForScores.getChildCount(); i++)
        {
            EditText et_score = (EditText) linearlayoutForScores.getChildAt(i);
            if(et_score.length() == 0)
            {
                Toast.makeText(this,"All scores must not be empty.",Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            scoreList.add(Integer.parseInt(et_score.getText().toString()));
        }

        // Do the logical part
        GameManager instanceOfGameM = GameManager.getInstance();
        if(isAddMode)
        {
            instanceOfGameM.addGame(configName,numOfPlayers,scoreList,strDifficultyInGame,strTheme, imageString);
        }
        else
        {
            instanceOfGameM.updateOneGameWhenGameChanges(configName,
                    indexOfGameInList,
                    strDifficultyInGame,
                    scoreList,
                    numOfPlayers,
                    strTheme,
                    imageString
            );
        }

        saveDataToSP();
        popUpDialog();
    }

    private void popUpDialog()
    {
        // play sound
        playSounds();

        // pop up dialog
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        MessageFragment messageFragment = new MessageFragment();
        messageFragment.setter(configName,indexOfGameInList,isAddMode);
        messageFragment.show(supportFragmentManager,"MessageFragment");
    }

    private void playSounds() {
        soundPool.play(soundGameId,1,1,1,0,1);
    }


    private void saveDataToSP() {
        SharedPreferencesUtils.saveDataOfGameManager(getApplicationContext());
    }

    public static Intent makeIntent(Context context, int indexOfConfigInList, String configName)
    {
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra(INDEX_OF_GAME_IN_LIST,indexOfConfigInList);
        intent.putExtra(CONFIG_NAME,configName);
        return intent;
    }
}