package com.example.pandasintegration;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

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

    private List<String> applicationDetails;
    private MyAdapter adapter;

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

        adapter = new MyAdapter(this, applicationDetails, new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showComparison(position);
            }
        });

        recyclerView.setAdapter(adapter);

        //Making the Avg of class 10 and 12
        TextView classXavg=findViewById(R.id.textview_Xmarks);
        TextView classXIIavg=findViewById(R.id.textview_XIImarks);

        PyObject Xavg=pyobj.callAttr("class10_avg");
        classXavg.setText(Xavg.toString());

        PyObject XIIavg=pyobj.callAttr("class12_avg");
        classXIIavg.setText(XIIavg.toString());
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
}
