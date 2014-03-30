package com.jerome.splashdemo.app;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

public class SplashActivity extends Activity implements View.OnTouchListener {
  private static final int INITIAL_HIDE_DELAY = 300;

  private View mDecorView;
  private VideoView video;
  private View coverView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
    coverView = findViewById(R.id.layout_logo);
    initVideoView();
    initWindowMode();
    showSystemUI();
  }

  private void initWindowMode() {
    Window window = getWindow();
    window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
        WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    mDecorView = window.getDecorView();
    mDecorView = getWindow().getDecorView();
    coverView.setClickable(true);
    final GestureDetector clickDetector =
        new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
          @Override
          public boolean onSingleTapUp(MotionEvent e) {
            boolean visible =
                (mDecorView.getSystemUiVisibility() & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0;
            if (visible) {
              hideSystemUI();
            } else {
              showSystemUI();
            }
            return true;
          }
        }
        );
    coverView.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View view, MotionEvent motionEvent) {
        return clickDetector.onTouchEvent(motionEvent);
      }
    });
  }

  private void initVideoView() {
    video = (VideoView) findViewById(R.id.videoView);
    video.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.on_board_video);
    video.start();
    video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
      @Override
      public void onPrepared(MediaPlayer mp) {
        mp.setLooping(true);
      }
    });
    video.setOnTouchListener(this);
    coverView.findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
      }
    });
  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    if (hasFocus) {
      delayedHide(INITIAL_HIDE_DELAY);
    } else {
      mHideHandler.removeMessages(0);
    }
  }

  private void hideSystemUI() {
    mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        | View.SYSTEM_UI_FLAG_FULLSCREEN
        | View.SYSTEM_UI_FLAG_LOW_PROFILE
        | View.SYSTEM_UI_FLAG_IMMERSIVE);
  }

  private void showSystemUI() {
    mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
  }

  private final Handler mHideHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      hideSystemUI();
    }
  };

  private void delayedHide(int delayMillis) {
    mHideHandler.removeMessages(0);
    mHideHandler.sendEmptyMessageDelayed(0, delayMillis);
  }

  @Override public boolean onTouch(View view, MotionEvent motionEvent) {
    delayedHide(INITIAL_HIDE_DELAY);
    return false;
  }
}
