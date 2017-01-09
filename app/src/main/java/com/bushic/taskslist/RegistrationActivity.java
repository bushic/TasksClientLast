package com.bushic.taskslist;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Дмитрий on 19.07.2016.
 */
public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        TextView text = (TextView)findViewById(R.id.textViewExist);
        text.setOnClickListener(this);

        Button button = (Button)findViewById(R.id.buttonReg);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        //startActivity(intent);
        new TasksListUser().execute();
    }

    public class TasksListUser extends AsyncTask<Void,Void,User>{

        @Override
        protected User doInBackground(Void... params) {
            RestTemplate template = new RestTemplate();
            template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            return template.getForObject(Constants.URL.GET_USERBYID + "1",User.class);
        }

        @Override
        protected void onPostExecute(User user) {
            List<User> list = new ArrayList<>();
            list.add(user);

            TextView text = (TextView)findViewById(R.id.textViewReg);
            text.setText(user.getLogin());
        }
    }
}
