package com.example.schmid_charlesa_esig.wakemeup;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

public class TalkToMe extends AppCompatActivity {
    TextToSpeech textToSpeech;
    EditText textToRead;
    Button listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk_to_me);

        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        textToRead=(EditText)findViewById(R.id.textToMakeSpeak);
        listener=(Button)findViewById(R.id.btnConvertTTS);

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
                GlobalVar.setVarNameUser(textToRead.getText().toString());
                String toSpeak = GlobalVar.getVarNameUser();
                textToSpeech.speak(GlobalVar.getVarNameUser(), TextToSpeech.QUEUE_FLUSH, null);

                Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                System.out.println(GlobalVar.getVarNameUser());

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id == android.R.id.home){finish();}
        return super.onOptionsItemSelected(item);
    }

//    public void onPause(){
//        if(textToSpeech !=null){
//            textToSpeech.stop();
//            textToSpeech.shutdown();
//            System.out.println("LA");
//        }
//        super.onPause();
//    }
}
