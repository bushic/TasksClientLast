package com.bushic.taskslist;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public LoginActivity loginActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button button = (Button)findViewById(R.id.buttonLog);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        /*Intent intent = new Intent(this, ListsActivity.class);
        startActivity(intent);*/

        EditText editLogin = (EditText) findViewById(R.id.editLogin2);
        EditText editPassword = (EditText) findViewById(R.id.editPassword2);
        String[] params = new String[2];
        params[0] = editLogin.getText().toString();
        params[1] = editPassword.getText().toString();

        if (params[0].compareTo("")==0||params[1].compareTo("")==0){
            TextView text = (TextView)findViewById(R.id.textViewLog);
            text.setText("Заполните все поля");
        }
        else{
            new TasksListUser().execute(params);
        }
    }

    public class TasksListUser extends AsyncTask<String,Void,User> {

        @Override
        protected User doInBackground(String... params) {
            RestTemplate template = new RestTemplate();
            template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            User user = template.getForObject(Constants.URL.GET_USERBYLOGIN+params[0], User.class);

            if (user != null) {
                if (user.getPassword().compareTo(params[1])!=0) {
                    return null;
                }
                else {
                    return user;
                }
            }
            else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(User user) {
            TextView text = (TextView)findViewById(R.id.textViewLog);

            if (user == null) {
                text.setText("Неправильный логин или пароль");
            }
            else{
                Intent intent = new Intent(loginActivity, ListsActivity.class);
                startActivity(intent);
            }
        }
    }
}
