package com.bushic.taskslist;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Дмитрий on 19.07.2016.
 */
public class ListsActivity extends AppCompatActivity {

    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        ListsActivity.currentUser = currentUser;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        ListView listView = (ListView)findViewById(R.id.lists);
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.listsfab);
        fab.attachToListView(listView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogLayout().show(getSupportFragmentManager(),"list");
            }
        });

        new TasksLists().execute(currentUser.getId());
    }

    public class TasksLists extends AsyncTask<Long,Void,List<Lists>> {

        @Override
        protected List<Lists> doInBackground(Long... params) {
            RestTemplate template = new RestTemplate();
            template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ResponseEntity<Permission[]> responseEntity = template.getForEntity(Constants.URL.GET_PERMISSONSBYUSERID+params[0], Permission[].class);
            Permission[] permissions = responseEntity.getBody();

            List<Lists> list = new ArrayList<>();
            Lists item;

            for (int i=0;i<permissions.length;i++) {
                item = template.getForObject(Constants.URL.GET_LISTBYID+permissions[i].getListid(),Lists.class);
                list.add(item);
            }

            return list;
        }

        @Override
        protected void onPostExecute(List<Lists> lists) {
            ListView listView = (ListView)findViewById(R.id.lists);
        }
    }
}