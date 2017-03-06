package com.example.schmid_charlesa_esig.wakemeup;

import android.content.Intent;
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
    String [] toDoListName = {
            "Eat a fondue",
            "Go to Laponia",
            "Go to the medical",
            "Visit Wask"
    };
    String [] toDoListDesc = {
            "Buy cheese, bread, white wine and bread gf hrwoie hoighowterhgitrwhoiehiughteroiuwhzterwgphterwgouherioihpoerwqhfrehiuff7uck",
            "Buy tickets, plan a journey",
            "Cause your liver is dying",
            "Surprise"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);

        listView =(ListView) findViewById(R.id.listViewTodo);
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,toDoListName);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getBaseContext(),adapterView.getItemAtPosition(i)+"is selected",Toast.LENGTH_SHORT).show();

                Intent myIntent = new Intent(getApplicationContext(), SetTaskActivity.class);

                myIntent.putExtra("nameTrans",adapterView.getItemAtPosition(i).toString());
                myIntent.putExtra("descTrans",toDoListDesc[i]);
                startActivity(myIntent);
            }
        });
    }

}
