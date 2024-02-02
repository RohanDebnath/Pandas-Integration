package com.example.pandasintegration;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class SelectedStudentListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StudentListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_student_list);

        recyclerView = findViewById(R.id.recyclerView);

        // Retrieve selectedCSVData from intent
        String selectedCSVData = getIntent().getStringExtra("selectedCSVData");

        // Parse CSV data into a List of Strings
        List<String> studentDetails = Arrays.asList(selectedCSVData.split("\n"));

        // Convert studentDetails to a List of Student objects
        List<Student> studentList = new ArrayList<>();
        for (int i = 1; i < studentDetails.size(); i++) {
            String studentInfo = studentDetails.get(i);
            String[] infoArray = studentInfo.split(",");

            // Ensure the array has the correct length
            if (infoArray.length == 5) {
                studentList.add(new Student(infoArray[0], infoArray[1], infoArray[2], infoArray[3], infoArray[4]));
            }
        }


        // Set up RecyclerView and Adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudentListAdapter(studentList);
        recyclerView.setAdapter(adapter);
    }
}
