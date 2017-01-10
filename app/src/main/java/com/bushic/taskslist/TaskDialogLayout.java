package com.bushic.taskslist;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class TaskDialogLayout extends DialogFragment implements DialogInterface.OnClickListener {

    private View form=null;
    private long currentListsID;
    private TasksActivity tasksActivity;
    private int selectedTask = -1;
    private Task currentTask=null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        form = getActivity().getLayoutInflater()
                .inflate(R.layout.layout_task_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        EditText taskName=(EditText)form.findViewById(R.id.taskname);
        EditText taskDescription=(EditText)form.findViewById(R.id.taskdescription);
        CheckBox taskDone=(CheckBox)form.findViewById(R.id.taskdone);

        if (currentTask!=null)
        {
            taskName.setText(currentTask.getName());
            taskDescription.setText(currentTask.getDescription());
            taskDone.setChecked(currentTask.getDone());
        }
        return(builder.setTitle("Заполнение данными").setView(form)
                .setPositiveButton(android.R.string.ok, this)
                .setNegativeButton(android.R.string.cancel, null).create());
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        EditText taskName=(EditText)form.findViewById(R.id.taskname);
        EditText taskDescription=(EditText)form.findViewById(R.id.taskdescription);
        CheckBox taskDone=(CheckBox)form.findViewById(R.id.taskdone);
        String name = taskName.getText().toString();
        String description = taskDescription.getText().toString();
        Boolean done = taskDone.isChecked();

        Task newTask = new Task();
        newTask.setName(name);
        newTask.setDescription(description);
        newTask.setDone(done);
        newTask.setListid(currentListsID);

        if(currentTask!=null)
        {
            newTask.setId(currentTask.getId());
        }

        if (name.compareTo("")==0||description.compareTo("")==0) {

        } else {
            new Tasks().execute(newTask);
        }
    }

    public long getCurrentListsID() {
        return currentListsID;
    }

    public void setCurrentListsID(long currentListsID) {
        this.currentListsID = currentListsID;
    }

    public TasksActivity getTasksActivity() {
        return tasksActivity;
    }

    public void setTasksActivity(TasksActivity tasksActivity) {
        this.tasksActivity = tasksActivity;
    }

    public int getSelectedTask() {
        return selectedTask;
    }

    public void setSelectedTask(int selectedTask) {
        this.selectedTask = selectedTask;
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }

    public class Tasks extends AsyncTask<Task,Void,Task> {

        @Override
        protected Task doInBackground(Task... params) {
            RestTemplate template = new RestTemplate();
            template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            return template.postForObject(Constants.URL.GET_TASKSBYLISTID,params[0],Task.class);
        }

        @Override
        protected void onPostExecute(Task task) {
            if (currentTask==null)
                tasksActivity.addTask(task);
            else{
                ArrayAdapter<Task> adapter = tasksActivity.getAdapter();
                adapter.remove(adapter.getItem(selectedTask));
                adapter.insert(task,selectedTask);
                tasksActivity.getAllTasks().remove(selectedTask);
                tasksActivity.getAllTasks().add(selectedTask,task);
            }
        }
    }
}
