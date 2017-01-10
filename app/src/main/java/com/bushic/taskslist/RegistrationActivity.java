package com.bushic.taskslist;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Дмитрий on 19.07.2016.
 */
public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    public RegistrationActivity registrationActivity = this;

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

        EditText editLogin = (EditText) findViewById(R.id.editLogin);
        EditText editPassword = (EditText) findViewById(R.id.editPassword);
        String[] params = {editLogin.getText().toString(),editPassword.getText().toString()};

        new TasksListUser().execute(params);


    }

    public class TasksListUser extends AsyncTask<String,Void,User>{

        @Override
        protected User doInBackground(String... params) {
            RestTemplate template = new RestTemplate();
            template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            User user = template.getForObject(Constants.URL.GET_USERBYLOGIN+params[0], User.class);

            if (user != null)
                return null;
            else {
                User newUser = new User();

                newUser.setLogin(params[0]);
                newUser.setPassword(params[1]);
                newUser.setRoot(false);

                return template.postForObject(Constants.URL.GET_USERBYLOGIN,newUser,User.class);
            }
        }

        @Override
        protected void onPostExecute(User user) {
            TextView text = (TextView)findViewById(R.id.textViewReg);

            if (user == null)
                text.setText("Пользователь с таким логином уже зарегистрирован");
            else{
                Intent intent = new Intent(registrationActivity, LoginActivity.class);
                startActivity(intent);
            }
        }
    }
}
