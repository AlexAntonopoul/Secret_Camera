package com.example.alex.cameracheck;

        import android.Manifest;
        import android.app.Activity;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.hardware.Camera;
        import android.net.Uri;
        import android.os.Build;
        import android.provider.MediaStore;
        import android.provider.Settings;
        import android.support.v4.content.ContextCompat;
        import android.os.Bundle;
        import android.view.View;
        import android.view.WindowManager;
        import android.widget.CompoundButton;
        import android.widget.ImageButton;
        import android.widget.Switch;
        import android.widget.Toast;


public class MainActivity extends Activity {

    private static final int REQUEST_MUL_RESULT = 1;
    protected Switch sw;
    private boolean retVal;
    protected boolean soundcheck=false;
    ImageButton sound;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);



        checkCameraWriteSystemPermission();
        checkWriteSettingsPermission();
        sound=(ImageButton) findViewById(R.id.imageButton);
        Bundle extras = getIntent().getExtras();
        /////////////////////////////////////
        if(extras!=null) {   //meta to debuging kai to katharismo ton intent anakalupsa oti se monh leitourgia ektos adb anagnwrizei intent kai pernaei to if twn extras
                            //etsi kaname arnhtikh logikh giati kathe fora tha anagnwrizei ta skoupidia opote ta vgalame
            soundcheck = extras.getBoolean("soundb");
            if (soundcheck == false) {
                sound.setBackgroundResource(R.drawable.speaker);
            } else {
                sound.setBackgroundResource(R.drawable.speakeroff);
            }
        }
        ////////////////////////////////////////
        sw=(Switch) findViewById(R.id.switch1);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked==true){
                    Bmain();
                }
            }
        });
    }

    public void fileloc(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            retVal = Settings.System.canWrite(this);
        }
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) || (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) || !retVal) {
            Toast.makeText(MainActivity.this, "GIVE THE PERMISSIONS WE ASKED!", Toast.LENGTH_LONG).show();
            finish();
            Intent againper = new Intent(this, MainActivity.class);
            startActivity(againper);
        } else {
            Intent gal = new Intent();
            gal.setType("image/*");
            gal.setAction(Intent.ACTION_VIEW);
            startActivity(gal);
        }
    }

    public void Bmain() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            retVal = Settings.System.canWrite(this);}
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) || (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)||!retVal) {
            Toast.makeText(MainActivity.this, "GIVE THE PERMISSIONS WE ASKED!", Toast.LENGTH_LONG).show();
            finish();
            Intent againper= new Intent(this,MainActivity.class);
            startActivity(againper);
        } else {
            Intent Bmain = new Intent(this, Main3Activity.class);
            Bmain.putExtra("sound", soundcheck);
            startActivity(Bmain);
            overridePendingTransition(R.anim.enterr,R.anim.exitl);
            finish();

        }
    }

    @Override
    public void onBackPressed(){
        finish();
    }

    public void exit(View view) {
        finish();
    }

    public void startF(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            retVal = Settings.System.canWrite(this);}
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) || (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)||!retVal) {
            Toast.makeText(MainActivity.this, "GIVE THE PERMISSIONS WE ASKED!", Toast.LENGTH_LONG).show();
            finish();
            Intent againper= new Intent(this,MainActivity.class);
            startActivity(againper);
        } else {

            if (Camera.getNumberOfCameras() == 2) {
                Intent FrontStart = new Intent(this, FrontActivity.class);
                FrontStart.putExtra("sound", soundcheck);
                startActivity(FrontStart);
            } else {
                Toast.makeText(MainActivity.this, "You Havent Front Camera", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkCameraWriteSystemPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //TODO Camera
            if((ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)==PackageManager.PERMISSION_DENIED)||(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)){
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_MUL_RESULT);
            }
        }
    }

    private void checkWriteSettingsPermission() {

        retVal = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //TODO Write Settings Permission
            retVal = Settings.System.canWrite(this);
            if (!retVal) {
                Toast.makeText(this, "ENABLE CHANGE SYSTEM SETTINGS PERMISSION", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:com.example.alex.cameracheck"));
                startActivity(intent);

            }
        }
    }


    public void startB(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            retVal = Settings.System.canWrite(this);}
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) || (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)||!retVal) {
            Toast.makeText(MainActivity.this, "GIVE THE PERMISSIONS WE ASKED!", Toast.LENGTH_LONG).show();
            finish();
            Intent againper= new Intent(this,MainActivity.class);
            startActivity(againper);
        } else {
            Intent BackStart = new Intent(this, BackActivity.class);
            BackStart.putExtra("sound", soundcheck);
            startActivity(BackStart);
        }
    }


    public void sound(View view) {

        if (soundcheck==false){
            sound.setBackgroundResource(R.drawable.speakeroff);
        }
        else{
            sound.setBackgroundResource(R.drawable.speaker);
        }
        soundcheck=!(soundcheck);
    }

}





