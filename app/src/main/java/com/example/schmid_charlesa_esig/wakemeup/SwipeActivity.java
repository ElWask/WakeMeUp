package com.example.schmid_charlesa_esig.wakemeup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class SwipeActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListView lv_languages;
    ArrayAdapter<String> language_adapter;
    ArrayList<String> languagesarraylist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //method for initialisation
        init();

        //setting array adaptet to listview
        System.out.println(lv_languages);
        lv_languages.setAdapter(language_adapter);
        System.out.println(lv_languages);
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        lv_languages,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {

                                    languagesarraylist.remove(position);
                                    language_adapter.notifyDataSetChanged();

                                }

                            }
                        });
        lv_languages.setOnTouchListener(touchListener);


    }

    private void init() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        languagesarraylist = new ArrayList<>();

        //adding few data to arraylist
        languagesarraylist.add("SQL");
        languagesarraylist.add("JAVA");
        languagesarraylist.add("JAVA SCRIPT ");
        languagesarraylist.add("C#");
        languagesarraylist.add("PYTHON");
        languagesarraylist.add("C++");
        languagesarraylist.add("IOS");
        languagesarraylist.add("ANDROID");
        languagesarraylist.add("PHP");
        languagesarraylist.add("LARAVEL");


        language_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_activated_1,languagesarraylist);

        lv_languages = (ListView) findViewById(R.id.lv_languages);
    }

}
