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

    private DBHelper DB;

    private List<Job> jobList;

    private JobAdapter JobAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindingView();

        DB = new DBHelper(this);
        jobList = new ArrayList<>();
        // add student
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String id = edt_id.getText().toString();
                    String name = edt_name.getText().toString();
                    String status = edt_status.getText().toString();
                    String desc = edt_desc.getText().toString();

                    if (!DB.checkExistStudentInDB(id)) {
                        Job studentModel = new Job(id, name, status, desc);
                        boolean checkInsertData = DB.insertStudent(studentModel);
                        if (checkInsertData) {
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
                        int i = DB.updateStudent(job);
                        if (i >= 1) {
                            Toast.makeText(MainActivity.this, "Update successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Update failed!", Toast.LENGTH_SHORT).show();
                        }
                    }

//                    jobList = DB.getAllStudents();
//                    JobAdapter = new JobAdapter(MainActivity.this, jobList);
//                    recyclerView.setAdapter(JobAdapter);
//                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // list student
        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jobList.clear();
                String id = edt_id.getText().toString();
                String name = edt_name.getText().toString();
                String status = edt_status.getText().toString();
                String desc = edt_desc.getText().toString();

                ArrayList<String> arrSearch = new ArrayList<>();
                arrSearch.add(id);
                arrSearch.add(name);
                arrSearch.add(status);
                arrSearch.add(desc);

                List<Job> jobList = DB.searchStudents(arrSearch);

                JobAdapter = new JobAdapter(MainActivity.this, jobList);
                recyclerView.setAdapter(JobAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            }
        });

        // delete student
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    jobList.clear();
                    int id = Integer.parseInt(edt_id.getText().toString().trim());
                    Job studentModel = DB.getStudent(id);
                    DB.deleteStudent(studentModel);
                    Toast.makeText(MainActivity.this, "Delete successfully!", Toast.LENGTH_SHORT).show();

                    jobList = DB.getAllStudents();
                    JobAdapter = new JobAdapter(MainActivity.this, jobList);
                    recyclerView.setAdapter(JobAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // update student
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    jobList.clear();
                    int id = Integer.parseInt(edt_id.getText().toString().trim());
                    String name = edt_name.getText().toString().trim();
                    String status = edt_status.getText().toString().trim();
                    String desc = edt_desc.getText().toString().trim();
                    Job studentModel = DB.getStudent(id);
                    studentModel.setName(name);
                    studentModel.setStatus(status);
                    studentModel.setDescription(desc);
                    DB.updateStudent(studentModel);
                    Toast.makeText(MainActivity.this, "Update successfully!", Toast.LENGTH_SHORT).show();

                    jobList = DB.getAllStudents();
                    JobAdapter = new JobAdapter(MainActivity.this, jobList);
                    recyclerView.setAdapter(JobAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        jobList = DB.getAllStudents();
        JobAdapter = new JobAdapter(MainActivity.this, jobList);
        recyclerView.setAdapter(JobAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    private void bindingView() {
        edt_id = findViewById(R.id.edt_id);
        edt_name = findViewById(R.id.edt_name);
        edt_status = findViewById(R.id.edt_status);
        edt_desc = findViewById(R.id.edt_desc);
        btn_add = findViewById(R.id.btn_add);
        btn_update = findViewById(R.id.btn_update);
        btn_delete = findViewById(R.id.btn_delete);
        btn_list = findViewById(R.id.btn_list);
        recyclerView = findViewById(R.id.recyclerView);
    }
}