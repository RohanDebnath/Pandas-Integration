package com.example.pandasintegration;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;

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
            List<String> allApplicationDetails = extras.getStringArrayList("allApplicationDetails");
            int selectedPosition = extras.getInt("selectedPosition");

            // Retrieve details for the selected student
            String selectedStudentDetails = allApplicationDetails.get(selectedPosition);
            String[] selectedDetails = selectedStudentDetails.split(",");

            // Set the title of the activity with student name
            setTitle("Comparison for " + selectedDetails[0]);

            // Create ScatterChart
            ScatterChart scatterChart = findViewById(R.id.scatterChart);

            // Create entries for class 10 and 12 marks for all students
            // Create entries for class 10 and 12 marks for all students
            List<Entry> entries = new ArrayList<>();
            for (int i = 0; i < allApplicationDetails.size(); i++) {
                String[] details = allApplicationDetails.get(i).split(",");

                // Skip the header row or any non-numeric values
                try {
                    float xMark = Float.parseFloat(details[1]);
                    float xiiMark = Float.parseFloat(details[2]);

                    // Add entry for each student
                    entries.add(new Entry(xMark, xiiMark));
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace(); // Log the exception or handle it as needed
                }
            }

            // Create dataset for ScatterChart
            ScatterDataSet dataSet = new ScatterDataSet(entries, "Other Students");
            dataSet.setColors(Color.BLUE); // Set color for blue dots
            dataSet.setScatterShape(ScatterChart.ScatterShape.CIRCLE); // Set shape of the dots

            // Highlight the selected student with a red dot
            dataSet.getEntryForIndex(selectedPosition).setIcon(getResources().getDrawable(R.drawable.red_dot));

            // Customize X-axis
            XAxis xAxis = scatterChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            // Customize Y-axis
            YAxis yAxis = scatterChart.getAxisLeft();
            yAxis.setAxisMinimum(0f);
            yAxis.setAxisMaximum(100f);

            // Set data to the ScatterChart
            scatterChart.setData(new ScatterData(dataSet));
            scatterChart.invalidate(); // refresh chart
        }
    }
}
