package com.smartmobileye.smartcamera;


import android.app.FragmentManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;
import com.afollestad.materialdialogs.MaterialDialog;

import org.jcodec.api.android.SequenceEncoder;

import java.io.File;
import java.io.IOException;


public class PlayVideo extends AppCompatActivity implements EasyVideoCallback {

    private EasyVideoPlayer player;
    MediaMetadataRetriever mediaMetadataRetriever;

    //updata lai lai duong link qua file video
    String viewSource = "/storage/emulated/0/DCIM/Camera/VID_20170322_101111.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);


        mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(viewSource);

        createVideoFromFrame(mediaMetadataRetriever);

        //get name of all video in
        printNamesToLogCat(getApplicationContext());

        player = (EasyVideoPlayer) findViewById(R.id.player);
//        //set video
//        Uri uri = Uri.parse("/storage/emulated/0/DCIM/Camera/test.mp4");
//        player.setSource(uri);
        assert player != null;
        player.setCallback(this);
//        // All further configuration is done from the XML layout.

    }


    /**
     * get frames and change frames after save video
     *
     * @param mediaMetadataRetriever
     */
    public void createVideoFromFrame(MediaMetadataRetriever mediaMetadataRetriever) {
        SequenceEncoder enc = null;
        try {
            enc = new SequenceEncoder(new File("/storage/emulated/0/DCIM/Camera/VID_20170322_test.mp4"));
            // GOP size will be supported in 0.2
//            enc.getEncoder().setKeyInterval(25);
            int time = 1000;
            for (int i = 0; i < 100; i++) {
                Log.d("VIDEO", "" + i);
                time += 10;
                //frames is process at there .......phuong ------------------------------------------
                Bitmap bmFrame = mediaMetadataRetriever
                        .getFrameAtTime(500 * time); ///micro second in your video x
                enc.encodeImage(bmFrame);
            }
            enc.finish();
            Log.d("VIDEO", "Thanh cong");
        } catch (IOException e) {
            Log.d("VIDEO", "That bai");
            e.printStackTrace();
        }
    }

    //get all video in strorage            phuong ------------------------------------------
    public static void printNamesToLogCat(Context context) {
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Video.VideoColumns.DATA};
        Cursor c = context.getContentResolver().query(uri, projection, null, null, null);
        int vidsCount = 0;
        if (c != null) {
            vidsCount = c.getCount();
            while (c.moveToNext()) {
                Log.d("VIDEO", c.getString(0));
            }
            c.close();
        }
    }

    @Override
    public FragmentManager getFragmentManager() {

        return super.getFragmentManager();
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.pause();
    }


    @Override
    public void onStarted(EasyVideoPlayer player) {
    }

    @Override
    public void onPaused(EasyVideoPlayer player) {
    }

    @Override
    public void onPreparing(EasyVideoPlayer player) {
        Log.d("EVP-Sample", "onPreparing()");
    }

    @Override
    public void onPrepared(EasyVideoPlayer player) {
        Log.d("EVP-Sample", "onPrepared()");
    }

    @Override
    public void onBuffering(int percent) {
        Log.d("EVP-Sample", "onBuffering(): " + percent + "%");
    }

    @Override
    public void onError(EasyVideoPlayer player, Exception e) {
        Log.d("EVP-Sample", "onError(): " + e.getMessage());
        new MaterialDialog.Builder(this)
                .title(R.string.error)
                .content(e.getMessage())
                .positiveText(android.R.string.ok)
                .show();
    }

    @Override
    public void onCompletion(EasyVideoPlayer player) {
        Log.d("EVP-Sample", "onCompletion()");
    }

    @Override
    public void onRetry(EasyVideoPlayer player, Uri source) {
        Toast.makeText(this, "Retry", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSubmit(EasyVideoPlayer player, Uri source) {
        Toast.makeText(this, "Submit", Toast.LENGTH_SHORT).show();
    }


//    @Override
//    public void onClickVideoFrame(EasyVideoPlayer player) {
//        Toast.makeText(this, "Click video frame.", Toast.LENGTH_SHORT).show();
//
//    }

}