package com.example.pandasintegration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }
        Python py = Python.getInstance();
        PyObject pyobj = py.getModule("python_pandas_worksheet");
        TextView tv=findViewById(R.id.textview);
        PyObject obj=pyobj.callAttr("application_recieved");
        PyObject list_obj=pyobj.callAttr("get_application_details");
        tv.setText(obj.toString());

        String csvData = list_obj.toString();  // Get CSV data as a string

        // Parse CSV data into a List of Strings
        List<String> applicationDetails = Arrays.asList(csvData.split("\n"));

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MyAdapter adapter = new MyAdapter(applicationDetails);
        recyclerView.setAdapter(adapter);
    }
}