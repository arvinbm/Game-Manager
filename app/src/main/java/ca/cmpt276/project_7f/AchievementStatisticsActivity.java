package ca.cmpt276.project_7f;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import ca.cmpt276.project_7f.model.Config;
import ca.cmpt276.project_7f.model.ConfigManager;
import ca.cmpt276.project_7f.model.GameManager;

public class AchievementStatisticsActivity extends AppCompatActivity {

    private int indexOfConfigInList;
    private ArrayList<Integer> countOfEachAchievementInCorrespondingGameList;
    private BarChart barChart;
    private ImageView statistics_back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement_statistics);
    }

    @Override
    protected void onResume() {
        super.onResume();

        initial();
        extractDataFromIntent();
        getAchievementCountList();
        generateBarChart();
        onButtonsClick();
    }

    private void onButtonsClick() {
        statistics_back_button.setOnClickListener(v -> onBackPressed());
    }

    private void initial() {
        barChart = findViewById(R.id.barChart);
        statistics_back_button = findViewById(R.id.statistics_back_button);
    }

    private void extractDataFromIntent() {
        Intent intent = getIntent();
        indexOfConfigInList = intent.getIntExtra("indexOfConfigInList",-1);
    }

    private void getAchievementCountList() {
        ConfigManager configManagerInstance = ConfigManager.getInstance();
        Config configByIndex = configManagerInstance.getConfigByIndex(indexOfConfigInList);
        String configName = configByIndex.getName();
        GameManager gameManagerInstance = GameManager.getInstance();
        countOfEachAchievementInCorrespondingGameList =
                gameManagerInstance.getCountOfEachAchievementInCorrespondingGameList(getApplicationContext(), configName);
    }

    private void generateBarChart() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for(int i = 0; i < 10; i++)
        {
            Integer count = countOfEachAchievementInCorrespondingGameList.get(i);
            BarEntry barEntry = new BarEntry(i,count);
            barEntries.add(barEntry);
        }

        BarDataSet barDataSet = new BarDataSet(barEntries,"barDataSet");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setLabel(null);

        ArrayList<String> levels = new ArrayList<>();
        for(int i = 0; i < 10; i++)
        {
            String s = "Level_" + (i+1);
            levels.add(s);
        }

        BarData barData = new BarData();
        barData.addDataSet(barDataSet);

        barChart.setData(barData);
        Description description = new Description();
        description.setText("Level_1 is highest achievement and Level_10 is lowest achievement");
        description.setTextSize(12);
        description.setTextColor(Color.BLUE);
        barChart.setDescription(description);
        barChart.setDrawGridBackground(false);
        barChart.invalidate();
        barChart.animateY(1000);
        barChart.setScaleEnabled(false);
        barChart.setPinchZoom(false);
        barChart.setFitBars(true);
        barChart.setDrawBarShadow(false);
        barChart.setFitBars(true);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(0);
        xAxis.setGranularityEnabled(true);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMaximum(10);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(levels));
    }

    public static Intent makeIntent(Context context, int indexOfConfigInList)
    {
        Intent intent = new Intent(context,AchievementStatisticsActivity.class);
        intent.putExtra("indexOfConfigInList",indexOfConfigInList);
        return intent;
    }
}