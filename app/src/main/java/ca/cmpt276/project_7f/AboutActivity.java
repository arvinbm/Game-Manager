package ca.cmpt276.project_7f;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;

public class AboutActivity extends AppCompatActivity {

    ImageView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        btnInit();
        onClick();

    }

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        return intent;
    }

    private void btnInit() {
        btn_back = findViewById(R.id.btn_back_about);
    }

    private void onClick() {
        btn_back.setOnClickListener(v->onBackClick());
    }

    private void onBackClick() {
        finish();
    }
}