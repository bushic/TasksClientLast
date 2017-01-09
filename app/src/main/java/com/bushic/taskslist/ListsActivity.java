package com.bushic.taskslist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

/**
 * Created by Дмитрий on 19.07.2016.
 */
public class ListsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        ListView listView = (ListView)findViewById(R.id.lists);
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.listsfab);
        fab.attachToListView(listView);
    }
}