package com.bushic.taskslist;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class DialogLayout extends DialogFragment implements DialogInterface.OnClickListener {

    private View form=null;

    private static User currentUser;
    private ListsActivity listsActivity;
    private Lists currentList = null;
    private int selectedList = -1;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        DialogLayout.currentUser = currentUser;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        form = getActivity().getLayoutInflater()
                .inflate(R.layout.layout_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        EditText listName=(EditText)form.findViewById(R.id.listname);
        EditText listDescription=(EditText)form.findViewById(R.id.listdescription);
        if (currentList!=null)
        {
            listName.setText(currentList.getName());
            listDescription.setText(currentList.getDescription());
        }

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
        if( currentList!=null)
        {
            newLists.setId(currentList.getId());
        }

        if (name.compareTo("")==0||description.compareTo("")==0) {

        } else {
            new TasksList().execute(newLists);
        }
    }

    public void setListsActivity(ListsActivity listsActivity) {
        this.listsActivity = listsActivity;
    }

    public Lists getCurrentList() {
        return currentList;
    }

    public void setCurrentList(Lists currentList) {
        this.currentList = currentList;
    }

    public int getSelectedList() {
        return selectedList;
    }

    public void setSelectedList(int selectedList) {
        this.selectedList = selectedList;
    }

    public class TasksList extends AsyncTask<Lists,Void,Lists> {

        @Override
        protected Lists doInBackground(Lists... params) {
            RestTemplate template = new RestTemplate();
            template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            Lists lists = template.postForObject(Constants.URL.GET_LISTBYID,params[0],Lists.class);

            Permission permission = new Permission();
            permission.setListid(lists.getId());
            permission.setUserid(currentUser.getId());

            if (currentList==null)
                template.postForObject(Constants.URL.GET_PERMISSONSBYUSERID,permission,Permission.class);

            return lists;
        }

        @Override
        protected void onPostExecute(Lists lists) {
            //listsActivity.getAdapter().add(lists);
            if (currentList==null)
                listsActivity.addInList(lists);
            else{
                ArrayAdapter<Lists> adapter = listsActivity.getAdapter();
                adapter.remove(adapter.getItem(selectedList));
                adapter.insert(lists,selectedList);
                listsActivity.getAllLists().remove(selectedList);
                listsActivity.getAllLists().add(selectedList,lists);
            }
        }
    }
}
