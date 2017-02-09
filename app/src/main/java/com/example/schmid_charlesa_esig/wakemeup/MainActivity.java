package com.example.schmid_charlesa_esig.wakemeup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
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
    public void gotoNotif(View view){
        Intent i = new Intent(this,NotifActivity.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                }else{
                    System.out.println("Pas de connection");
                }


            }
        });

        listener.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String toSpeak = textToRead.getText().toString();
                textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

                Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                GlobalVar.setVarNameUser(textToRead.getText().toString());
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
}