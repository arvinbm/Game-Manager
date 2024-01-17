package ca.cmpt276.project_7f;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ca.cmpt276.project_7f.model.Config;
import ca.cmpt276.project_7f.model.ConfigManager;
import ca.cmpt276.project_7f.utils.SharedPreferencesUtils;

// showing the configuration.
public class ConfigurationActivity extends AppCompatActivity {

    public static final String ADD_A_CONFIGURATION = "Add a configuration";
    public static final String EDIT_A_CONFIGURATION = "Edit a configuration";
    private ConfigManager instanceOfCM;
    private boolean isAddMode;
    private int indexOfConfigInList;
    private Button btn_goToGameList;
    private Button btn_goToAchievements;
    private Button btn_delete;
    private ImageView btn_save;
    private ImageView btn_back;
    private ImageView btn_photo;
    private EditText et_configName;
    private EditText et_configGreatScore;
    private EditText et_configPoorScore;
    private Button btn_goToBarChart;
    private String imageString;
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == 101) {
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
        setContentView(R.layout.activity_configuration);
    }

    @Override
    protected void onResume() {
        super.onResume();

        initial();
        extractDataFromIntent();
        toolbar();
        setButtonInvisible();
        fillData();
        onClickButtons();
    }

    public static Intent makeIntent(Context context, int index) {
        Intent intent = new Intent(context, ConfigurationActivity.class);
        intent.putExtra("indexOfConfigInList", index);
        return intent;
    }

    private void initial() {
        btn_goToGameList = findViewById(R.id.btn_goToGameList);
        btn_goToAchievements = findViewById(R.id.btn_goToAchievements);
        btn_save = findViewById(R.id.config_save_button_config);
        btn_back = findViewById(R.id.config_back_button);
        btn_delete = findViewById(R.id.btn_delete);
        btn_photo = findViewById(R.id.config_photo_button);
        et_configName = findViewById(R.id.et_configName);
        et_configGreatScore = findViewById(R.id.et_configGreatScore);
        et_configPoorScore = findViewById(R.id.et_configPoorScore);
        instanceOfCM = ConfigManager.getInstance();
        btn_goToBarChart = findViewById(R.id.btn_goToBarChart);
    }

    private void extractDataFromIntent() {
        Intent intent = getIntent();
        indexOfConfigInList = intent.getIntExtra("indexOfConfigInList", -1);
        if(indexOfConfigInList == -1) {
            isAddMode = true;
        }
        else {
            isAddMode = false;
        }
    }

    private void toolbar() {
        TextView tv_title = findViewById(R.id.tv_config_toolbar_title);
        if(isAddMode){
            tv_title.setText(ADD_A_CONFIGURATION);
        }
        else {
            tv_title.setText(EDIT_A_CONFIGURATION);
        }
    }

    private void setButtonInvisible() {
        if(isAddMode) {
            btn_goToGameList.setVisibility(View.INVISIBLE);
            btn_goToAchievements.setVisibility(View.INVISIBLE);
            btn_delete.setVisibility(View.INVISIBLE);
            btn_goToBarChart.setVisibility(View.INVISIBLE);
        }
    }

    private void fillData() {
        if(!isAddMode) {
            Config configByIndex = instanceOfCM.getConfigByIndex(indexOfConfigInList);
            String name = configByIndex.getName();
            int greatScore = configByIndex.getGreatScore();
            int poorScore = configByIndex.getPoorScore();
            et_configName.setText(name);
            et_configGreatScore.setText(String.valueOf(greatScore));
            et_configPoorScore.setText(String.valueOf(poorScore));
        }
    }

    private void onClickButtons() {
        // Click save
        btn_save.setOnClickListener(v->onSaveCLick());

        // Click delete
        btn_delete.setOnClickListener(v->onDeleteClick());

        // Click go_to_game_list
        btn_goToGameList.setOnClickListener(v->onGameListClick());

        // Click go_to_achievements
        btn_goToAchievements.setOnClickListener(v->onAchievements());

        // Click to go back
        btn_back.setOnClickListener(v->onBackClick());

        // Click to go to photo activity
        btn_photo.setOnClickListener(view -> onPhotoClick());

        // Click to go to chart
        btn_goToBarChart.setOnClickListener(v -> onChartClick());
    }

    private void onPhotoClick() {
        if (et_configName.length() != 0) {
            Intent intent = PhotoActivity.makeIntentForConfig(getApplicationContext(),indexOfConfigInList);
            activityResultLauncher.launch(intent);
        }

        else {
            Toast.makeText(this, "Add a configuration name to proceed.",
                    Toast.LENGTH_SHORT).show();
        }
        // click to go chart
        btn_goToBarChart.setOnClickListener(v-> onChartClick());
    }

    private void onChartClick() {
        Intent intent = AchievementStatisticsActivity.makeIntent(getApplicationContext(), indexOfConfigInList);
        startActivity(intent);
    }

    private void onAchievements() {
        Intent intent = AchievementsActivity.makeIntent(this, indexOfConfigInList);
        startActivity(intent);
    }

    private void onGameListClick() {
        Intent intent = GameListActivity.makeIntent(this, indexOfConfigInList);
        startActivity(intent);
    }

    private void onDeleteClick() {
        instanceOfCM.removeConfig(indexOfConfigInList);
        saveData();
        finish();
    }

    private void onBackClick() {
        finish();
    }

    private void saveData() {
        SharedPreferencesUtils.saveDataOfConfigManager(getApplicationContext());
        SharedPreferencesUtils.saveDataOfGameManager(getApplicationContext());
    }

    private void onSaveCLick() {
        if(et_configName.length() == 0 || et_configGreatScore.length() == 0 || et_configPoorScore.length() == 0) {
            Toast.makeText(this,"All fields must not be empty",Toast.LENGTH_SHORT).show();
            return;
        }

        String configName = et_configName.getText().toString();
        String configGreatScore = et_configGreatScore.getText().toString();
        String configPoorScore = et_configPoorScore.getText().toString();
        int greatScore = stringToInt(configGreatScore);
        int poorScore = stringToInt(configPoorScore);

        if(greatScore <= poorScore) {
            Toast.makeText(this,"Great score must be bigger than poor Score.",Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        Config config = new Config();
        config.setName(configName);
        config.setGreatScore(greatScore);
        config.setPoorScore(poorScore);
        config.setImageString(imageString);

        if(indexOfConfigInList == -1) {
            if(instanceOfCM.isNameExisted(configName)) {
                Toast.makeText(this,
                        "The name of " + configName + " already exists. please use another name.",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            // add mode
            instanceOfCM.addConfig(config);
        }
        else {
            // edit mode
            Config configByIndex = instanceOfCM.getConfigByIndex(indexOfConfigInList);
            String oldName = configByIndex.getName();
            // if name remains same then directly edit it
            if(configName.equals(oldName)) {
                instanceOfCM.editConfig(indexOfConfigInList,config);
            }
            // if name has been changed then check if new name existed.
            else {
                // if name existed then reject.
                if(instanceOfCM.isNameExisted(configName)) {
                    Toast.makeText(this,
                            "The name of " + configName + " has been existed. please use another name.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                // if name did not exist, then edit it.
                else {
                    instanceOfCM.editConfig(indexOfConfigInList,config);
                }
            }
        }

        saveData();
        finish();
    }

    private int stringToInt(String str)
    {
        return Integer.parseInt(str);
    }
}