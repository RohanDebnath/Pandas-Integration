package com.example.pandasintegration;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Declare public and global variables
    public static float classXValue;
    public static float class12Value;
    public static int numberOfStudents;
    public String selectedCSVData;
    private List<String> applicationDetails;
    private MyAdapter adapter;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize SharedPreferences
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }
        Python py = Python.getInstance();
        PyObject pyobj = py.getModule("python_pandas_worksheet");
        TextView tv = findViewById(R.id.textview);
        PyObject obj = pyobj.callAttr("application_received");
        PyObject list_obj = pyobj.callAttr("get_application_details");
        tv.setText(obj.toString());

        String csvData = list_obj.toString();  // Get CSV data as a string

        // Parse CSV data into a List of Strings
        applicationDetails = Arrays.asList(csvData.split("\n"));

        // Load cutoff values from SharedPreferences
        loadCutoffFromPreferences();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MyAdapter(this, applicationDetails, new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showComparison(position);
            }
        });

        recyclerView.setAdapter(adapter);

        // Making the Avg of class 10 and 12
        TextView classXavg = findViewById(R.id.textview_Xmarks);
        TextView classXIIavg = findViewById(R.id.textview_XIImarks);
        TextView carricular_activities = findViewById(R.id.textview_CarricularActivity);

        PyObject Xavg = pyobj.callAttr("class10_avg");
        classXavg.setText(Xavg.toString());

        PyObject XIIavg = pyobj.callAttr("class12_avg");
        classXIIavg.setText(XIIavg.toString());

        PyObject Carricular_Activities = pyobj.callAttr("curricular_activities");
        carricular_activities.setText(Carricular_Activities.toString());

        //Creating the newActivity for View of Selected Student
        TextView viewSelectedList = findViewById(R.id.view_SelectedList);
        viewSelectedList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSelectedStudentListActivity();
            }
        });

        //Showing Selected Student List size
        TextView Selected_StudentSize=findViewById(R.id.noOfSelected_Student);
//        PyObject size = pyobj.callAttr("get_length");
//        Selected_StudentSize.setText(size.toString());
    }

    private void showComparison(int position) {
        Intent intent = new Intent(this, ComparisonActivity.class);

        // Convert the Arrays$ArrayList to a standard ArrayList
        ArrayList<String> arrayListDetails = new ArrayList<>(applicationDetails);

        intent.putStringArrayListExtra("allApplicationDetails", arrayListDetails);
        intent.putExtra("selectedPosition", position);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }

        // Set a query listener to handle search queries
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle the query when the user submits it (optional)
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the RecyclerView adapter based on the search query
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // Handle search icon click if needed (optional)
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void openCutoffDialog(View view) {
        showCutoffDialog();
    }

    public void openStudentDialog(View view) {
        showStudentDialog();
    }
    private void showCutoffDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_set_cutoff, null);

        final EditText classXEditText = dialogView.findViewById(R.id.classXEditText);
        final EditText class12EditText = dialogView.findViewById(R.id.class12EditText);

        // Set default values from global variables
        classXEditText.setText(String.valueOf(classXValue));
        class12EditText.setText(String.valueOf(class12Value));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setTitle("Set Cutoff Marks")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Save entered cutoff values to SharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putFloat("classX", Float.parseFloat(classXEditText.getText().toString()));
                        editor.putFloat("class12", Float.parseFloat(class12EditText.getText().toString()));
                        editor.apply();

                        // Update global variables
                        classXValue = Float.parseFloat(classXEditText.getText().toString());
                        class12Value = Float.parseFloat(class12EditText.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void loadCutoffFromPreferences() {
        // Load cutoff values from SharedPreferences and update the UI
        classXValue = sharedPreferences.getFloat("classX", 0);
        class12Value = sharedPreferences.getFloat("class12", 0);
        numberOfStudents = sharedPreferences.getInt("numberOfStudents", 0);

        // Use the cutoff values as needed in your UI
    }
    private void showStudentDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_set_students, null);

        final EditText numberOfStudentsEditText = dialogView.findViewById(R.id.numberOfStudentsEditText);
        Button saveButton = dialogView.findViewById(R.id.saveButton);

        // Set default value from global variable
        numberOfStudentsEditText.setText(String.valueOf(numberOfStudents));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setTitle("Set Number of Students");

        final AlertDialog alertDialog = builder.create();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save entered number of students to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("numberOfStudents", Integer.parseInt(numberOfStudentsEditText.getText().toString()));
                editor.apply();

                // Update global variable
                numberOfStudents = Integer.parseInt(numberOfStudentsEditText.getText().toString());
                Python py = Python.getInstance();
                PyObject pyobj = py.getModule("python_pandas_worksheet");
                PyObject Selected_Studentobj=pyobj.callAttr("filter_students",classXValue,class12Value,numberOfStudents);
                PyObject finalDataBase=pyobj.callAttr("merge_dataframes");
                selectedCSVData=finalDataBase.toString();
                alertDialog.dismiss();
                getNumberOfSelection(v);
            }
        });

        alertDialog.show();
    }
    public void openCurricularActivitiesDialog(View view) {
        // Create custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_select_curricular_activities);

        // Get references to the input fields and save button
        final EditText editTextActivity = dialog.findViewById(R.id.editTextActivity);
        final EditText editTextNumStudents = dialog.findViewById(R.id.editTextNumStudents);
        Button btnSave = dialog.findViewById(R.id.btnSaveCA);

        // Set click listener for the save button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve values from input fields
                String activity = editTextActivity.getText().toString();
                String numStudentsStr = editTextNumStudents.getText().toString();
                Python py = Python.getInstance();
                PyObject pyobj = py.getModule("python_pandas_worksheet");

                PyObject Selected_Studentobj=pyobj.callAttr("select_students_by_activity",activity,Integer.parseInt(numStudentsStr));
                PyObject finalDataBase=pyobj.callAttr("merge_dataframes");
                selectedCSVData=finalDataBase.toString();
                openSelectedStudentListActivity();
                getNumberOfSelection(v);
                dialog.dismiss();
            }
        });

        // Show the dialog
        dialog.show();
    }


    private void openSelectedStudentListActivity() {
        Intent intent = new Intent(this, SelectedStudentListActivity.class);
        intent.putExtra("selectedCSVData", selectedCSVData);
        startActivity(intent);
    }
    public  void getNumberOfSelection(View view){
        TextView Selected_StudentSize=findViewById(R.id.noOfSelected_Student);
        Python py = Python.getInstance();
        PyObject pyobj = py.getModule("python_pandas_worksheet");
        PyObject size = pyobj.callAttr("get_length");
        Selected_StudentSize.setText(size.toString());
    }

    public void sendEmails(View view) {
        Python py = Python.getInstance();
        PyObject pyobj = py.getModule("python_pandas_worksheet");
        pyobj.callAttr("send_emails");
        Toast.makeText(this, "Email has been sent", Toast.LENGTH_SHORT).show();
    }

}
