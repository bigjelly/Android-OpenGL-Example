package com.opengl.bigjelly.android_opengl_example.lesson4;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.View;

import com.opengl.bigjelly.android_opengl_example.utils.MatrixState;

/**
 * @author maboyu
 * @version V1.0
 * @since 2019/03/08
 */
public class MySurfaceView extends GLSurfaceView implements View.OnTouchListener {

    private Lesson4Render myRender;
    private float mPreviousX;//上次的触控位置X坐标
    private float mPreviousY;//上次的触控位置Y坐标

    public MySurfaceView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        myRender = new Lesson4Render(context);
        this.setEGLContextClientVersion(2);
        this.setRenderer(myRender);
        // 设置渲染模式为主动渲染
        this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        this.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();//当前的触控位置X坐标
        float y = event.getY();//当前的触控位置X坐标
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE://检测到移动事件时
                float dx = x - mPreviousX;
                float dy = y - mPreviousY;
//                if (dx > 0) {
//                    MatrixState.translate(0.1f, 0, 0);
//                } else {
//                    MatrixState.translate(-0.1f, 0, 0);
//                }
                if (dx > dy) {
                    if (dx > 0) {
                        MatrixState.rotate(10, 1, 0, 0);
                    } else {
                        MatrixState.rotate(10, -1, 0, 0);
                    }
                } else {
                    if (dy > 0) {
                        MatrixState.rotate(10, 0, -1, 0);
                    } else {
                        MatrixState.rotate(10, 0, 1, 0);
                    }
                }


        }
        mPreviousX = x;
        mPreviousY = y;
        return true;
    }
}
