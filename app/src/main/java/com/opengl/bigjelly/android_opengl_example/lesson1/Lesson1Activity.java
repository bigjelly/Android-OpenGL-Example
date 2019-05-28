package com.opengl.bigjelly.android_opengl_example.lesson1;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.opengl.bigjelly.android_opengl_example.lesson2.Lesson2Render;
import com.opengl.bigjelly.android_opengl_example.lesson3.Lesson3Render;
import com.opengl.bigjelly.android_opengl_example.utils.Constants;


/**
 * @author maboyu
 * @version V1.0
 * @since 2019/03/08
 */
public class Lesson1Activity extends AppCompatActivity {

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
        int type = intent.getIntExtra(Constants.TYPE, 1);
        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(2); // Pick an OpenGL ES 2.0 context.
        if (type == 1) {
            glSurfaceView.setRenderer(new MyRender());
        } else if (type == 2) {
            glSurfaceView.setRenderer(new Lesson2Render(this));
        } else if (type == 3) {
            // 设置为横屏模式
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            glSurfaceView.setRenderer(new Lesson3Render(this));
        }

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
