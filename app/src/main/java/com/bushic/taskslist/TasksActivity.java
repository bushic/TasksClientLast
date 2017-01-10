package com.bushic.taskslist;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Дмитрий on 21.07.2016.
 */
public class TasksActivity extends AppCompatActivity {

    private static User currentUser;
    private static long currentListsID;
    private List<Task> allTasks = new ArrayList<>();
    private static ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        ListView listView = (ListView)findViewById(R.id.tasks);
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.tasksfab);
        fab.attachToListView(listView);

        final TaskDialogLayout dialog = new TaskDialogLayout();
        dialog.setTasksActivity(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show(getSupportFragmentManager(),"task");
            }
        });

        TaskDialogLayout.setCurrentListsID(currentListsID);

        new Tasks().execute(currentListsID);
    }

    public static User getCurrentUser() {
        return currentUser;
    }
    public static void setCurrentUser(User currentUser) {
        TasksActivity.currentUser = currentUser;
    }
    public static long getCurrentListsID() {
        return currentListsID;
    }
    public static void setCurrentListsID(long currentListsID) {
        TasksActivity.currentListsID = currentListsID;
    }
    public void addTask(Task task){
        adapter.add(task);
    }

    public class Tasks extends AsyncTask<Long,Void,List<Task>> {


        @Override
        protected List<Task> doInBackground(Long... params) {
            RestTemplate template = new RestTemplate();
            template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ResponseEntity<Task[]> responseEntity = template.getForEntity(Constants.URL.GET_TASKSBYLISTID+params[0], Task[].class);
            Task[] tasks = responseEntity.getBody();
            List<Task> list = new ArrayList<>();

            for(int i=0;i<tasks.length;i++){
                list.add(tasks[i]);
            }

            return list;
        }

        @Override
        protected void onPostExecute(final List<Task> tasks) {
            ListView listView = (ListView)findViewById(R.id.tasks);

            adapter  = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_2, android.R.id.text1,tasks) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                    TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                    text1.setTextColor(Color.BLACK);
                    text2.setTextColor(Color.BLACK);

                    text1.setText(tasks.get(position).getName());
                    text2.setText(tasks.get(position).getDescription());

                    allTasks.add(tasks.get(position));

                    return view;
                }
            };

            listView.setAdapter(adapter);

            adapter.setNotifyOnChange(true);
        }
    }
}
