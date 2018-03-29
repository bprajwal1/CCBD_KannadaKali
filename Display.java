package com.example.kkccbd;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.IOException;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by rahul on 15/11/17.
 */

public class Display extends AppCompatActivity {

    String idNum;
    String imageName;
    String textName;
    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer;
    MediaRecorder mediaRecorder;
    String savePath;
    String fileName;
    File file;
    boolean flag = false;
    private TransferUtility transferUtility;
    String line = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        Intent intent = getIntent();
        idNum = intent.getStringExtra(Util.EXTRA_MESSAGE);

        imageName = "image_" + idNum;
        textName = "text_" + idNum;

        ImageView imageImageView = (ImageView) this.findViewById(R.id.displayImage);
        ImageView textImageView = (ImageView) this.findViewById(R.id.displayText);

        Context context = imageImageView.getContext();
        int id = context.getResources().getIdentifier(imageName, "mipmap", context.getPackageName());
        imageImageView.setImageResource(id);

        context = textImageView.getContext();
        id = context.getResources().getIdentifier(textName, "mipmap", context.getPackageName());
        textImageView.setImageResource(id);

        transferUtility = Util.getTransferUtility(this);

        ImageView rightarrow = (ImageView) findViewById(R.id.rightButton);
        rightarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Display.class);
                int id = Integer.parseInt(idNum) + 1;
                String message = String.valueOf(id);
                if (getApplicationContext().getResources().getIdentifier("image_" + message, "mipmap", getApplicationContext().getPackageName()) > 0) {
                    intent.putExtra(Util.EXTRA_MESSAGE, message);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "End of list", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageView leftarrow = (ImageView) findViewById(R.id.leftButton);
        leftarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Display.class);
                int id = Integer.parseInt(idNum) - 1;
                String message = String.valueOf(id);
                if (getApplicationContext().getResources().getIdentifier("image_" + message, "mipmap", getApplicationContext().getPackageName()) > 0) {
                    intent.putExtra(Util.EXTRA_MESSAGE, message);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Start of list", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageView done = (ImageView) findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), line, Toast.LENGTH_SHORT).show();
            }
        });

        ImageView speaker = (ImageView) findViewById(R.id.speakerButton);
        speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Util.isNetwork(getApplicationContext())){
                    Toast.makeText(getApplicationContext(), "Internet required", Toast.LENGTH_SHORT).show();
                }
                else {

                    String objectURL = "https://s3.amazonaws.com/kannadalearning-userfiles-mobilehub-129890051/Fruits/apple.mp3";

                    Uri clip = Uri.parse(objectURL);

                    mediaPlayer = new MediaPlayer();

                    try {
                        mediaPlayer.setDataSource(String.valueOf(clip));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), "Playing...", Toast.LENGTH_SHORT).show();
                    mediaPlayer.start();
                }
            }
        });

        ImageView recorder = (ImageView) findViewById(R.id.recordButton);
        recorder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(flag == true)
                {
                    Toast.makeText(getApplicationContext(), "Already recording!", Toast.LENGTH_SHORT).show();
                    return true;
                }
                if (!checkPermission())
                {
                    requestPermission();
                }
                else
                {
//                    savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
//                            idNum + "_" + System.currentTimeMillis() + ".3gp";

                    fileName = "rec_" + idNum + "_" + System.currentTimeMillis() + ".3gp";
                    savePath = getApplicationContext().getCacheDir().getAbsolutePath() + "/" + fileName;
                    file = new File(getApplicationContext().getCacheDir(), fileName);

                    mediaRecorder = new MediaRecorder();
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                    mediaRecorder.setOutputFile(savePath);

                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                        Toast.makeText(getApplicationContext(), "Recording...",
                                Toast.LENGTH_SHORT).show();
                        ImageView img = findViewById(R.id.recordButton);
                        img.setImageResource(R.drawable.stop);
                        flag = true;
                    } catch (IllegalStateException e) {
                        Toast.makeText(getApplicationContext(), "Unable to record",
                                Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "Unable to record",
                                Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                return true;
            }
        });

        recorder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (flag == true) {
                    mediaRecorder.stop();
                    ImageView img = findViewById(R.id.recordButton);
                    img.setImageResource(R.drawable.recorder);

                    mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(savePath);
                        mediaPlayer.prepare();
//                        Toast.makeText(getApplicationContext(), "Playing...",
//                                Toast.LENGTH_SHORT).show();
                        mediaPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Snackbar mySnackbar = Snackbar.make(findViewById(R.id.coordinatorLayout),
                            "Upload?", Snackbar.LENGTH_LONG);
                    mySnackbar.setAction("OK", new MyUploadListener());
                    mySnackbar.show();

                    flag = false;
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Long-press to record",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageView home = (ImageView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(Display.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(getApplicationContext(), "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    public void transferObserverListener(TransferObserver transferObserver){

        transferObserver.setTransferListener(new TransferListener(){

            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.e("statechange", state+"");
                if(state == TransferState.COMPLETED)
                {
// Instantiate the RequestQueue.
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    String url ="http://192.168.1.6:5000/" + fileName;

// Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // Display the first 500 characters of the response string.
                                    line = response;
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            line = "That didn't work!";
                        }
                    });
// Add the request to the RequestQueue.
                    queue.add(stringRequest);
                }
                //textView.setText("statechange " + state);
                Toast.makeText(getApplicationContext(), "Upload: " + state, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                int percentage = (int) (bytesCurrent/(bytesTotal+1) * 100);
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e("error","error");
                Toast toast = Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    public class MyUploadListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            if(Util.isNetwork(getApplicationContext()))
            {
                TransferObserver observer = transferUtility.upload(
                        "kannadakali",
                        fileName,
                        file
                );

                transferObserverListener(observer);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Internet required",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}