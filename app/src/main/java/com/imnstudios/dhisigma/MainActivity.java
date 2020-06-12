package com.imnstudios.dhisigma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.imnstudios.dhisigma.adapters.EmployeeListAdapter;
import com.imnstudios.dhisigma.models.EmployeeModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DHISIGMA_DEBUG";

    private RecyclerView employeeListView;
    private EmployeeListAdapter employeeListAdapter;
    private ArrayList<EmployeeModel> employeeList;
    private ProgressBar progressBar;
    private DatabaseReference mDatabase;
    private ImageButton sortUp;
    private ImageButton sortDown;
    private SearchView searchView;

    private String searchQuery;
    private boolean searchFlag = false;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting appbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //init views
        sortUp = toolbar.findViewById(R.id.sort_up);
        sortDown = toolbar.findViewById(R.id.sort_down);
        searchView = toolbar.findViewById(R.id.search_view);
        employeeListView = findViewById(R.id.employee_list_view);
        progressBar = findViewById(R.id.progress_bar);

        //init firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("Employees");
        startPopulatingInAsc();

        //init recyclerView
        employeeListView.setLayoutManager(new LinearLayoutManager(this));

        //sorting
        sortUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!searchFlag) {
                    employeeList.clear();
                    startPopulatingInAsc();
                } else {
                    searchEmployeeAsc();
                }
            }
        });

        sortDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!searchFlag) {
                    employeeList.clear();
                    startPopulatingInDesc();
                } else {
                    searchEmployeeDesc();
                }
            }
        });

        //search
        searchView.setOnQueryTextListener(searchQueryListener);
        int searchCloseButtonId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        ImageView closeButton = (ImageView) this.searchView.findViewById(searchCloseButtonId);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, " Close Search");
                searchView.clearFocus();
                employeeList.clear();
                searchFlag = false;
                startPopulatingInAsc();
                searchView.setQuery("", false);
            }
        });

    }

    android.widget.SearchView.OnQueryTextListener searchQueryListener = new android.widget.SearchView.OnQueryTextListener() {

        @Override
        public boolean onQueryTextSubmit(String query) {
            if (flag) {
                searchView.clearFocus();
                searchQuery = query;
                searchFlag = true;
                searchEmployeeAsc();
            } else {
                Toast.makeText(MainActivity.this, "Please wait", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };

    private void searchEmployeeAsc() {
        employeeList.clear();
        Log.d(TAG, " Searching in ASC");
        mDatabase.orderByChild("employeeCode").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                employeeList = new ArrayList<>();
                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                    EmployeeModel employee = dataSnapshot2.getValue(EmployeeModel.class);
                    assert employee != null;
                    if ((employee.getEmployeeName().toLowerCase().contains(searchQuery.toLowerCase())) ||
                            (String.valueOf(employee.getEmployeeCode()).toLowerCase().contains(searchQuery.toLowerCase())))
                        employeeList.add(employee);
                    Log.d(TAG, " Searching ASC Success");
                }

                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, " Searching ASC Error: " + databaseError.toString());
            }
        });
    }

    private void searchEmployeeDesc() {
        employeeList.clear();
        Log.d(TAG, " Searching in Desc");
        mDatabase.orderByChild("employeeCodeDesc").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                employeeList = new ArrayList<>();
                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                    EmployeeModel employee = dataSnapshot2.getValue(EmployeeModel.class);
                    assert employee != null;
                    if ((employee.getEmployeeName().toLowerCase().contains(searchQuery.toLowerCase())) ||
                            (String.valueOf(employee.getEmployeeCode()).toLowerCase().contains(searchQuery.toLowerCase())))
                        employeeList.add(employee);
                    Log.d(TAG, " Searching Desc Success");
                }

                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, " Searching Desc Error: " + databaseError.toString());
            }
        });
    }

    private void startPopulatingInDesc() {
        Log.d(TAG, " Populating in DESC");
        mDatabase.orderByChild("employeeCodeDesc").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                employeeList = new ArrayList<>();
                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                    EmployeeModel employee = dataSnapshot2.getValue(EmployeeModel.class);
                    employeeList.add(employee);
                    Log.d(TAG, " DESC Success");
                }
                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, " DESC Error: " + databaseError.toString());
            }
        });
    }

    private void startPopulatingInAsc() {
        Log.d(TAG, " Populating in ASC");
        mDatabase.orderByChild("employeeCode").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                employeeList = new ArrayList<>();
                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                    EmployeeModel employee = dataSnapshot2.getValue(EmployeeModel.class);
                    employeeList.add(employee);
                    Log.d(TAG, " ASC Success");
                }
                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, " ASC Error: " + databaseError.toString());
            }
        });
    }


    private void setAdapter() {
        flag = true;
        Log.d(TAG, " Setting Adapter");

        if (employeeList.size() == 0) {
            Toast.makeText(this, "Can't find any employees.", Toast.LENGTH_SHORT).show();
        }
        employeeListAdapter = new EmployeeListAdapter(getApplicationContext(), employeeList);
        employeeListView.setAdapter(employeeListAdapter);
        progressBar.setVisibility(View.INVISIBLE);
    }
}