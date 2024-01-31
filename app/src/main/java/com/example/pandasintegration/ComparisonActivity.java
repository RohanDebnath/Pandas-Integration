package com.example.pandasintegration;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class ComparisonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparison);

        // Get data from the intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String studentName = extras.getString("studentName");
            String xMarks = extras.getString("xMarks");
            String xiiMarks = extras.getString("xiiMarks");

            // Set the title of the activity with student name
            setTitle(studentName);

            // Create BarChart
            BarChart barChart = findViewById(R.id.barChart);

            // Create entries for class 10 and 12 marks
            List<BarEntry> entries = new ArrayList<>();
            entries.add(new BarEntry(0, Float.parseFloat(xMarks)));
            entries.add(new BarEntry(1, Float.parseFloat(xiiMarks)));

            // Create dataset for BarChart
            BarDataSet dataSet = new BarDataSet(entries, "Marks");
            BarData barData = new BarData(dataSet);

            // Customize X-axis
            XAxis xAxis = barChart.getXAxis();
            xAxis.setValueFormatter(new CustomXAxisValueFormatter());

            // Set data to the BarChart
            barChart.setData(barData);
            barChart.invalidate(); // refresh chart
        }
    }

    private static class CustomXAxisValueFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            if (value == 0) return "Class 10";
            else if (value == 1) return "Class 12";
            else return "";
        }
    }
}
