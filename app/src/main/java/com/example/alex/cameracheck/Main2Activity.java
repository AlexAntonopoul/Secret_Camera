package com.example.alex.cameracheck;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main2Activity extends Activity {
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private BackCameraPreview mBackCamPreview;
    private FrontCameraPreview mFrontCamPreview;
    private static final String TAG="Main Activity";
    private Camera mBCamera;
    private Camera mFCamera;
    private int NumofCameras;
    private long mLastClickTime = 0;
    private String filepath;
    //private String mCurrentPhotoPath="file : ";
    private String mPath = "/storage/emulated/0/Pictures/SecretCamera";
    private int currentVolume;
    private float curBrightnessValue;
    private int brightnessmode;
    private boolean soundonoff;
    //private VolumeProviderCompat myVolumeProvider = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Bundle extras = getIntent().getExtras();
        soundonoff=extras.getBoolean("sound");
        try {
            curBrightnessValue=android.provider.Settings.System.getInt(
                    getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);
            brightnessmode=android.provider.Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Settings.SettingNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);}
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 0);
        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 5, 0);

        NumofCameras=Camera.getNumberOfCameras();
    }





    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        final MediaPlayer mp = MediaPlayer.create(this,R.raw.beep1);
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_UP) {
                    if (event.getEventTime() - event.getDownTime() > ViewConfiguration.getLongPressTimeout()) {
                        //TODO long click action
                    }

                    else {
                        if (NumofCameras == 2) {
                            if (SystemClock.elapsedRealtime() - mLastClickTime < 3000) {
                                return true;
                            }
                            mLastClickTime = SystemClock.elapsedRealtime();

                            // do your magic here
                            //TODO click action
                            mFCamera = getCameraInstance(1);
                            mFrontCamPreview = new FrontCameraPreview(this, mFCamera);
                            FrameLayout frontPreview = (FrameLayout) findViewById(R.id.front_camera_preview);
                            frontPreview.addView(mFrontCamPreview);

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mFCamera.takePicture(null, null, mPicture);
                                }
                            }, 1000);

                            final Handler handler2 = new Handler();
                            handler2.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mFCamera.release();
                                    if(!soundonoff){mp.start();}
                                }
                            }, 2000);
                        }
                        else{
                            return true;
                        }
                    }
                }


                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_UP) {
                    if (event.getEventTime() - event.getDownTime() > ViewConfiguration.getLongPressTimeout()) {
                        //TODO long click action
                    }

                    else {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 3000){
                            return true;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        //TODO click actiontion
                        mBCamera=getCameraInstance(0);
                        mBackCamPreview = new BackCameraPreview(this, mBCamera);
                        FrameLayout backPreview = (FrameLayout) findViewById(R.id.back_camera_preview);
                        backPreview.addView(mBackCamPreview);

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mBCamera.takePicture(null,null,mPicture);
                            }
                        }, 1000);

                        final Handler handler2 = new Handler();
                        handler2.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mBCamera.release();
                                if(!soundonoff){mp.start();}
                            }
                        }, 2000);
                    }
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }



    public static Camera getCameraInstance(int cameraid){
        Camera c = null;
        try {
            c = Camera.open(cameraid);
           // if(cameraid==0){Camera.Parameters params = c.getParameters();params.setRotation(90);c.setParameters(params);}else if(cameraid==1){    Camera.Parameters params = c.getParameters();    params.setRotation(270);    c.setParameters(params);} attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
                Log.d(TAG, "Error creating media file, check storage permissions: ");
                return;
            }
            filepath=pictureFile.getAbsolutePath();
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }


            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"
                    + filepath)));

        }
    };

    private void rotateImage(String filepath){
        Bitmap bitmap = BitmapFactory.decodeFile(filepath);
        File pictureFile=new File(filepath);
        int width=bitmap.getWidth();
        int height=bitmap.getHeight();
        Matrix matrix=new Matrix();
        matrix.postRotate(270);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,width,height,true);
        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap,0,0,scaledBitmap.getWidth(),scaledBitmap.getHeight(),matrix,true);
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(pictureFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        rotatedBitmap.compress(Bitmap.CompressFormat.PNG,0,os);
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            //fos.write(data);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }


    }


    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }


    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "SecretCamera");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("SecretCamera", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }



    @Override
    protected void onPause(){
        super.onPause();

        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, brightnessmode);
        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, (int) curBrightnessValue);//this will set the automatic mode on
        AudioManager audioManager1 = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audioManager1.setStreamVolume(AudioManager.STREAM_MUSIC,currentVolume, 0);
        finish();
        //putExtra();
    }

    @Override
    public void onBackPressed(){
        //TODO SYNC DATA GIA EMFANISH SE GALLERY
    finish();
    }

}

