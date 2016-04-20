package com.roman.mobidev.todolist.activities;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.roman.mobidev.todolist.R;
import com.roman.mobidev.todolist.adapters.TasksCursorAdapter;
import com.roman.mobidev.todolist.db.MyContentProvider;
import com.roman.mobidev.todolist.helpers.Loaders;
import com.roman.mobidev.todolist.models.Task;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int INTENT_ID = 100;

    private ListView tasksListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initGui();
        getSupportLoaderManager().initLoader
                (Loaders.tasks.ordinal(), null, this);
    }

    private void initGui() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tasksListView = (ListView) findViewById(R.id.tasksListView);
        tasksListView.setAdapter(new TasksCursorAdapter(this, null, true));
        tasksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startTaskDetailsActivity(parent, position);

            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCreateNewActivity();
            }
        });
    }

    private void startCreateNewActivity() {
        Intent intent = TaskEditActivity.createNewTaskIntent(MainActivity.this);
        startActivityForResult(intent, TaskEditActivity.INTENT_ID);
    }


    private void startTaskDetailsActivity(AdapterView<?> parent, int position) {

        Task task = ((TasksCursorAdapter) parent.getAdapter()).getTaskFromPosition(position);
        Intent intent = TaskDetailsActivity.createTaskDetailsIntent(MainActivity.this, task);
        startActivityForResult(intent, TaskEditActivity.INTENT_ID);
    }
    @Override
    protected void onResume() {
        getSupportLoaderManager().getLoader(Loaders.tasks.ordinal()).forceLoad();
        super.onResume();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (Loaders.values()[id]){
            case tasks:
                return new CursorLoader(this,
                        MyContentProvider.TASKS_CONTENT_URI,
                        null, null, null, null);
            default:
                throw new IllegalArgumentException("Wrong loader id: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        ((TasksCursorAdapter)tasksListView.getAdapter()).
                swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((TasksCursorAdapter)tasksListView.getAdapter()).swapCursor(null);
    }

}
