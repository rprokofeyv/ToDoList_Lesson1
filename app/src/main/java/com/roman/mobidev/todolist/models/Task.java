package com.roman.mobidev.todolist.models;

import android.database.Cursor;

import com.roman.mobidev.todolist.helpers.TaskFields;

import java.io.Serializable;

/**
 * Created by roman on 05.04.16.
 */
public class Task implements Serializable{

    public static Task createFromCursor(Cursor cursor){
        Task task = new Task();
        task.id = cursor.getInt(TaskFields._id.ordinal());
        task.name = cursor.getString(TaskFields.NAME.ordinal());
        task.description = cursor.getString(TaskFields.DESCRIPTION.ordinal());
        task.expire = cursor.getString(TaskFields.EXPIRE.ordinal());
        task.status = cursor.getString(TaskFields.STATUS.ordinal());
        return task;
    }

    public int id;
    public String name;
    public String description;
    public String expire;
    public String status;
}
