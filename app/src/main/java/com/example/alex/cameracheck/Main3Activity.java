package com.example.alex.cameracheck;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

public class Main3Activity extends Activity {

    protected Switch sw;
    protected boolean soundonoff;
    ImageButton sound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Bundle extras = getIntent().getExtras();
        soundonoff=extras.getBoolean("sound");

        sound=(ImageButton) findViewById(R.id.imageButton);

        if (soundonoff==false){
            sound.setBackgroundResource(R.drawable.speaker);
        }
        else{
            sound.setBackgroundResource(R.drawable.speakeroff);
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sw=(Switch) findViewById(R.id.switch1);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked==false){
                    fbc();
                }
            }
        });
    }


    public void exit(View view) {
        finish();
    }

    public void start(View view) {
        Intent blackman= new Intent(this,Main2Activity.class);
        blackman.putExtra("sound",soundonoff);
        startActivity(blackman);
    }


    public void fileloc(View view) {
        Intent gal = new Intent();
        gal.setType("image/*");
        gal.setAction(Intent.ACTION_VIEW);
        startActivity(gal);
    }

    public void fbc()
    {
        Intent FBc= new Intent(this,MainActivity.class);
        FBc.putExtra("soundb",soundonoff);
        startActivity(FBc);
        overridePendingTransition(R.anim.enterl,R.anim.exitr);
        finish();
    }

    public void sound(View view) {
        if (soundonoff==false){
            sound.setBackgroundResource(R.drawable.speakeroff);
        }
        else{
            sound.setBackgroundResource(R.drawable.speaker);
        }
        soundonoff=!(soundonoff);

    }
    //public void onActivityResult() { ImageView

}
