package com.example.schmid_charlesa_esig.wakemeup;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class SetTaskActivity extends AppCompatActivity {
    Intent myIntent;
    String firstKeyName, secondKeyName;

    TextView nameTask, descTask;
    EditText editNameTask, editDescTask;

    Button saveEdit;

    public void gotoEditHour(View view){
        Intent i = new Intent(getApplicationContext(),AlarmActivity.class);
        i.putExtra("nameTrans",nameTask.getText());
        System.out.println(nameTask.getText());
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_task);

        // getIntent() is a method from the started activity
        myIntent = getIntent(); // gets the previously created intent
        firstKeyName = myIntent.getStringExtra("nameTrans"); // will return "FirstKeyValue"
        secondKeyName = myIntent.getStringExtra("descTrans");
        nameTask = (TextView) findViewById(R.id.taskName);
        descTask = (TextView) findViewById(R.id.taskDesc);

        editNameTask = (EditText) findViewById(R.id.editTitle);
        editDescTask = (EditText) findViewById(R.id.editDesc);

        saveEdit = (Button) findViewById(R.id.goChooseAnHour);


        nameTask.setText(firstKeyName);
        descTask.setText(secondKeyName);

        editNameTask.setText(firstKeyName);
        editDescTask.setText(secondKeyName);

        //Button a parametrer pour modifier la BDD


    }
}
