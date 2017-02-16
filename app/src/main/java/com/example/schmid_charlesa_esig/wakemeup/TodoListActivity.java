package com.example.schmid_charlesa_esig.wakemeup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class TodoListActivity extends AppCompatActivity {
    ListView listView;
    ArrayAdapter adapter;
    String [] toDoList = {
            "Eat a fondue",
            "Go to Laponia",
            "Go to the medical",
            "Visit Wask"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);

        listView =(ListView) findViewById(R.id.listViewTodo);
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,toDoList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getBaseContext(),adapterView.getItemAtPosition(i)+"is selected",Toast.LENGTH_SHORT);
                System.out.println("yes sir");
            }
        });
    }

}
