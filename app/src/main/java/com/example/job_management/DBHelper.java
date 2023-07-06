package com.example.job_management;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String JOB_TABLE = "Job";
    public static final String ID_COLUMN = "id";
    public static final String NAME_COLUMN = "name";
    public static final String STATUS_COLUMN = "status";
    public static final String DESC_COLUMN = "description";

    public DBHelper(Context context) {
        super(context, JOB_TABLE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create table " + JOB_TABLE + "("
                + ID_COLUMN + " TEXT primary key, "
                + NAME_COLUMN + " TEXT, " + STATUS_COLUMN
                + " TEXT, " + DESC_COLUMN + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > oldVersion){
            db.execSQL("drop table if exists " + JOB_TABLE + "1");
            onCreate(db);
        }
    }

    public boolean checkExistJob(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from " + JOB_TABLE + " where " + ID_COLUMN + " = " + id;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public boolean addJob(Job job) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, job.getId());
        values.put(NAME_COLUMN, job.getName());
        values.put(STATUS_COLUMN, job.getStatus());
        values.put(DESC_COLUMN, job.getDescription());
        long newRowId = DB.insert(JOB_TABLE, null, values);
        DB.close();
        if (newRowId == -1) return false;
        return true;
    }

    public void deleteJob(Job job) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(JOB_TABLE, ID_COLUMN + " = ?",
                new String[]{job.getId()});
        db.close();
    }


    public Job getJobById(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(JOB_TABLE, new String[]{ID_COLUMN,
                        NAME_COLUMN, STATUS_COLUMN, DESC_COLUMN}, ID_COLUMN + "= ?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) cursor.moveToFirst();
        Job job = new Job(cursor.getString(0), cursor.getString(1),
                cursor.getString(2), cursor.getString(3));
        return job;
    }

    public List<Job> getAllJobs() {
        List<Job> jobList = new ArrayList<>();
        String sql = "SELECT  * FROM " + JOB_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

            while (cursor.moveToNext()) {
                Job job = new Job();
                job.setId(cursor.getString(0));
                job.setName(cursor.getString(1));
                job.setStatus(cursor.getString(2));
                job.setDescription(cursor.getString(3));
                // Adding student to list
                jobList.add(job);
            } ;
        return jobList;
    }

    public List<Job> searchStudents(ArrayList<String> arrSearch) {
        List<Job> jobList = new ArrayList<>();
        // Select All Query
        String selectQuery = "select * from " + JOB_TABLE ;


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list

        while (cursor.moveToNext()){
                Job job = new Job();
                job.setId((cursor.getString(0)));
                job.setName(cursor.getString(1));
                job.setStatus(cursor.getString(2));
                job.setDescription(cursor.getString(3));
                jobList.add(job);
            }
        return jobList;
    }

    public int updateJob(Job job) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_COLUMN, job.getName());
        values.put(STATUS_COLUMN, job.getStatus());
        values.put(DESC_COLUMN, job.getDescription());
        return db.update(JOB_TABLE, values, ID_COLUMN + " = ?",
                new String[]{job.getId()});
    }


//    public Cursor getData() {
//        SQLiteDatabase DB = this.getWritableDatabase();
//        Cursor cursor = DB.rawQuery("select * from " + STUDENT_TABLE, null);
//        return cursor;
//    }
}
