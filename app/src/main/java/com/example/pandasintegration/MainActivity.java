package com.example.pandasintegration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> applicationDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }
        Python py = Python.getInstance();
        PyObject pyobj = py.getModule("python_pandas_worksheet");
        TextView tv = findViewById(R.id.textview);
        PyObject obj = pyobj.callAttr("application_recieved");
        PyObject list_obj = pyobj.callAttr("get_application_details");
        tv.setText(obj.toString());

        String csvData = list_obj.toString();  // Get CSV data as a string

        // Parse CSV data into a List of Strings
        applicationDetails = Arrays.asList(csvData.split("\n"));

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MyAdapter adapter = new MyAdapter(this, applicationDetails, new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showComparison(position);
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private void showComparison(int position) {
        Intent intent = new Intent(this, ComparisonActivity.class);

        // Convert the Arrays$ArrayList to a standard ArrayList
        ArrayList<String> arrayListDetails = new ArrayList<>(applicationDetails);

        intent.putStringArrayListExtra("allApplicationDetails", arrayListDetails);
        intent.putExtra("selectedPosition", position);
        startActivity(intent);
    }

}
