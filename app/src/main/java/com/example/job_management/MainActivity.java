package com.example.job_management;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText edt_id, edt_name, edt_status, edt_desc;
    private Button btn_add, btn_update, btn_delete, btn_list;
    private RecyclerView recyclerView;
    private SQLiteHelper DB;
    private List<Job> jobList;
    private JobAdapter JobAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edt_id = findViewById(R.id.edt_id);
        edt_name = findViewById(R.id.edt_name);
        edt_status = findViewById(R.id.edt_status);
        edt_desc = findViewById(R.id.edt_desc);
        btn_add = findViewById(R.id.btn_add);
        btn_update = findViewById(R.id.btn_update);
        btn_delete = findViewById(R.id.btn_delete);
        btn_list = findViewById(R.id.btn_list);
        recyclerView = findViewById(R.id.recyclerView);
        DB = new SQLiteHelper(this);
        jobList = new ArrayList<>();

        // add
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String id = edt_id.getText().toString();
                    String name = edt_name.getText().toString();
                    String status = edt_status.getText().toString();
                    String desc = edt_desc.getText().toString();
                    if (!DB.checkExistJob(id)) {
                        Job job = new Job(id, name, status, desc);
                        boolean isAdded = DB.addJob(job);
                        if (isAdded) {
                            Toast.makeText(MainActivity.this, "Add successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Add failed!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Job job = new Job();
                        job.setId(id);
                        job.setName(name);
                        job.setStatus(status);
                        job.setDescription(desc);
                        int i = DB.updateJob(job);
                        if (i >= 1) {
                            Toast.makeText(MainActivity.this, "Update successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Update failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // update
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String id = edt_id.getText().toString();
                    String name = edt_name.getText().toString();
                    String status = edt_status.getText().toString();
                    String desc = edt_desc.getText().toString();
                    Job job = DB.getJobById(id);
                    job.setName(name);
                    job.setStatus(status);
                    job.setDescription(desc);
                    DB.updateJob(job);
                    Toast.makeText(MainActivity.this, "Update successfully!", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // delete
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String id = edt_id.getText().toString();
                    Job job = DB.getJobById(id);
                    DB.deleteJob(job);
                    Toast.makeText(MainActivity.this, "Delete successfully!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // list
        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Job> jobList;
                String id = edt_id.getText().toString();
                String name = edt_name.getText().toString();
                String status = edt_status.getText().toString();
                String desc = edt_desc.getText().toString();

                ArrayList<String> jobSearch = new ArrayList<>();
                jobSearch.add(id);
                jobSearch.add(name);
                jobSearch.add(status);
                jobSearch.add(desc);

                if(id.isEmpty() && name.isEmpty() && status.isEmpty() && desc.isEmpty()){
                    jobList = DB.getAllJobs();
                }
                else {
                    jobList = DB.searchJobs(jobSearch);
                }

                JobAdapter = new JobAdapter(MainActivity.this, jobList);
                recyclerView.setAdapter(JobAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            }
        });
    }


}

