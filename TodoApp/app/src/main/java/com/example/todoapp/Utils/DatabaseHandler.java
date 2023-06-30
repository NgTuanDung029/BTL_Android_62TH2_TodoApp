package com.example.todoapp.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todoapp.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String NAME = "toDoListDatabase";
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String START_TIME = "startTime";
    private static final String END_TIME = "endTime";
    private static final String STATUS = "status";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, " + STATUS + " INTEGER, " + START_TIME + " TEXT, " + END_TIME + " TEXT)";
    private SQLiteDatabase db;

    public DatabaseHandler(Context context){
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //Drop the older table
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        //Create tables again
        onCreate(db);
    }

    public void openDatabase(){
        db = this.getWritableDatabase();
    }

    public void insertTask(ToDoModel task){
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTask());
        cv.put(STATUS, 0);
        cv.put(START_TIME, task.getStartTime());
        cv.put(END_TIME, task.getEndTime());
        db.insert(TODO_TABLE, null,cv);
    }

    @SuppressLint("Range")
    public List<ToDoModel> getAllTasks(){
        List<ToDoModel> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
         try{
             cur = db.query(TODO_TABLE, null,null,null,null,null, null,null);
             if (cur != null){
                 if(cur.moveToFirst()){
                     do{
                         ToDoModel task = new ToDoModel();
                         task.setId(cur.getInt(cur.getColumnIndex(ID)));
                         task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                         task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                         task.setStartTime(cur.getString(cur.getColumnIndex(START_TIME)));
                         task.setEndTime(cur.getString(cur.getColumnIndex(END_TIME)));
                         taskList.add(task);
                     }while (cur.moveToNext());
                 }
             }
         }
         finally {
             db.endTransaction();
             cur.close();
         }
         return taskList;
    }

    public void updateStatus (int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateTask(int id, String task , String starTime, String endTime){
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        cv.put(START_TIME, starTime);
        cv.put(END_TIME, endTime);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id){
        db.delete(TODO_TABLE, ID + "= ?", new String[] {String.valueOf(id)});
    }
}
