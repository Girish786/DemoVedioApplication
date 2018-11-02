package com.voiceassitnat.demovedioapplication;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {
    VideoView videoView;
    private MediaController mediaController;
    EditText ed_title,ed_description;
    Button btn_upload;
    private int position = 0;
    public static final int REQUEST_PICK_VIDEO = 3;
    private String filemanagerstring;
    private String selectedImagePath;
    private MediaController media_Controller;
    DisplayMetrics dm;
    public void init(){
        videoView=findViewById(R.id.videoView);
        ed_title=findViewById(R.id.ed_title);
        ed_description=findViewById(R.id.ed_description);
        btn_upload=findViewById(R.id.btn_upload);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
      btn_upload.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent pickVideoIntent = new Intent(Intent.ACTION_GET_CONTENT);
              pickVideoIntent.setType("video/*");
              startActivityForResult(pickVideoIntent, REQUEST_PICK_VIDEO);
          }
      });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Uri selectedImageUri=data.getData();
        filemanagerstring=selectedImageUri.getPath();
        selectedImagePath=getPath(selectedImageUri);
        Log.e("vediopath",selectedImagePath);
       if(resultCode== RESULT_OK){


               if(selectedImagePath !=null){
                   media_Controller = new MediaController(this);
                   dm = new DisplayMetrics();
                   this.getWindowManager().getDefaultDisplay().getMetrics(dm);
                   int height = dm.heightPixels;
                   int width = dm.widthPixels;
                   videoView.setMinimumWidth(width);
                   videoView.setMinimumHeight(height);
                   videoView.setMediaController(media_Controller);

                   videoView.setVideoURI(Uri.parse(selectedImagePath));
                   Log.e("vediopath",selectedImagePath);

                   videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                       public void onPrepared(MediaPlayer mediaPlayer) {
                           // close the progress bar and play the video
                          // progressDialog.dismiss();
                           //if we have a position on savedInstanceState, the video playback should start from here
                          videoView.seekTo(position);
                           if (position == 0) {
                               videoView.start();
                           } else {
                               //if we come from a resumed activity, video playback will be paused
                               videoView.pause();
                           }
                       }
                   });


           }
       }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getPath(Uri uri){
        String[] projection = { MediaStore.Video.VideoColumns.DATA ,MediaStore.Video.Media.DISPLAY_NAME};
        Cursor cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        if(cursor!=null)
        {
            int column_index=cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);

        }else
            return null;
    }

    public ArrayList<String> getAllMedia() {
        HashSet<String> videoItemHashSet = new HashSet<>();
        String[] projection = { MediaStore.Video.VideoColumns.DATA ,MediaStore.Video.Media.DISPLAY_NAME};
        Cursor cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        try {
            cursor.moveToFirst();
            do{
                videoItemHashSet.add((cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))));
            }while(cursor.moveToNext());

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<String> downloadedList = new ArrayList<>(videoItemHashSet);
        return downloadedList;
    }
}