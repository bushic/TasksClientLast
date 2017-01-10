package com.bushic.taskslist;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
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
    private static ArrayAdapter adapter;
    private ListsActivity listsActivity;
    private List<Lists> allLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        setListsActivity(this);
        ListView listView = (ListView)findViewById(R.id.lists);
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.listsfab);
        fab.attachToListView(listView);
        final DialogLayout dialog = new DialogLayout();
        dialog.setCurrentUser(currentUser);
        dialog.setListsActivity(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show(getSupportFragmentManager(),"list");
            }
        });

        new TasksLists().execute(currentUser.getId());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TasksActivity.setCurrentListsID(allLists.get(position).getId());
                TasksActivity.setCurrentUser(currentUser);
                Intent intent = new Intent(listsActivity,TasksActivity.class);
                startActivity(intent);
            }
        });
    }

    public void addInList(Lists lists){
        adapter.add(lists);
    }
    public ListsActivity getListsActivity() {
        return listsActivity;
    }
    public void setListsActivity(ListsActivity listsActivity) {
        this.listsActivity = listsActivity;
    }
    public static User getCurrentUser() {
        return currentUser;
    }
    public static void setCurrentUser(User currentUser) {
        ListsActivity.currentUser = currentUser;
    }
    public static ArrayAdapter getAdapter() {
        return adapter;
    }
    public static void setAdapter(ArrayAdapter adapter) {
        ListsActivity.adapter = adapter;
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
        protected void onPostExecute(final List<Lists> list) {
            ListView listView = (ListView)findViewById(R.id.lists);

            adapter  = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_2, android.R.id.text1,list) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                    TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                    text1.setTextColor(Color.BLACK);
                    text2.setTextColor(Color.BLACK);

                    text1.setText(list.get(position).getName());
                    text2.setText(list.get(position).getDescription());

                    allLists.add(list.get(position));

                    return view;
                }
            };

            listView.setAdapter(adapter);

            adapter.setNotifyOnChange(true);
        }
    }
}