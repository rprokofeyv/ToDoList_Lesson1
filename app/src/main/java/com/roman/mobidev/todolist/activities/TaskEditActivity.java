package com.roman.mobidev.todolist.activities;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.roman.mobidev.todolist.R;
import com.roman.mobidev.todolist.db.MyContentProvider;
import com.roman.mobidev.todolist.db.TaskAsyncQueryHandler;
import com.roman.mobidev.todolist.helpers.BundleKeys;
import com.roman.mobidev.todolist.helpers.TaskFields;
import com.roman.mobidev.todolist.models.Task;

public class TaskEditActivity extends AppCompatActivity implements TaskAsyncQueryHandler.TaskAsyncQueryHandlerListener {

    public static final int INTENT_ID = 101;

    private final int HANDLER_TOKEN = 300;

    private enum Actions {
        create,
        edit
    }

    public static Intent createNewTaskIntent(Context context){
        Intent intent = new Intent(context, TaskEditActivity.class);
        intent.setAction(Actions.create.name());
        return  intent;
    }

    public static Intent createEditTaskIntent(Context context, Task task){
        Intent intent = new Intent(context, TaskEditActivity.class);
        intent.setAction(Actions.edit.name());
        intent.putExtra(BundleKeys.TASK_OBJECT.name(), task);
        return  intent;
    }

    private EditText nameEditText;
    private TextView descriptionEditText;

    private TaskAsyncQueryHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);
        nameEditText = (EditText)findViewById(R.id.nameEditText);
        descriptionEditText = (EditText)findViewById(R.id.descriptionEditText);

        handler = new TaskAsyncQueryHandler(getContentResolver(), this);

        if (isEditMode()) {
            Task editableTask = (Task)getIntent().getExtras().getSerializable(BundleKeys.TASK_OBJECT.name());
            nameEditText.setText(editableTask.name);
            descriptionEditText.setText(editableTask.description);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {

            if (isEditMode()){
                updateTask();
            }else{
                createNewTask();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createNewTask() {

        ContentValues values = new ContentValues();
        values.put(TaskFields.NAME.name(), nameEditText.getText().toString());
        values.put(TaskFields.DESCRIPTION.name(), descriptionEditText.getText().toString());

        handler.startInsert(HANDLER_TOKEN, null, MyContentProvider.TASKS_CONTENT_URI, values);
    }

    private void updateTask() {

        Task editableTask = (Task)getIntent().getExtras().getSerializable(BundleKeys.TASK_OBJECT.name());

        ContentValues values = new ContentValues();
        values.put(TaskFields.NAME.name(), nameEditText.getText().toString());
        values.put(TaskFields.DESCRIPTION.name(), descriptionEditText.getText().toString());
        Uri updateUri = ContentUris.withAppendedId(MyContentProvider.TASKS_CONTENT_URI, editableTask.id);

        handler.startUpdate(HANDLER_TOKEN, null, updateUri, values, null, null);
    }

    private boolean isEditMode(){
        return getIntent().getAction().equalsIgnoreCase(Actions.edit.name());
    }

    @Override
    public void onInsertComplete() {
        closeActivity();
    }

    @Override
    public void onDeleteComplete() {

    }

    @Override
    public void onUpdateComplete() {
        closeActivity();
    }

    private void closeActivity(){
        setResult(Activity.RESULT_OK);
        finish();
    }
}
