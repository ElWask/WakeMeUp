package com.example.schmid_charlesa_esig.wakemeup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Locale;

import android.widget.Toast;




public class MainActivity extends Activity {
    TextToSpeech textToSpeech;
    EditText textToRead;
    Button listener,gotoAlarm;


    public void gotoAlarm(View view){
        Intent i = new Intent(this,AlarmActivity.class);
        startActivity(i);
    }
    public void gotoTask(View view){

        Intent i = new Intent(this,TodoListActivity.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //DBAdapter.init(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textToRead=(EditText)findViewById(R.id.editText);
        listener=(Button)findViewById(R.id.button);
        gotoAlarm=(Button)findViewById(R.id.AlarmPageButton);

        textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.FRENCH);
                    System.out.println("Trouve french pour connection");
                    //textToSpeech.speak(GlobalVar.getVarNameUser(), TextToSpeech.QUEUE_FLUSH,null);
                }else{
                    System.out.println("Pas de connection");
                }
            }
        });

        listener.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalVar.setVarNameUser(textToRead.getText().toString());
                String toSpeak = GlobalVar.getVarNameUser();
                textToSpeech.speak(GlobalVar.getVarNameUser(), TextToSpeech.QUEUE_FLUSH, null);

                Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                System.out.println(GlobalVar.getVarNameUser());
            }
        });
    }

    public void onPause(){
        if(textToSpeech !=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
            System.out.println("LA");
        }
        super.onPause();
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