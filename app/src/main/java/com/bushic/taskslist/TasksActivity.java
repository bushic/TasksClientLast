package com.bushic.taskslist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

/**
 * Created by Дмитрий on 21.07.2016.
 */
public class TasksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        ListView listView = (ListView)findViewById(R.id.tasks);
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.tasksfab);
        fab.attachToListView(listView);
    }
}
