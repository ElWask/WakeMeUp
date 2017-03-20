package com.example.schmid_charlesa_esig.wakemeup;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import java.util.Locale;

import android.widget.ImageView;
import android.widget.Toast;




public class MainActivity extends Activity {
    Button gotoAlarm;
    ImageView logoImg;

    public void gotoAddTask(View view){

        Intent i = new Intent(this,TodoListActivity.class);
        i.putExtra("addTask","yes");
        startActivity(i);
    }
    public void gotoTask(View view){

        Intent i = new Intent(this,TodoListActivity.class);
        i.putExtra("addTask","no");
        startActivity(i);
    }
    public void gotoTaskOfTheDay(View view){

        Intent i = new Intent(this,DayTask.class);
        startActivity(i);
    }
    public void gotoAlarm(View view){

        Intent i = new Intent(this,AlarmActivity.class);
        startActivity(i);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //DBAdapter.init(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gotoAlarm=(Button)findViewById(R.id.AlarmPageButton);
        logoImg = (ImageView)findViewById(R.id.logoImg);

        logoImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent i = new Intent(getApplicationContext(),TalkToMe.class);
                startActivity(i);
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menualarm,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.parameters:
                startActivity(new Intent(this,MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}