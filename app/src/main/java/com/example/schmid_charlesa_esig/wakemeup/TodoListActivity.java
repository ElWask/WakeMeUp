package com.example.schmid_charlesa_esig.wakemeup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;


public class TodoListActivity extends AppCompatActivity {
    ListView listView;
    int[] item_resource = {
            R.drawable.binarytree,
            R.drawable.alarmicon,
            R.drawable.alcoliqueanon,
            R.drawable.charlyschmid,
    };

    String [] title_resource;
    String[] desc_resource;
    TodoAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);

        title_resource = getResources().getStringArray(R.array.todo_titles);
        desc_resource = getResources().getStringArray(R.array.todo_desc);
        listView =(ListView) findViewById(R.id.listViewTodo);
        int i=0 ;
        adapter = new TodoAdapter(getApplicationContext(),R.layout.row_layout);
        listView.setAdapter(adapter);
        for (String titles :title_resource){
            TodoDataProvider dataProvider = new TodoDataProvider(item_resource[i],titles,desc_resource[i]);
            adapter.add(dataProvider);
            i++;
        }
    }

}
