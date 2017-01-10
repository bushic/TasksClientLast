package com.bushic.taskslist;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class DialogLayout extends DialogFragment implements DialogInterface.OnClickListener {

    private View form=null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        form = getActivity().getLayoutInflater()
                .inflate(R.layout.layout_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return(builder.setTitle("Заполнение данными").setView(form)
                .setPositiveButton(android.R.string.ok, this)
                .setNegativeButton(android.R.string.cancel, null).create());
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        EditText listName=(EditText)form.findViewById(R.id.listname);
        EditText listDescription=(EditText)form.findViewById(R.id.listdescription);
        String name = listName.getText().toString();
        String description = listDescription.getText().toString();

        Lists newLists = new Lists();
        newLists.setName(name);
        newLists.setDescription(description);

        if (name.compareTo("")==0||description.compareTo("")==0) {

        } else {
            new TasksList().execute(newLists);
        }
    }

    public class TasksList extends AsyncTask<Lists,Void,Lists> {

        @Override
        protected Lists doInBackground(Lists... params) {
            RestTemplate template = new RestTemplate();
            template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            return template.postForObject(Constants.URL.GET_LISTBYID,params[0],Lists.class);
        }
    }
}
