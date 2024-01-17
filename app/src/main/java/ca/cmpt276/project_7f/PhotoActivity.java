package ca.cmpt276.project_7f;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ca.cmpt276.project_7f.model.Config;
import ca.cmpt276.project_7f.model.ConfigManager;
import ca.cmpt276.project_7f.model.Game;
import ca.cmpt276.project_7f.model.GameManager;
import ca.cmpt276.project_7f.utils.Base64Utils;

public class PhotoActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_FOR_CAMERA = 1;
    public static final int REQUEST_CODE_TAKE_A_PHOTO = 2;
    private boolean isFromConfigActivity;
    private int indexOfGameInList;
    private String configName;
    private ImageView iv_photo;
    private Button btn_take_photo;
    private Button btn_confirm_photo;
    private ImageView iv_toolbar_back;
    private int indexOfConfigInList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        initial();
        extractDataFromIntent();
        updateToolBarTitle();
        loadPhoto();
    }


    @Override
    protected void onResume() {
        super.onResume();

        onButtonsCLick();
    }

    private void loadPhoto() {

        // From GameActivity
        if(indexOfGameInList != -1)
        {
            GameManager instanceGM = GameManager.getInstance();
            Game game = instanceGM.getGame(configName, indexOfGameInList);
            String imageString = game.getImageString();
            if(imageString != null) {
                Bitmap bitmap = Base64Utils.stringToBitmap(imageString);
                iv_photo.setImageBitmap(bitmap);
            }
        }
        else if(indexOfConfigInList != -1)
        {
            ConfigManager instanceCM = ConfigManager.getInstance();
            Config configByIndex = instanceCM.getConfigByIndex(indexOfConfigInList);
            if (configByIndex.getImageString() != null) {
                String imageString = configByIndex.getImageString();
                Bitmap bitmap = Base64Utils.stringToBitmap(imageString);
                iv_photo.setImageBitmap(bitmap);
            }
        }
//        // From ConfigurationActivity
//        else if (){
//            ConfigManager instanceCM = ConfigManager.getInstance();
//            Config config = instanceCM.getConfigByName(configName);
//            if (config.getImageString() == null) {
//                Log.e("TAG11",config.getImageString()+"");
//            }
//            String imageString = config.getImageString();
//            Bitmap bitmap = Base64Utils.stringToBitmap(imageString);
//            iv_photo.setImageBitmap(bitmap);
//        }
    }

    private void initial() {
        btn_take_photo = findViewById(R.id.btn_take_photo);
        iv_photo = findViewById(R.id.iv_photo);
        btn_confirm_photo = findViewById(R.id.btn_confirm_photo);
        iv_toolbar_back = findViewById(R.id.game_photo_back_button);
    }

    public static Intent makeIntentForGame(Context context, int indexOfGameInList, String configName)
    {
        Intent intent = new Intent(context,PhotoActivity.class);
        intent.putExtra("indexOfGameInList", indexOfGameInList);
        intent.putExtra("configName", configName);
        return intent;
    }
    public static Intent makeIntentForConfig(Context context, int indexOfConfigInList)
    {
        Intent intent = new Intent(context,PhotoActivity.class);
        intent.putExtra("indexOfConfigInList", indexOfConfigInList);
        return intent;
    }

    private void extractDataFromIntent()
    {
        Intent intent = getIntent();
        indexOfGameInList = intent.getIntExtra("indexOfGameInList", -1);
        configName = intent.getStringExtra("configName");
        indexOfConfigInList = intent.getIntExtra("indexOfConfigInList",-1);

        // Check if PhotoActivity was launch from GameActivity or ConfigurationActivity
        isFromConfigActivity = indexOfConfigInList != -1;
    }

    private void updateToolBarTitle() {
        TextView toolbar_title = findViewById(R.id.tv_game_photo_toolbar_title);
        if(isFromConfigActivity) {
            toolbar_title.setText("Configuration Photo");
        }
        else {
            toolbar_title.setText("Game Photo");
        }
    }


    private void onButtonsCLick() {
        btn_take_photo.setOnClickListener(v->onTakePhotoButtonClick());
        btn_confirm_photo.setOnClickListener(v->onConfirmPhotoButtonClick());
        iv_toolbar_back.setOnClickListener(v->onGoBackClick());
    }

    private void onGoBackClick() {
        onBackPressed();
    }

    private void onConfirmPhotoButtonClick() {
        setResult();
    }

    private void onTakePhotoButtonClick() {
        askPermissionForCamera();
    }

    private void askPermissionForCamera() {
        // no permission, ask for permission.
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
        {
            String[] strings = {Manifest.permission.CAMERA};
            ActivityCompat.requestPermissions(PhotoActivity.this, strings, REQUEST_CODE_FOR_CAMERA);
        }
        // already had permission and open camera.
        else
        {
            openCamera();
        }
    }

    @Override
    // when pop-up window, read user choice from the window.
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_FOR_CAMERA)
        {
            // if user click give permission.
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                openCamera();
            }
            // if user click deny permission.
            else
            {
                Toast.makeText(this,"Camera permission is needed.",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, REQUEST_CODE_TAKE_A_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_TAKE_A_PHOTO) {
            Bundle extras = data.getExtras();
            if(extras == null)
            {
                Toast.makeText(this,"Taking photo has been cancelled.",Toast.LENGTH_SHORT).show();
                return;
            }
            Bitmap bitmapPhoto = (Bitmap) extras.get("data");
            iv_photo.setImageBitmap(bitmapPhoto);
        }
    }

    private void setResult() {
        Drawable drawable = iv_photo.getDrawable();
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap bitmap = bitmapDrawable.getBitmap();
        String imageString = Base64Utils.bitmapToString(bitmap);

        if (!isFromConfigActivity) {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("imageString", imageString);
            setResult(100,intent);
        }
        else {
            Intent intent = new Intent(this, ConfigurationActivity.class);
            intent.putExtra("imageString", imageString);
            setResult(101,intent);
        }

        finish();
    }
}