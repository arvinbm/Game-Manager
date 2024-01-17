package ca.cmpt276.project_7f;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ca.cmpt276.project_7f.model.Config;
import ca.cmpt276.project_7f.model.ConfigManager;
import ca.cmpt276.project_7f.utils.Base64Utils;
import ca.cmpt276.project_7f.utils.SharedPreferencesUtils;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

// showing the list of configuration and providing an add button.

public class ConfigurationListActivity extends AppCompatActivity {

    private FloatingActionButton fab_config_list;
    private ListView listview_config_list;
    private TextView tv_noConfigHint;
    private final ArrayList<String> configsToDisplay = new ArrayList<>();
    private ImageView btn_about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration_list);
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadData();
        initial();
        showHint();
        populateListView();
        registerClickCallBack();
        onClick();
    }

    private void loadData()
    {
        SharedPreferencesUtils.loadDataOfGameManager(getApplicationContext());
        SharedPreferencesUtils.loadDataOfConfigManager(getApplicationContext());
    }

    private void showHint() {
        ConfigManager instanceOfConfigM = ConfigManager.getInstance();
        int sizeOfConfigList = instanceOfConfigM.getSizeOfConfigList();
        if(sizeOfConfigList > 0)
        {
            tv_noConfigHint.setVisibility(View.INVISIBLE);
        }
        else
        {
            tv_noConfigHint.setVisibility(View.VISIBLE);
        }
    }

    private void initial() {
        fab_config_list = findViewById(R.id.fab_config_list);
        listview_config_list = findViewById(R.id.listview_config_list);
        tv_noConfigHint = findViewById(R.id.tv_noConfigHint);
        btn_about = findViewById(R.id.btn_about_config_list);
    }

    private void populateListView() {
        ConfigManager instanceOfConfigM = ConfigManager.getInstance();
        ArrayList<Config> configList = instanceOfConfigM.getConfigList();

        // Create a list of items
        configsToDisplay.clear();
        for (int i = 0; i < configList.size(); ++i) {
            String nameOfConfig = configList.get(i).getName();
            configsToDisplay.add(nameOfConfig);
        }

        // Build adapter
        ArrayAdapter<String> adapter = new MyListAdapter();

        // Configure the list view
        listview_config_list.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter <String> {
        public MyListAdapter() {
            super(ConfigurationListActivity.this, R.layout.item_view_config,
                    configsToDisplay);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;

            // Make sure we have a View (Sometimes convertView is null)
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.item_view_config, parent,
                        false);
            }

            // Find the config name to work with
            String configName = configsToDisplay.get(position);
            ConfigManager instanceOfConfigM = ConfigManager.getInstance();
            Config currentConfig = instanceOfConfigM.getConfigByName(configName);
            String imageString = currentConfig.getImageString();

            // Fill the view
            ImageView imageView = itemView.findViewById(R.id.image_config_list);
            if (imageString != null) {
                // Convert imageString to bitmap
                Bitmap bitmap = Base64Utils.stringToBitmap(imageString);
                imageView.setImageBitmap(bitmap);
            }

            else {
                // Set default image if the image was not provided by the user
                imageView.setImageResource(R.drawable.img);
            }

            // Fill the name of the config
            TextView makeText = itemView.findViewById(R.id.name_config_list);
            makeText.setText(currentConfig.getName());

            return itemView;
        }
    }

    private void registerClickCallBack() {
        listview_config_list.setOnItemClickListener((parent, viewClicked, position, id) -> {
            Intent intent = ConfigurationActivity.makeIntent
                    (ConfigurationListActivity.this, position);
            startActivity(intent);
        });
    }

    private void onClick() {
        fab_config_list.setOnClickListener(v -> onClickFab());
        btn_about.setOnClickListener(v -> onClickAbout());
    }

    private void onClickFab() {
        Intent intent = ConfigurationActivity.makeIntent(this, -1);
        startActivity(intent);
    }
    private void onClickAbout() {
        Intent intent = AboutActivity.makeIntent(this);
        startActivity(intent);
    }

}