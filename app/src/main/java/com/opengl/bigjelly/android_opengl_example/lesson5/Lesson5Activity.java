package com.opengl.bigjelly.android_opengl_example.lesson5;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.opengl.bigjelly.android_opengl_example.lesson2.Lesson2Render;
import com.opengl.bigjelly.android_opengl_example.lesson4.MySurfaceView;


/**
 * @author maboyu
 * @version V1.0
 * @since 2019/03/08
 */
public class Lesson5Activity extends AppCompatActivity {

    private GLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
        }
        // 设置为全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        glSurfaceView = new TouchSurfaceView(this);
        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(3); // Pick an OpenGL ES 2.0 context.
        glSurfaceView.setRenderer(new Lesson5Render(this));
        setContentView(glSurfaceView);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        glSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        glSurfaceView.onResume();
    }
}
